package com.example.zhiweixu.mailclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import JavaBean.Entity.FriendInfo;
import JavaBean.Entity.FriendInfoAdapter;
import JavaBean.Entity.Mail;
import JavaBean.Entity.MailAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static Activity ActivityA;
    ListView listView;
    MySocket socket;

    ObjectOutputStream oos;
    ObjectInputStream ois;
    private List<FriendInfo> friendInfos;

    // 用户登录状态记录
    private SharedPreferences sp;

    // 用户是否存在判断
    private boolean user_exit;

    private List<Mail> mails;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private SwipeRefreshLayout swipeRefreshLayout;

    private String command ;

    private boolean loginState;

    private String uuid;

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 先判断是不是登录状态
        sp = getSharedPreferences("user_login_state", Context.MODE_PRIVATE);
        uuid = sp.getString("uuid", null);


    
        command = "list";

        if(getIntent().getStringExtra("command")!=null) {
            command = getIntent().getStringExtra("command");
        }

        System.out.println(command);

        ActivityA = this;
        if("list".equals(command)) {
            Thread a = new Thread(new Runnable() {
                @Override
                public void run() {
                    loginState = checkLogin();
                }
            });
            a.start();

            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (!loginState){
                System.out.println("you are not log in");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            else {
                Thread a1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        autoRefresh();
                    }
                });
                a1.start();

                try {
                    a1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listView = findViewById(R.id.test_lv);
                final MailAdapter adapter = new MailAdapter(MainActivity.this, R.layout.mail_item, mails);
                listView.setAdapter(adapter);

                swipeRefreshLayout = findViewById(R.id.swipeLayout);
                swipeRefreshLayout.setColorSchemeResources(R.color.colorBule);
                swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
                swipeRefreshLayout.setProgressViewEndTarget(true, 200);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                autoRefresh();
                            }
                        }).start();
                    }
                });


                listView.setAdapter(new ArrayAdapter<Mail>(this, android.R.layout.simple_list_item_1, mails));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                                                        Bundle bundle = new Bundle();
                                                        String subject = mails.get((int) id).getSubject();
                                                        int mail_id = mails.get((int) id).getMail_id();
                                                        String sender = mails.get((int) id).getFrom();
                                                        String to = mails.get((int) id).getTo();
                                                        String content = mails.get((int) id).getContent();
                                                        int readStat = mails.get((int) id).getReadStat();
                                                        System.out.println("readStat" + " " + readStat);

                                                        Timestamp time = mails.get((int) id).getTime();


                                                        bundle.putString("subject", subject);
                                                        bundle.putInt("mail_id", mail_id);
                                                        bundle.putString("sender", sender);
                                                        bundle.putString("to", to);
                                                        bundle.putString("content", content);
                                                        bundle.putString("time", time.toString());
                                                        bundle.putInt("readStat", readStat);
                                                        //                                              bundle.putSerializable("MainActivity",MainActivity.this);

                                                        intent.putExtras(bundle);
                                                        startActivity(intent);

                                                        //我们需要的内容，跳转页面或显示详细信息
                                                    }
                                                }

                );

            }
        }
        else if("friends".equals(command)) {
            Thread a = new Thread(new Runnable() {
                @Override
                public void run() {
                    friendInfos = getFriend();
                }
            });
            a.start();
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            listView = findViewById(R.id.test_lv);
            final FriendInfoAdapter adapter = new FriendInfoAdapter(MainActivity.this, R.layout.mail_item, friendInfos);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, FriendinfoActivity.class);
                    Bundle bundle = new Bundle();
                    String keywords = friendInfos.get((int) id).getkeywordRst();
                    String friendId = friendInfos.get((int) id).getFriendId();
                    bundle.putString("keywords", keywords);
                    bundle.putString("friendId", friendId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeEmail(view);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private boolean checkLogin() {
        try {
            socket = MySocket.getInstance();
            oos = MySocket.getOos();
            ois= MySocket.getOis();
            String str1 = "auth," + uuid;

            oos.writeObject(str1);
            oos.flush();

            // 接受服务器返回的对象;

            Boolean userlogin = (Boolean)ois.readObject();
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + userlogin);
            if (!userlogin){
                return false;
            }else{
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;

    }
    private void getData(){
        try {
                //根据参数做出不同的变化
                String str = command;
                oos.writeObject(str);
                oos.flush();

                System.out.println("client sent list");
                mails = (List<Mail>)ois.readObject();
                System.out.println(mails.size());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<FriendInfo> getFriend(){
        List<FriendInfo> data = new ArrayList<>();
        try {
            try {

                oos = MySocket.getOos();
                ois= MySocket.getOis();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str = "friends";
            oos.writeObject(str);
            oos.flush();

            System.out.println("client sent friends");
            data = (List<FriendInfo>)ois.readObject();
            System.out.println(data.size());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void writeEmail(View view) {
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    public void autoRefresh(){
        getData();
        mHandler.sendEmptyMessage(1);
    }




    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            swipeRefreshLayout.setRefreshing(false);
            MailAdapter adapter=new MailAdapter(MainActivity.this,R.layout.mail_item,mails);
            listView.setAdapter(adapter);
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
    @Override


    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            sp = getSharedPreferences("user_login_state", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("uuid");
            editor.clear();
            editor.commit();
            // sfsdf
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            // Handle the camera action
        } else if (id == R.id.nav_draftbox) {
            Intent intent =new Intent(MainActivity.this, DraftActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("command","draft");
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (id == R.id.nav_unread) { // 未读



        } else if (id == R.id.nav_sent) {  // 发件箱
            Intent intent =new Intent(MainActivity.this, DraftActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("command","sent");
            intent.putExtras(bundle);
            startActivity(intent);

        } else if (id == R.id.nav_friends) {
            Toast.makeText(MainActivity.this,"friend clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("command", "friends");
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
