package com.example.classdesign.Administrator;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.classdesign.R;

import java.util.ArrayList;
import java.util.List;

public class AdministorSignUpActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CheckFragmentPagerAdapter checkFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administor_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar_check);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.check_tab_layout);
        viewPager = findViewById(R.id.check_view_pager);

        initContent();
        initTab();
    }

    private void initTab(){
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.colorSignUp), ContextCompat.getColor(this, R.color.colorHint));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorHint));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initContent(){
        checkFragmentPagerAdapter = new CheckFragmentPagerAdapter(getSupportFragmentManager(),2);
        viewPager.setAdapter(checkFragmentPagerAdapter);
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
