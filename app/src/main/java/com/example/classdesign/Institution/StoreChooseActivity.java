package com.example.classdesign.Institution;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.classdesign.ActivityCollector;
import com.example.classdesign.MyUtilities;
import com.example.classdesign.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreChooseActivity extends AppCompatActivity {

    private List<Store> storeList = new ArrayList<>();
    private int removePosition;

    private Handler mHandler;
    private Handler removeHandler;
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            storeAdapter.refresh(storeList);
        }
    };

    private AlertDialog deleteChoose; //删除框

    private StoreAdapter storeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_choose);
        Toolbar toolbar = findViewById(R.id.toolbar_store_choose);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_store);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreChooseActivity.this, AddStoreActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        initStoreList();
        RecyclerView recyclerView = findViewById(R.id.store_choose_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        storeAdapter = new StoreAdapter(storeList);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("是否删除该店铺？");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeStore();
            }
        });
        deleteChoose = alertDialogBuilder.create();
        storeAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                MyUtilities.setEnterStore(storeList.get(position));
                Intent intent = new Intent(StoreChooseActivity.this,StoreCourseActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                removePosition = position;
                deleteChoose.show();
            }
        });

        recyclerView.setAdapter(storeAdapter);
    }

    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            Store removeStore = storeList.get(removePosition);
            storeList.remove(removePosition);
            refreshHandler.sendMessage(refreshHandler.obtainMessage());
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(removeStore);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/deleteStore")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
            } catch (IOException e) {
                Toast.makeText(StoreChooseActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void removeStore(){
        HandlerThread removeStoreThread = new HandlerThread("removeStoreThread");
        removeStoreThread.start();
        removeHandler = new Handler(removeStoreThread.getLooper());
        removeHandler.post(removeRunnable);
    }

    private Runnable initStoreRunnable = new Runnable() {
        @Override
        public void run() {
            //                发送json数据
            OkHttpClient client = new OkHttpClient();
            Institution institution = new Institution();
            institution.setUsername(MyUtilities.getUsername());
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(institution);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/showStore")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                storeList = gson.fromJson(responseData,new TypeToken<List<Store>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(StoreChooseActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void initStoreList(){
        storeList.clear();
        HandlerThread initStoreListThread = new HandlerThread("InitStoreListThread");
        initStoreListThread.start();
        mHandler = new Handler(initStoreListThread.getLooper());
        mHandler.post(initStoreRunnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 1:
                if (resultCode == RESULT_OK){
                    mHandler.removeCallbacks(initStoreRunnable);
                    initStoreList();
                }
                break;
        }
    }
}
