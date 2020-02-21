package com.example.classdesign.Student;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.classdesign.Course;
import com.example.classdesign.CourseAdapter;
import com.example.classdesign.MyUtilities;
import com.example.classdesign.R;
import com.example.classdesign.VideoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentSearchActivity extends AppCompatActivity {


    private List<Course> courseList = new ArrayList<>();
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

    private Button math;
    private Button chinese;
    private Button english;
    private Button science;
    private Button art;
    private Button pe;
    private Button computer;
    private Button other;

    private Map<String, Boolean> changeColor = new HashMap<>();

    private void initialColorChangeMap(){
        changeColor.put("math",false);
        changeColor.put("chinese",false);
        changeColor.put("english",false);
        changeColor.put("science",false);
        changeColor.put("art",false);
        changeColor.put("pe",false);
        changeColor.put("computer",false);
        changeColor.put("other",false);
    }

    private void initialTypeButton(){
        initialColorChangeMap();
        math = findViewById(R.id.math_search);
        chinese = findViewById(R.id.chinese_search);
        english = findViewById(R.id.english_search);
        science = findViewById(R.id.science_search);
        art = findViewById(R.id.art_search);
        pe = findViewById(R.id.pe_search);
        computer = findViewById(R.id.computer_search);
        other = findViewById(R.id.other_search);

        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("math")){
                    math.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("math",true);
                    initCourseList();
                }
                else{
                    math.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("math",false);
                    initCourseList();
                }
            }
        });
        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("chinese")){
                    chinese.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("chinese",true);
                    initCourseList();
                }
                else{
                    chinese.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("chinese",false);
                    initCourseList();
                }
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("english")){
                    english.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("english",true);
                    initCourseList();
                }
                else{
                    english.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("english",false);
                    initCourseList();
                }
            }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("science")){
                    science.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("science",true);
                    initCourseList();
                }
                else{
                    science.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("science",false);
                    initCourseList();
                }
            }
        });
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("art")){
                    art.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("art",true);
                    initCourseList();
                }
                else{
                    art.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("art",false);
                    initCourseList();
                }
            }
        });
        pe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("pe")){
                    pe.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("pe",true);
                    initCourseList();
                }
                else{
                    pe.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("pe",false);
                    initCourseList();
                }
            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("computer")){
                    computer.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("computer",true);
                    initCourseList();
                }
                else{
                    computer.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("computer",false);
                    initCourseList();
                }
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("other")){
                    other.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("other",true);
                    initCourseList();
                }
                else{
                    other.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("other",false);
                    initCourseList();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        Toolbar toolbar = findViewById(R.id.toolbar_student_search);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.search_collapsing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initialTypeButton();
        initCourseList();
        RecyclerView recyclerView = findViewById(R.id.student_search_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        courseAdapter = new CourseAdapter(courseList);
        courseAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                MyUtilities.setCourse(courseList.get(position));
                Intent intent = new Intent(StudentSearchActivity.this, VideoActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });

        recyclerView.setAdapter(courseAdapter);

    }

    private Runnable initCourseRunnable = new Runnable() {
        @Override
        public void run() {
            //                发送json数据
            OkHttpClient client = new OkHttpClient();
            Course getCourse = new Course();
            getCourse.setType(MyUtilities.changeColorToString(changeColor));
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(getCourse);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/search")
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
                Toast.makeText(StudentSearchActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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
