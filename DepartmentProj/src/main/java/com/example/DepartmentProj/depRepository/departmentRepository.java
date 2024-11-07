package com.example.DepartmentProj.depRepository;

import java.util.List;
import java.util.Optional;


//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.DepartmentProj.departmentData;

public interface departmentRepository extends MongoRepository<departmentData, Integer> {
//	JpaRepository  MongoRepository
//    boolean existsByPhoneNumber(String phoneNumber);
	Optional<departmentData> findByPhoneNumber(String phoneNumber);

	    void deleteAllByIdIn(List<Integer> ids);
	

	
}
