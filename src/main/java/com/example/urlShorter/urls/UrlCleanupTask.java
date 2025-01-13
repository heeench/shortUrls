package com.example.urlShorter.urls;

import com.example.urlShorter.urls.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UrlCleanupTask {

    @Autowired
    private final UrlRepository urlRepository;

    public UrlCleanupTask(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(fixedRate = 60000) // Запуск каждые 60 секунд
    public void cleanupExpiredUrls() {
        urlRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
}

