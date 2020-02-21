package com.example.classdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.example.classdesign.Student.Parent;
import com.example.classdesign.Student.ShowMyCommentActivity;
import com.example.classdesign.Student.StudentCurriculumActivity;
import com.example.classdesign.Student.StudentModifyActivity;
import com.example.classdesign.Student.StudentSearchActivity;
import com.example.classdesign.Student.StudentSelfInfoActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentActivity extends AppCompatActivity {

    private Item[] items = {
            new Item("查询",R.drawable.search),new Item("个人课表",R.drawable.curriculum),
            new Item("新闻阅读",R.drawable.news),
            new Item("个人信息",R.drawable.selfinfo),new Item("信息修改",R.drawable.modify),
            new Item("购物车",R.drawable.car),new Item("我的评论",R.drawable.comment)
    };

    private List<Item> itemList = new ArrayList<>();

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        itemList.clear();
        itemList.addAll(Arrays.asList(items));
        Parent parent = new Parent();
        parent.setUsername(MyUtilities.getUsername());
        MyUtilities.setEnterParent(parent);
        RecyclerView recyclerView = findViewById(R.id.student_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(itemList);
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent intent0 = new Intent(StudentActivity.this, StudentSearchActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(StudentActivity.this, StudentCurriculumActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(StudentActivity.this, NewsActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(StudentActivity.this, StudentSelfInfoActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(StudentActivity.this, StudentModifyActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(StudentActivity.this, CarActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(StudentActivity.this, ShowMyCommentActivity.class);
                        startActivity(intent6);
                        break;
                }
            }
        });
        recyclerView.setAdapter(itemAdapter);

    }
}
