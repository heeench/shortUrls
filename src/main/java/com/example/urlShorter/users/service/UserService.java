package com.example.urlShorter.users.service;

import com.example.urlShorter.urls.model.Url;
import com.example.urlShorter.urls.service.UrlService;
import com.example.urlShorter.users.repository.UserRepository;
import com.example.urlShorter.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository, UrlService urlService) {
        this.userRepository = userRepository;
    }
    public Optional<User> getUserByCredentials(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
    public User createUser(User user) {
        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());

        return userRepository.save(newUser);
    }

    public Optional<User> getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    public List<Url> getUserUrls(UUID id) {
        return userRepository.findUrlsByUserId(id);
    }
}
