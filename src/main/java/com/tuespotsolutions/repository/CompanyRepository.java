package com.tuespotsolutions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tuespotsolutions.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	  Boolean existsByName(String username);
	  Boolean existsByUsername(String username);
	  Boolean existsByEmail(String email);
	  Boolean existsByMobileNumber(String mobileNumber);
	  Optional<Company> findByEmail(String email);
	  Optional<Company> findByMobileNumber(String mobileNumber);
	  Optional<Company> findByUsername(String username);
	  Optional<Company> findByName(String username);
	  
	  @Query(value = "SELECT * FROM company where status=true  order by rand() limit 10;", nativeQuery = true)
	  public List<Company> getRandomCompanyLogos();
	  
	  
	 
}
