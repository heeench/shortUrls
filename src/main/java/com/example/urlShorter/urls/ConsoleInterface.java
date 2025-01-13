package com.example.urlShorter.urls;

import com.example.urlShorter.urls.model.Url;
import com.example.urlShorter.urls.service.UrlService;
import com.example.urlShorter.users.model.User;
import com.example.urlShorter.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


@Component
public class ConsoleInterface {

    private final UrlService urlService;
    private final UserService userService;
    private final UrlConfig urlConfig;
    private User currentUser; // Текущий пользователь сессии

    @Autowired
    public ConsoleInterface(UrlService urlService, UserService userService, UrlConfig urlConfig) {
        this.urlService = urlService;
        this.userService = userService;
        this.urlConfig = urlConfig;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== URL Shortener Console Interface ===");

        while (true) {
            displayMenu();
            String choice = scanner.nextLine();
            handleMenuChoice(choice, scanner);
        }
    }

    private void displayMenu() {
        System.out.println("\nВыберите действие:");
        if (currentUser == null) {
            System.out.println("1. Создать пользователя");
            System.out.println("2. Войти в аккаунт");
        } else {
            System.out.println("3. Создать короткую ссылку");
            System.out.println("4. Перейти по короткой ссылке");
            System.out.println("5. Показать профиль");
            System.out.println("6. Обновить количество переходов");
            System.out.println("7. Обновить срок жизни ссылки");
            System.out.println("8. Удалить ссылку");
            System.out.println("9. Выйти из аккаунта");
        }
        System.out.println("0. Выход");
    }

    private void handleMenuChoice(String choice, Scanner scanner) {
        switch (choice) {
            case "1":
                if (currentUser == null) createUser(scanner);
                else unauthorizedMessage();
                break;
            case "2":
                if (currentUser == null) login(scanner);
                else unauthorizedMessage();
                break;
            case "3":
                if (currentUser != null) createShortUrl(scanner);
                else unauthorizedMessage();
                break;
            case "4":
                if (currentUser != null) openShortUrl(scanner);
                else unauthorizedMessage();
                break;
            case "5":
                if (currentUser != null) showProfile();
                else unauthorizedMessage();
                break;
            case "6":
                if (currentUser != null) updateRemainingClicks(scanner);
                else unauthorizedMessage();
                break;
            case "7":
                if (currentUser != null) updateExpirationTime(scanner);
                else unauthorizedMessage();
                break;
            case "8":
                if (currentUser != null) deleteUrl(scanner);
                else unauthorizedMessage();
                break;
            case "9":
                if (currentUser != null) logout();
                else unauthorizedMessage();
                break;
            case "0":
                System.out.println("Выход...");
                System.exit(0);
            default:
                System.out.println("Некорректный ввод. Попробуйте снова.");
        }
    }

