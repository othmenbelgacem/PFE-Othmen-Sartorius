package com.sartorius.tma.business.services.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sartorius.tma.persistence.entities.Media;

public class RemoteFileService implements IFileService {
	private static final Logger logger = LoggerFactory.getLogger(RemoteFileService.class);

	@Autowired
	private AmazonS3 s3Client;

	@Value("${storage.spaces.bucket}")
	private String spaceBucket;

	@Override
	public String storeFile(MultipartFile file) throws IOException {
		String[] splitName = file.getOriginalFilename().split("\\.");
		String extension = null;
		if (splitName.length != 0) {
			extension = splitName[splitName.length - 1];
		} else {
			String[] splitExtension = file.getContentType().split("/");
			extension = splitExtension[1];
		}
		String fileName = RandomStringUtils.random(10, true, true) + "." + extension;

		saveFileToServer(file.getInputStream(), fileName, file.getContentType());
		return fileName;

	}



	private void saveFileToServer(InputStream file, String key, String contentType) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.available());
		if (contentType != null && !"".equals(contentType)) {
			metadata.setContentType(contentType);
		}
		s3Client.putObject(new PutObjectRequest(spaceBucket, key, file, metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	@Override
	public Resource loadFileAsResource(String fileName) throws Exception {
		try {
			S3Object s3object = s3Client.getObject(new GetObjectRequest(spaceBucket, fileName));
			Resource resource = new InputStreamResource(s3object.getObjectContent());
			if (resource.exists()) {
				return resource;
			} else {
				throw new Exception("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new Exception("File not found " + fileName, ex);
		}
	}
	

	@Override
	public Long getFileSize(String fileName) {
		return s3Client.getObjectMetadata(spaceBucket, fileName).getContentLength();
	}

	@Override
	public byte[] readByteRangeNew(String fileName, long start, long end) throws Exception {
		return s3Client.getObject(spaceBucket, fileName).getObjectContent().readAllBytes();
	}
}
