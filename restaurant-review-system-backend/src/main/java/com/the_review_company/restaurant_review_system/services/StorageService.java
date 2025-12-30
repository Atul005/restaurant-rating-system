package com.the_review_company.restaurant_review_system.services;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

public interface StorageService {

    String store(MultipartFile multipartFile, String filename);
    Optional<Resource> loadAsResource(String id);

}
