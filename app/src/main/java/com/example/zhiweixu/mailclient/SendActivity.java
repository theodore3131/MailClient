package com.example.zhiweixu.mailclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import JavaBean.Entity.Mail;

public class SendActivity extends AppCompatActivity{

    private BufferedReader in = null;
    private PrintWriter out = null;
    private Mail mail = null;
    private ExecutorService mThreadPool;
    // 用户登录状态记录
    private SharedPreferences sp;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        // 发送邮件页面的导航栏,把bar加上
        Toolbar toolbar = findViewById(R.id.send_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 设置标题
        toolbar.setTitle("Send Message");
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
            switch (item.getItemId()) {
                case R.id.action_send:

                    // 如果是发送邮件, 则将数据发送到服务器

                    EditText to_edittext = findViewById(R.id.to_editText);
                    String receiver = to_edittext.getText().toString();

                    EditText subject_edittext = findViewById(R.id.subject_editText);
                    String subject = subject_edittext.getText().toString();

                    EditText content_edittext = findViewById(R.id.content_editText);
                    String content = content_edittext.getText().toString();

                    String [] receiver_list = receiver.split(";");

                    sp = getSharedPreferences("user_login_state", Context.MODE_WORLD_READABLE);
                    String sender = sp.getString("username","boss@bro.com");

//                    asdasdasd
                    Log.d("in sendactivity", "看看send里面的用户:" + sender);

                    // 设置输入文本的信息
                    mail = new Mail();
                    mail.setFrom(sender);
                    mail.setToList(receiver_list);
                    mail.setSubject(subject);
                    mail.setContent(content);

                    // 初始化线程池
                    mThreadPool = Executors.newCachedThreadPool();
                    // 利用线程池直接开启一个线程 & 执行该线程
                    mThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 创建Socket对象 & 指定服务端的IP 及 端口号
                                Socket socket = new Socket("47.106.157.18", 9090);
                                OutputStream os = socket.getOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(os);
                                Timestamp time = new Timestamp(new Date().getTime());
                                mail.setTime(time);
                                mail.setSendStat(1);
                                // sender不能为空
                                oos.writeObject(mail);
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    String mess = "" + receiver + "," + subject + "," + content;
                    Toast.makeText(SendActivity.this, mess, Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
            }
        });
    }
}
