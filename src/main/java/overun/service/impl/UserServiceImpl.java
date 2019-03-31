package overun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import overun.mapper.UserMapper;
import overun.model.User;
import overun.model.UserExample;
import overun.service.UserService;

import java.util.List;

/**
 * Created by ZhangPY on 2019/3/31
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> selectByExample(UserExample example) {
        return userMapper.selectByExample(example);
    }
}
