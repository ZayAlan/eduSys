package com.example.classdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.classdesign.Teacher.Teacher;
import com.example.classdesign.Teacher.TeacherApplyActivity;
import com.example.classdesign.Teacher.TeacherCurriculumActivity;
import com.example.classdesign.Teacher.TeacherModifyActivity;
import com.example.classdesign.Teacher.TeacherSelfInfoActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {
    private Item[] items = {
            new Item("课程列表",R.drawable.curriculum),
            new Item("通知公告",R.drawable.board),new Item("新闻阅读",R.drawable.news),
            new Item("教师信息",R.drawable.selfinfo),new Item("信息修改",R.drawable.modify)
    };

    private List<Item> itemList = new ArrayList<>();

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        itemList.clear();
        itemList.addAll(Arrays.asList(items));
        Teacher teacher = new Teacher();
        teacher.setUsername(MyUtilities.getUsername());
        MyUtilities.setEnterTeacher(teacher);
        RecyclerView recyclerView = findViewById(R.id.teacher_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(itemList);
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent intent0 = new Intent(TeacherActivity.this, TeacherCurriculumActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent2 = new Intent(TeacherActivity.this, BoardActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(TeacherActivity.this, NewsActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(TeacherActivity.this, TeacherSelfInfoActivity.class);
                        startActivity(intent4);
                        break;
                    case 4:
                        Intent intent5 = new Intent(TeacherActivity.this, TeacherModifyActivity.class);
                        startActivity(intent5);
                        break;
                }
            }
        });
        recyclerView.setAdapter(itemAdapter);

    }
}
