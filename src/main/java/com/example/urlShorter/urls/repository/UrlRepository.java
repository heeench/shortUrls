package com.example.urlShorter.urls.repository;

import com.example.urlShorter.urls.model.Url;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {
    UUID findCreatorUuidById(Long id);

    boolean existsByShortUrlAndCreator_Id(String shortUrl, UUID creatorId);

    Optional<Url> findByShortUrl(String shortUrl);

    void deleteByExpirationTimeBefore(LocalDateTime time);

    boolean existsByShortUrl(String shortUrl);
}
