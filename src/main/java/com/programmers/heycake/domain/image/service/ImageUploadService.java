package com.programmers.heycake.domain.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

	String upload(MultipartFile multipartFile, String dirName);

	void delete(String subPath, String savedFilename);
}
