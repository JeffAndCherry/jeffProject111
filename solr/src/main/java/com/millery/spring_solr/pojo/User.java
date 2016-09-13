package com.millery.spring_solr.pojo;

/**
 * Created by hp on 2016/8/30.
 */
public class User {
    private String id;// 用户编号
    private String username;// 用户名
    private String description;// 描述
    private String content;//内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
