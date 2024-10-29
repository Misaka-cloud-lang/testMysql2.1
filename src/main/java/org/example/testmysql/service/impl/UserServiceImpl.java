package org.example.testmysql.service.impl;

/**
 * @author zyp
 * @version 1.0
 */
import org.example.testmysql.mapper.UserMapper;
import org.example.testmysql.pojo.User;
import org.example.testmysql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    // 查询所有用户
    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }
//计算在指定时间范围内活跃的用户数量
    @Override
    public int countActiveUsersInTimeRange(Timestamp startTime, Timestamp endTime) {
        return userMapper.countActiveUsersInTimeRange(startTime, endTime);
    }
//   据给定的经纬度范围参数，计算在该区域内的用户数量。
    @Override
    public int countUsersInRegion(double minLongitude, double maxLongitude, double minLatitude, double maxLatitude) {
        return userMapper.countUsersInRegion(minLongitude, maxLongitude, minLatitude, maxLatitude);
    }
    //返回在指定时间范围内活跃的用户数量
    @Override
    public List<User> getUsersInTimeRange(Timestamp startTime, Timestamp endTime) {
        return userMapper.getUsersInTimeRange(startTime, endTime);
    }
    //   据给定的经纬度范围参数，返回在该区域内的用户数量。

    @Override
    public List<User> getUsersInRegion(double minLongitude, double maxLongitude, double minLatitude, double maxLatitude) {
        return userMapper.getUsersInRegion(minLongitude, maxLongitude, minLatitude, maxLatitude);
    }
}