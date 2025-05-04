package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    //method to find the long url using the shortCode
    Optional<UrlMapping> findByShortCode(String shortCode);

    //method to detect any collisions with existing short codes
    boolean existsByShortCode(String shortCode);
}
