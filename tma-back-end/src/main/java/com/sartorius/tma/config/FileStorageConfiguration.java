package com.sartorius.tma.config;

import javax.mail.internet.AddressException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sartorius.tma.business.services.files.IFileService;
import com.sartorius.tma.business.services.files.LocalFileService;
import com.sartorius.tma.business.services.files.RemoteFileService;

@Configuration
public class FileStorageConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageConfiguration.class);

	@Value("${storage.spaces.key}")
	private String spaceKey;

	@Value("${storage.spaces.secret}")
	private String spaceSecret;

	@Bean
	@ConditionalOnProperty(name = "storage.remote", havingValue = "true", matchIfMissing = false)
	public AmazonS3 getAmazonS3() throws AddressException {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(spaceKey, spaceSecret);
		return AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
	}

	@Bean
	@ConditionalOnProperty(name = "storage.remote", havingValue = "true", matchIfMissing = false)
	public IFileService getRemoteFileService() {
		return new RemoteFileService();
	}

	@Bean
	@ConditionalOnProperty(name = "storage.remote", havingValue = "false", matchIfMissing = false)
	public IFileService getLocalFileService(FileStorageProperties fileStorageProperties) throws Exception {
		return new LocalFileService(fileStorageProperties);
	}

}
