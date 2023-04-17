package com.zoho.attendance.service;
// GeofenceService.java

import com.zoho.attendance.dto.GeoFenceDTO;
import com.zoho.attendance.entity.GeoFence;
import com.zoho.attendance.entity.GeoPoint;
import com.zoho.attendance.repository.GeoFenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeofenceService {

    public static final String RETURN_CODE = "returnCode";
    public static final String RETURN_MSG = "returnMsg";
    @Autowired
    private GeoFenceRepository geoFenceRepository;

    public GeoFence createGeoFence(GeoFenceDTO request) {
        GeoFence geoFence = new GeoFence();
        geoFence.setName(request.getName());
        geoFence.setAddress(request.getAddress());
        geoFence.setRadius(request.getRadius()); // Update to use meters

        // Generate vertices for the polygon based on center point and radius
        List<GeoPoint> vertices = new ArrayList<>();
        int numVertices = 6; // You can adjust this to increase/decrease the number of vertices
        double angleIncrement = 2 * Math.PI / numVertices;
        for (int i = 0; i < numVertices; i++) {
            double angle = i * angleIncrement;
            double vertexLatitude = request.getLatitude() + (request.getRadius() * Math.cos(angle) / 111320.0); // Update to use meters and convert to decimal degrees
            double vertexLongitude = request.getLongitude() + (request.getRadius() * Math.sin(angle) / (111320.0 * Math.cos(Math.toRadians(request.getLatitude())))); // Update to use meters and convert to decimal degrees

            GeoPoint vertex = new GeoPoint();
            vertex.setLatitude(vertexLatitude);
            vertex.setLongitude(vertexLongitude);

            vertices.add(vertex);
        }

        // Close the polygon by adding the first vertex as the last vertex
        vertices.add(vertices.get(0));

        geoFence.setVertices(vertices);

        return geoFenceRepository.save(geoFence);
    }

    public List<GeoFence> findAll() {
        return geoFenceRepository.findAll();
    }

    public GeoFence findById(Long geoFenceId) {
        return geoFenceRepository.findById(geoFenceId)
                .orElseThrow(() -> new IllegalArgumentException("GeoFence not found with ID: " + geoFenceId));
    }

    public Map<String, Object> updateGeoFence(GeoFenceDTO request) {
        Map<String, Object> responseMap = new HashMap<>();
        if(geoFenceRepository.updateGeoFence(request.getName(),request.getAddress(),request.getRadius(),request.getId())>0){
            responseMap.put(RETURN_CODE, 0);
            responseMap.put(RETURN_MSG, "data updated successfully");
            return responseMap;
        }
        responseMap.put(RETURN_CODE, 1);
        responseMap.put(RETURN_MSG, "No record found");
        return responseMap;
    }

    public void deleteGeoFence(Long geoFenceId) {
        geoFenceRepository.deleteById(geoFenceId);
    }

    public Map<String, Object> isInsideGeoFence(double latitude, double longitude, Long geoFenceId) {
        Map<String, Object> responseMap = new HashMap<>();
        GeoFence geoFence = geoFenceRepository.findById(geoFenceId)
                .orElseThrow(() -> new IllegalArgumentException("GeoFence not found with ID: " + geoFenceId));
        System.out.println(geoFence);
        if (geoFence.contains(latitude, longitude)) {
            responseMap.put("returnCode", 0);
            responseMap.put("returnMsg", "Location inside range");
        } else {
            double distance = calculateDistanceToNearestEdge(latitude, longitude, geoFence.getVertices());
            responseMap.put("returnCode", 2);
            responseMap.put("returnMsg", "Your location is " + distance + " meters away from range");
        }
        return responseMap;
    }

    public double calculateDistanceToNearestEdge(double currentLat, double currentLon, List<GeoPoint> vertices) {
        double minDistance = Double.MAX_VALUE;

        for (GeoPoint geoPoint : vertices) {
            double distance = haversine(currentLat, currentLon, geoPoint.getLatitude(), geoPoint.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the Earth in meters
        final double R = 6371000.0;

        // Convert latitudes and longitudes from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences between latitudes and longitudes
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Calculate the haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        double distance = R * c;
        return distance;
    }


}
