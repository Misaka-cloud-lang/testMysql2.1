package org.example.testmysql.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author zyp
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    // 主键ID
    private Integer number;

    // 经度
    private Double longitude;

    // 纬度
    private Double latitude;

    // 上传时间
    private Timestamp uploadTime;

    // 设备ID
    private String deviceId;

}
