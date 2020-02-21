package com.example.classdesign;


import java.sql.Date;

public class Course {
    public Course() {
        super();
        // TODO Auto-generated constructor stub
    }
    private int cid;
    private int sid;//商店id，如果是个人教师对应的课程，该段填-1
    private String teachername;//授课老师
    private String username;//个人教师对应的username，如果是店铺对应的，填教育机构username
    private String content;//内容
    private String type;//类别" "空格分隔
    private String cname;//课程名字
    private String url;
    private String address;//教室地址
    private String time;//时间
    private int flag;//如果是商店请填1，个人老师请填0
    private double price;//价格
    private double evaluation;//好评度，第一次插入时请填0
    public Course(int cid, int sid, String teachername, String username, String content, String type, String address,
                  double price, double evaluation, String time, int flag) {
        super();
        this.cid = cid;
        this.sid = sid;
        this.teachername = teachername;
        this.username = username;
        this.content = content;
        this.type = type;
        this.address = address;
        this.price = price;
        this.evaluation = evaluation;
        this.time = time;
        this.flag = flag;
    }

    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
    public String getTeachername() {
        return teachername;
    }
    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getEvaluation() {
        return evaluation;
    }
    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCname() {
        return cname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
