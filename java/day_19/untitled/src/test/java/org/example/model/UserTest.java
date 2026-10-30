package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        user u = new user();
        u.setId(10);
        u.setName("Alice");
        u.setEmail("alice@test.com");
        u.setMobile("1122334455");

        assertEquals(10, u.getId());
        assertEquals("Alice", u.getName());
        assertEquals("alice@test.com", u.getEmail());
        assertEquals("1122334455", u.getMobile());
    }

    @Test
    public void testParameterizedConstructor() {
        user u = new user(1, "Bob", "bob@test.com", "9988776655");
        assertEquals(1, u.getId());
        assertEquals("Bob", u.getName());
        assertEquals("bob@test.com", u.getEmail());
        assertEquals("9988776655", u.getMobile());
    }

    @Test
    public void testToJson() {
        user u = new user(1, "Charlie", "charlie@test.com", "5555555555");
        String json = u.toJson();
        assertTrue(json.contains("\"id\": 1"));
        assertTrue(json.contains("\"name\": \"Charlie\""));
        assertTrue(json.contains("\"email\": \"charlie@test.com\""));
        assertTrue(json.contains("\"mobile\": \"5555555555\""));
    }

    @Test
    public void testEqualsAndHashCode() {
        user u1 = new user(1, "User One", "one@test.com", "111");
        user u2 = new user(1, "User One Updated", "one_updated@test.com", "111");
        user u3 = new user(2, "User Two", "two@test.com", "222");

        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    public void testToString() {
        user u = new user(1, "Dave", "dave@test.com", "12345");
        String str = u.toString();
        assertTrue(str.contains("Dave"));
        assertTrue(str.contains("dave@test.com"));
    }
}

