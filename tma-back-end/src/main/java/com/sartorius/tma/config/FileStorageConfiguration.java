package com.sartorius.tma.config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.sartorius.tma.business.services.files.IFileService;
import com.sartorius.tma.business.services.files.LocalFileService;


@Configuration
public class FileStorageConfiguration {

	@Bean
	@ConditionalOnProperty(name = "storage.remote", havingValue = "false", matchIfMissing = false)
	public IFileService getLocalFileService(FileStorageProperties fileStorageProperties) throws Exception {
		return new LocalFileService(fileStorageProperties);
	}

}
