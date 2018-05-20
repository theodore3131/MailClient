package com.example.zhiweixu.mailclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import JavaBean.Entity.Mail;

public class SendActivity extends AppCompatActivity {

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
                        Mail mail = new Mail();
                        EditText to_edittext = findViewById(R.id.to_editText);
                        String receiver = to_edittext.getText().toString();

                        EditText subject_edittext = findViewById(R.id.subject_editText);
                        String subject = subject_edittext.getText().toString();

                        EditText content_edittext = findViewById(R.id.content_editText);
                        String content = content_edittext.getText().toString();

                        mail.setReceiver(receiver);
                        mail.setSubject(subject);
                        mail.setContent(content);
                        String mess = "" + receiver + "," + subject + "," + content;
                        Toast.makeText(SendActivity.this, mess, Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }
}
