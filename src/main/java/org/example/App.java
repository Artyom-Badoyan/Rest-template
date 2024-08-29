package org.example;

import org.example.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class App {
    private static final String BASE_URL = "http://localhost:8081/api/users";
    private static String sessionId;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Получение всех пользователей
        getAllUsers(restTemplate);

        // 2. Сохранение нового пользователя
        saveUser(restTemplate, new User(3L, "James", "Brown", (byte) 25));

        // 3. Изменение пользователя
        updateUser(restTemplate, new User(3L, "Thomas", "Shelby", (byte) 25));

        // 4. Удаление пользователя
        deleteUser(restTemplate, 3L);
    }

    private static void getAllUsers(RestTemplate restTemplate) {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);
        sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("Session ID: " + sessionId);
        System.out.println("Users: " + response.getBody());
        System.out.println("HTTP Status Code (getAllUsers): " + response.getStatusCodeValue());
    }

    private static void saveUser(RestTemplate restTemplate, User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, sessionId);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, request, String.class);

        System.out.println("Save Response: " + response.getBody());
        System.out.println("HTTP Status Code (saveUser): " + response.getStatusCodeValue());
    }

    private static void updateUser(RestTemplate restTemplate, User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.COOKIE, sessionId);

        HttpEntity<User> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, request, String.class);

        System.out.println("Update Response: " + response.getBody());
        System.out.println("HTTP Status Code (updateUser): " + response.getStatusCodeValue());
    }

    private static void deleteUser(RestTemplate restTemplate, Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionId);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, request, String.class);

        System.out.println("Delete Response: " + response.getBody());
        System.out.println("HTTP Status Code (deleteUser): " + response.getStatusCodeValue());
    }
}