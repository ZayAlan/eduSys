package com.example.classdesign.Teacher;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

public class TeacherSelfInfoActivity extends AppCompatActivity {

    TextView username;
    TextView type;
    TextView name;
    TextView sex;
    TextView age;
    TextView domain;
    TextView workage;
    TextView id;
    TextView suitage;
    TextView contact;
    TextView resume;

    private List<Teacher> mList = new ArrayList<>();
    private Teacher mInstitution = new Teacher();
    private Handler mHandler;
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mInstitution = mList.get(0);
            username.setText(mInstitution.getUsername());
            type.setText("个人教师");
            domain.setText(mInstitution.getTerritory());
            id.setText(mInstitution.getId());
            name.setText(mInstitution.getName());
            age.setText(Integer.toString(mInstitution.getAge()));
            sex.setText(mInstitution.getSex());
            suitage.setText(Integer.toString(mInstitution.getAdapagel())+" 岁 - "+Integer.toString(mInstitution.getAdapageh())+" 岁");
            contact.setText(mInstitution.getCmethod());
            resume.setText(mInstitution.getResume());
            workage.setText(Integer.toString(mInstitution.getCareer()));        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_self_info);
        Toolbar toolbar = findViewById(R.id.toolbar_teacher_self_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.show_teacher_info_username);
        type = findViewById(R.id.show_teacher_info_type);
        domain = findViewById(R.id.show_teacher_info_domain);
        id = findViewById(R.id.show_teacher_info_id);
        name = findViewById(R.id.show_teacher_info_name);
        suitage = findViewById(R.id.show_teacher_info_suitage);
        contact = findViewById(R.id.show_teacher_info_contact);
        resume = findViewById(R.id.show_teacher_info_resume);
        sex = findViewById(R.id.show_teacher_info_sex);
        age = findViewById(R.id.show_teacher_info_age);
        workage = findViewById(R.id.show_teacher_info_work);
        showInformation();
    }

    private Runnable showInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            Teacher sendInstitution = new Teacher();
            sendInstitution.setUsername(MyUtilities.getUsername());
            String jsonData = sendGson.toJson(sendInstitution);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/user/showTeacher")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                mList = gson.fromJson(responseData,new TypeToken<List<Teacher>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(TeacherSelfInfoActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void showInformation(){
        HandlerThread showInformationThread = new HandlerThread("showInformationThread");
        showInformationThread.start();
        mHandler = new Handler(showInformationThread.getLooper());
        mHandler.post(showInformationRunnable);
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
