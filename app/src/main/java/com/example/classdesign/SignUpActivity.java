package com.example.classdesign;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classdesign.Institution.Institution;
import com.example.classdesign.Student.Parent;
import com.example.classdesign.Teacher.Teacher;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    Parent parent = new Parent();
    Teacher teacher = new Teacher();
    Thread thread;
    Institution institution = new Institution();
    int teacherSex = 0;
    int studentSex = 0;
    Button instSuitAgeButton;
    Button teaSuitAgeButton;
    TextView instSuitAgeText1,instSuitAgeText2;
    TextView teaSuitAgeText1,teaSuitAgeText2;

    private SwapChooseView swapChooseViewSuitAge1,swapChooseViewSuitAge2,swapChooseViewTea1,swapChooseViewTea2;
    private String suitStartYear = "0";
    private String suitEndYear = "0";
    private String suitTeaStartYear = "0";
    private String suitTeaEndYear = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ActivityCollector.addActivity(this);
        final Button institutionButton = findViewById(R.id.institution);
        final View institutionInfo = findViewById(R.id.institution_information);
        final Button teacherButton = findViewById(R.id.teacher);
        final View teacherInfo = findViewById(R.id.teacher_information);
        final Button studentButton = findViewById(R.id.student);
        final View studentInfo = findViewById(R.id.student_information);
        final Button maleButton = findViewById(R.id.teacher_male);
        final Button femaleButton = findViewById(R.id.teacher_female);
        final Button studentMaleButton = findViewById(R.id.student_male);
        final Button studentFemaleButton = findViewById(R.id.student_female);

        instSuitAgeButton = findViewById(R.id.inst_suitage_button);
        instSuitAgeText1 = findViewById(R.id.start_suit_age);
        instSuitAgeText2 = findViewById(R.id.end_suit_age);

        instSuitAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySuitAge();
            }
        });

        teaSuitAgeButton = findViewById(R.id.teacher_suitage_button);
        teaSuitAgeText1 = findViewById(R.id.teacher_start_suit_age);
        teaSuitAgeText2 = findViewById(R.id.teacher_end_suit_age);

        teaSuitAgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teaSuitAge();
            }
        });

        Button makeSure = findViewById(R.id.makeSureButton);

        maleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setBackgroundResource(R.drawable.signupshape);
                femaleButton.setBackgroundResource(R.drawable.signupshape2);

                maleButton.setTextColor(Color.parseColor("#FB7299"));
                femaleButton.setTextColor(Color.parseColor("#999999"));
                teacherSex = 1;
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleButton.setBackgroundResource(R.drawable.signupshape2);
                femaleButton.setBackgroundResource(R.drawable.signupshape);

                femaleButton.setTextColor(Color.parseColor("#FB7299"));
                maleButton.setTextColor(Color.parseColor("#999999"));
                teacherSex = 2;
            }
        });

        studentMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentMaleButton.setBackgroundResource(R.drawable.signupshape);
                studentFemaleButton.setBackgroundResource(R.drawable.signupshape2);

                studentMaleButton.setTextColor(Color.parseColor("#FB7299"));
                studentFemaleButton.setTextColor(Color.parseColor("#999999"));
                studentSex = 1;
            }
        });

        studentFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentMaleButton.setBackgroundResource(R.drawable.signupshape2);
                studentFemaleButton.setBackgroundResource(R.drawable.signupshape);

                studentFemaleButton.setTextColor(Color.parseColor("#FB7299"));
                studentMaleButton.setTextColor(Color.parseColor("#999999"));
                studentSex = 2;
            }
        });

        institutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                institutionButton.setBackgroundResource(R.drawable.signupshape);
                teacherButton.setBackgroundResource(R.drawable.signupshape2);
                studentButton.setBackgroundResource(R.drawable.signupshape2);

                institutionButton.setTextColor(Color.parseColor("#FB7299"));
                teacherButton.setTextColor(Color.parseColor("#999999"));
                studentButton.setTextColor(Color.parseColor("#999999"));

                institutionInfo.setVisibility(View.VISIBLE);
                teacherInfo.setVisibility(View.GONE);
                studentInfo.setVisibility(View.GONE);
            }
        });

        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                institutionButton.setBackgroundResource(R.drawable.signupshape2);
                teacherButton.setBackgroundResource(R.drawable.signupshape);
                studentButton.setBackgroundResource(R.drawable.signupshape2);

                institutionButton.setTextColor(Color.parseColor("#999999"));
                teacherButton.setTextColor(Color.parseColor("#FB7299"));
                studentButton.setTextColor(Color.parseColor("#999999"));

                institutionInfo.setVisibility(View.GONE);
                teacherInfo.setVisibility(View.VISIBLE);
                studentInfo.setVisibility(View.GONE);
            }
        });

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                institutionButton.setBackgroundResource(R.drawable.signupshape2);
                teacherButton.setBackgroundResource(R.drawable.signupshape2);
                studentButton.setBackgroundResource(R.drawable.signupshape);

                institutionButton.setTextColor(Color.parseColor("#999999"));
                teacherButton.setTextColor(Color.parseColor("#999999"));
                studentButton.setTextColor(Color.parseColor("#FB7299"));


                institutionInfo.setVisibility(View.GONE);
                teacherInfo.setVisibility(View.GONE);
                studentInfo.setVisibility(View.VISIBLE);
            }
        });

        makeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int institutionVis = institutionInfo.getVisibility();
                int teacherVis = teacherInfo.getVisibility();
                int studentVis = studentInfo.getVisibility();
                int fillOK = 0;

                if(institutionVis == View.VISIBLE){
                    fillOK = checkInstitutionSheet();
                }else if(studentVis == View.VISIBLE){
                    fillOK = checkStudentSheet();
                }else{
                    fillOK = checkTeacherSheet();
                }
                sendSignUpInformation(fillOK);
            }
        });
    }

    private void mySuitAge(){
        //初始化对话框             R.style.CalendarDialog 是自定义的弹框主题，在styles设置
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //初始化自定义布局参数
        LayoutInflater layoutInflater = getLayoutInflater();
        //绑定布局
        View customLayout = layoutInflater.inflate(R.layout.suitage, (ViewGroup) findViewById(R.id.customDialog));
        //为对话框设置视图
        builder.setView(customLayout);

        //加载年月日的三个 CalendarView 的 id
        swapChooseViewSuitAge1 = (SwapChooseView) customLayout.findViewById(R.id.start_year);
        swapChooseViewSuitAge2 = customLayout.findViewById(R.id.end_year);

        //定义滚动选择器的数据项（年月日的）
        ArrayList<String> startYear = new ArrayList<>();
        ArrayList<String> endYear = new ArrayList<>();

        //为数据项赋值
        for(int i=1;i<=30;i++)           // 1日到31日
            startYear.add(i + "");
        for(int i=1;i<=30;i++)           // 1日到31日
            endYear.add(i + "");

        //为滚动选择器设置数据
        swapChooseViewSuitAge1.setData(startYear);
        swapChooseViewSuitAge2.setData(endYear);

        swapChooseViewSuitAge1.setOnSelectListener(new SwapChooseView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                suitStartYear = text;
            }
        });

        swapChooseViewSuitAge2.setOnSelectListener(new SwapChooseView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                suitEndYear = text;
            }
        });

        //对话框的确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Integer.parseInt(suitStartYear)>Integer.parseInt(suitEndYear)){
                    Toast.makeText(SignUpActivity.this, "选择错误！", Toast.LENGTH_SHORT).show();
                }else {
                    instSuitAgeText1.setText(suitStartYear + " 岁");
                    instSuitAgeText2.setText(suitEndYear + " 岁");
                    instSuitAgeButton.setVisibility(View.GONE);
                    instSuitAgeText1.setVisibility(View.VISIBLE);
                    instSuitAgeText2.setVisibility(View.VISIBLE);
                    TextView textView = findViewById(R.id.ganggang);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });
        //对话框的取消按钮
        builder.setNegativeButton("取消", null);
        //显示对话框
        builder.show();


    }

    private void teaSuitAge(){
        //初始化对话框             R.style.CalendarDialog 是自定义的弹框主题，在styles设置
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //初始化自定义布局参数
        LayoutInflater layoutInflater = getLayoutInflater();
        //绑定布局
        View customLayout = layoutInflater.inflate(R.layout.suitage, (ViewGroup) findViewById(R.id.customDialog));
        //为对话框设置视图
        builder.setView(customLayout);

        //加载年月日的三个 CalendarView 的 id
        swapChooseViewTea1 = (SwapChooseView) customLayout.findViewById(R.id.start_year);
        swapChooseViewTea2 = customLayout.findViewById(R.id.end_year);

        //定义滚动选择器的数据项（年月日的）
        ArrayList<String> startYear = new ArrayList<>();
        ArrayList<String> endYear = new ArrayList<>();

        //为数据项赋值
        for(int i=1;i<=30;i++)           // 1日到31日
            startYear.add(i + "");
        for(int i=1;i<=30;i++)           // 1日到31日
            endYear.add(i + "");

        //为滚动选择器设置数据
        swapChooseViewTea1.setData(startYear);
        swapChooseViewTea2.setData(endYear);

        swapChooseViewTea1.setOnSelectListener(new SwapChooseView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                suitTeaStartYear = text;
            }
        });

        swapChooseViewTea2.setOnSelectListener(new SwapChooseView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                suitTeaEndYear = text;
            }
        });

        //对话框的确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Integer.parseInt(suitTeaStartYear)>Integer.parseInt(suitTeaEndYear)){
                    Toast.makeText(SignUpActivity.this, "选择错误！", Toast.LENGTH_SHORT).show();
                }else {
                    teaSuitAgeText1.setText(suitTeaStartYear + " 岁");
                    teaSuitAgeText2.setText(suitTeaEndYear + " 岁");
                    teaSuitAgeButton.setVisibility(View.GONE);
                    teaSuitAgeText1.setVisibility(View.VISIBLE);
                    teaSuitAgeText2.setVisibility(View.VISIBLE);
                    TextView textView = findViewById(R.id.teacher_ganggang);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });
        //对话框的取消按钮
        builder.setNegativeButton("取消", null);
        //显示对话框
        builder.show();


    }

    private void sendSignUpInformation(final int fillOK){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                发送json数据

                OkHttpClient client = new OkHttpClient();
                Gson gson = new Gson();
                String jsonData = "";
                String signUpUrl = MyUtilities.MYURL;
                switch (fillOK){
                    case 1:
                        jsonData = gson.toJson(institution);
                        signUpUrl+="/user/registEduInstitution";
                        break;
                    case 2:
                        jsonData = gson.toJson(teacher);
                        signUpUrl+="/user/registTeacher";
                        break;
                    case 3:
                        jsonData = gson.toJson(parent);
                        signUpUrl+="/user/registParent";
                        break;
                        default:
                            //注册失败处理
                            break;
                }
                if(fillOK!=0) {
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
                    Request request = new Request.Builder()
                            .url(signUpUrl)
                            .post(requestBody)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        int signUpType = MyUtilities.parseJSONwithGSONtoGetStatus(response.body().string());
                        //其他处理，比如审核
                        if (signUpType == 1) {
//                            Looper.prepare();
//                            Toast.makeText(SignUpActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
//                            Looper.loop();
                            Intent intent = new Intent(SignUpActivity.this, ShowResultActivity.class);
                            startActivity(intent);
                        } else if (signUpType == -1) {
                            Looper.prepare();
                            Toast.makeText(SignUpActivity.this, "The username have exited!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(SignUpActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    } catch (IOException e) {
                        Looper.prepare();
                        Toast.makeText(SignUpActivity.this, "can't connect!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }else{
                    Looper.prepare();
                    Toast.makeText(SignUpActivity.this, "Wrong information!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
        thread.start();
    }



    private int checkInstitutionSheet(){
        EditText userNameText = findViewById(R.id.signUpUsername);
        EditText passwordText = findViewById(R.id.signUpPassword);
        EditText regionText = findViewById(R.id.institution_education_region);
        EditText codeText = findViewById(R.id.identify_code);
        EditText addressText = findViewById(R.id.shop_address);
        EditText contactText = findViewById(R.id.institution_contact);
        EditText resumeText = findViewById(R.id.institution_resume);

        //缺少判断填写内容是否正确
        boolean fillOK = userNameText.getText().toString().trim().equals("") ||
                passwordText.getText().toString().trim().equals("") ||
                regionText.getText().toString().trim().equals("") ||
                codeText.getText().toString().trim().equals("") ||
                addressText.getText().toString().trim().equals("") ||
                instSuitAgeButton.getVisibility() == View.VISIBLE ||
                contactText.getText().toString().trim().equals("") ||
                resumeText.getText().toString().trim().equals("");
        if(!fillOK){
            institution.setUsername(userNameText.getText().toString().trim());
            institution.setPassword(passwordText.getText().toString().trim());
            institution.setTerritory(regionText.getText().toString().trim());
            institution.setEid(codeText.getText().toString().trim());
            institution.setAdapageh(Integer.parseInt(suitEndYear));
            institution.setAdapagel(Integer.parseInt(suitStartYear));
            institution.setAddress(addressText.getText().toString().trim());
            institution.setCmethod(contactText.getText().toString().trim());
            institution.setResume(resumeText.getText().toString());
            institution.setIsok(0);
            institution.setEvaluation(0);
            return 1;
        }
        else return 0;
    }

    private int checkStudentSheet(){
        EditText userNameText = findViewById(R.id.signUpUsername);
        EditText passwordText = findViewById(R.id.signUpPassword);
        EditText nameStudentText = findViewById(R.id.student_name);
        EditText ageText = findViewById(R.id.student_age);
        EditText nameParentText = findViewById(R.id.parent_name);
        EditText contactText = findViewById(R.id.student_contact);

        //缺少判断填写内容是否正确
        boolean fillOK = userNameText.getText().toString().trim().equals("") ||
                passwordText.getText().toString().trim().equals("") ||
                nameStudentText.getText().toString().trim().equals("") ||
                nameParentText.getText().toString().trim().equals("") ||
                studentSex == 0 ||
                ageText.getText().toString().trim().equals("") || Integer.parseInt(ageText.getText().toString().trim()) > 100 ||
                contactText.getText().toString().trim().equals("");
        if(!fillOK){
            parent.setUsername(userNameText.getText().toString().trim());
            parent.setPassword(passwordText.getText().toString().trim());
            parent.setChname(nameStudentText.getText().toString().trim());
            parent.setPname(nameParentText.getText().toString().trim());
            if (studentSex == 1) parent.setSex("男");
            else parent.setSex("女");
            parent.setAge(Integer.parseInt(ageText.getText().toString().trim()));
            parent.setCmethod(contactText.getText().toString().trim());
            return 3;
        }
        else return 0;
    }

    private int checkTeacherSheet(){
        EditText userNameText = findViewById(R.id.signUpUsername);
        EditText passwordText = findViewById(R.id.signUpPassword);
        EditText nameText = findViewById(R.id.teacher_name);
        EditText ageText = findViewById(R.id.teacher_age);
        EditText regionText = findViewById(R.id.teacher_region);
        EditText workCareerText = findViewById(R.id.teacher_career);
        EditText idText = findViewById(R.id.teacher_ID);
        EditText contactText = findViewById(R.id.teacher_contact);
        EditText resumeText = findViewById(R.id.teacher_resume);

        //缺少判断填写内容是否正确
        boolean fillOK = userNameText.getText().toString().trim().equals("") ||
                passwordText.getText().toString().trim().equals("") ||
                regionText.getText().toString().trim().equals("") ||
                nameText.getText().toString().trim().equals("") ||
                teacherSex == 0 ||
                instSuitAgeButton.getVisibility() == View.VISIBLE ||
                contactText.getText().toString().trim().equals("") ||
                resumeText.getText().toString().trim().equals("") ||
                ageText.getText().toString().trim().equals("") || Integer.parseInt(ageText.getText().toString().trim()) > 100 ||
                workCareerText.getText().toString().trim().equals("") || Integer.parseInt(workCareerText.getText().toString().trim()) > Integer.parseInt(ageText.getText().toString().trim()) ||
                idText.getText().toString().trim().equals("");
        if(!fillOK){
            teacher.setUsername(userNameText.getText().toString().trim());
            teacher.setPassword(passwordText.getText().toString().trim());
            teacher.setTerritory(regionText.getText().toString().trim());
            teacher.setName(nameText.getText().toString().trim());
            if (teacherSex == 1) teacher.setSex("男");
            else teacher.setSex("女");
            teacher.setAdapageh(Integer.parseInt(suitTeaEndYear));
            teacher.setAdapagel(Integer.parseInt(suitTeaStartYear));
            teacher.setCmethod(contactText.getText().toString().trim());
            teacher.setAge(Integer.parseInt(ageText.getText().toString().trim()));
            teacher.setResume(resumeText.getText().toString());
            teacher.setCareer(Integer.parseInt(workCareerText.getText().toString().trim()));
            teacher.setId(idText.getText().toString().trim());
            teacher.setIsok(0);
            teacher.setEvaluation(0);
            return 2;
        }
        else return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyUtilities.destroyThread(thread);
        ActivityCollector.removeActivity(this);
    }
}
