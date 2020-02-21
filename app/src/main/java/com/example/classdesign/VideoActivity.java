package com.example.classdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.classdesign.User.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VideoActivity extends AppCompatActivity {

    private List<Board> boardList = new ArrayList<>();

    TextView courseName;
    TextView courseFare;
    TextView courseScore;
    ImageView imageView;
    TextView boardContent;
    TextView userName;
    TextView comment;
    Board board;
    Button addToCar;

    private boolean existInCar = false;

    private String videoUrl = "";
    private Course course;
    private NiceVideoPlayer niceVideoPlayer;
    private Handler mHandler;
    private Handler addCarHandler;
    private Handler showCommentHandler;

    private CommentAdapter commentAdapter;
    private List<Remark> remarks = new ArrayList<>();


    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            initVideo();
//            runOnUiThread(new Runnable() {//使用runOnUIThread()方法更新主线程
//                @Override
//                public void run() {
//                                   }
//            });
            courseName.setText(course.getCname());
            courseFare.setText(Double.toString(course.getPrice())+"元");
            courseScore.setText(String.format("%.2f",course.getEvaluation()/5*100)+"%");
            userName.setText(course.getUsername());
            board = boardList.get(0);
            Glide.with(VideoActivity.this)
                    .load(MyUtilities.MYURL+board.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher_round)// 占位图设置：加载过程中显示的图片
                    .error(R.mipmap.ic_launcher_round)// 异常占位图
                    .transform(new CenterCrop(VideoActivity.this))
                    .into(imageView);
            boardContent.setText(board.getContent());
            commentAdapter.refresh(remarks);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Toolbar toolbar = findViewById(R.id.toolbar_video);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("课程试听");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        courseName = findViewById(R.id.video_course_name);
        courseFare = findViewById(R.id.video_course_fare);
        courseScore = findViewById(R.id.video_score);
        imageView = findViewById(R.id.video_board_picture);
        boardContent = findViewById(R.id.video_board_content);
        userName = findViewById(R.id.video_username);
        comment = findViewById(R.id.video_comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoActivity.this, CommentActivity.class);
                startActivityForResult(intent,3);
            }
        });
        addToCar = findViewById(R.id.add_to_car);
        addToCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(existInCar)
                {
                    addToCar.setText("已添加");
                    addToCar.setBackgroundColor(getResources().getColor(R.color.colorShowInfo));
                }else{
                    addToCarInformation();
                    existInCar = true;
                }
            }
        });
        course = MyUtilities.getCourse();
        videoUrl = MyUtilities.MYURL+course.getUrl();
        getInformation();
        RecyclerView recyclerView = findViewById(R.id.comment_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(remarks);
        recyclerView.setAdapter(commentAdapter);
    }

    private Runnable addToCarRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            CarItem sendCar = new CarItem();
            sendCar.setAddress(course.getAddress());
            sendCar.setBuyer(MyUtilities.getUsername());
            sendCar.setCid(course.getCid());
            sendCar.setCname(course.getCname());
            sendCar.setContent(course.getContent());
            sendCar.setPrice(course.getPrice());
            sendCar.setPublisher(course.getUsername());
            sendCar.setTeachername(course.getTeachername());
            sendCar.setTime(course.getTime());
            sendCar.setChose(false);
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(sendCar);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/addShoppingCar")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                int addType = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                if (addType == 1) {
                    Toast.makeText(VideoActivity.this, "加入购物车成功！", Toast.LENGTH_SHORT).show();
                    existInCar = true;
                }else if (addType == -1){
                    Toast.makeText(VideoActivity.this, "您的购物车中已有该课程！", Toast.LENGTH_SHORT).show();
                    existInCar = true;
                }else if (addType == -2) {
                    Toast.makeText(VideoActivity.this, "您已选过该课程！", Toast.LENGTH_SHORT).show();
                    existInCar = true;
                } else{
                    Toast.makeText(VideoActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(VideoActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void addToCarInformation(){
        HandlerThread addToCarThread = new HandlerThread("addToCarThread");
        addToCarThread.start();
        addCarHandler = new Handler(addToCarThread.getLooper());
        addCarHandler.post(addToCarRunnable);
    }

    private Runnable getInformationRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            User sendUser = new User();
            sendUser.setUsername(course.getUsername());
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(sendUser);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/message/showAnnouncement")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                boardList = gson.fromJson(responseData,new TypeToken<List<Board>>(){}.getType());
                String getCommentData = sendGson.toJson(course);
                RequestBody requestBody1 = FormBody.create(MediaType.parse("application/json; charset=utf-8"), getCommentData);
                Request request1 = new Request.Builder()
                        .url(MyUtilities.MYURL+"/course/showCourseRemark")
                        .post(requestBody1)
                        .build();
                Response response1 = client.newCall(request1).execute();
                String responseData1 = response1.body().string();
                Gson gson1 = new Gson();
                remarks = gson1.fromJson(responseData1,new TypeToken<List<Remark>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(VideoActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void getInformation(){
        HandlerThread getInformationThread = new HandlerThread("getInformationThread");
        getInformationThread.start();
        mHandler = new Handler(getInformationThread.getLooper());
        mHandler.post(getInformationRunnable);
    }

    private void initVideo(){
        niceVideoPlayer = findViewById(R.id.add_nice_video_player);
        niceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK);
        niceVideoPlayer.setUp(videoUrl,null);
        TxVideoPlayerController controller = new TxVideoPlayerController(VideoActivity.this);
        controller.setTitle(course.getCname());
        controller.setLenght(98000);
        controller.setImage(R.drawable.pic);
//        Glide.with(this)
//                .load(R.drawable.pic)
//                .placeholder(R.drawable.pic)
//                .crossFade()
//                .into(controller.imageView());
        niceVideoPlayer.setController(controller);
    }

    public void enterTinyWindow(View view) {
        if (niceVideoPlayer.isIdle()) {
            Toast.makeText(this, "要点击播放后才能进入小窗口", Toast.LENGTH_SHORT).show();
        } else {
            niceVideoPlayer.enterTinyWindow();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 3:
                if (resultCode == RESULT_OK){
                    mHandler.removeCallbacks(getInformationRunnable);
                    getInformation();
                }
                break;
        }
    }
}
