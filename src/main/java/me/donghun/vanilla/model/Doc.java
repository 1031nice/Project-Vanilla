package me.donghun.vanilla.model;


import java.sql.Timestamp;
import java.util.ArrayList;

public class Doc {

    private Long id;
    private String title;
    private String content;
    private String userId;
    private int hit;
    private Timestamp date;

    private ArrayList<Comment> comments;

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
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public Doc() {
        this(0L, null, null, null, 0, null);
    }

    public Doc(String title, String content) {
        this(0L, title, content, null, 0, null);
    }

    public Doc(Long id, String title, String content) {
        this(id, title, content, null, 0, null);
    }

    // db에서 읽어올 때 쓰는 생성자
    public Doc(Long id, String title, String content, Timestamp date, int hit, String userId) {
        this.id = id;
        setTitle(title);
        setContent(content);
        this.date = date;
        this.hit = hit;
        this.userId = userId;
        this.setComments(new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", hit=" + hit +
                ", date=" + date +
                ", comments=" + comments +
                '}';
    }
}
