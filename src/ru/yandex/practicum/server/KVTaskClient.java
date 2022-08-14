package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String uri;
    private String apiKey;

    public KVTaskClient(String uri) {
        this.uri = uri;
        apiKey = register();
    }

    private String register() {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8078/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiKey = response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return apiKey;
    }

    public void put(String key, String json) {
        if (key.equals("tasks")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiKey))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (!(response.statusCode() == 200)) {
                    throw new TaskClientException("Что-то пошло не так. Сервер вернул код состояния:" +
                            response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        } else if (key.equals("epics")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiKey))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (!(response.statusCode() == 200)) {
                    throw new TaskClientException("Что-то пошло не так. Сервер вернул код состояния:" +
                            response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        } else if (key.equals("subtasks")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiKey))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (!(response.statusCode() == 200)) {
                    throw new TaskClientException("Что-то пошло не так. Сервер вернул код состояния:" +
                            response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        } else if (key.equals("history")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiKey))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (!(response.statusCode() == 200)) {
                    throw new TaskClientException("Что-то пошло не так. Сервер вернул код состояния:" +
                            response.statusCode());
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        }
    }

    public String load(String key) {
        String task = null;
        HttpClient client = HttpClient.newHttpClient();
        if (key.equals("tasks")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiKey))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .build();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    if (response.body() != null) {
                        task = response.body();
                    }
                } else {
                    throw new TaskClientException("response.statusCode() != 200");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        } else if (key.equals("epics")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiKey))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .build();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    if (response.body() != null) {
                        task = response.body();
                    }
                } else {
                    throw new TaskClientException("response.statusCode() != 200");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        } else if (key.equals("subtasks")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiKey))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .build();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    if (response.body() != null) {
                        task = response.body();
                    }
                } else {
                    throw new TaskClientException("response.statusCode() != 200");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        } else if (key.equals("history")) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiKey))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .build();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    if (response.body() != null) {
                        task = response.body();
                    }
                } else {
                    throw new TaskClientException("response.statusCode() != 200");
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        }
        return task;
    }
}
