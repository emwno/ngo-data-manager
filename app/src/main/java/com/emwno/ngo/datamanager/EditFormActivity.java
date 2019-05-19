package com.emwno.ngo.datamanager;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.emwno.ngo.datamanager.FormActivity.Irrelevant;
import com.emwno.ngo.datamanager.databinding.ActivityFormBinding;
import com.emwno.ngo.datamanager.model.Community;
import com.emwno.ngo.datamanager.model.Pest;
import com.emwno.ngo.datamanager.model.Soil;
import com.emwno.ngo.datamanager.model.Tree;
import com.emwno.ngo.datamanager.model.TreeData;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.ilhasoft.support.validation.Validator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class EditFormActivity extends AppCompatActivity {

    private Community mCommunity;
    private Validator mValidator;
    private LovelyProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = new LovelyProgressDialog(this)
                .setTitle("Loading Community")
                .setTopColorRes(R.color.colorPrimaryDark);
        mDialog.show();

        Backendless.Data.of(Community.class).findById(getIntent().getStringExtra("objectId"), new BackendlessCallback<Community>() {
            @Override
            public void handleResponse(Community response) {
                mCommunity = response;
                ActivityFormBinding binding = DataBindingUtil.setContentView(EditFormActivity.this, R.layout.activity_form);
                binding.setCommunity(mCommunity);

                mValidator = new Validator(binding);
                mValidator.enableFormValidationMode();

                setupForm();
                mDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.form_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveForm:
                if (mValidator.validate())
                    saveData();
                break;
            default:
                break;
        }
        return true;
    }

    private void setupForm() {
        List<Soil> soils = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Soil.class).findAll());
        findViewById(R.id.soilType).setOnClickListener(v ->
                new LovelyChoiceDialog(EditFormActivity.this)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setTitle("Select Soil")
                        .setItems(soils, (position, item) -> mCommunity.setSoilType(item))
                        .show());

        List<Pest> pests = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Pest.class).findAll());
        boolean[] optionsSelected = new boolean[pests.size()];
        findViewById(R.id.pestTypes).setOnClickListener(v ->
                new LovelyChoiceDialog(EditFormActivity.this)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setTitle("Select Pests")
                        .setItemsMultiChoice(pests, optionsSelected, (positions, items) -> {
                            mCommunity.setPests(items);
                            for (int i = 0; i < optionsSelected.length; i++)
                                optionsSelected[i] = positions.contains(i);
                        })
                        .setConfirmButtonText("Select")
                        .show());

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(mCommunity.getTreeDateReceived()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        findViewById(R.id.treeDateReceived).setOnClickListener(v ->
                new SpinnerDatePickerDialogBuilder()
                        .context(EditFormActivity.this)
                        .defaultDate(year, month, day)
                        .callback((view, yearOf, monthOfYear, dayOfMonth) ->
                                ((AppCompatTextView) v).setText(dayOfMonth + "/" + ++monthOfYear + "/" + yearOf))
                        .build()
                        .show());

        List<Tree> trees = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Tree.class).findAll());
        findViewById(R.id.addTree).setOnClickListener(v -> new LovelyChoiceDialog(EditFormActivity.this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Add Tree")
                .setItems(trees, (position, item) -> {
                    trees.remove(item);
                    TreeData treeData = new TreeData();
                    treeData.setTree(item);
                    mCommunity.getTreeData().add(treeData);

                    ViewDataBinding vb = DataBindingUtil.inflate(LayoutInflater.from(EditFormActivity.this), R.layout.form_tree_single, null, false);
                    vb.setVariable(BR.treeData, mCommunity.getTreeData().get(mCommunity.getTreeData().size() - 1));
                    ((LinearLayout) findViewById(R.id.formLayout)).addView(vb.getRoot());
                })
                .show());

        for (int i = 0; i < mCommunity.getTreeData().size(); i++) {
            trees.remove(mCommunity.getTreeData().get(i).getTree());
            ViewDataBinding vb = DataBindingUtil.inflate(LayoutInflater.from(EditFormActivity.this), R.layout.form_tree_single, null, false);
            vb.setVariable(BR.treeData, mCommunity.getTreeData().get(i));
            ((LinearLayout) findViewById(R.id.formLayout)).addView(vb.getRoot());
        }

    }

    private void saveData() {
        mDialog.setTitle("Saving Community to Server");
        mDialog.show();

        Disposable disposable = saveCommunity()
                .flatMap(t -> addSoilRelation())
                .flatMap(t -> addPestRelations())
                .flatMap(t -> saveTreeData())
                .flatMap(t -> addCommunityTreeDataRelation())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            Log.e("king", "all saved");
                            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        },
                        throwable -> {
                            Log.e("king", throwable.getMessage());
                            Toast.makeText(this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        },
                        mDialog::dismiss);
    }

    private Observable<Object> saveCommunity() {
        mDialog.setTitle("Saving Community");
        return Observable.create(emitter -> {
            Backendless.Data.of(Community.class).save(mCommunity);
            Log.e("king", "community saved");
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

    private Observable<Object> addSoilRelation() {
        mDialog.setTitle("Saving Relations");
        return Observable.create(emitter -> {
            List<Soil> s = new ArrayList<>();
            s.add(mCommunity.getSoilType());
            Backendless.Data.of(Community.class).setRelation(mCommunity, "soilType", s);
            Log.e("king", "saved soil relation");
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

    private Observable<Object> addPestRelations() {
        return Observable.create(emitter -> {
            Backendless.Data.of(Community.class).setRelation(mCommunity, "pests", mCommunity.getPests());
            Log.e("king", "saved pests relation");
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

    private Observable<Object> saveTreeData() {
        return Observable.create(emitter -> {
            for (int i = 0; i < mCommunity.getTreeData().size(); i++) {
                if (mCommunity.getTreeData().get(i).getObjectId() == null) {
                    Log.e("king", "null");
                    // Save to Backendless
                    String response = Backendless.Data.of(TreeData.class).save(mCommunity.getTreeData().get(i)).getObjectId();
                    mCommunity.getTreeData().get(i).setObjectId(response);

                    // Add Tree relation to TreeData
                    List<Tree> s = new ArrayList<>();
                    s.add(mCommunity.getTreeData().get(i).getTree());
                    Backendless.Data.of(TreeData.class).setRelation(mCommunity.getTreeData().get(i), "tree", s);
                } else {
                    Log.e("king", "got id");
                    Backendless.Data.of(TreeData.class).save(mCommunity.getTreeData().get(i));
                }
            }
            Log.e("king", "saved tree data");
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

    private Observable<Object> addCommunityTreeDataRelation() {
        return Observable.create(emitter -> {
            Backendless.Data.of(Community.class).setRelation(mCommunity, "treeData", mCommunity.getTreeData());
            Log.e("king", "added community tree relations");
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

}
