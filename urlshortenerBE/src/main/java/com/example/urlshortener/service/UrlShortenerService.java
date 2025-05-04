package com.example.urlshortener.service;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UrlShortenerService {
    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int SHORT_CODE_LENGTH = 6;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    private final SecureRandom random = new SecureRandom();
    @Autowired
    private ApplicationArguments applicationArguments;

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(SHORT_CODE_LENGTH);
        for(int i = 0; i < SHORT_CODE_LENGTH; i++) {
            int index = random.nextInt(ALPHANUMERIC.length());
            code.append(ALPHANUMERIC.charAt(index));
        }
        return code.toString();
    }

    @Transactional
    public UrlMapping shortenUrlString(String longUrl) {
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (urlMappingRepository.existsByShortCode(shortCode));

        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl(longUrl);
        mapping.setShortCode(shortCode);
        return urlMappingRepository.save(mapping);
    }

    @Cacheable(value = "urlCache", key = "#shortCode")
    public String getLongUrl(String shortCode) {
        return urlMappingRepository.findByShortCode(shortCode).map(UrlMapping::getLongUrl).orElse(null);
    }
}
