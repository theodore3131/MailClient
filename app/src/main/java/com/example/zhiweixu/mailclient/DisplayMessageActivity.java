package com.example.zhiweixu.mailclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import JavaBean.Entity.Mail;

import static com.example.zhiweixu.mailclient.MainActivity.ActivityA;

public class DisplayMessageActivity extends AppCompatActivity {
    private ExecutorService mThreadPool ;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.receive_menu,menu);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityA.finish();
            Intent intent =new Intent(DisplayMessageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    boolean deleStat = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        final Bundle bundle=this.getIntent().getExtras();
        final String subject=bundle.getString("subject");
        TextView subject2=findViewById(R.id.Subject2);
        subject2.setText(subject);

        String from=bundle.getString("sender");
        TextView from2=findViewById(R.id.From2);
        from2.setText(from);

        String time=bundle.getString("time");
        TextView time2=findViewById(R.id.Time2);
        time2.setText(time);

        String content=bundle.getString("content");
        TextView content1=findViewById(R.id.Content1);
        content1.setText(content);


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
                Intent intent =new Intent(DisplayMessageActivity.this, MainActivity.class);
                startActivity(intent);
                ActivityA.finish();


                finish();
            }
        });
        System.out.println("readStat"+" "+bundle.getInt("readStat"));
        if(bundle.getInt("readStat")==0){
            mThreadPool = Executors.newCachedThreadPool();
            // 利用线程池直接开启一个线程 & 执行该线程
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 创建Socket对象 & 指定服务端的IP 及 端口号

                        ObjectOutputStream oos = MySocket.getOos();
                        String str = "read"+' '+bundle.getInt("mail_id");
                        oos.writeObject(str);

                        ObjectInputStream ois=MySocket.getOis();
                        try{
                            ois.readObject();
                        }catch (ClassNotFoundException e){

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }




        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Thread a1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 创建Socket对象 & 指定服务端的IP 及 端口号
                            ObjectOutputStream oos = MySocket.getOos();
                            ObjectInputStream ois = MySocket.getOis();
                            String str = "dele"+' '+bundle.getInt("mail_id");
                            oos.writeObject(str);
                            deleStat = (Boolean)ois.readObject();
                            System.out.println(deleStat);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                a1.start();

                try {
                    a1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (deleStat) {
                    Intent intent = new Intent(DisplayMessageActivity.this, MainActivity.class);
                    startActivity(intent);
                    ActivityA.finish();
                    finish();
                }
                return false;
            }
        });

    }
}
