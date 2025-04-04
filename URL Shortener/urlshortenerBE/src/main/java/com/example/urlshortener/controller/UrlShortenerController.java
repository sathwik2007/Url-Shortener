package com.example.urlshortener.controller;

import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.service.UrlShortenerService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api")
public class UrlShortenerController {
    @Autowired
    private UrlShortenerService urlShortenerService;

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
