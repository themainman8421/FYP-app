package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;

//class that displays how the polls work
public class PollingMethodsExplained extends AppCompatActivity {

    TabLayout tabs;
    SharedPreferences sharedPreferences;
    int Select[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polling_methods_explained);

        //getting the shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setBackground(getDrawable(R.drawable.black_background));
        tabs.getSelectedTabPosition();

        Select = new int[3];
        Arrays.fill(Select,-1);

        updateTabColors();




    }

    public void submit(View view) {
        Intent intent = new Intent(PollingMethodsExplained.this, CreatePoll.class);
        startActivity(intent);
    }

    //creating the top menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating a new menu inflater
        MenuInflater inflater = getMenuInflater();
        //setting which menu to inflate it with
        inflater.inflate(R.menu.topbar_menu, menu);
        return true;
    }

    //    https://github.com/RajNirmal/PreExam2.0/blob/master/app/src/main/java/com/spintum/preexam/Exam_cards.java
    private void updateTabColors(){
        LinearLayout tabsContainer = (LinearLayout) tabs.getChildAt(0);
        for (int i = 0; i < tabs.getTabCount(); i++) {
            if (i == 0){
                LinearLayout item = (LinearLayout) tabsContainer.getChildAt(i);
                TextView tv = (TextView) item.getChildAt(1);
                tv.setTextSize(20);
                tv.setTextColor(Select[i] == -1 ? Color.HSVToColor(new float[]{ 20f, 80f, 100f } ) : Color.BLUE);
            }else if(i == 1){
                LinearLayout item = (LinearLayout) tabsContainer.getChildAt(i);
                TextView tv = (TextView) item.getChildAt(1);
                tv.setTextSize(20);
                tv.setTextColor(Select[i] == -1 ? Color.HSVToColor(new float[]{ 209f, 42f, 94f } ) : Color.BLUE);
            }else{
                LinearLayout item = (LinearLayout) tabsContainer.getChildAt(i);
                TextView tv = (TextView) item.getChildAt(1);
                tv.setTextSize(20);
                tv.setTextColor(Select[i] == -1 ? Color.HSVToColor(new float[]{ 2f, 64f, 85f } ) : Color.BLUE);
            }
        }
    }

    //method for when an item is selected from the menu bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //if the id selected is for the log out button
            case R.id.LogOut:
                //get a shared preference editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //update the values withing the preferences
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(PollingMethodsExplained.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
