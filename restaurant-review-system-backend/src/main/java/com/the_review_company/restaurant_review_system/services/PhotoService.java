package com.the_review_company.restaurant_review_system.services;


import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoService {

    Photo uploadPhoto(MultipartFile file);
    Optional<Resource> getPhotoAsResource(String id);

}
