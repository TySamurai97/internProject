package com.example.tanay.projectscheduler.Activities;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.tanay.projectscheduler.Fragments.ProjectListFragment;
import com.example.tanay.projectscheduler.POJO.Project;
import com.example.tanay.projectscheduler.R;
import com.example.tanay.projectscheduler.Utils.GlobalUtils;
import com.example.tanay.projectscheduler.Utils.ViewPagerAdapter;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    SharedPreferences session;
    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragList = new ArrayList<>();
    ArrayList<String> fragTitleList = new ArrayList<>();

    ArrayList<Project> projectList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tablayout);

        // add new fragments
        fragList.add(new ProjectListFragment());
        fragTitleList.add("Projects");

        ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager(),StartActivity.this, fragList, fragTitleList);

        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                logOut();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOut(){
        GlobalUtils.SessionManager.clear();
        finish();
    }

    public ArrayList<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(ArrayList<Project> pl){
        projectList = pl;
    }
}
