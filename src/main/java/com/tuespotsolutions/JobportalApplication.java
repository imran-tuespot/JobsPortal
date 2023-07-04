package com.tuespotsolutions;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.tuespotsolutions.entity.Admin;
import com.tuespotsolutions.entity.Roles;
import com.tuespotsolutions.entity.User;
import com.tuespotsolutions.repository.AdminRepository;
import com.tuespotsolutions.repository.PackageDiscriptionRepository;
import com.tuespotsolutions.repository.PackagesRepository;
import com.tuespotsolutions.repository.RolesRepository;
import com.tuespotsolutions.repository.UserRepository;
import com.tuespotsolutions.util.ConstantConfiguration;

@SpringBootApplication
@EnableWebMvc
public class JobportalApplication extends SpringBootServletInitializer implements CommandLineRunner {

	
	public static void main(String[] args) {
		SpringApplication.run(JobportalApplication.class, args);

		Logger logger = LoggerFactory.getLogger(JobportalApplication.class);
		logger.info("Application Running 15-05-2023");
	}	
	
	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PackagesRepository packagesRepository;
	
	@Autowired
	private PackageDiscriptionRepository discriptionRepository;
	

	@Override
	public void run(String... args) throws Exception {

		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8",
				new ClassPathResource("indiaDb/indian_state_district_city.sql"));
		resourceDatabasePopulator.execute(dataSource);
		
		ResourceDatabasePopulator jobFiltersMasterData = new ResourceDatabasePopulator(false, false, "UTF-8",
				new ClassPathResource("jobFilters/job_filters.sql"));
		jobFiltersMasterData.execute(dataSource);

		Set<Roles> roles = new HashSet<Roles>();
		Roles roles1 = new Roles();
		roles1.setId(1L);
		roles1.setName(ConstantConfiguration.ROLE_COMPANY);
		roles.add(roles1);

		Roles roles2 = new Roles();
		roles2.setId(2L);
		roles2.setName(ConstantConfiguration.ROLE_CANDIDATE);
		roles.add(roles2);

		Roles roles3 = new Roles();
		roles3.setId(3L);
		roles3.setName(ConstantConfiguration.ROLE_ADMIN);
		roles.add(roles3);
		this.rolesRepository.saveAll(roles);

		
		if(!adminRepository.existsByUserName("admin@admin.com")) {
			java.util.Date utilDate = new java.util.Date();
			Timestamp ts = new Timestamp(utilDate.getTime());
			java.sql.Date sqlDate = new java.sql.Date(ts.getTime());
			Admin admin = new Admin();
			admin.setId(1L);
			admin.setName("Admin");
			admin.setUserName("admin@admin.com");
			admin.setEmail("admin@admin.com");
			admin.setModifiedOn(sqlDate);
			admin.setCreatedOn(sqlDate);
			admin.setStatus(true);
			Admin save = this.adminRepository.save(admin);

			User user = new User();
			user.setId(1L);
			user.setApplicantName(save.getName());
			user.setCreatedOn(sqlDate);
			user.setEmail(save.getEmail());
			user.setMobileNumber("7814478557");
			user.setModifiedOn(sqlDate);
			user.setStatus(true);
			user.setUsername(save.getUserName());
			user.setPassword(passwordEncoder.encode("admin"));
			user.setTypeId(save.getId());
			user.setUserType(ConstantConfiguration.ADMIN);

			Roles adminRole = this.rolesRepository.findById(3L).get();

			Set<Roles> roleSet = new HashSet<Roles>();
			Roles roleS = new Roles();
			roleS.setId(adminRole.getId());
			roleS.setName(adminRole.getName());
			roleSet.add(roleS);
			user.setRoles(roleSet);

			User save2 = this.userRepository.save(user);

			System.err.println(save2.toString());
		}
		
		
//		===================== Default Packages  =====================================
		
	
//		===================== Default Packages end  ===================================
		
	}

}
