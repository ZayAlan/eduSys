package com.example.classdesign.Institution;

public class Institution {
    private String username;
    private String password;
    private String territory;//从业领域
    private String address;
    private String resume;
    private String eid;//机构编号
    private String contact;//联系方式
    private int adapageh;//教育适合年龄上界
    private int adapagel;//教育适合年龄下界
    private int isok;
    private double evaluation;
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
    public String getTerritory() {
        return territory;
    }
    public void setTerritory(String territory) {
        this.territory = territory;
    }
    public String getEid() {
        return eid;
    }
    public void setEid(String eid) {
        this.eid = eid;
    }
    public String getCmethod() {
        return contact;
    }
    public void setCmethod(String cmethod) {
        this.contact = cmethod;
    }
    public int getAdapageh() {
        return adapageh;
    }
    public void setAdapageh(int adapageh) {
        this.adapageh = adapageh;
    }
    public int getAdapagel() {
        return adapagel;
    }
    public void setAdapagel(int adapagel) {
        this.adapagel = adapagel;
    }
    public int getIsok() {
        return isok;
    }
    public void setIsok(int isok) {
        this.isok = isok;
    }
    public double getEvaluation() {
        return evaluation;
    }
    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
