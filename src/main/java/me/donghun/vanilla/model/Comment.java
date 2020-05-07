package me.donghun.vanilla.model;

import java.sql.Timestamp;

public class Comment {

    private Long id;
    private Long documentId;
    private String content;
    private String userId;
    private Timestamp date;

    public Long getId() {
        return id;
    }

    public Long getDocumentId() {
        return documentId;
    }
    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", documentId=" + documentId +
                ", content='" + content + '\'' +
                ", author='" + userId + '\'' +
                ", date=" + date +
                '}';
    }
}
