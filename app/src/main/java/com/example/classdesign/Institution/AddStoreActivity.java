package com.example.classdesign.Institution;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classdesign.ActivityCollector;
import com.example.classdesign.MyUtilities;
import com.example.classdesign.R;
import com.example.classdesign.ShowResultActivity;
import com.example.classdesign.SignUpActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddStoreActivity extends AppCompatActivity {

    private Handler mHandler;
    EditText sname;
    EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        ActivityCollector.addActivity(this);
        Toolbar toolbar = findViewById(R.id.toolbar_add_store);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button addStoreSure = findViewById(R.id.add_store_sure);
        sname = findViewById(R.id.add_store_sname);
        address = findViewById(R.id.add_store_address);
        addStoreSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sname.getText().toString().trim().equals("") ||
                        address.getText().toString().trim().equals(""))
                    Toast.makeText(AddStoreActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                else sendAddStoreInformation();
            }
        });
    }

    private Runnable sendAddStoreInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();
            String addStoreUrl = MyUtilities.MYURL+"/course/addStore";
            Store store = new Store(sname.getText().toString().trim(), MyUtilities.getUsername(), address.getText().toString().trim());
            String jsonData = gson.toJson(store);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(addStoreUrl)
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                int addType = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                Intent intent =new Intent();
                setResult(RESULT_OK,intent);
                mHandler.removeCallbacks(sendAddStoreInformationRunnable);
                ActivityCollector.finishAll();
            } catch (IOException e) {
                Toast.makeText(AddStoreActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void sendAddStoreInformation(){
        HandlerThread sendAddStoreInformationThread = new HandlerThread("sendAddStoreInformationThread");
        sendAddStoreInformationThread.start();
        mHandler = new Handler(sendAddStoreInformationThread.getLooper());
        mHandler.post(sendAddStoreInformationRunnable);
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
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
