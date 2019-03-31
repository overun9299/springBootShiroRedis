package overun.service;

import overun.model.User;
import overun.model.UserExample;

import java.util.List;

/**
 * Created by ZhangPY on 2019/3/31
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 */
public interface UserService {

    /**
     * 条件查询
     * @param example
     * @return
     */
    List<User> selectByExample(UserExample example);
}
