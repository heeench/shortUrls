
# Сервис укороченных URL

Этот репозиторий содержит простой **сервис для укорочения URL**, который позволяет пользователям сокращать ссылки, управлять временем их жизни, отслеживать количество переходов и выполнять другие операции с ссылками. Сервис построен с использованием **Spring Boot** и поддерживает создание, разрешение и управление короткими ссылками.

## Возможности

- **Создание пользовательских аккаунтов**: Регистрация нового пользователя с логином и паролем.
- **Вход в систему**: Аутентификация пользователей.
- **Создание коротких ссылок**: Преобразование длинных URL в короткие с возможностью ограничения переходов и установки срока жизни.
- **Разрешение коротких ссылок**: Доступ к оригинальному URL с помощью короткой ссылки.
- **Управление профилем**: Просмотр профиля пользователя и всех коротких ссылок, созданных им.
- **Обновление настроек короткой ссылки**: Изменение оставшегося количества переходов или времени жизни существующей ссылки.
- **Удаление коротких ссылок**: Удаление ранее созданных коротких ссылок.

## Требования

- **Java 17** или выше
- **Maven** или **Gradle** для управления проектом
- **Spring Boot** для создания приложения
- **JDBC/База данных** (например, MySQL, H2) для хранения данных о пользователях и ссылках

## Установка

1. **Клонировать репозиторий**:
   ```bash
   git clone https://github.com/yourusername/url-shortener-service.git

2. **Перейти в каталог проекта**:
    ```bash
    cd url-shortener-service

3. **Собрать проект, используя Maven**:
    ```bash
    mvn clean install

4. **Запустить приложение**:
    ```bash
    mvn spring-boot:run

5. **Настроить базу данных**:
   укажите настройки для подключения к базе данных в application.properties или application.yml.

6. **Использование консольного интерфейса**:
   после запуска приложения пользователь может взаимодействовать с сервисом через консольный интерфейс.

##Инструкция по использованию консольного интерфейса
После запуска сервиса пользователи могут взаимодействовать с консольным интерфейсом, вводя свои выборы. 
Доступные опции:
  1. Создать пользователя: 
    Введите 1 для создания нового пользователя.
     ```yaml
      Введите логин: user123
      Введите пароль: password123  
      Пользователь успешно создан. Ваш UUID: 123e4567-e89b-12d3-a456-426614174000
   Пользователю будет присвоен UUID для дальнейшей идентификации.

  2. Войти в систему:
    Введите 2 для входа в аккаунт.
     ```yaml
      Введите логин: user123
      Введите пароль: password123
      Вы успешно вошли в аккаунт. Ваш UUID: 123e4567-e89b-12d3-a456-426614174000
   После успешного входа система отобразит UUID пользователя.
   
  3. Создать короткую ссылку: 
      Введите 3 для создания новой короткой ссылки.
     ```yaml
      Введите длинный URL: https://example.com
      Введите лимит переходов (или оставьте пустым): 2
      Введите срок жизни в секундах (или оставьте пустым): 3600
      Короткая ссылка создана: TYAWVQ
  5. Перейти по короткой ссылке:
      Введите 4 для перехода по короткой ссылке.
     ```yaml
      Введите короткую ссылку: TYAWVQ
      Открываем ссылку: https://example.com
  7. Показать профиль:
      Введите 5 для просмотра профиля.
     ```yaml
      === Профиль пользователя ===
      Логин: user123
      UUID: 123e4567-e89b-12d3-a456-426614174000
      Созданные ссылки:
       - id: 1 
         - TYAWVQ -> https://example.com 
         - Осталось переходов -> 2 
         - Дата истечения действия ссылки -> 2025-01-13T23:08:26.146127

  9. Обновить количество оставшихся переходов:
      Введите 6 для обновления оставшихся переходов.
      ```yaml
      Введите ID ссылки для обновления переходов: 1
      Введите новое количество переходов: 20
      Количество переходов успешно обновлено.
  10. Обновить срок жизни:
      Введите 7 для обновления срока жизни.
      ```yaml
      Введите ID ссылки для обновления срока жизни: 1
      Введите новый срок жизни в секундах: 3600
      Срок жизни ссылки успешно обновлен.
  12. Удалить короткую ссылку:
      Введите 8 для удаления короткой ссылки.
      ```yaml
      Введите ID ссылки для удаления: 1
      Ссылка успешно удалена.
  14. Выйти из аккаунта: Выйти из системы.
      Введите 9 для выхода.
      ```yaml
       Вы успешно вышли из аккаунта.

       Выберите действие:
       1. Создать пользователя
       2. Войти в аккаунт
       0. Выход

  15. Выход: Выйти из консольного интерфейса.
      Введите 0 для выхода из сервиса.
      ```yaml
      Выход...

## Обработка ошибок
Система проверяет несколько типов ошибок, включая:

Неверные учетные данные пользователя: если пользователь вводит неправильный логин или пароль, система уведомит пользователя и предложит попробовать снова.
Неверная ссылка или ссылка не найдена: если короткая ссылка не существует, система отобразит ошибку.
Ссылка заблокирована: если ссылка превысила максимальное количество переходов или срок ее жизни истек, пользователю будет сообщено, что ссылка заблокирована.
