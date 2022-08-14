package server;

import adapter.LocalDateTimeAdapter;
import adapter.SubTaskAdapter;
import adapter.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final HttpServer httpServer;
    Gson gson = new Gson();
    GsonBuilder gsonBuilder = new GsonBuilder();

    public HttpTaskServer() throws IOException, URISyntaxException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/history", new TaskHandler());
    }

    public void startHttpTaskServer() {
        System.out.println("Запускаем сервер на порту " + PORT);
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер остановлен");
    }

    public static void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public static String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }


    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            gsonBuilder.registerTypeAdapter(Task.class, new TaskAdapter());
            Gson gson = gsonBuilder.create();
            final String path = exchange.getRequestURI().getPath();
            final String query = exchange.getRequestURI().getQuery();
            try {
                if (path.endsWith("tasks/task")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        if (query == null) {
                            final HashMap<Integer, Task> tasks = taskManager.getAllTasks();
                            final String response = gson.toJson(tasks);
                            sendText(exchange, response);
                            exchange.close();
                            return;
                        }
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        final Task task = taskManager.getTaskById(id);
                        final String response = gson.toJson(task);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("POST")) {
                        String body = readText(exchange);
                        if (body.isEmpty()) {
                            exchange.sendResponseHeaders(405, 0);
                            exchange.close();
                            return;
                        }
                        final Task task = gson.fromJson(body, Task.class);
                        if (task.getId() == null) {
                            taskManager.createTasks(task);
                        } else {
                            taskManager.refreshTasks(task);
                        }
                        final String response = gson.toJson(task);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("DELETE")) {
                        if (query == null) {
                            taskManager.clearTasks();
                            exchange.close();
                        }
                        String id = query.substring(3);
                        exchange.sendResponseHeaders(200, 0);
                        taskManager.removeTaskById(Integer.parseInt(id));
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(404, 0);
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

    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            final String path = exchange.getRequestURI().getPath();
            final String query = exchange.getRequestURI().getQuery();
            try {
                if (path.endsWith("tasks/epic")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        if (query == null) {
                            final HashMap<Integer, Epic> epics = taskManager.getAllEpics();
                            final String response = gson.toJson(epics);
                            sendText(exchange, response);
                            exchange.close();
                            return;
                        }
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        final Epic epic = taskManager.getEpicById(id);
                        final String response = gson.toJson(epic);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("POST")) {
                        String body = readText(exchange);
                        if (body.isEmpty()) {
                            exchange.sendResponseHeaders(405, 0);
                            exchange.close();
                            return;
                        }
                        final Epic epic = gson.fromJson(body, Epic.class);
                        if (epic.getId() == null) {
                            taskManager.createEpics(epic);
                        } else {
                            taskManager.refreshEpics(epic);
                        }
                        final String response = gson.toJson(epic);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("DELETE")) {
                        if (query == null) {
                            taskManager.clearEpics();
                            exchange.close();
                        }
                        String id = query.substring(3);
                        exchange.sendResponseHeaders(200, 0);
                        taskManager.removeEpicById(Integer.parseInt(id));
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(404, 0);
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

    class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
            gsonBuilder.registerTypeAdapter(SubTask.class, new SubTaskAdapter());
            Gson gson = gsonBuilder.create();
            final String path = exchange.getRequestURI().getPath();
            final String query = exchange.getRequestURI().getQuery();
            try {
                if (path.endsWith("tasks/subtask")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        if (query == null) {
                            final HashMap<Integer, SubTask> tasks = taskManager.getAllSubTasks();
                            final String response = gson.toJson(tasks);
                            sendText(exchange, response);
                            exchange.close();
                            return;
                        }
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        final SubTask subtask = taskManager.getSubTaskById(id);
                        final String response = gson.toJson(subtask);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("POST")) {
                        String body = readText(exchange);
                        if (body.isEmpty()) {
                            exchange.sendResponseHeaders(405, 0);
                            exchange.close();
                            return;
                        }
                        final SubTask subtask = gson.fromJson(body, SubTask.class);
                        if (subtask.getId() == null) {
                            taskManager.createSubTasks(subtask);
                        } else {
                            taskManager.refreshSubTasks(subtask);
                        }
                        final String response = gson.toJson(subtask);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("DELETE")) {
                        if (query == null) {
                            taskManager.clearSubTasks();
                            exchange.close();
                        }
                        String id = query.substring(3);
                        exchange.sendResponseHeaders(200, 0);
                        taskManager.removeSubTaskById(Integer.parseInt(id));
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(404, 0);
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
