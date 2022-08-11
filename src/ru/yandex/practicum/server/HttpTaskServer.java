package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;
import service.Managers;
import adapter.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class HttpTaskServer extends Managers {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static InMemoryTaskManager fb;
    HttpServer httpServer;

    static {
        fb = new InMemoryTaskManager();
    }

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new TaskHandler());
        httpServer.createContext("/tasks/epic", new TaskHandler());
        httpServer.createContext("/tasks/history", new TaskHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    static class TaskHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            try {
                System.out.println("Началась обработка /tasks запроса от клиента.");
                OutputStream os = exchange.getResponseBody();
                InputStream is = exchange.getRequestBody();
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                if (path.endsWith("/tasks/task")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                    gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
                    Gson gson = gsonBuilder.create();
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    switch (method) {
                        case "GET":
                            String jsonTask = gson.toJson(fb.getAllTasks());
                            exchange.sendResponseHeaders(200, jsonTask.getBytes().length);
                            os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                            os.close();
                            break;
                        case "POST":
                            System.out.println("Тело запроса:\n" + body);
                            Task task = gson.fromJson(body, Task.class);
                            fb.createTasks(task);
                            exchange.sendResponseHeaders(200, 0);
                            exchange.close();
                            break;
                        case "DELETE":
                            System.out.println("Тело запроса:\n" + body);
                            exchange.sendResponseHeaders(200, 0);
                            fb.clearTasks();
                            exchange.close();
                        default:
                    }
                } else if (path.endsWith("/tasks/task/id=")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                    gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
                    Gson gson = gsonBuilder.create();
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    switch (method) {
                        case "GET":
                            String[] uriParts = path.split("=");
                            int id = Integer.parseInt(uriParts[1]);
                            String jsonTask = gson.toJson(fb.getTaskById(id));
                            exchange.sendResponseHeaders(200, jsonTask.getBytes().length);
                            os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                            os.close();
                            break;
                        case "POST":
                            Task task = gson.fromJson(body, Task.class);
                            exchange.sendResponseHeaders(200, 0);
                            fb.refreshTasks(task);
                            exchange.close();
                            break;
                        case "DELETE":
                            String[] uriSplit = path.split("=");
                            int idDel = Integer.parseInt(uriSplit[1]);
                            exchange.sendResponseHeaders(200, 0);
                            fb.removeTaskById(idDel);
                            exchange.close();
                        default:
                            throw new IllegalStateException("Unexpected value: " + method);
                    }
                } else if (path.endsWith("/tasks/epic")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    switch (method) {
                        case "GET":
                            String jsonTask = gson.toJson(fb.getAllEpics());
                            exchange.sendResponseHeaders(200, jsonTask.getBytes().length);
                            os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                            os.close();
                            break;
                        case "POST":
                            System.out.println("Тело запроса:\n" + body);
                            Epic epic = gson.fromJson(body, Epic.class);
                            fb.createEpics(epic);
                            exchange.sendResponseHeaders(200, 0);
                            exchange.close();
                            break;
                        case "DELETE":
                            System.out.println("Тело запроса:\n" + body);
                            Epic epic1 = gson.fromJson(body, Epic.class);
                            System.out.println(epic1);
                            exchange.sendResponseHeaders(200, 0);
                            fb.clearEpics();
                            exchange.close();
                        default:
                            throw new IllegalStateException("Unexpected value: " + method);
                    }
                } else if (path.endsWith("/tasks/epic/id=")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    switch (method) {
                        case "GET":
                            String[] uriParts = path.split("=");
                            int id = Integer.parseInt(uriParts[1]);
                            String jsonTask = gson.toJson(fb.getEpicById(id));
                            exchange.sendResponseHeaders(200, jsonTask.getBytes().length);
                            os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                            os.close();
                            break;
                        case "POST":
                            Epic epic = gson.fromJson(body, Epic.class);
                            exchange.sendResponseHeaders(200, 0);
                            fb.refreshTasks(epic);
                            exchange.close();
                            break;
                        case "DELETE":
                            String[] uriSplit = path.split("=");
                            int idDel = Integer.parseInt(uriSplit[1]);
                            exchange.sendResponseHeaders(200, 0);
                            fb.removeEpicById(idDel);
                            exchange.close();
                        default:
                            throw new IllegalStateException("Unexpected value: " + method);
                    }
                } else if (path.endsWith("/tasks/subtask")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                    gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
                    Gson gson = gsonBuilder.create();
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    switch (method) {
                        case "GET":
                            String jsonTask = gson.toJson(fb.getAllSubTasks());
                            exchange.sendResponseHeaders(200, jsonTask.getBytes().length);
                            os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                            os.close();
                            break;
                        case "POST":
                            SubTask subTask = gson.fromJson(body, SubTask.class);
                            System.out.println(subTask);
                            exchange.sendResponseHeaders(200, 0);
                            fb.createTasks(subTask);
                            exchange.close();
                            break;
                        case "DELETE":
                            exchange.sendResponseHeaders(200, 0);
                            fb.clearSubTasks();
                            exchange.close();
                        default:
                            throw new IllegalStateException("Unexpected value: " + method);
                    }
                } else if (path.endsWith("/tasks/subtask/id=")) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                    gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
                    Gson gson = gsonBuilder.create();
                    String body = new String(is.readAllBytes(), DEFAULT_CHARSET);
                    switch (method) {
                        case "GET":
                            String[] uriParts = path.split("=");
                            int id = Integer.parseInt(uriParts[1]);
                            String jsonTask = gson.toJson(fb.getSubTaskById(id));
                            exchange.sendResponseHeaders(200, jsonTask.getBytes().length);
                            os.write(jsonTask.getBytes(DEFAULT_CHARSET));
                            os.close();
                            break;
                        case "POST":
                            SubTask subTask = gson.fromJson(body, SubTask.class);
                            exchange.sendResponseHeaders(200, 0);
                            fb.refreshTasks(subTask);
                            exchange.close();
                            break;
                        case "DELETE":
                            String[] uriSplit = path.split("=");
                            int idDel = Integer.parseInt(uriSplit[1]);
                            exchange.sendResponseHeaders(200, 0);
                            fb.removeSubTaskById(idDel);
                            exchange.close();
                        default:
                            throw new IllegalStateException("Unexpected value: " + method);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

}
