package com.example.classdesign.Institution;

public class Store {
    public Store() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Store(String sname, String username, String address){
        this.sname = sname;
        this.username = username;
        this.address = address;
    }
    private int sid;
    private String sname;
    private String username;
    private String address;
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Store(int sid, String sname, String username, String address) {
        super();
        this.sid = sid;
        this.sname = sname;
        this.username = username;
        this.address = address;
    }
    public int getSid() {
        return sid;
    }
    public void setSid(int sid) {
        this.sid = sid;
    }
}
