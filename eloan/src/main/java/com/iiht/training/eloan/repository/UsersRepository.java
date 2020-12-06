package com.iiht.training.eloan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iiht.training.eloan.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{

	boolean existsByEmail(String email);
	List<Users> findByRole(String role);
//	Optional<Users> findById(Long id);
	
	
	@Query(value="select u.role from Users u where u.id=:userId",nativeQuery=true)
	String verifyUser(@Param("userId")Long userId);

	


}
