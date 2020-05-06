package me.donghun.vanilla.model;


import java.sql.Timestamp;

public class Doc {

    private Long id;
    private String title;
    private String content;
    private String userId;
    private int hit;
    private Timestamp date;

    public int getHit() {
        return hit;
    }
    public void setHit(int hit) {
        this.hit = hit;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id){this.id = id;}
    public Timestamp getDate() {
        return date;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Doc() {
    }

    public Doc(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    public Doc(Long id, String title, String content) {
        this.id = id;
        setTitle(title);
        setContent(content);
        this.hit = 0;
    }

    // db에서 읽어올 때 쓰는 생성자
    public Doc(Long id, String title, String content, Timestamp date, int hit, String userId) {
        this.id = id;
        setTitle(title);
        setContent(content);
        this.date = date;
        this.hit = hit;
        this.userId = userId;
    }

}
