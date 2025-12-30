package com.the_review_company.restaurant_review_system.controllers;

import com.the_review_company.restaurant_review_system.domain.DTOs.PhotoDTO;
import com.the_review_company.restaurant_review_system.domain.entities.Photo;
import com.the_review_company.restaurant_review_system.mapper.PhotoMapper;
import com.the_review_company.restaurant_review_system.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final PhotoMapper photoMapper;


    @PostMapping
    public ResponseEntity<PhotoDTO> uploadPhoto(@RequestParam("file") MultipartFile file){
        Photo savedPhoto = photoService.uploadPhoto(file);
        return ResponseEntity.ok(photoMapper.toDTO(savedPhoto));
    }


    @GetMapping(path = "/{photoId:.+}")
    public ResponseEntity<Resource> getPhotoById(@PathVariable String photoId){
        return photoService.getPhotoAsResource(photoId)
                .map(photo ->
                ResponseEntity.ok()
                        .contentType(MediaTypeFactory.getMediaType(photo)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(photo)
        ).orElse(ResponseEntity.notFound().build());
    }


}
