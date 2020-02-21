package com.example.classdesign;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classdesign.Student.Parent;
import com.example.classdesign.User.User;
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

public class CarActivity extends AppCompatActivity implements CheckInterface,ModifyCountInterface {

    public TextView tv_settlement, tv_show_price;
    private CheckBox ck_all;
    private ListView list_shopping_cart;
    private CarAdapter shoppingCartAdapter;
    private TextView tv_edit;
    private boolean flag = false;
    private List<CarItem> shoppingCartBeanList = new ArrayList<>();
    private List<CarItem> sendCarList = new ArrayList<>();
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

    private Handler mHandler;
    private Handler removeHandler;
    private Handler sendHandler;
    private int removePosition;

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            shoppingCartAdapter.refresh(shoppingCartBeanList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        Toolbar toolbar = findViewById(R.id.toolbar_car);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list_shopping_cart = findViewById(R.id.list_shopping_cart);
        ck_all = findViewById(R.id.ck_all);
        ck_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shoppingCartBeanList.size() != 0) {
                    if (ck_all.isChecked()) {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChose(true);
                        }
                        shoppingCartAdapter.refresh(shoppingCartBeanList);
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChose(false);
                        }
                        shoppingCartAdapter.refresh(shoppingCartBeanList);
                    }
                }
                statistics();
            }
        });
        tv_show_price = findViewById(R.id.tv_show_price);
        tv_settlement = findViewById(R.id.tv_settlement);
        tv_settlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToTimeTable();
            }
        });
        tv_edit = findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = !flag;
                if (flag) {
                    tv_edit.setText("完成");
                    shoppingCartAdapter.isShow(false);
                } else {
                    tv_edit.setText("编辑");
                    shoppingCartAdapter.isShow(true);
                }
            }
        });

        //初始化数据list
        initCourseList();

        shoppingCartAdapter = new CarAdapter(this);
        shoppingCartAdapter.setCheckInterface(this);
        shoppingCartAdapter.setModifyCountInterface(this);
        list_shopping_cart.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
    }

    private Runnable sendToTimeTableRunnable = new Runnable() {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(sendCarList);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/addTimetable")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
                Toast.makeText(CarActivity.this, "购买成功！课程已添加到您的课程表！", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(CarActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };

    private void sendToTimeTable(){
        sendCarList.clear();
        for(int i = 0;i<shoppingCartBeanList.size();i++){
            if (shoppingCartBeanList.get(i).isChose()){
                sendCarList.add(shoppingCartBeanList.get(i));
            }
        }
        for (int i=0;i<sendCarList.size();i++){
            shoppingCartBeanList.remove(sendCarList.get(i));
        }
        if (sendCarList.isEmpty()){
            Toast.makeText(this, "未选择要购买的课程！", Toast.LENGTH_SHORT).show();
        }else{
            HandlerThread sendToTimeTableThread = new HandlerThread("sendToTimeTableThread");
            sendToTimeTableThread.start();
            sendHandler = new Handler(sendToTimeTableThread.getLooper());
            sendHandler.post(sendToTimeTableRunnable);
        }

    }

    private Runnable initCourseRunnable = new Runnable() {
        @Override
        public void run() {
            //                发送json数据
            OkHttpClient client = new OkHttpClient();
            Parent parent = new Parent();
            parent.setUsername(MyUtilities.getUsername());
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(parent);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/showShoppingCar")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
                Gson gson = new Gson();
                shoppingCartBeanList = gson.fromJson(responseData,new TypeToken<List<CarItem>>(){}.getType());
                refreshHandler.sendMessage(refreshHandler.obtainMessage());
            } catch (IOException e) {
                Toast.makeText(CarActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void initCourseList(){
        shoppingCartBeanList.clear();
        HandlerThread initCourseListThread = new HandlerThread("InitCourseListThread");
        initCourseListThread.start();
        mHandler = new Handler(initCourseListThread.getLooper());
        mHandler.post(initCourseRunnable);
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        shoppingCartBeanList.get(position).setChose(isChecked);

        if (isAllCheck())
            ck_all.setChecked(true);
        else
            ck_all.setChecked(false);

        shoppingCartAdapter.refresh(shoppingCartBeanList);
        statistics();
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {

        for (CarItem group : shoppingCartBeanList) {
            if (!group.isChose())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            CarItem shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChose()) {
                totalCount++;
                totalPrice += shoppingCartBean.getPrice();
            }
        }
        tv_show_price.setText("合计:" + totalPrice);
        tv_settlement.setText("结算(" + totalCount + ")");
    }



    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            CarItem removeCourse = shoppingCartBeanList.get(removePosition);
            shoppingCartBeanList.remove(removePosition);
            refreshHandler.sendMessage(refreshHandler.obtainMessage());
            OkHttpClient client = new OkHttpClient();
            Gson sendGson = new Gson();
            String jsonData = sendGson.toJson(removeCourse);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
            Request request = new Request.Builder()
                    .url(MyUtilities.MYURL+"/course/deleteShoppingCar")
                    .post(requestBody)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                String responseData = response.body().string();
            } catch (IOException e) {
                Toast.makeText(CarActivity.this, "can't connected!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    };
    private void removeCourse(){
        HandlerThread removeCourseThread = new HandlerThread("removeCourseThread");
        removeCourseThread.start();
        removeHandler = new Handler(removeCourseThread.getLooper());
        removeHandler.post(removeRunnable);
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        removePosition = position;
        removeCourse();
        statistics();
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
