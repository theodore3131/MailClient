package com.example.zhiweixu.mailclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import JavaBean.Entity.Mail;

public class DisplayMessageActivity extends AppCompatActivity {
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receive_menu,menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        Bundle bundle=this.getIntent().getExtras();
        String name=bundle.getString("name");
        Log.i("获取到的name值",name);
        TextView subject2=findViewById(R.id.Subject2);
        subject2.setText(name);

        Toolbar toolbar = findViewById(R.id.receive_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("Receive Message");
        //添加导航位置图标
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 退出的时候自动保存


                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent =new Intent(DisplayMessageActivity.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
//        Toolbar myChildToolbar=(Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myChildToolbar);
//        ActionBar ab=getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
    }
}
