package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.user;
import org.example.service.USerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private final USerService userService = new USerService();

    public USerService getUserService() {
        return userService;
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Map<String, String> params = extractParameters(httpServletRequest);
        Integer id = parseInteger(params.get("id"));
        String name = params.get("name");
        String email = params.get("email");
        String mobile = params.get("mobile");

        if (id == null || name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || mobile == null || mobile.trim().isEmpty()) {
            sendMessageResponse(httpServletResponse, HttpServletResponse.SC_BAD_REQUEST,
                    "User not added successfully: all fields (id, name, email, mobile) are required");
            return;
        }

        if (userService.exists(id)) {
            sendMessageResponse(httpServletResponse, HttpServletResponse.SC_CONFLICT,
                    "User with id " + id + " already exists");
            return;
        }

        user userToCreate = new user(id, name.trim(), email.trim(), mobile.trim());
        user created = userService.create(userToCreate);

        sendJsonResponse(httpServletResponse, HttpServletResponse.SC_CREATED, created.toJson());
    }

    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String idParam = httpServletRequest.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            // Get all users
            List<user> users = userService.getalluser();
            sendJsonResponse(httpServletResponse, HttpServletResponse.SC_OK, formatUserListJson(users));
        } else {
            // Get user by id
            Integer id = parseInteger(idParam.trim());
            if (id == null) {
                sendMessageResponse(httpServletResponse, HttpServletResponse.SC_BAD_REQUEST, "Invalid id parameter");
                return;
            }

            user found = userService.getuserbyid(id);
            if (found == null) {
                sendMessageResponse(httpServletResponse, HttpServletResponse.SC_NOT_FOUND, "User not found with id: " + id);
                return;
            }

            sendJsonResponse(httpServletResponse, HttpServletResponse.SC_OK, found.toJson());
        }
    }

    @Override
    public void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Map<String, String> params = extractParameters(httpServletRequest);
        Integer id = parseInteger(params.get("id"));

        if (id == null) {
            sendMessageResponse(httpServletResponse, HttpServletResponse.SC_BAD_REQUEST,
                    "Valid id is required for update");
            return;
        }

        user existing = userService.getuserbyid(id);
        if (existing == null) {
            sendMessageResponse(httpServletResponse, HttpServletResponse.SC_NOT_FOUND,
                    "User not found with id: " + id);
            return;
        }

        String name = params.get("name");
        String email = params.get("email");
        String mobile = params.get("mobile");

        user updateReq = new user(id, name, email, mobile);
        user updated = userService.update(id, updateReq);

        sendJsonResponse(httpServletResponse, HttpServletResponse.SC_OK, updated.toJson());
    }

    @Override
    public void doDelete(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Map<String, String> params = extractParameters(httpServletRequest);
        Integer id = parseInteger(params.get("id"));

        if (id == null) {
            sendMessageResponse(httpServletResponse, HttpServletResponse.SC_BAD_REQUEST,
                    "Valid id parameter is required for delete");
            return;
        }

        boolean deleted = userService.delete(id);
        if (!deleted) {
            sendMessageResponse(httpServletResponse, HttpServletResponse.SC_NOT_FOUND,
                    "User not found with id: " + id);
            return;
        }

        sendMessageResponse(httpServletResponse, HttpServletResponse.SC_OK,
                "User deleted successfully with id: " + id);
    }

    private void sendJsonResponse(HttpServletResponse response, int statusCode, String json) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void sendMessageResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        String json = "{\n  \"message\": \"" + escapeJson(message) + "\"\n}";
        sendJsonResponse(response, statusCode, json);
    }

    private Map<String, String> extractParameters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        // 1. Read request parameters (query string or application/x-www-form-urlencoded)
        String[] keys = {"id", "name", "email", "mobile"};
        for (String key : keys) {
            String value = request.getParameter(key);
            if (value != null && !value.trim().isEmpty()) {
                params.put(key, value.trim());
            }
        }

        // 2. Read request body if parameters are not completely present and body is JSON
        try {
            StringBuilder bodyBuilder = new StringBuilder();
            BufferedReader reader = request.getReader();
            if (reader != null) {
                String line;
                while ((line = reader.readLine()) != null) {
                    bodyBuilder.append(line);
                }
            }
            String body = bodyBuilder.toString().trim();
            if (!body.isEmpty()) {
                parseSimpleJson(body, params);
            }
        } catch (Exception ignored) {
        }

        return params;
    }

    private static void parseSimpleJson(String json, Map<String, String> targetMap) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(?:\"([^\"]*)\"|([0-9]+|true|false|null))");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            String key = matcher.group(1);
            String strVal = matcher.group(2);
            String numVal = matcher.group(3);
            String val = strVal != null ? strVal : numVal;
            if (val != null && !targetMap.containsKey(key)) {
                targetMap.put(key, val.trim());
            }
        }
    }

    private static String formatUserListJson(List<user> users) {
        if (users == null || users.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < users.size(); i++) {
            sb.append("  ").append(users.get(i).toJson().replace("\n", "\n  "));
            if (i < users.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

    private static Integer parseInteger(String str) {
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
