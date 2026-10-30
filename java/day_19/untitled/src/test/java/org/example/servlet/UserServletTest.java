package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.user;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserServletTest {

    private UserServlet servlet;

    @BeforeEach
    public void setUp() {
        servlet = new UserServlet();
    }

    private static class ResponseStub {
        int status = 200;
        String contentType;
        String characterEncoding;
        final StringWriter writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);

        HttpServletResponse createProxy() {
            return (HttpServletResponse) Proxy.newProxyInstance(
                    HttpServletResponse.class.getClassLoader(),
                    new Class<?>[]{HttpServletResponse.class},
                    (proxy, method, args) -> {
                        String name = method.getName();
                        if (name.equals("setStatus")) {
                            status = (Integer) args[0];
                            return null;
                        } else if (name.equals("getStatus")) {
                            return status;
                        } else if (name.equals("setContentType")) {
                            contentType = (String) args[0];
                            return null;
                        } else if (name.equals("setCharacterEncoding")) {
                            characterEncoding = (String) args[0];
                            return null;
                        } else if (name.equals("getWriter")) {
                            return printWriter;
                        }
                        return null;
                    }
            );
        }

        String getOutput() {
            printWriter.flush();
            return writer.toString();
        }
    }

    private HttpServletRequest createRequestProxy(Map<String, String> params, String body) {
        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if (name.equals("getParameter")) {
                        return params.get(args[0]);
                    } else if (name.equals("getContentType")) {
                        return body != null ? "application/json" : "application/x-www-form-urlencoded";
                    } else if (name.equals("getReader")) {
                        return new BufferedReader(new StringReader(body != null ? body : ""));
                    }
                    return null;
                }
        );
    }

    @Test
    public void testDoPostSuccess() throws ServletException, IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", "Alice");
        params.put("email", "alice@example.com");
        params.put("mobile", "9998887776");

        ResponseStub respStub = new ResponseStub();
        servlet.doPost(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(201, respStub.status);
        assertTrue(respStub.getOutput().contains("\"name\": \"Alice\""));
        assertNotNull(servlet.getUserService().getuserbyid(1));
    }

    @Test
    public void testDoPostMissingFields() throws ServletException, IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        // Missing name, email, mobile

        ResponseStub respStub = new ResponseStub();
        servlet.doPost(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(400, respStub.status);
        assertTrue(respStub.getOutput().contains("all fields (id, name, email, mobile) are required"));
    }

    @Test
    public void testDoPostDuplicateId() throws ServletException, IOException {
        servlet.getUserService().create(new user(1, "Existing", "exist@test.com", "111"));

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", "New Alice");
        params.put("email", "alice@test.com");
        params.put("mobile", "999");

        ResponseStub respStub = new ResponseStub();
        servlet.doPost(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(409, respStub.status);
        assertTrue(respStub.getOutput().contains("already exists"));
    }

    @Test
    public void testDoGetAll() throws ServletException, IOException {
        servlet.getUserService().create(new user(1, "Alice", "alice@test.com", "111"));
        servlet.getUserService().create(new user(2, "Bob", "bob@test.com", "222"));

        ResponseStub respStub = new ResponseStub();
        servlet.doGet(createRequestProxy(new HashMap<>(), null), respStub.createProxy());

        assertEquals(200, respStub.status);
        String output = respStub.getOutput();
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
    }

    @Test
    public void testDoGetByIdFound() throws ServletException, IOException {
        servlet.getUserService().create(new user(5, "Charlie", "charlie@test.com", "555"));

        Map<String, String> params = new HashMap<>();
        params.put("id", "5");

        ResponseStub respStub = new ResponseStub();
        servlet.doGet(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(200, respStub.status);
        assertTrue(respStub.getOutput().contains("Charlie"));
    }

    @Test
    public void testDoGetByIdNotFound() throws ServletException, IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", "99");

        ResponseStub respStub = new ResponseStub();
        servlet.doGet(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(404, respStub.status);
        assertTrue(respStub.getOutput().contains("User not found"));
    }

    @Test
    public void testDoPutSuccess() throws ServletException, IOException {
        servlet.getUserService().create(new user(1, "Old Name", "old@test.com", "111"));

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("name", "Updated Name");
        params.put("email", "new@test.com");
        params.put("mobile", "999");

        ResponseStub respStub = new ResponseStub();
        servlet.doPut(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(200, respStub.status);
        assertTrue(respStub.getOutput().contains("Updated Name"));
        assertEquals("Updated Name", servlet.getUserService().getuserbyid(1).getName());
    }

    @Test
    public void testDoPutNotFound() throws ServletException, IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", "404");

        ResponseStub respStub = new ResponseStub();
        servlet.doPut(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(404, respStub.status);
        assertTrue(respStub.getOutput().contains("User not found"));
    }

    @Test
    public void testDoDeleteSuccess() throws ServletException, IOException {
        servlet.getUserService().create(new user(1, "To Delete", "delete@test.com", "111"));

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");

        ResponseStub respStub = new ResponseStub();
        servlet.doDelete(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(200, respStub.status);
        assertTrue(respStub.getOutput().contains("User deleted successfully"));
        assertNull(servlet.getUserService().getuserbyid(1));
    }

    @Test
    public void testDoDeleteNotFound() throws ServletException, IOException {
        Map<String, String> params = new HashMap<>();
        params.put("id", "999");

        ResponseStub respStub = new ResponseStub();
        servlet.doDelete(createRequestProxy(params, null), respStub.createProxy());

        assertEquals(404, respStub.status);
        assertTrue(respStub.getOutput().contains("User not found"));
    }
}

