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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class StudentModifyActivity extends AppCompatActivity {

    TextView username;
    TextView type;
    EditText name;
    EditText sex;
    EditText age;
    EditText parent;
    EditText contact;
    Button sure;


    private List<Parent> mList = new ArrayList<>();
    private Parent mInstitution = new Parent();
    private Handler mHandler;
    private Handler modifyHandler;

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
        setContentView(R.layout.activity_student_modify);
        Toolbar toolbar = findViewById(R.id.toolbar_student_modify_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.modify_student_info_username);
        type = findViewById(R.id.modify_student_info_type);
        name = findViewById(R.id.modify_student_info_name);
        sex = findViewById(R.id.modify_student_info_sex);
        age = findViewById(R.id.modify_student_info_age);
        parent = findViewById(R.id.modify_student_info_parent);
        contact = findViewById(R.id.modify_student_info_contact);
        sure = findViewById(R.id.student_modify_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().equals("") ||
                        sex.getText().toString().trim().equals("") ||
                        contact.getText().toString().trim().equals("") ||
                        parent.getText().toString().trim().equals("") ||
                        age.getText().toString().trim().equals("")
                )
                    Toast.makeText(StudentModifyActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                else sendModifyInformation();
            }
        });
        showInformation();
    }


    private Runnable sendModifyInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String addCourseUrl = MyUtilities.MYURL+"/user/modifyParent";
            mInstitution.setChname(name.getText().toString().trim());
            mInstitution.setSex(sex.getText().toString().trim());
            mInstitution.setCmethod(contact.getText().toString().trim());
            mInstitution.setPname(parent.getText().toString().trim());
            mInstitution.setAge(Integer.parseInt(age.getText().toString().trim()));
            String jsonData = gson.toJson(mInstitution);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(addCourseUrl)
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                int addType = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                if (addType != -1) {
                    Toast.makeText(StudentModifyActivity.this, "Succeed!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(StudentModifyActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(StudentModifyActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void sendModifyInformation(){
        HandlerThread sendModifyInformationThread = new HandlerThread("sendModifyInformationThread");
        sendModifyInformationThread.start();
        modifyHandler = new Handler(sendModifyInformationThread.getLooper());
        modifyHandler.post(sendModifyInformationRunnable);
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
                Toast.makeText(StudentModifyActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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
