package com.example.classdesign;

import com.example.classdesign.Institution.Store;
import com.example.classdesign.Student.Parent;
import com.example.classdesign.Teacher.Teacher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.SQLData;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyUtilities {

    public static final String MYURL = "http://118.89.183.170:8080";
    private static Store enterStore;
    private static Teacher enterTeacher;
    private static Parent enterParent;
    private static int kindRole;
    private static String username;
    private static News news;
    private static Course course;

    public static String changeListToString(List<String> list){
        String result = "";
        for (String temp : list){
            result = result+" "+temp;
        }
        return result.trim();
    }

    public static String changeColorToString(Map<String, Boolean> map){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            if (value){
                list.add(key);
            }
        }
        String result = changeListToString(list);
        return result;
    }

    public static int parseJSONwithGSONtoGetStatus(String jsonData){
        Gson gson = new Gson();
        int mystatus = 0;
        List<Status> statusList = gson.fromJson(jsonData, new TypeToken<List<Status>>(){}.getType());
        for (Status status: statusList){
            mystatus = status.getStatus();
        }
        return mystatus;
    }

    public static Date parseStringToDate(String y, String m, String d) {
        String input = y + "-" + m + "-" + d;
        Date t = Date.valueOf(input);
        return t;
    }

    public static Date parseStringToDate() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        return date;
    }

    public static void destroyThread(Thread thread) {
        try {
            if (null != thread && Thread.State.RUNNABLE == thread .getState()) {
                try {
                    Thread.sleep(500);
                    thread .interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            thread = null;
        }
    }

    public static void setUsername(String username) {
        MyUtilities.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static Store getEnterStore() {
        return enterStore;
    }

    public static void setEnterStore(Store enterStore) {
        MyUtilities.enterStore = enterStore;
    }

    public static int getKindRole() {
        return kindRole;
    }

    public static void setKindRole(int kindRole) {
        MyUtilities.kindRole = kindRole;
    }

    public static Teacher getEnterTeacher() {
        return enterTeacher;
    }

    public static void setEnterTeacher(Teacher enterTeacher) {
        MyUtilities.enterTeacher = enterTeacher;
    }

    public static Parent getEnterParent() {
        return enterParent;
    }

    public static void setEnterParent(Parent enterParent) {
        MyUtilities.enterParent = enterParent;
    }

    public static News getNews() {
        return news;
    }

    public static void setNews(News news) {
        MyUtilities.news = news;
    }

    public static Course getCourse() {
        return course;
    }

    public static void setCourse(Course course) {
        MyUtilities.course = course;
    }
}
