package com.example.zhiweixu.mailclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText typedUsername;
    private EditText typedPassword;
    private EditText typedAckpsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        typedUsername = (EditText) findViewById(R.id.setUsername) ;
        typedPassword = (EditText) findViewById(R.id.setPassword) ;
        typedAckpsd = (EditText) findViewById(R.id.ackPassword) ;
        Button button = (Button) findViewById(R.id.user_register_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String username = typedUsername.getText().toString();
        String password = typedPassword.getText().toString();
        String ackpsd = typedAckpsd.getText().toString();

        if(!username.contains("@bro.com")){
            Toast.makeText(RegisterActivity.this, "invalid username", Toast.LENGTH_SHORT).show();
        }
        if(!ackpsd.equals(password)){
            Toast.makeText(RegisterActivity.this, "different acknowlege password", Toast.LENGTH_SHORT).show();
        }
        /**
         * 发数据到服务端注册
         */
    }

}
