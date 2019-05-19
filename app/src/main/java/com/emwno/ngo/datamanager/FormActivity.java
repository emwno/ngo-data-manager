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
import com.emwno.ngo.datamanager.databinding.ActivityFormBinding;
import com.emwno.ngo.datamanager.model.Community;
import com.emwno.ngo.datamanager.model.Pest;
import com.emwno.ngo.datamanager.model.Soil;
import com.emwno.ngo.datamanager.model.Tree;
import com.emwno.ngo.datamanager.model.TreeData;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.ilhasoft.support.validation.Validator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class FormActivity extends AppCompatActivity {

    private Community mCommunity;
    private Validator mValidator;
    private LovelyProgressDialog mSaveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFormBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_form);

        mValidator = new Validator(binding);
        mValidator.enableFormValidationMode();

        mCommunity = new Community();
        mCommunity.setPests(new ArrayList<>());
        mCommunity.setTreeData(new ArrayList<>());

        mSaveDialog = new LovelyProgressDialog(this)
                .setTitle("Connecting to Server")
                .setTopColorRes(R.color.colorPrimaryDark);

        binding.setCommunity(mCommunity);

        List<Soil> soils = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Soil.class).findAll());
        findViewById(R.id.soilType).setOnClickListener(v ->
                new LovelyChoiceDialog(FormActivity.this)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setTitle("Select Soil")
                        .setItems(soils, (position, item) -> mCommunity.setSoilType(item))
                        .show());

        List<Pest> pests = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Pest.class).findAll());
        boolean[] optionsSelected = new boolean[pests.size()];
        findViewById(R.id.pestTypes).setOnClickListener(v ->
                new LovelyChoiceDialog(FormActivity.this)
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .setTitle("Select Pests")
                        .setItemsMultiChoice(pests, optionsSelected, (positions, items) -> {
                            mCommunity.setPests(items);
                            for (int i = 0; i < optionsSelected.length; i++)
                                optionsSelected[i] = positions.contains(i);
                        })
                        .setConfirmButtonText("Select")
                        .show());

        findViewById(R.id.treeDateReceived).setOnClickListener(v ->
                new SpinnerDatePickerDialogBuilder()
                        .context(FormActivity.this)
                        .callback((view, year, monthOfYear, dayOfMonth) ->
                                ((AppCompatTextView) v).setText(dayOfMonth + "/" + ++monthOfYear + "/" + year))
                        .build()
                        .show());

        List<Tree> trees = Realm.getDefaultInstance().copyFromRealm(Realm.getDefaultInstance().where(Tree.class).findAll());
        findViewById(R.id.addTree).setOnClickListener(v -> new LovelyChoiceDialog(FormActivity.this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Add Tree")
                .setItems(trees, (position, item) -> {
                    trees.remove(item);
                    TreeData treeData = new TreeData();
                    treeData.setTree(item);
                    mCommunity.getTreeData().add(treeData);

                    ViewDataBinding vb = DataBindingUtil.inflate(LayoutInflater.from(FormActivity.this), R.layout.form_tree_single, null, false);
                    vb.setVariable(BR.treeData, mCommunity.getTreeData().get(mCommunity.getTreeData().size() - 1));
                    ((LinearLayout) findViewById(R.id.formLayout)).addView(vb.getRoot());
                })
                .show());

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

    private void saveData() {
        mSaveDialog.show();

        Disposable disposable = saveCommunity()
                .flatMap(t -> addSoilRelation())
                .flatMap(t -> addPestRelations())
                .flatMap(t -> saveTreeData())
                .flatMap(t -> addTreeDataTreeRelation())
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
                            mSaveDialog.dismiss();
                        },
                        mSaveDialog::dismiss);
    }

    private Observable<Object> saveCommunity() {
        mSaveDialog.setTitle("Saving Community");
        return Observable.create(emitter -> {
            List<String> comm = Backendless.Data.of(Community.class).create(Collections.singletonList(mCommunity));
            Log.e("king", "community saved");
            mCommunity.setObjectId(comm.get(0));
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

    private Observable<Object> addSoilRelation() {
        mSaveDialog.setTitle("Saving Relations");
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
            List<String> response = Backendless.Data.of(TreeData.class).create(mCommunity.getTreeData());
            Log.e("king", "saved tree data");
            for (int i = 0; i < response.size(); i++)
                mCommunity.getTreeData().get(i).setObjectId(response.get(i));
            emitter.onNext(Irrelevant.INSTANCE);
            emitter.onComplete();
        });
    }

    private Observable<Object> addTreeDataTreeRelation() {
        return Observable.create(emitter -> {
            for (TreeData treeData : mCommunity.getTreeData()) {
                List<Tree> s = new ArrayList<>();
                s.add(treeData.getTree());
                Backendless.Data.of(TreeData.class).setRelation(treeData, "tree", s);
                Log.e("king", "added treedata tree relation");
                emitter.onNext(Irrelevant.INSTANCE);
                emitter.onComplete();
            }
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

    enum Irrelevant {
        INSTANCE
    }

}
