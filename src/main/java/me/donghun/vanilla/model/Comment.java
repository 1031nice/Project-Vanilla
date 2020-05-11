package me.donghun.vanilla.model;

import java.sql.Timestamp;

public class Comment {

    private Long id;
    private Long documentId;
    private String comment;
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
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
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

    public Comment(Long id, Long documentId, String comment, String userId, Timestamp date) {
        this.id = id;
        this.documentId = documentId;
        this.comment = comment;
        this.userId = userId;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", documentId=" + documentId +
                ", content='" + comment + '\'' +
                ", author='" + userId + '\'' +
                ", date=" + date +
                '}';
    }
}
