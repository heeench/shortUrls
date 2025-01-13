package com.example.urlShorter.users.repository;

import com.example.urlShorter.urls.model.Url;
import com.example.urlShorter.users.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository <User, UUID> {
    Optional<User> findById(UUID uuid);
    Optional<User> findByLoginAndPassword(String login, String password);

    @Query("SELECT u.urls FROM User u WHERE u.id = :userId")
    List<Url> findUrlsByUserId(@Param("userId") UUID userId);
}
