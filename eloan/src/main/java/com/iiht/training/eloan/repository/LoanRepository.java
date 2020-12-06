package com.iiht.training.eloan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iiht.training.eloan.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{

	List<Loan> findAllByCustomerId(Long customerId);
	List<Loan> findAllByStatus(Integer status);
	
	@Query(value="select l.id from Loan l where l.customer_id=:customerId",nativeQuery = true)
	List<Long> findByCustomerId(@Param("customerId") Long customerId);
	
	@Query(value="select l.customer_id from Loan l where l.id=:id",nativeQuery=true)
	Long findByLoanAppId(@Param("id") Long id);
	
	@Query(value="select l.status from Loan l where l.id=:id",nativeQuery=true)
	Integer getStatus(@Param("id") Long id);
	
	@Query(value="select l.remark from Loan l where l.id=:id",nativeQuery=true)
	String getRemark(@Param("id") Long id);
	
	@Query(value="select l.id from Loan l where l.status=:status",nativeQuery = true)
	List<Long> findByStatus(@Param("status") Long status);
	
	@Query(value="select l.customer_id from Loan l where l.status=:status",nativeQuery = true)
	List<Long> findCustomerByStatus(@Param("status") Long status);
	
	@Modifying
	@Transactional
	@Query(value="update Loan l set l.status=1,l.remark='Processed' where l.id=:id",nativeQuery=true)
	void updateLoan(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query(value="update Loan l set l.status=-1,l.remark='Rejected' where l.id=:id",nativeQuery=true)	
	void rejectLoan(@Param("id") Long id);

	@Modifying
	@Transactional
	@Query(value="update Loan l set l.status=2,l.remark='Sanctioned' where l.id=:id",nativeQuery=true)	
	void sanctionLoan(@Param("id") Long id);
}
