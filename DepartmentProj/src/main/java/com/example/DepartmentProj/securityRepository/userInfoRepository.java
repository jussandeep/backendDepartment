package com.example.DepartmentProj.securityRepository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.DepartmentProj.securityEntity.userInfo;


public interface userInfoRepository extends MongoRepository<userInfo, Integer> {
    Optional<userInfo> findByName(String email);
    Optional<userInfo> findByEmail(String email);
}
