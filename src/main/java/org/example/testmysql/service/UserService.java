package org.example.testmysql.service;

import org.example.testmysql.pojo.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author zyp
 * @version 1.0
 */
public interface UserService {
    List<User> getAllUsers();

    int countActiveUsersInTimeRange(Timestamp startTime, Timestamp endTime);

    int countUsersInRegion(double minLongitude, double maxLongitude, double minLatitude, double maxLatitude);
    List<User> getUsersInTimeRange(Timestamp startTime, Timestamp endTime);

    List<User> getUsersInRegion(double minLongitude, double maxLongitude, double minLatitude, double maxLatitude);
}
