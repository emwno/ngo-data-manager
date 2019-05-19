package com.emwno.ngo.datamanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.emwno.ngo.datamanager.model.Community;
import com.emwno.ngo.datamanager.model.Search;
import com.emwno.ngo.datamanager.model.TreeData;
import com.emwno.ngo.datamanager.table.TableViewAdapter;
import com.emwno.ngo.datamanager.table.TableViewListener;
import com.evrencoskun.tableview.TableView;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private List<Community> mCommunityList;
    private List<Search> mTreeFilterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        LovelyProgressDialog dialog = new LovelyProgressDialog(this)
                .setTitle("Fetching Server Data")
                .setTopColorRes(R.color.colorPrimaryDark);
        dialog.show();

        String whereQuery = getIntent().getStringExtra("whereQuery");
        mTreeFilterList = getIntent().getParcelableArrayListExtra("treeFilterList");

        TableView tableView = findViewById(R.id.table);
        tableView.setTableViewListener((TableViewListener) (rowHeaderView, row) -> {
            Intent intent = new Intent(TableActivity.this, EditFormActivity.class);
            intent.putExtra("objectId", mCommunityList.get(row).getObjectId());
            startActivity(intent);
        });

        TableViewAdapter adapter = new TableViewAdapter(this);
        tableView.setAdapter(adapter);

        List<String> column = new ArrayList<>();
        column.add("Name");
        column.add("Leader Name");
        column.add("Phone");
        column.add("Address");
        column.add("Soil Type");
        column.add("Cultivable Area");
        column.add("Pests");
        column.add("Tree(s)");
        column.add("Tree(s) Received");
        column.add("Tree(s) Alive");
        column.add("Created");
        column.add("Updated");

        DataQueryBuilder query = DataQueryBuilder.create();
        if (whereQuery != null && whereQuery.length() > 0) query.setWhereClause(whereQuery);
        query.setSortBy("created");

        Backendless.Data.of(Community.class).find(query, new AsyncCallback<List<Community>>() {
            @Override
            public void handleResponse(List<Community> response) {
                mCommunityList = response;
                List<List<String>> cellList = getCellList(response);
                List<String> row = new ArrayList<>();
                for (int i = 0; i < cellList.size(); i++) {
                    row.add("" + (i + 1));
                }
                adapter.setAllItems(column, row, cellList);
                dialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(TableActivity.this, fault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<List<String>> getCellList(List<Community> response) {
        List<List<String>> list = new ArrayList<>();

        for (int i = 0; i < response.size(); i++) {
            if (mTreeFilterList != null && filterTreeData(response.get(i))) continue;

            List<String> cellList = new ArrayList<>();
            cellList.add(response.get(i).getName());
            cellList.add(response.get(i).getLeaderName());
            cellList.add(response.get(i).getPhone());
            cellList.add(response.get(i).getAddress());
            cellList.add(response.get(i).getSoilType().getType());
            cellList.add(response.get(i).getCultivableArea() + "");
            cellList.add(DataBindingHelper.pestListToString(response.get(i).getPests()));
            cellList.add(DataBindingHelper.treeListToString(response.get(i).getTreeData()));
            cellList.add(DataBindingHelper.treeReceivedListToString(response.get(i).getTreeData()));
            cellList.add(DataBindingHelper.treeAliveListToString(response.get(i).getTreeData()));
            cellList.add(response.get(i).getCreated().toString());
            cellList.add(response.get(i).getUpdated() == null ? "-" : response.get(i).getUpdated().toString());
            list.add(cellList);
        }

        return list;
    }

    private boolean filterTreeData(Community community) {
        int alive = 0;
        int received = 0;

        for (TreeData treeData : community.getTreeData()) {
            alive += treeData.getTreeNumberAlive();
            received += treeData.getTreeNumberReceived();
        }

        for (Search search : mTreeFilterList) {
            if (search.getField().contains("alive")) {
                if (!filterNumberQuery(search, alive)) return true;
            } else if (search.getField().contains("dead")) {
                if (!filterNumberQuery(search, received - alive)) return true;
            } else if (search.getField().contains("date")) {
                if (!filterDateQuery(search, community.getTreeDateReceived())) return true;
            }
        }

        return false;
    }

    private boolean filterNumberQuery(Search search, int data) {
        if (search.getQuery().contains("=")) {
            return data == Integer.valueOf(search.getData());
        } else if (search.getQuery().contains(">")) {
            return data > Integer.valueOf(search.getData());
        } else if (search.getQuery().contains("<")) {
            return data < Integer.valueOf(search.getData());
        } else {
            return false;
        }
    }

    private boolean filterDateQuery(Search search, String treeDateString) {
        try {
            Date searchDate = new SimpleDateFormat("dd/MM/yyyy").parse(search.getData());
            Date treeDate = new SimpleDateFormat("dd/MM/yyyy").parse(treeDateString);

            if (search.getQuery().contains("=")) {
                return treeDate.equals(searchDate);
            } else if (search.getQuery().contains(">")) {
                return treeDate.after(searchDate);
            } else if (search.getQuery().contains("<")) {
                return treeDate.before(searchDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
