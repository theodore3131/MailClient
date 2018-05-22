package com.example.zhiweixu.mailclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;
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
import java.util.List;
import java.util.concurrent.ExecutorService;

import JavaBean.Entity.Mail;
import JavaBean.Entity.MailAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private  ListView listView;
        private Socket socket;

        private List<Mail> mails;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    private  ExecutorService mThreadPool ;
    // 利用线程池直接开启一个线程 & 执行该线程




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);




        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("47.106.157.18", 9091);
                    InputStream ins = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();

                    BufferedReader brd = new BufferedReader(new InputStreamReader(ins));
                    ObjectOutputStream oos = new ObjectOutputStream(os);
                    ObjectInputStream ois=new ObjectInputStream(ins);

                    String str = "list";
                    oos.writeObject(str);
                    oos.flush();

                    System.out.println("client sent list");
                    mails = (List<Mail>)ois.readObject();
                    System.out.println(mails.size());
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



//        mThreadPool.execute(new Runnable(){
//
//            @Override
//            public void run() {
//                try {
//                    socket = new Socket("localhost", 9091);
//                    InputStream ips = socket.getInputStream();
//                    ObjectInputStream ois = new ObjectInputStream(ips);
//                    mail = new ArrayList<>();
//                    mail = (List<Mail>) ois.readObject();
//                    ois.close();
//                    socket.close();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        List<Mail> mails=new ArrayList<>();
//        for(int i=0;i<20;i++){
//            Mail mai=new Mail();
//            mai.setMail_id(i);
//            mai.setSubject(i+" "+i);
//            Timestamp time = new Timestamp(new Date().getTime());
//            mai.setTime(time);
//            mai.setContent("hhhhhhhhhh"+i);
//            mails.add(mai);
//        }
        listView =(ListView) findViewById(R.id.test_lv);

        MailAdapter adapter=new MailAdapter(MainActivity.this,R.layout.mail_item,mails);
        listView.setAdapter(adapter);

   //     listView.setAdapter(new ArrayAdapter<Mail>(this, android.R.layout.simple_list_item_1, mails ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent =new Intent(MainActivity.this, DisplayMessageActivity.class);
                                                Bundle bundle=new Bundle();
                                                String subject=mails.get((int)id).getSubject();
                                                int mail_id= mails.get((int)id).getMail_id();
                                                String sender=mails.get((int)id).getFrom();
                                                String to=mails.get((int)id).getTo();
                                                String content=mails.get((int)id).getContent();
                                                Timestamp time=mails.get((int)id).getTime();

                                                bundle.putString("subject",subject);
                                                bundle.putInt("mail_id",mail_id);
                                                bundle.putString("sender",sender);
                                                bundle.putString("to",to);
                                                bundle.putString("content",content);
                                                bundle.putString("time",time.toString());

                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                //我们需要的内容，跳转页面或显示详细信息
                                            }
                                        }

        );



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

//    private List<Mail> getData(){
//        List<Mail> data = new ArrayList<Mail>();
//        for(int i=0;i<mails.size();i++){
//            data.add(mails.get(i));
//        }
////        for(int i=0;i<mail.size();i++){
////            data.add(mail.get(i).getReceiver());
////        }
//
//
//
//
//        return data;
//    }
    public void writeEmail(View view) {
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }


    private static boolean isExit = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
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

        } else if (id == R.id.nav_unread) {

        } else if (id == R.id.nav_sent) {

        } else if (id == R.id.nav_friends) {
            Toast.makeText(MainActivity.this,"friend clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
