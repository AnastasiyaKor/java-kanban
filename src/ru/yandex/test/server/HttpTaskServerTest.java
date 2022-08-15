package server;

import adapter.LocalDateTimeAdapter;
import adapter.SubTaskAdapter;
import adapter.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    HttpTaskServer httpTaskServer;
    KVServer kvServer;

    @BeforeEach
    public void startServers() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.startHttpTaskServer();
    }

    @AfterEach
    public void stopServer() {
        kvServer.stop();
        httpTaskServer.stop();
    }

    @Test
    void test1_CheckingTheGETRequestTask() {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test2_CheckingTheGETRequestSubtask() {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test3_CheckingTheGETRequestEpic() {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test4_CheckingThePOSTRequestTask() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        Gson gson = gsonBuilder.create();
        Task task = new Task("task1", "desTask1", 30, "2000-11-11T10:00");
        String json = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test5_CheckingThePOSTRequestEpic() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Epic task = new Epic("эпик1", "описание эпика 1");
        String json = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test6_CheckingThePOSTRequestSubtask() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()); ;
        gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
        Gson gson = gsonBuilder.create();
        SubTask subTask = new SubTask("подазача1", "описание подзадачи 1", 30,
                "2000-11-11T10:00", 0);
        String json = gson.toJson(subTask);
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test7_CheckingThePOSTRequestTaskId() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
        Gson gson = gsonBuilder.create();
        Task task = new Task("задача1", "описание задачи1", 30, "2000-12-11T10:00");
        String json = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/task?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test8_CheckingThePOSTRequestEpicId() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Epic epic = new Epic("эпик1", "описание эпика1");
        String json = gson.toJson(epic);
        URI uri = URI.create("http://localhost:8080/tasks/epic?id=0");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test9_CheckingTheDELETERequestTask() {
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" + "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test10_CheckingTheDELETERequestSubtask() {
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test11_CheckingTheDELETERequestEpic() {
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test12_CheckingTheGETRequestHistory() {
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test13_CheckingTheGETRequestPriority() {
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void test15_CheckingTheGETRequestPriority() {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "*/*")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response;
        try {
            response = client.send(request, handler);
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            throw new TaskClientException("Во время выполнения запроса ресурса по url-адресу: '" + uri +
                    "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

}