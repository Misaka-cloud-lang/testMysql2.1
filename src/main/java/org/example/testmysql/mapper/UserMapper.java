package org.example.testmysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.testmysql.pojo.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author zyp
 * @version 1.0
 */
@Mapper
public interface UserMapper {
    @Select("select * from userdb.game_record")
    //查询全体
    public List<User> list();

    List<User> getAllUsers();
    //计算在指定时间范围内活跃的用户数量
    int countActiveUsersInTimeRange(Timestamp startTime, Timestamp endTime);

//   据给定的经纬度范围参数，计算在该区域内的用户数量。
    int countUsersInRegion(double minLongitude, double maxLongitude, double minLatitude, double maxLatitude);
    List<User> getUsersInTimeRange(Timestamp startTime, Timestamp endTime);

    List<User> getUsersInRegion(double minLongitude,  double maxLongitude, double minLatitude,double maxLatitude);
}

