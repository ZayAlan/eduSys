package com.example.classdesign.Administrator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.classdesign.MyUtilities;
import com.example.classdesign.R;
import com.example.classdesign.Teacher.Teacher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckTeacherFragment extends Fragment {

    private View view;

    private List<Teacher> courseList = new ArrayList<>();
    private int removePosition;

    private CheckTeaItemAdapter courseAdapter;
    private Handler mHandler;
    private Handler removeHandler;
    private Handler disagreeHandler;

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            courseAdapter.refresh(courseList);
        }
    };

    private AlertDialog agreeChoose; //删除框
    private AlertDialog disagreeChoose; //

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.checkteacherfragment,container,false);

        initCourseList();
        RecyclerView recyclerView = view.findViewById(R.id.check_teacher_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        courseAdapter = new CheckTeaItemAdapter(courseList);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("通过审核？");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeCourse();
            }
        });
        agreeChoose = alertDialogBuilder.create();

        final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getContext());
        alertDialogBuilder2.setTitle("拒绝注册？");
        alertDialogBuilder2.setCancelable(true);
        alertDialogBuilder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                disagreeAccount();
            }
        });
        disagreeChoose = alertDialogBuilder2.create();

        courseAdapter.setOnItemClickListener(new CheckTeaItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                removePosition = position;
                agreeChoose.show();
            }

            @Override
            public void onLongClick(int position) {
                removePosition = position;
                disagreeChoose.show();
            }
        });
        recyclerView.setAdapter(courseAdapter);
        return view;
    }


    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            Teacher removeCourse = courseList.get(removePosition);
            courseList.remove(removePosition);
            refreshHandler.sendMessage(refreshHandler.obtainMessage());
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(removeCourse);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/user/allowTeacher")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
            } catch (IOException e) {
                Toast.makeText(getActivity(), "can't connected!", Toast.LENGTH_SHORT).show();
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

    private Runnable disagreeRunnable = new Runnable() {
        @Override
        public void run() {
            Teacher removeCourse = courseList.get(removePosition);
            courseList.remove(removePosition);
            refreshHandler.sendMessage(refreshHandler.obtainMessage());
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(removeCourse);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/user/disallowTeacher")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
            } catch (IOException e) {
                Toast.makeText(getActivity(), "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void disagreeAccount(){
        HandlerThread disagreeThread = new HandlerThread("disagreeThread");
        disagreeThread.start();
        disagreeHandler = new Handler(disagreeThread.getLooper());
        disagreeHandler.post(disagreeRunnable);
    }

    private Runnable initCourseRunnable = new Runnable() {
        @Override
        public void run() {
            //                发送json数据
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/user/showUncheckedTeacher")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                courseList = gson.fromJson(responseData,new TypeToken<List<Teacher>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(getActivity(), "can't connected!", Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(initCourseRunnable);
    }
}
