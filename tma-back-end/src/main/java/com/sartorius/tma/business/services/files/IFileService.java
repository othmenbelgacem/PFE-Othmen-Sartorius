package com.sartorius.tma.business.services.files;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.sartorius.tma.persistence.entities.Media;

public interface IFileService {

	String storeFile(MultipartFile file) throws Exception;

	String storePdfFile(String resourceName, byte[] pdfFile) throws Exception;

	Resource loadFileAsResource(String fileName) throws Exception;

	void deleteFile(Media media);
	
	Long getFileSize(String fileName);
	
	byte[] readByteRangeNew(String fileName, long start, long end) throws Exception;

}
