package com.zoho.attendance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeoFence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double radius;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GeoPoint> vertices;



    // Method to check if a given latitude and longitude are within the polygon
    public boolean contains(double latitude, double longitude) {
        int numVertices = vertices.size();
        boolean inside = false;
        System.out.println("vertices->"+vertices);
        for (int i = 0, j = numVertices - 1; i < numVertices; j = i++) {
            double vertex1Latitude = vertices.get(i).getLatitude();
            double vertex1Longitude = vertices.get(i).getLongitude();
            double vertex2Latitude = vertices.get(j).getLatitude();
            double vertex2Longitude = vertices.get(j).getLongitude();

            // Check if the latitude is within the range of the vertices' latitudes
            if ((vertex1Latitude > latitude) != (vertex2Latitude > latitude)) {
                System.out.println("vertex1Latitude->"+vertex1Latitude);
                // Calculate the longitude of the point on the edge of the polygon at the given latitude
                double edgeLongitude = ((vertex2Longitude - vertex1Longitude) * (latitude - vertex1Latitude)) / (vertex2Latitude - vertex1Latitude) + vertex1Longitude;
                System.out.println("edgeLongitude->"+edgeLongitude);
                // Check if the longitude of the point falls within the edge's longitude range
                if (longitude < edgeLongitude) {
                    inside = !inside;
                }
            }
        }
        return inside;
    }
}
