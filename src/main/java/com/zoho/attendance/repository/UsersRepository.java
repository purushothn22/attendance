package com.zoho.attendance.repository;

import com.zoho.attendance.entity.UsersEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UsersRepository extends CrudRepository<UsersEntity, String> {

    UsersEntity findByEmpId(String empId);

    @Transactional
    void deleteByEmpId(String empId);

    @Transactional
    @Modifying
    @Query(value = "update users set password=?1 where emp_id=?2", nativeQuery = true)
    int updatePassword(String password,String empId);
}