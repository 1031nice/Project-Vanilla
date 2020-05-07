package me.donghun.vanilla.model;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

public class User {

    @NotEmpty
    private String id;
    @NotEmpty
    private String pw;
    @NotEmpty
    private String name;

    private ArrayList<Doc> documents;

    public User() {
    }
    public User(String id, String pw, String name) {
        this(id, pw);
        setName(name);
    }
    public User(String id, String pw) {
        setId(id);
        setPw(pw);
        this.name = null;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Doc> getDocuments() {
        return documents;
    }
    public void setDocuments(ArrayList<Doc> documents) {
        this.documents = documents;
    }
}
