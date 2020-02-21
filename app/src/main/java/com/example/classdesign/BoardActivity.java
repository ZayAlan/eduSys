package com.example.classdesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BoardActivity extends AppCompatActivity {

    ImageView imageView;
    Button add;
    EditText content;
    Button publish;

    private static final int REQUEST_CODE_GALLERY = 0x10;// 图库选取图片标识请求码
    private static final int CROP_PHOTO = 0x12;// 裁剪图片标识请求码
    private static final int STORAGE_PERMISSION = 0x20;// 动态申请存储权限标识

    private File imageFile = null;// 声明File对象
    private Uri imageUri = null;// 裁剪后的图片uri

    private Handler publishHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Toolbar toolbar = findViewById(R.id.toolbar_teacher_board);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.teacher_board_picture);
        add = findViewById(R.id.add_teacher_picture_button);
        content = findViewById(R.id.add_teacher_board_content);
        publish = findViewById(R.id.publish_teacher_board_button);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBoardInformation();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });
        requestStoragePermission();
    }

    private void gallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){// 操作成功了

            switch (requestCode){

                case REQUEST_CODE_GALLERY:// 图库选择图片

                    Uri uri = data.getData();// 获取图片的uri

                    Intent intent_gallery_crop = new Intent("com.android.camera.action.CROP");
                    intent_gallery_crop.setDataAndType(uri, "image/*");

                    // 设置裁剪
                    intent_gallery_crop.putExtra("crop", "true");
                    intent_gallery_crop.putExtra("scale", true);
                    // aspectX aspectY 是宽高的比例
                    intent_gallery_crop.putExtra("aspectX", 1);
                    intent_gallery_crop.putExtra("aspectY", 1);
                    // outputX outputY 是裁剪图片宽高
                    intent_gallery_crop.putExtra("outputX", 400);
                    intent_gallery_crop.putExtra("outputY", 400);

                    intent_gallery_crop.putExtra("return-data", false);

                    // 创建文件保存裁剪的图片
                    createImageFile();
                    imageUri = Uri.fromFile(imageFile);

                    if (imageUri != null){
                        intent_gallery_crop.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        intent_gallery_crop.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    }

                    startActivityForResult(intent_gallery_crop, CROP_PHOTO);

                    break;

                case CROP_PHOTO:// 裁剪图片

                    try{

                        if (imageUri != null){
                            displayImage(imageUri);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;

            }

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

    private void createImageFile() {

        try{

            if (imageFile != null && imageFile.exists()){
                imageFile.delete();
            }
            // 新建文件
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            imageFile = new File(Environment.getExternalStorageDirectory(),
                    simpleDateFormat.format(date)+"board.jpg");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void displayImage(Uri imageUri) {
        try{
            // glide根据图片的uri加载图片
            Glide.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher_round)// 占位图设置：加载过程中显示的图片
                    .error(R.mipmap.ic_launcher_round)// 异常占位图
                    .transform(new CenterCrop(this))
                    .into(imageView);
            imageView.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Runnable sendBoardInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            RequestBody body = new MultipartBody.Builder()
                    .addFormDataPart("file",imageFile.getName(),RequestBody.create(MediaType.parse("image/png"),imageFile))
                    .setType(MultipartBody.FORM)
                    .build();
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/message/imageUpload")
                    .post(body)
                    .build();
            Response response = null;

            try {
                response = client.newCall(request).execute();
                String getUrl = response.body().string();
                if(getUrl.equals("error")){
                    Toast.makeText(BoardActivity.this, "can't upload!", Toast.LENGTH_SHORT).show();
                }else{
                    Board board = new Board();
                    board.setUrl(getUrl);
                    board.setPublisher(MyUtilities.getUsername());
                    board.setContent(content.getText().toString().trim());
                    String jsonData = sendGson.toJson(board);
                    RequestBody requestBody2 = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
                    Request request1 = new Request.Builder()
                            .url(MyUtilities.MYURL+"/message/addAnnouncement")
                            .post(requestBody2)
                            .build();
                    Response response1 = client.newCall(request1).execute();
                    int addType = MyUtilities.parseJSONwithGSONtoGetStatus(response1.body().string());
                    if (addType != -1) {
                        Toast.makeText(BoardActivity.this, "Succeed!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(BoardActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (IOException e) {
                Toast.makeText(BoardActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void sendBoardInformation(){
        HandlerThread sendModifyInformationThread = new HandlerThread("sendModifyInformationThread");
        sendModifyInformationThread.start();
        publishHandler = new Handler(sendModifyInformationThread.getLooper());
        publishHandler.post(sendBoardInformationRunnable);
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
