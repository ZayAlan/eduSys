package com.example.classdesign;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.classdesign.User.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Thread thread;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button signUpButton = findViewById(R.id.SignUpButton);
        Button signInButton = findViewById(R.id.SignInButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestAboutAccount();
            }
        });

    }

    private void sendRequestAboutAccount(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                EditText usernameText = findViewById(R.id.input_account);
                EditText passwordText = findViewById(R.id.input_password);
                user = new User();
                user.setUsername(usernameText.getText().toString());
                user.setPassword(passwordText.getText().toString());
                Gson gson = new Gson();
                String jsonData = gson.toJson(user);
                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),jsonData);
                Request request = new Request.Builder()
                        .url(MyUtilities.MYURL+"/user/logIn")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    int signType = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                    choseTypeJump(signType);
                } catch (IOException e) {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "can't connect!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    private void choseTypeJump(int signType){
        switch (signType){
            case 3:
                Intent intent1 = new Intent(MainActivity.this, InstitutionActivity.class);
                startActivity(intent1);
                MyUtilities.setUsername(user.getUsername());
                break;
            case 1:
                Intent intent2 = new Intent(MainActivity.this,TeacherActivity.class);
                startActivity(intent2);
                MyUtilities.setUsername(user.getUsername());
                break;
            case 2:
                Intent intent3 = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent3);
                MyUtilities.setUsername(user.getUsername());
                break;
            case 4:
                Intent intent4 = new Intent(MainActivity.this, AdministorActivity.class);
                startActivity(intent4);
                MyUtilities.setUsername(user.getUsername());
                break;
            case 0:
                Looper.prepare();
                Toast.makeText(MainActivity.this, "Username not exits!", Toast.LENGTH_SHORT).show();
                Looper.loop();
                break;
            case -1:
                Looper.prepare();
                Toast.makeText(MainActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                Looper.loop();
                break;
            case -2:
                Looper.prepare();
                Toast.makeText(MainActivity.this, "The account hasn't been checked yet!", Toast.LENGTH_SHORT).show();
                Looper.loop();
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyUtilities.destroyThread(thread);
    }
}
