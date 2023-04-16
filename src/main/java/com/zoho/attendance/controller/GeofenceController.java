package com.zoho.attendance.controller;
// GeofenceController.java

import com.zoho.attendance.dto.GeoFenceDTO;
import com.zoho.attendance.entity.GeoFence;
import com.zoho.attendance.service.GeofenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/geofence")
public class GeofenceController {
    @Autowired
    private GeofenceService geofenceService;

    @GetMapping()
    public List<GeoFence> findAll() {
        return geofenceService.findAll();
    }

    @GetMapping(path = "/{geoFenceId}")
    public GeoFence findById(@PathVariable Long geoFenceId) {
        return geofenceService.findById(geoFenceId);
    }

    @PostMapping()
    public GeoFence createGeoFence(@RequestBody GeoFenceDTO request) {
        return geofenceService.createGeoFence(request);
    }

    @PutMapping()
    public Map<String, Object> updateGeoFence(@RequestBody GeoFenceDTO request) {
        return geofenceService.updateGeoFence(request);
    }

    @DeleteMapping(path = "/{geoFenceId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long geoFenceId) {
        geofenceService.deleteGeoFence(geoFenceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/checkRange")
    public Map<String, Object> isInsideGeoFence(@PathVariable Long id, @RequestParam double latitude, @RequestParam double longitude) {
        return geofenceService.isInsideGeoFence(latitude, longitude, id);
    }

}