    private void updateRemainingClicks(Scanner scanner) {
        try {
            System.out.print("Введите ID ссылки для обновления переходов: ");
            Long urlId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите новое количество переходов: ");
            int newClicks = Integer.parseInt(scanner.nextLine());

            boolean result = urlService.updateRemainingClicks(currentUser.getId(), urlId, newClicks);
            if (result) {
                System.out.println("Количество переходов успешно обновлено.");
            } else {
                System.out.println("Ошибка при обновлении количества переходов.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: Ссылка неккоректна или пуста. (Подробнее: " + e.getMessage()+ ")");
        }
    }

    private void updateExpirationTime(Scanner scanner) {
        try {
            System.out.print("Введите ID ссылки для обновления срока жизни: ");
            Long urlId = Long.parseLong(scanner.nextLine());

            System.out.print("Введите новый срок жизни в секундах: ");
            Long lifetimeSec = Long.parseLong(scanner.nextLine());

            boolean result = urlService.updateExpirationTime(currentUser.getId(), urlId, lifetimeSec);
            if (result) {
                System.out.println("Срок жизни ссылки успешно обновлен.");
            } else {
                System.out.println("Ошибка при обновлении срока жизни.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: Ссылка неккоректна или пуста. (Подробнее: " + e.getMessage()+ ")");
        }
    }

    private void deleteUrl(Scanner scanner) {
        try {
            System.out.print("Введите ID ссылки для удаления: ");
            Long urlId = Long.parseLong(scanner.nextLine());

            boolean result = urlService.deleteUrl(currentUser.getId(), urlId);
            if (result) {
                System.out.println("Ссылка успешно удалена.");
            } else {
                System.out.println("Ошибка при удалении ссылки.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: Ссылка неккоректна или пуста. (Подробнее: " + e.getMessage()+ ")");
        }
    }

    private void createUser(Scanner scanner) {
        try {
            System.out.print("Введите логин: ");
            String login = scanner.nextLine();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            User user = new User();
            user.setLogin(login);
            user.setPassword(password);

            User createdUser = userService.createUser(user);
            System.out.println("Пользователь успешно создан. Ваш UUID: " + createdUser.getId());
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    private void login(Scanner scanner) {
        try {
            System.out.print("Введите логин: ");
            String login = scanner.nextLine();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            Optional<User> user = userService.getUserByCredentials(login, password);
            if (user.isPresent()) {
                currentUser = user.get();
                System.out.println("Вы успешно вошли в аккаунт. Ваш UUID: " + currentUser.getId());
            } else {
                System.out.println("Неверный логин или пароль.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при входе: " + e.getMessage());
        }
    }

    private void createShortUrl(Scanner scanner) {
        if (currentUser == null) {
            System.out.println("Вы должны войти в систему перед созданием ссылки.");
            return;
        }

        try {
            System.out.print("Введите длинный URL: ");
            String originalUrl = scanner.nextLine();

            System.out.print("Введите лимит переходов (или оставьте пустым): ");
            String limitInput = scanner.nextLine();
            Integer limit = limitInput.isEmpty() ? urlConfig.getDefaultMaxClicks() : Integer.parseInt(limitInput);

            System.out.print("Введите срок жизни в секундах (или оставьте пустым): ");
            String lifetimeInput = scanner.nextLine();
            Long lifetimeSec = lifetimeInput.isEmpty() ? urlConfig.getMaxLifetime() : Long.parseLong(lifetimeInput);

            Url url = urlService.createUrl(currentUser.getId(), originalUrl, limit, lifetimeSec);
            System.out.println("Короткая ссылка создана: " + url.getShortUrl());
        } catch (Exception e) {
            System.out.println("Ошибка при создании ссылки: " + e.getMessage());
        }
    }

    private void openShortUrl(Scanner scanner) {
        try {
            System.out.print("Введите короткую ссылку: ");
            String shortUrl = scanner.nextLine();

            String originalUrl = urlService.resolveUrl(shortUrl);

            if (originalUrl != null && !originalUrl.trim().isEmpty()) {
                System.out.println("Открываем ссылку: " + originalUrl);

                URI uri = new URI(originalUrl);

                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    // Для Windows
                    Runtime.getRuntime().exec("cmd /c start " + originalUrl);
                } else if (System.getProperty("os.name").toLowerCase().contains("nix") ||
                        System.getProperty("os.name").toLowerCase().contains("nux") ||
                        System.getProperty("os.name").toLowerCase().contains("mac")) {
                    // Для Linux
                    Runtime.getRuntime().exec("xdg-open " + originalUrl);
                    // Runtime.getRuntime().exec("open " + originalUrl); // Для MacOS
                }
            } else {
                System.out.println("Ошибка: Полученная ссылка пуста или некорректна.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при открытии ссылки: " + e.getMessage());
        }
    }



    private void showProfile() {
        try {
            System.out.println("=== Профиль пользователя ===");
            System.out.println("Логин: " + currentUser.getLogin());
            System.out.println("UUID: " + currentUser.getId());

            List<Url> urls = userService.getUserUrls(currentUser.getId());
            if (urls.isEmpty()) {
                System.out.println("У вас пока нет созданных ссылок.");
            } else {
                System.out.println("Созданные ссылки:");
                urls.forEach(url -> System.out.println(" - " + "id: " + url.getId() +
                        " \n   - "+ url.getShortUrl() + " -> " + url.getOriginalUrl() +
                        " \n   - " + "Осталось переходов -> " + url.getRemainingClicks() +
                        " \n   - " + "Дата истечения действия ссылки -> " + url.getExpirationTime()));
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении профиля: " + e.getMessage());
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Вы успешно вышли из аккаунта.");
    }

    private void unauthorizedMessage() {
        System.out.println("Пожалуйста, войдите в аккаунт, чтобы выполнить это действие.");
    }
}
