package com.example.s3demo.repository;

import com.example.s3demo.model.entity.File;
import com.example.s3demo.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
