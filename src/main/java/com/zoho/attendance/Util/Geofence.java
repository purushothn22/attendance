package com.zoho.attendance.Util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Geofence {
    private double latitude;
    private double longitude;
    private double radius;
    private String location;



/*    public static void main(String[] args) {
        // Create a geofence with latitude = 37.7749, longitude = -122.4194, and radius = 1000 meters
        Geofence geofence = new Geofence(37.7749, -122.4194, 1000);

        // Test if a point is inside the geofence
        double lat = 37.7793;
        double lon = -122.4192;
        if (geofence.isInside(lat, lon)) {
            System.out.println("Point is inside the geofence.");
        } else {
            System.out.println("Point is outside the geofence.");
        }
    }*/
}
