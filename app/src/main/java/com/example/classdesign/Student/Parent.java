package com.example.classdesign.Student;

public class Parent {
    private String username;
    private String password;
    private String chname;//孩子姓名
    private String sex;
    private int age;
    private String pname;//家长姓名
    private String contact;//联系方式
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getChname() {
        return chname;
    }
    public void setChname(String chname) {
        this.chname = chname;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPname() {
        return pname;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }
    public String getCmethod() {
        return contact;
    }
    public void setCmethod(String cmethod) {
        this.contact = cmethod;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
