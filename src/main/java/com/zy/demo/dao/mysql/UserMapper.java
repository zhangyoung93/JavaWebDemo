package com.zy.demo.dao.mysql;

import com.zy.demo.model.User;
import org.springframework.stereotype.Repository;

/**
 * 用户Mapper
 * @author zy
 */
@Repository
public interface UserMapper {
    int deleteByUserName(String userName);

    int insert(User record);

    int insertSelective(User record);

    User selectByParam(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}