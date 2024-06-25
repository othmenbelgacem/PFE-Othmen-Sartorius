package com.sartorius.tma.business.services.files;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.sartorius.tma.config.FileStorageProperties;
import com.sartorius.tma.persistence.entities.Media;

public class LocalFileService implements IFileService {
	private static final Logger log = LoggerFactory.getLogger(LocalFileService.class);

	private final Path fileStorageLocation;

	@Autowired
	public LocalFileService(FileStorageProperties fileStorageProperties) throws Exception {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (IOException ex) {
			throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	@Override
	public String storeFile(MultipartFile file) throws Exception {
		String[] splitName = file.getOriginalFilename().split("\\.");
		String extension = null;
		if (splitName.length != 0) {
			extension = splitName[splitName.length - 1];
		} else {
			String[] splitExtension = file.getContentType().split("/");
			extension = splitExtension[1];
		}
		String fileName = RandomStringUtils.random(10, true, true) + "." + extension;

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	@Override
	public Resource loadFileAsResource(String fileName) throws Exception {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
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
	public String storePdfFile(String resourceName, byte[] pdfFile) throws Exception {
		String fileName = resourceName + ".pdf";
		try {
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(new ByteArrayInputStream(pdfFile), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (Exception ex) {
			throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	@Async
	@Override
	public void deleteFile(Media media) {
		try {
			Path filePath = this.fileStorageLocation.resolve(media.getMediaLabel()).normalize();
			Files.delete(filePath);
			// mediaService.deleteMedia(media.getId());
		} catch (NoSuchFileException x) {
			log.error("%s: no such" + " file", media.getMediaLabel());
		} catch (IOException x) {
			log.error(x.getMessage());
		}
	}

	@Override
	public Long getFileSize(String fileName) {
		return Optional.ofNullable(fileName).map(file -> getAbsolutePath(fileName)).map(this::sizeFromFile).orElse(0L);
	}

	private Path getAbsolutePath(String fileName) {

		Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
		return filePath;
	}
	
	private Long sizeFromFile(Path path) {
		try {
			return Files.size(path);
		} catch (IOException ioException) {
			log.error("Error while getting the file size", ioException);
		}
		return 0L;
	}

	@Override
	public byte[] readByteRangeNew(String fileName, long start, long end) throws Exception {
		Path path = getAbsolutePath(fileName);
		byte[] data = Files.readAllBytes(path);
		byte[] result = new byte[(int) (end - start) + 1];
		System.arraycopy(data, (int) start, result, 0, (int) (end - start) + 1);
		return result;
	}

}
