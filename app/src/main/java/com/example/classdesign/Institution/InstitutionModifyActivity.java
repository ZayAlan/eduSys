package com.example.classdesign.Institution;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.classdesign.ActivityCollector;
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

public class InstitutionModifyActivity extends AppCompatActivity {

    TextView username;
    TextView type;
    EditText domain;
    TextView id;
    EditText address;
    TextView suitage;
    EditText contact;
    EditText resume;
    Button sure;


    private List<Institution> mList = new ArrayList<>();
    private Institution mInstitution = new Institution();
    private Handler mHandler;
    private Handler modifyHandler;
    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mInstitution = mList.get(0);
            username.setText(mInstitution.getUsername());
            type.setText("教育机构");
            domain.setText(mInstitution.getTerritory());
            id.setText(mInstitution.getEid());
            address.setText(mInstitution.getAddress());
            suitage.setText(Integer.toString(mInstitution.getAdapagel())+" 岁 - "+Integer.toString(mInstitution.getAdapageh())+" 岁");
            contact.setText(mInstitution.getCmethod());
            resume.setText(mInstitution.getResume());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_modify);
        Toolbar toolbar = findViewById(R.id.toolbar_inst_modify_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username = findViewById(R.id.modify_insti_info_username);
        type = findViewById(R.id.modify_insti_info_type);
        domain = findViewById(R.id.modify_insti_info_domain);
        id = findViewById(R.id.modify_insti_info_id);
        address = findViewById(R.id.modify_insti_info_address);
        suitage = findViewById(R.id.modify_insti_info_suitage);
        contact = findViewById(R.id.modify_insti_info_contact);
        resume = findViewById(R.id.modify_insti_info_resume);
        sure = findViewById(R.id.inst_modify_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(domain.getText().toString().trim().equals("") ||
                        address.getText().toString().trim().equals("") ||
                        contact.getText().toString().trim().equals("") ||
                        resume.getText().toString().trim().equals("")
                )
                    Toast.makeText(InstitutionModifyActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
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
            String addCourseUrl = MyUtilities.MYURL+"/user/modifyEduInstitution";
            mInstitution.setTerritory(domain.getText().toString().trim());
            mInstitution.setAddress(address.getText().toString().trim());
            mInstitution.setCmethod(contact.getText().toString().trim());
            mInstitution.setResume(resume.getText().toString().trim());
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
                    Toast.makeText(InstitutionModifyActivity.this, "Succeed!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(InstitutionModifyActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(InstitutionModifyActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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
            Institution sendInstitution = new Institution();
            sendInstitution.setUsername(MyUtilities.getUsername());
            String jsonData = sendGson.toJson(sendInstitution);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/user/showEduInstitution")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                mList = gson.fromJson(responseData,new TypeToken<List<Institution>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(InstitutionModifyActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
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
