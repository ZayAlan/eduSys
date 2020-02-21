package com.example.classdesign;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.EntityIterator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.classdesign.Institution.Store;
import com.example.classdesign.Teacher.Teacher;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddCourseActivity extends AppCompatActivity {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(1000, TimeUnit.MINUTES)
            .readTimeout(1000, TimeUnit.MINUTES)
            .writeTimeout(1000, TimeUnit.MINUTES)
            .build();
    private ProgressBar uploadProgress;

    private Handler mHandler;
    private EditText course_sname;
    private EditText course_teacherName;
    private EditText course_address;
    private EditText course_content;
    private EditText course_price;
    private EditText course_time_year;
    private EditText course_time_month;
    private EditText course_time_day;

    private Button math;
    private Button chinese;
    private Button english;
    private Button science;
    private Button art;
    private Button pe;
    private Button computer;
    private Button other;

    private Button addVideo;

    private static final int REQUEST_CODE_GALLERY = 0x10;// 图库选取视频标识请求码
    private static final int STORAGE_PERMISSION = 0x20;// 动态申请存储权限标识

    private File videoFile = null;// 声明File对象
    private String videoTitle = "";

    private Map<String, Boolean> changeColor = new HashMap<>();
    private List<String> type = new ArrayList<>();

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
        math = findViewById(R.id.math);
        chinese = findViewById(R.id.chinese);
        english = findViewById(R.id.english);
        science = findViewById(R.id.science);
        art = findViewById(R.id.art);
        pe = findViewById(R.id.pe);
        computer = findViewById(R.id.computer);
        other = findViewById(R.id.other);

        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("math")){
                    math.setBackgroundResource(R.drawable.signupshape);
                    math.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("math",true);
                }
                else{
                    math.setBackgroundResource(R.drawable.signupshape2);
                    math.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("math",false);
                }
            }
        });
        chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("chinese")){
                    chinese.setBackgroundResource(R.drawable.signupshape);
                    chinese.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("chinese",true);
                }
                else{
                    chinese.setBackgroundResource(R.drawable.signupshape2);
                    chinese.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("chinese",false);
                }
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("english")){
                    english.setBackgroundResource(R.drawable.signupshape);
                    english.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("english",true);
                }
                else{
                    english.setBackgroundResource(R.drawable.signupshape2);
                    english.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("english",false);
                }
            }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("science")){
                    science.setBackgroundResource(R.drawable.signupshape);
                    science.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("science",true);
                }
                else{
                    science.setBackgroundResource(R.drawable.signupshape2);
                    science.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("science",false);
                }
            }
        });
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("art")){
                    art.setBackgroundResource(R.drawable.signupshape);
                    art.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("art",true);
                }
                else{
                    art.setBackgroundResource(R.drawable.signupshape2);
                    art.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("art",false);
                }
            }
        });
        pe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("pe")){
                    pe.setBackgroundResource(R.drawable.signupshape);
                    pe.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("pe",true);
                }
                else{
                    pe.setBackgroundResource(R.drawable.signupshape2);
                    pe.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("pe",false);
                }
            }
        });
        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("computer")){
                    computer.setBackgroundResource(R.drawable.signupshape);
                    computer.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("computer",true);
                }
                else{
                    computer.setBackgroundResource(R.drawable.signupshape2);
                    computer.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("computer",false);
                }
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!changeColor.get("other")){
                    other.setBackgroundResource(R.drawable.signupshape);
                    other.setTextColor(Color.parseColor("#FB7299"));
                    changeColor.put("other",true);
                }
                else{
                    other.setBackgroundResource(R.drawable.signupshape2);
                    other.setTextColor(Color.parseColor("#999999"));
                    changeColor.put("other",false);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        ActivityCollector.addActivity(this);
        Toolbar toolbar = findViewById(R.id.toolbar_add_course);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialTypeButton();
        Button addCourseSure = findViewById(R.id.add_course_sure);
        course_sname = findViewById(R.id.add_course_sname);
        course_teacherName = findViewById(R.id.add_course_teacher_name);
        course_address = findViewById(R.id.add_course_address);
        course_content = findViewById(R.id.add_course_content);
        course_price = findViewById(R.id.add_course_price);
        course_time_year = findViewById(R.id.add_course_time_y);
        course_time_month = findViewById(R.id.add_course_time_m);
        course_time_day = findViewById(R.id.add_course_time_d);

        addVideo = findViewById(R.id.add_course_video);

        addVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideo();
            }
        });

        addCourseSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<String, Boolean> entry : changeColor.entrySet()) {
                    String key = entry.getKey();
                    Boolean value = entry.getValue();
                    if (value){
                        type.add(key);
                    }
                }
                if(course_sname.getText().toString().trim().equals("") ||
                        course_teacherName.getText().toString().trim().equals("") ||
                        course_address.getText().toString().trim().equals("") ||
                        course_content.getText().toString().trim().equals("") ||
                        course_price.getText().toString().trim().equals("") ||
                        course_time_year.getText().toString().trim().equals("") || Integer.parseInt(course_time_year.getText().toString().trim())>2200 ||
                        Integer.parseInt(course_time_year.getText().toString().trim())<1900 ||
                        course_time_month.getText().toString().trim().equals("") || Integer.parseInt(course_time_month.getText().toString().trim())> 12 ||
                        Integer.parseInt(course_time_month.getText().toString().trim())<1 ||
                        course_time_day.getText().toString().trim().equals("") || Integer.parseInt(course_time_day.getText().toString().trim())> 31 ||
                        Integer.parseInt(course_time_day.getText().toString().trim())< 1 ||
                        type.isEmpty()
                )
                    Toast.makeText(AddCourseActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                else sendAddCourseInformation();
            }
        });

        uploadProgress = findViewById(R.id.post_progress);

        requestStoragePermission();
    }



    private void getVideo(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // 视频路径：MediaStore.Audio.Media.DATA
                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
//                        Date date = new Date(System.currentTimeMillis());
                        videoFile = new File(videoPath);
                        addVideo.setText("添加成功");
                    }
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestStoragePermission() {

        int hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED){
            // 拥有权限，可以执行涉及到存储权限的操作
        }else {
            // 没有权限，向用户申请该权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 用户同意，执行相应操作
            }else {
                // 用户不同意，向用户展示该权限作用
            }
        }

    }

    private Runnable sendAddCourseInformationRunnable = new Runnable() {
        @Override
        public void run() {
            Gson gson = new Gson();
            MultipartBody body = new MultipartBody.Builder()
                    .addFormDataPart("file",videoFile.getName(),RequestBody.create(MediaType.parse("video/mp4"),videoFile))
                    .setType(MultipartBody.FORM)
                    .build();
            RequestBody requestBody1 = ProgressHelper.withProgress(body, new ProgressUIListener() {
                @Override
                public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                    uploadProgress.setProgress((int) (100 * percent));
                }
            });
            Request request1 = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/videoUpload")
                    .post(requestBody1)
                    .build();
            Response response1 = null;
            try {
                response1 = client.newCall(request1).execute();
                String getUrl = response1.body().string();
                if(getUrl.equals("error")){
                    Toast.makeText(AddCourseActivity.this, "can't upload!", Toast.LENGTH_SHORT).show();
                }else {
                    String addCourseUrl = MyUtilities.MYURL+"/course/addCourse";
                    Course course = new Course();
                    course.setUrl(getUrl);
                    int roleType = MyUtilities.getKindRole();
                    switch (roleType){
                        case 1:
                            Store enterStore = MyUtilities.getEnterStore();
                            course.setSid(enterStore.getSid());
                            course.setUsername(enterStore.getUsername());
                            course.setFlag(1);
                            break;
                        case 2:
                            Teacher teacher = MyUtilities.getEnterTeacher();
                            course.setSid(-1);
                            course.setUsername(teacher.getUsername());
                            course.setFlag(0);
                            break;
                    }
                    course.setType(MyUtilities.changeListToString(type));
                    course.setCname(course_sname.getText().toString().trim());
                    course.setTeachername(course_teacherName.getText().toString().trim());
                    course.setAddress(course_address.getText().toString().trim());
                    course.setContent(course_content.getText().toString().trim());
                    course.setTime(MyUtilities.parseStringToDate(course_time_year.getText().toString().trim(),
                            course_time_month.getText().toString().trim(), course_time_day.getText().toString().trim()).toString());
                    course.setPrice(Double.parseDouble(course_price.getText().toString().trim()));
                    course.setEvaluation(0);
                    String jsonData = gson.toJson(course);
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
                    Request request = new Request.Builder()
                            .url(addCourseUrl)
                            .post(requestBody)
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    int addType = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                    if (addType != -1) {
                        Intent intent =new Intent();
                        setResult(RESULT_OK,intent);
                        mHandler.removeCallbacks(sendAddCourseInformationRunnable);
                        ActivityCollector.finishAll();
                    }else{
                        Toast.makeText(AddCourseActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void sendAddCourseInformation(){
        HandlerThread sendAddCourseInformationThread = new HandlerThread("sendAddCourseInformationThread");
        sendAddCourseInformationThread.start();
        mHandler = new Handler(sendAddCourseInformationThread.getLooper());
        mHandler.post(sendAddCourseInformationRunnable);
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
