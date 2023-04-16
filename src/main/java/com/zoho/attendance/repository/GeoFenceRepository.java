package com.zoho.attendance.repository;

import com.zoho.attendance.entity.GeoFence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GeoFenceRepository extends JpaRepository<GeoFence, Long> {
    public static final String UPDATE_GEOFENCE= "update geo_fence set name=?1,address=?2,radius=?3 where id=?4";

    @Transactional
    @Modifying
    @Query(value=UPDATE_GEOFENCE, nativeQuery = true)
    int updateGeoFence(String name, String address, double radius,Long id);
}