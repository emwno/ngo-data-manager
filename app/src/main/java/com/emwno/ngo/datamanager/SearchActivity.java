package com.emwno.ngo.datamanager;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.emwno.ngo.datamanager.model.Search;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private HashMap<String, String> searchables;
    private HashMap<String, String> searchablesRem;
    private HashMap<String, String> searchQueriesText;
    private HashMap<String, String> searchQueriesNum;
    private HashMap<String, String> searchQueriesDate;
    private List<Search> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchables = new HashMap<>();
        searchablesRem = new HashMap<>();
        searchables.put("Number Tree Dead", "treeData.dead");
        searchables.put("Number Tree Alive", "treeData.alive");
        searchables.put("Name", "name");
//        searchables.put("Tree Received Date", "treeData.date");

        searchQueriesText = new HashMap<>();
        searchQueriesText.put("Equal to", "= '~'");
        searchQueriesText.put("Contains", "LIKE '%~%'");
        searchQueriesText.put("Starts with", "LIKE '~%'");
        searchQueriesText.put("Ends with", "LIKE '%~'");

        searchQueriesNum = new HashMap<>();
        searchQueriesNum.put("Equal to", "= ~");
        searchQueriesNum.put("Greater than", "> ~");
        searchQueriesNum.put("Less than", "< ~");

        searchQueriesDate = new HashMap<>();
        searchQueriesDate.put("After", "> ~");
        searchQueriesDate.put("Before", "< ~");
        searchQueriesDate.put("On", "= ~");

        searchList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addSearch:
                addSearchItem();
                break;
            case R.id.doSearch:
                doSearch();
                break;
            default:
                break;
        }
        return true;
    }

    private void addSearchItem() {
        new LovelyChoiceDialog(SearchActivity.this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Select Search Parameter")
                .setItems(searchables.keySet().toArray(new String[0]), (position, item) -> {
                    Search search = new Search();
                    search.setName(item);
                    search.setField(searchables.get(item));
                    searchList.add(search);
                    String[] items;
                    if (search.getName().contains("Number")) {
                        items = searchQueriesNum.keySet().toArray(new String[0]);
                    } else if (search.getName().contains("Date")) {
                        items = searchQueriesDate.keySet().toArray(new String[0]);
                    } else {
                        items = searchQueriesText.keySet().toArray(new String[0]);
                    }


                    ViewDataBinding vb = DataBindingUtil.inflate(LayoutInflater.from(SearchActivity.this), R.layout.search_single, null, false);
                    vb.setVariable(BR.search, searchList.get(searchList.size() - 1));
                    vb.getRoot().findViewById(R.id.searchQuery).setOnClickListener(v1 ->
                            new LovelyChoiceDialog(SearchActivity.this)
                                    .setTopColorRes(R.color.colorPrimaryDark)
                                    .setTitle("Select Query")
                                    .setItems(items,
                                            (position1, item1) -> {
                                                searchList.remove(search);
                                                search.setQuery(search.getName().contains("Number") ? searchQueriesNum.get(item1) : searchQueriesText.get(item1));
                                                searchList.add(search);
                                                ((AppCompatTextView) v1).setText(item1);
                                            })
                                    .show());
                    ((LinearLayout) SearchActivity.this.findViewById(R.id.searchLayout)).addView(vb.getRoot());
                })
                .show();
    }

    private void doSearch() {
        ArrayList<Search> treeFilterList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (Search search : searchList) {
            if (search.getName().contains("Tree")) {
                treeFilterList.add(search);
                continue;
            }
            sb.append(search.getField());
            sb.append(" ");
            sb.append(search.getQuery().replace("~", search.getData()));
            sb.append(" and ");
        }
        if (sb.lastIndexOf("and") != -1) sb.delete(sb.lastIndexOf("and"), sb.length());

        Intent intent = new Intent(SearchActivity.this, TableActivity.class);
        intent.putExtra("whereQuery", sb.toString());
        intent.putParcelableArrayListExtra("treeFilterList", treeFilterList);
        startActivity(intent);
    }

}