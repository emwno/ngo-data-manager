package com.emwno.ngo.datamanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.backendless.Backendless;
import com.emwno.ngo.datamanager.model.Pest;
import com.emwno.ngo.datamanager.model.Soil;
import com.emwno.ngo.datamanager.model.Tree;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Realm.getDefaultInstance().where(Tree.class).count() == 0)
            getInitialData();

        findViewById(R.id.newButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tableButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TableActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.searchButton).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

    }

    private void getInitialData() {
        LovelyProgressDialog dialog = new LovelyProgressDialog(this)
                .setTitle("Syncing Server Data")
                .setTopColorRes(R.color.colorPrimaryDark);
        dialog.show();

        Disposable disposable = Observable.concat(fetchTree(), fetchPest(), fetchSoil())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmObjects -> Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(realmObjects)),
                        throwable -> Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show(),
                        dialog::dismiss);
    }

    private Observable<List<Tree>> fetchTree() {
        return Observable.create(emitter -> {
            List<Tree> response = Backendless.Data.of(Tree.class).find();
            Log.e("king", "got tree");
            emitter.onNext(response);
            emitter.onComplete();
        });
    }

    private Observable<List<Pest>> fetchPest() {
        return Observable.create(emitter -> {
            List<Pest> response = Backendless.Data.of(Pest.class).find();
            Log.e("king", "got pest");
            emitter.onNext(response);
            emitter.onComplete();
        });
    }

    private Observable<List<Soil>> fetchSoil() {
        return Observable.create(emitter -> {
            List<Soil> response = Backendless.Data.of(Soil.class).find();
            Log.e("king", "got Soil");
            emitter.onNext(response);
            emitter.onComplete();
        });
    }
}
