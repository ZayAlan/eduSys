package com.example.classdesign;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
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

public class ArticleActivity extends AppCompatActivity {

//    private List<News> mNewsList = new ArrayList<>();
    private News news;
    ImageView imageView;
    TextView content;
    TextView time;
    TextView title;
//    private Handler mHandler;
//    @SuppressLint("HandlerLeak")
//    private Handler refreshHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            news = mNewsList.get(0);
//            content.setText(news.getContent());
//            time.setText(news.getTime());
//            title.setText(news.getTitle());
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = findViewById(R.id.article_toolbar);
        title = findViewById(R.id.article_head_title);
        imageView = findViewById(R.id.article_image_view);
        content = findViewById(R.id.article_content);
        time = findViewById(R.id.article_write_time);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.article_collap_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        news = MyUtilities.getNews();
//        collapsingToolbarLayout.setTitle(news.getTitle());
        title.setText(news.getTitle());
        time.setText(news.getTime());
        content.setText(news.getContent());
        Glide.with(this)
                .load(MyUtilities.MYURL+news.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher_round)// 占位图设置：加载过程中显示的图片
                .error(R.mipmap.ic_launcher_round)// 异常占位图
                .transform(new CenterCrop(this))
                .into(imageView);
//        initArticle();
    }
//
//    private Runnable initArticleRunnable = new Runnable() {
//        @Override
//        public void run() {
//            OkHttpClient client = new OkHttpClient();
//            Gson sendGson = new Gson();
//            Request request = new Request.Builder()
//                    .url(MyUtilities.MYURL+"/course/showTeacherTimetable")
//                    .build();
//            Response response = null;
//            try {
//                response = client.newCall(request).execute();
//                String responseData = response.body().string();
//                Gson gson = new Gson();
//                mNewsList = gson.fromJson(responseData,new TypeToken<List<News>>(){}.getType());
//                refreshHandler.sendMessage(refreshHandler.obtainMessage());
//            } catch (IOException e) {
//                Toast.makeText(ArticleActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private void initArticle(){
//        mNewsList.clear();
//        HandlerThread initCourseListThread = new HandlerThread("InitCourseListThread");
//        initCourseListThread.start();
//        mHandler = new Handler(initCourseListThread.getLooper());
//        mHandler.post(initArticleRunnable);
//    }

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
