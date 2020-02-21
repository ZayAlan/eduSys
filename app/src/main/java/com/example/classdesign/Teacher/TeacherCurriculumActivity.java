package com.example.classdesign.Teacher;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.classdesign.AddCourseActivity;
import com.example.classdesign.Course;
import com.example.classdesign.CourseAdapter;
import com.example.classdesign.MyUtilities;
import com.example.classdesign.R;
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

public class TeacherCurriculumActivity extends AppCompatActivity {
    private List<Course> courseList = new ArrayList<>();
    private int removePosition;

    private CourseAdapter courseAdapter;
    private Handler mHandler;
    private Handler removeHandler;
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            courseAdapter.refresh(courseList);
        }
    };

    private AlertDialog deleteChoose; //删除框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_curriculum);
        Toolbar toolbar = findViewById(R.id.toolbar_teacher_course);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_teacher_course);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtilities.setKindRole(2); //设置教育机构
                Intent intent = new Intent(TeacherCurriculumActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 2);
            }
        });
        initCourseList();
        RecyclerView recyclerView = findViewById(R.id.teacher_course_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        courseAdapter = new CourseAdapter(courseList);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("是否删除该课程？");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeCourse();
            }
        });
        deleteChoose = alertDialogBuilder.create();
        courseAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(int position) {
                removePosition = position;
                deleteChoose.show();
            }
        });

        recyclerView.setAdapter(courseAdapter);
    }

    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            Course removeCourse = courseList.get(removePosition);
            courseList.remove(removePosition);
            refreshHandler.sendMessage(refreshHandler.obtainMessage());
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(removeCourse);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/deleteCourse")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
            } catch (IOException e) {
                Toast.makeText(TeacherCurriculumActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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
            Teacher enterTeacher = MyUtilities.getEnterTeacher();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(enterTeacher);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/showTeacherTimetable")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                courseList = gson.fromJson(responseData,new TypeToken<List<Course>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(TeacherCurriculumActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 2:
                if (resultCode == RESULT_OK){
                    mHandler.removeCallbacks(initCourseRunnable);
                    initCourseList();
                }
                break;
        }
    }


}
