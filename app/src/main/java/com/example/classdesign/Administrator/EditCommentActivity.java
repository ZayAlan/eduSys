package com.example.classdesign.Administrator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.classdesign.MyUtilities;
import com.example.classdesign.R;
import com.example.classdesign.Remark;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditCommentActivity extends AppCompatActivity {

    private List<Remark> courseList = new ArrayList<>();
    private int removePosition;


    private EditCommentAdapter courseAdapter;
    private Handler mHandler;
    private AlertDialog deleteChoose; //删除框
    private Handler removeHandler;
    private Handler moveHandler;
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            courseAdapter.refresh(courseList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        Toolbar toolbar = findViewById(R.id.toolbar_edit_comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCourseList();
        DefaultItemTouchHelpCallback callback = new DefaultItemTouchHelpCallback(onItemTouchCallbackListener);
        callback.setDragEnable(true);
        callback.setSwipeEnable(true);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        RecyclerView recyclerView = findViewById(R.id.edit_comment_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        courseAdapter = new EditCommentAdapter(courseList);
        recyclerView.setAdapter(courseAdapter);
        touchHelper.attachToRecyclerView(recyclerView);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("是否删除该评论？");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeCourse();
            }
        });
        deleteChoose = alertDialogBuilder.create();
    }

    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener = new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
        @Override
        public void onSwiped(int adapterPosition) {
            removePosition = adapterPosition;
            removeCourse();
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            if (courseList != null) {
                // 更换数据源中的数据Item的位置
                Collections.swap(courseList, srcPosition, targetPosition);
                // 更新UI中的Item的位置，主要是给用户看到交互效果
                courseAdapter.notifyItemMoved(srcPosition, targetPosition);
                sendMoveInformation();
                return true;
            }
            return false;
        }
    };

    private Runnable moveRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(courseList);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/sortRemark")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
//                Gson gson = new Gson();
//                courseList = gson.fromJson(responseData,new TypeToken<List<Remark>>(){}.getType());
//                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(EditCommentActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void sendMoveInformation(){
        HandlerThread moveCourseThread = new HandlerThread("moveCourseThread");
        moveCourseThread.start();
        moveHandler = new Handler(moveCourseThread.getLooper());
        moveHandler.post(moveRunnable);
    }


    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            Remark removeCourse = courseList.get(removePosition);
            courseList.remove(removePosition);
            refreshHandler.sendMessage(refreshHandler.obtainMessage());
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(removeCourse);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/deleteRemark")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
            } catch (IOException e) {
                Toast.makeText(EditCommentActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void removeCourse(){
        HandlerThread removeCourseThread = new HandlerThread("removeCourseThread");
        removeCourseThread.start();
        removeHandler = new Handler(removeCourseThread.getLooper());
        removeHandler.post(removeRunnable);
    }

    private Runnable initCourseRunnable = new Runnable() {
        @Override
        public void run() {
            //                发送json数据
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/showAllRemark")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                courseList = gson.fromJson(responseData,new TypeToken<List<Remark>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(EditCommentActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void initCourseList(){
        courseList.clear();
        HandlerThread initCourseListThread = new HandlerThread("InitCourseListThread");
        initCourseListThread.start();
        mHandler = new Handler(initCourseListThread.getLooper());
        mHandler.post(initCourseRunnable);
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
}
