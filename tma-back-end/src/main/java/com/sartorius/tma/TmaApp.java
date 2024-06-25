package com.sartorius.tma;

import
		org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.sartorius.tma.config.FileStorageProperties;
import com.sartorius.tma.enumeration.RoleCode;
import com.sartorius.tma.persistence.entities.Role;
import com.sartorius.tma.persistence.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
@EnableFeignClients
@EnableConfigurationProperties({ FileStorageProperties.class })
public class TmaApp implements CommandLineRunner {

	private final RoleRepository roleRepository;


	public static void main(String[] args) {
		SpringApplication.run(TmaApp.class, args);
		log.info("------------ The TMA APP server was sucessuflly started ---");
	}

	@Override
	public void run(String... arg0) {
		log.info("------------ PROCESS TO EXECUTE WHEN STARTING THE SERVER  ---");
		Role adminRole = this.roleRepository.findByRoleCode(RoleCode.ADMINISTRATOR);
		if (adminRole == null) {
			adminRole = new Role();
			adminRole.setRoleCode(RoleCode.ADMINISTRATOR);
			adminRole.setRoleLabel("ADMINISTRATEUR");
			this.roleRepository.save(adminRole);
		}
		Role managerRole = this.roleRepository.findByRoleCode(RoleCode.MANAGER);
		if (managerRole == null) {
			managerRole = new Role();
			managerRole.setRoleCode(RoleCode.MANAGER);
			managerRole.setRoleLabel("MANAGER");
			this.roleRepository.save(managerRole);
		}
		Role operatorRole = this.roleRepository.findByRoleCode(RoleCode.OPERATOR);
		if (operatorRole == null) {
			operatorRole = new Role();
			operatorRole.setRoleCode(RoleCode.OPERATOR);
			operatorRole.setRoleLabel("OPERATOR");
			this.roleRepository.save(operatorRole);
		}
		Role trainerRole = this.roleRepository.findByRoleCode(RoleCode.TRAINER);
		if (trainerRole == null) {
			trainerRole = new Role();
			trainerRole.setRoleCode(RoleCode.TRAINER);
			trainerRole.setRoleLabel("TRAINER");
			this.roleRepository.save(trainerRole);
		}
	}
}