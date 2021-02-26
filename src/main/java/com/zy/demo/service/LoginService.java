package com.zy.demo.service;

import com.zy.demo.pojo.LoginDto;

/**
 * 登录Service
 * @author zy
 */
public interface LoginService {
    boolean doLogin(LoginDto loginDto) throws Exception;
}
