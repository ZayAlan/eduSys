package com.example.classdesign;

public class Remark {
    private int rid;//评论id,主键区别评论
    private String remarker;//评论者用户名
    private int cid;//被评论课程ID
    private int score;//评论分数
    private String article;//评论文字内容
    public String getRemarker() {
        return remarker;
    }
    public void setRemarker(String remarker) {
        this.remarker = remarker;
    }
    public int getCid() {
        return cid;
    }
    public void setCid(int cid) {
        this.cid = cid;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getArticle() {
        return article;
    }
    public void setArticle(String article) {
        this.article = article;
    }
    public int getRid() {
        return rid;
    }
    public void setRid(int rid) {
        this.rid = rid;
    }
}
