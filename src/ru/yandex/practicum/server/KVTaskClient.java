package server;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    URL url;

    public KVTaskClient(URL url) {
        this.url = url;
    }

    public void put(String key, String json) {
        if (key.equals("tasks")) {
            URI url = URI.create("http://localhost:8078/save/task?API_TOKEN=DEBUG");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                } else {
                    System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
                }
            } catch (NullPointerException | IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        }
    }

    public String load(String key) {
        String task = null;
        HttpClient client = HttpClient.newHttpClient();
        if (key.equals("tasks")) {
            URI url = URI.create("http://localhost:8078/load/tasks?API_TOKEN=DEBUG");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(url)
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Accept", "*/*")
                    .build();
            try {
                final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                task = response.body();
            } catch (NullPointerException | IOException | InterruptedException e) {
                System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                        "Проверьте, пожалуйста, адрес и повторите попытку.");
            }
        }
        return task;
    }
}
