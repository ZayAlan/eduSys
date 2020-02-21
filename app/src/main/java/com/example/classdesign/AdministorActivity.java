package com.example.classdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.classdesign.Administrator.AdministorBoardActivity;
import com.example.classdesign.Administrator.AdministorNewsActivity;
import com.example.classdesign.Administrator.AdministorSignUpActivity;
import com.example.classdesign.Administrator.EditCommentActivity;
import com.example.classdesign.Administrator.EditCommentAdapter;
import com.example.classdesign.Administrator.NewsListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdministorActivity extends AppCompatActivity {

    private Item[] items = {
            new Item("注册审核",R.drawable.signup),
            new Item("新闻列表",R.drawable.news),
            new Item("编辑评论",R.drawable.comment)
    };

    private List<Item> itemList = new ArrayList<>();

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administor);
        itemList.clear();
        itemList.addAll(Arrays.asList(items));
        RecyclerView recyclerView = findViewById(R.id.administor_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(itemList);
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent intent0 = new Intent(AdministorActivity.this, AdministorSignUpActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(AdministorActivity.this, NewsListActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent3 = new Intent(AdministorActivity.this, EditCommentActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        });
        recyclerView.setAdapter(itemAdapter);

    }
}
