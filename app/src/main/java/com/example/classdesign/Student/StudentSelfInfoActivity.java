package com.example.classdesign.Student;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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

public class StudentSelfInfoActivity extends AppCompatActivity {

    TextView username;
    TextView type;
    TextView name;
    TextView sex;
    TextView age;
    TextView parent;
    TextView contact;

    private List<Parent> mList = new ArrayList<>();
    private Parent mInstitution = new Parent();
    private Handler mHandler;
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mInstitution = mList.get(0);
            username.setText(mInstitution.getUsername());
            type.setText("学生家长");
            name.setText(mInstitution.getChname());
            sex.setText(mInstitution.getSex());
            age.setText(Integer.toString(mInstitution.getAge()));
            parent.setText(mInstitution.getPname());
            contact.setText(mInstitution.getCmethod());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_self_info);
        Toolbar toolbar = findViewById(R.id.toolbar_student_self_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.show_student_info_username);
        type = findViewById(R.id.show_student_info_type);
        name = findViewById(R.id.show_student_info_name);
        sex = findViewById(R.id.show_student_info_sex);
        age = findViewById(R.id.show_student_info_age);
        parent = findViewById(R.id.show_student_info_parent);
        contact = findViewById(R.id.show_student_info_contact);
        showInformation();
    }

    private Runnable showInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            Parent sendParent = new Parent();
            sendParent.setUsername(MyUtilities.getUsername());
            String jsonData = sendGson.toJson(sendParent);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/user/showParent")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                mList = gson.fromJson(responseData,new TypeToken<List<Parent>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(StudentSelfInfoActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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
