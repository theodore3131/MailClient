package com.example.zhiweixu.mailclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class FriendinfoActivity extends AppCompatActivity {

    private String[] data = {"Grake","Zack Simon","Yukon Chen"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendinfo);
//
//        ListView listView = (ListView) findViewById(R.id.friendinfo);
        //Toast.makeText(this, "this is friend activity", Toast.LENGTH_SHORT).show();
    }
}
