package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import com.example.urlshortener.service.QrCodeService;
import com.example.urlshortener.service.UrlShortenerService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api")
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UrlMappingRepository urlMappingRepository;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody UrlRequest request) {
        UrlMapping mapping = urlShortenerService.shortenUrlString(request.getLongUrl());
        String shortUrl = "http://localhost:8080/api/" + mapping.getShortCode();
        return ResponseEntity.ok(new UrlResponse(shortUrl));
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirect(@PathVariable String shortCode) {
        String longUrl = urlShortenerService.getLongUrl(shortCode);
        if(longUrl != null) {
            return new RedirectView(longUrl);
        } else {
            return new RedirectView("/error");
        }
    }

    @GetMapping("/qrcode/{shortCode}")
    public ResponseEntity<byte[]> getQrCode(@PathVariable String shortCode) {
        String shortUrl = urlShortenerService.getLongUrl(shortCode);

        try {
            byte[] qrImage = qrCodeService.generateQRCode(shortUrl, 200, 200);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


class UrlRequest {
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}


class UrlResponse {
    private String shortUrl;

    public UrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }


    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
