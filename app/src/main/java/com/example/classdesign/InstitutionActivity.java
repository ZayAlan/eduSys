package com.example.classdesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.classdesign.Institution.InstitutionApplyActivity;
import com.example.classdesign.Institution.InstitutionModifyActivity;
import com.example.classdesign.Institution.InstitutionSelfInfoActivity;
import com.example.classdesign.Institution.StoreChooseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstitutionActivity extends AppCompatActivity {

    private Item[] items = {
            new Item("店面选择",R.drawable.storechoose),
            new Item("通知公告",R.drawable.board),
            new Item("新闻阅读",R.drawable.news),
            new Item("机构信息",R.drawable.selfinfo),new Item("信息修改",R.drawable.modify)
    };

    private List<Item> itemList = new ArrayList<>();

    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution);
        itemList.clear();
        itemList.addAll(Arrays.asList(items));
        RecyclerView recyclerView = findViewById(R.id.institution_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(itemList);
        itemAdapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent intent0 = new Intent(InstitutionActivity.this, StoreChooseActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent2 = new Intent(InstitutionActivity.this, BoardActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(InstitutionActivity.this, NewsActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(InstitutionActivity.this, InstitutionSelfInfoActivity.class);
                        startActivity(intent4);
                        break;
                    case 4:
                        Intent intent5 = new Intent(InstitutionActivity.this, InstitutionModifyActivity.class);
                        startActivity(intent5);
                        break;
                }
            }
        });
        recyclerView.setAdapter(itemAdapter);

    }


}
