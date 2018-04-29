package com.simple.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Create by S I M P L E on 2017/09/24
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
