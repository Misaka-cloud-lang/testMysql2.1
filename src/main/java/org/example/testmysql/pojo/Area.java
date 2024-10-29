package org.example.testmysql.pojo;

/**
 * @author zyp
 * @version 1.0
 */
public class Area {
    private double minLongitude;
    private double maxLongitude;
    private double minLatitude;
    private double maxLatitude;

    public Area(double longitude, double latitude) {
        this.minLongitude = longitude - 0.000074;
        this.maxLongitude = longitude + 0.000074;
        this.minLatitude = latitude - 0.000063;
        this.maxLatitude = latitude + 0.000063;
    }

    public boolean contains(User user) {
        double userLongitude = user.getLongitude();
        double userLatitude = user.getLatitude();
        return userLongitude >= minLongitude && userLongitude <= maxLongitude &&
                userLatitude >= minLatitude && userLatitude <= maxLatitude;
    }

    @Override
    public String toString() {
        return "Area{" +
                "minLongitude=" + minLongitude +
                ", maxLongitude=" + maxLongitude +
                ", minLatitude=" + minLatitude +
                ", maxLatitude=" + maxLatitude +
                '}';
    }
}
