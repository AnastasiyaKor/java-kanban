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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;
    private final HttpServer httpServer;
    private final Gson gson1 = new Gson();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Task.class, new TaskAdapter())
            .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
            .create();

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TaskHandler());
        httpServer.createContext("/tasks/subtask", new SubtaskHandler());
        httpServer.createContext("/tasks/epic", new EpicHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.createContext("/tasks/", new PriorityHandler());
        httpServer.createContext("/tasks/subtask/epic/id=", new SubtaskByEpicHandler());
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

    private static void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private static String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    private class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
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
                            exchange.sendResponseHeaders(400, 0);
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
                            final String response = "Задачи успешно удалены";
                            sendText(exchange, response);
                            exchange.close();
                        }
                        String id = query.substring(3);
                        exchange.sendResponseHeaders(200, 0);
                        taskManager.removeTaskById(Integer.parseInt(id));
                        final String response = "Задача успешно удалена";
                        sendText(exchange, response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException | NumberFormatException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            final String path = exchange.getRequestURI().getPath();
            final String query = exchange.getRequestURI().getQuery();
            try {
                if (path.endsWith("tasks/epic")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        if (query == null) {
                            final HashMap<Integer, Epic> epics = taskManager.getAllEpics();
                            final String response = gson1.toJson(epics);
                            sendText(exchange, response);
                            exchange.close();
                            return;
                        }
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        final Epic epic = taskManager.getEpicById(id);
                        final String response = gson1.toJson(epic);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("POST")) {
                        String body = readText(exchange);
                        if (body.isEmpty()) {
                            exchange.sendResponseHeaders(400, 0);
                            exchange.close();
                            return;
                        }
                        final Epic epic = gson1.fromJson(body, Epic.class);
                        if (epic.getId() == null) {
                            taskManager.createEpics(epic);
                        } else {
                            taskManager.refreshEpics(epic);
                        }
                        final String response = gson1.toJson(epic);
                        sendText(exchange, response);
                        exchange.close();
                    } else if (exchange.getRequestMethod().equals("DELETE")) {
                        if (query == null) {
                            taskManager.clearEpics();
                            final String response = "Задачи успешно удалены";
                            sendText(exchange, response);
                            exchange.close();
                        }
                        String id = query.substring(3);
                        exchange.sendResponseHeaders(200, 0);
                        taskManager.removeEpicById(Integer.parseInt(id));
                        final String response = "Задача успешно удалена";
                        sendText(exchange, response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException | NumberFormatException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private class SubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
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
                            exchange.sendResponseHeaders(400, 0);
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
                            final String response = "Задачи успешно удалены";
                            sendText(exchange, response);
                            exchange.close();
                        }
                        String id = query.substring(3);
                        exchange.sendResponseHeaders(200, 0);
                        taskManager.removeSubTaskById(Integer.parseInt(id));
                        final String response = "Задача успешно удалена";
                        sendText(exchange, response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException | NumberFormatException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private class PriorityHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            final String path = exchange.getRequestURI().getPath();
            try {
                if (path.endsWith("tasks/")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        final Map<LocalDateTime, Task> tasks = taskManager.getPrioritizedTasks();
                        final String response = gson.toJson(tasks);
                        sendText(exchange, response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException | NumberFormatException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            final String path = exchange.getRequestURI().getPath();
            try {
                if (path.endsWith("tasks/history")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        final List<Task> tasks = taskManager.getHistory();
                        final String response = gson.toJson(tasks);
                        sendText(exchange, response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException | NumberFormatException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private class SubtaskByEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            final String path = exchange.getRequestURI().getPath();
            final String query = exchange.getRequestURI().getQuery();
            try {
                if (path.endsWith("tasks/subtask/epic/id=")) {
                    if (exchange.getRequestMethod().equals("GET")) {
                        String idParam = query.substring(3);
                        final int id = Integer.parseInt(idParam);
                        final ArrayList<Integer> subtask = taskManager.getSubtasksByEpicId(id);
                        final String response = gson.toJson(subtask);
                        sendText(exchange, response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException | NumberFormatException | JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }
}
