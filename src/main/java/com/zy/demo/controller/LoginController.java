package com.zy.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zy.demo.config.BusinessException;
import com.zy.demo.model.User;
import com.zy.demo.pojo.LoginDto;
import com.zy.demo.service.LoginService;
import com.zy.demo.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录Controller
 * @author zy
 */
@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/login")
    public BaseResponse login(@RequestBody String reqStr){
        BaseResponse baseResponse;
        try {
            ObjectNode on = new ObjectMapper().readValue(reqStr, ObjectNode.class);
            if(on == null){
                throw new BusinessException("登录信息为空！");
            }
            String userName = on.get("userName").asText();
            String userPassword = on.get("userPassword").asText();
            if(StringUtils.isBlank(userName)){
                throw new BusinessException("用户名为空！");
            }
            if(StringUtils.isBlank(userPassword)){
                throw new BusinessException("用户密码为空！");
            }
            LoginDto loginDto = new LoginDto();
            loginDto.setUser(new User(userName,userPassword));
            this.loginService.doLogin(loginDto);
            baseResponse = BaseResponse.success();
        }catch (Exception e){
            log.error(e.getMessage());
            baseResponse = BaseResponse.fail(e.getMessage());
        }
        return baseResponse;
    }
}
