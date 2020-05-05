package me.donghun.vanilla.model;

import java.sql.Timestamp;

public class Comment {

    private Long id;
    private Long documentId;
    private String content;
    private String author;
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
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
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
                ", author='" + author + '\'' +
                ", date=" + date +
                '}';
    }
}
