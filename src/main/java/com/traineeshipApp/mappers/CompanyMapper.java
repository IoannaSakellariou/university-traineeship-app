package com.traineeshipApp.mappers;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traineeshipApp.domainmodel.Company;
import com.traineeshipApp.domainmodel.User;

@Repository
public interface CompanyMapper extends JpaRepository<Company, Integer> {

    Optional<Company> findByUsername(String username);

	Optional<Company> findById(Integer companyId);
	
	Optional<Company> findByUser(User user);
	

}
