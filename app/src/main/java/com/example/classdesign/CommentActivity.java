package com.example.classdesign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.internal.runners.statements.RunAfters;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentActivity extends AppCompatActivity {

    EditText commentContent;
    private int lightButton = 0;
    private boolean light = false;
    private Handler mHandler;
    Button add;
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = findViewById(R.id.toolbar_add_comment);
        ActivityCollector.addActivity(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commentContent =findViewById(R.id.add_comment_content);
        add = findViewById(R.id.add_comment_button);
        one = findViewById(R.id.one_point);
        two = findViewById(R.id.two_point);
        three = findViewById(R.id.three_point);
        four = findViewById(R.id.four_point);
        five = findViewById(R.id.five_point);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentContent.getText().toString().trim().equals("") ||
                       lightButton == 0
                )
                    Toast.makeText(CommentActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                else sendAddCommentInformation();
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightButton != 0){
                    downButton(lightButton);
                }
                light = true;
                lightButton = 1;
                one.setTextColor(Color.parseColor("#FB7299"));
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightButton != 0){
                    downButton(lightButton);
                }
                light = true;
                lightButton = 2;
                two.setTextColor(Color.parseColor("#FB7299"));
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightButton != 0){
                    downButton(lightButton);
                }
                light = true;
                lightButton = 3;
                three.setTextColor(Color.parseColor("#FB7299"));
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightButton != 0){
                    downButton(lightButton);
                }
                light = true;
                lightButton = 4;
                four.setTextColor(Color.parseColor("#FB7299"));
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lightButton != 0){
                    downButton(lightButton);
                }
                light = true;
                lightButton = 5;
                five.setTextColor(Color.parseColor("#FB7299"));
            }
        });

    }

    void downButton(int i){
        switch (i){
            case 1:
                one.setTextColor(Color.parseColor("#999999"));
                break;
            case 2:
                two.setTextColor(Color.parseColor("#999999"));
                break;
            case 3:
                three.setTextColor(Color.parseColor("#999999"));
                break;
            case 4:
                four.setTextColor(Color.parseColor("#999999"));
                break;
            case 5:
                five.setTextColor(Color.parseColor("#999999"));
                break;
        }
    }

    private Runnable sendAddCommentInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Remark sendRemark = new Remark();
            sendRemark.setArticle(commentContent.getText().toString().trim());
            sendRemark.setCid(MyUtilities.getCourse().getCid());
            sendRemark.setRemarker(MyUtilities.getUsername());
            sendRemark.setScore(lightButton);
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(sendRemark);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/addRemark")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                int status = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                if (status == -2){
                    Toast.makeText(CommentActivity.this, "您未选择该课，不能做出评价", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent =new Intent();
                    setResult(RESULT_OK,intent);
                    mHandler.removeCallbacks(sendAddCommentInformationRunnable);
                    ActivityCollector.finishAll();
                }
            } catch (IOException e) {
                Toast.makeText(CommentActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void sendAddCommentInformation(){
        HandlerThread sendAddCommentInformationThread = new HandlerThread("sendAddCommentInformationThread");
        sendAddCommentInformationThread.start();
        mHandler = new Handler(sendAddCommentInformationThread.getLooper());
        mHandler.post(sendAddCommentInformationRunnable);
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
