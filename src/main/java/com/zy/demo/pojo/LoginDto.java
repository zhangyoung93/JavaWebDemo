package com.zy.demo.pojo;

import com.zy.demo.model.User;

/**
 * 登录DTO
 * @author zy
 */
public class LoginDto {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
