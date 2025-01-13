package com.example.urlShorter.urls.service;

import com.example.urlShorter.urls.UrlConfig;
import com.example.urlShorter.urls.exceptions.ClickLimitExceededException;
import com.example.urlShorter.urls.exceptions.LinkBlockedException;
import com.example.urlShorter.urls.repository.UrlRepository;
import com.example.urlShorter.urls.model.Url;
import com.example.urlShorter.users.model.User;
import com.example.urlShorter.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlConfig urlConfig;
    private final UserRepository userRepository;

    @Autowired
    public UrlService(UrlRepository urlRepository, UserRepository userRepository, UrlConfig urlConfig) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
        this.urlConfig = urlConfig;
    }

    public Url createUrl(UUID userUuid, String originalUrl, Integer limit, Long lifetimeSec) {
        User creator = userRepository.findById(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь c id: " + userUuid + " не найден."));

        String shortUrl;
        do {
            shortUrl = generateUniqueShortUrl(originalUrl, userUuid);
        } while (urlRepository.existsByShortUrl(shortUrl));

        Url newUrl = new Url();
        newUrl.setCreator(creator);
        newUrl.setOriginalUrl(originalUrl);
        newUrl.setShortUrl(shortUrl);
        newUrl.setRemainingClicks(limit != null ? limit : urlConfig.getDefaultMaxClicks());
        newUrl.setExpirationTime(calculateExpirationTime(lifetimeSec));

        return urlRepository.save(newUrl);
    }

    private String generateUniqueShortUrl(String originalUrl, UUID uuid) {
        try {
            String input = originalUrl + uuid.toString() + System.nanoTime();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            String base64Hash = Base64.getUrlEncoder().encodeToString(hash);
            return base64Hash.substring(0, 6).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка генерации короткой ссылки", e);
        }
    }

    private LocalDateTime calculateExpirationTime(Long lifetimeSec) {
        return LocalDateTime.now().plusSeconds(lifetimeSec != null ? lifetimeSec : urlConfig.getMaxLifetime());
    }

    public String resolveUrl(String shortCode) throws LinkBlockedException {
        Url url = urlRepository.findByShortUrl(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена"));

        if (url.isBlocked()) {
            throw new LinkBlockedException("Ссылка была заблокирована, так как количество переходов исчерпано. " +
                    "Вы можете обновить переходы в меню");
        }

        if (url.getRemainingClicks() == null || url.getRemainingClicks() <= 0) {
            url.setBlocked(true); // Блокируем ссылку
            urlRepository.save(url);
            throw new ClickLimitExceededException("Количество переходов исчерпано.");
        }

        url.setRemainingClicks(url.getRemainingClicks() - 1);

        if (url.getRemainingClicks() <= 0) {
            url.setBlocked(true);
        }

        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    public boolean deleteUrl(UUID userUuid, Long urlId) {
        if (!userRepository.existsById(userUuid)) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена"));

        if (!url.getCreator().getId().equals(userUuid)) {
            throw new IllegalArgumentException("Вы не являетесь владельцем этой ссылки");
        }

        urlRepository.deleteById(urlId);
        return !urlRepository.findById(urlId).isPresent();
    }

    public boolean updateRemainingClicks(UUID userUuid, Long urlId, int newClicks) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена"));

        if (!url.getCreator().getId().equals(userUuid)) {
            throw new IllegalArgumentException("У вас нет доступа.");
        }

        url.setRemainingClicks(newClicks);
        url.setBlocked(false);

        urlRepository.save(url);
        return true;
    }

    public boolean updateExpirationTime(UUID userUuid, Long urlId, Long lifetimeSec) {
        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена"));

        if (!url.getCreator().getId().equals(userUuid)) {
            throw new IllegalArgumentException("У вас нет доступа.");
        }

        LocalDateTime newExpirationTime = LocalDateTime.now().plusSeconds(lifetimeSec);
        url.setExpirationTime(newExpirationTime);
        urlRepository.save(url);
        return true;
    }
}
