package org.example.service;

import org.example.model.user;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class USerServiceTest {

    private USerService service;

    @BeforeEach
    public void setUp() {
        service = new USerService();
    }

    @Test
    public void testCreateUser() {
        user u = new user(1, "John", "john@example.com", "1234567890");
        user created = service.create(u);

        assertNotNull(created);
        assertEquals(1, created.getId());
        assertEquals("John", created.getName());
        assertEquals(1, service.count());
        assertTrue(service.exists(1));
    }

    @Test
    public void testCreateUserNull() {
        assertNull(service.create(null));
        user noIdUser = new user(null, "No Id", "noid@example.com", "000");
        assertNull(service.create(noIdUser));
        assertEquals(0, service.count());
    }

    @Test
    public void testGetAllUsers() {
        assertTrue(service.getalluser().isEmpty());

        user u1 = new user(1, "Alice", "alice@example.com", "111");
        user u2 = new user(2, "Bob", "bob@example.com", "222");

        service.create(u1);
        service.create(u2);

        List<user> all = service.getalluser();
        assertEquals(2, all.size());
        assertTrue(all.contains(u1));
        assertTrue(all.contains(u2));
    }

    @Test
    public void testGetUserById() {
        user u = new user(5, "Charlie", "charlie@example.com", "555");
        service.create(u);

        user found = service.getuserbyid(5);
        assertNotNull(found);
        assertEquals("Charlie", found.getName());

        assertNull(service.getuserbyid(999));
        assertNull(service.getuserbyid(null));
    }

    @Test
    public void testUpdateUser() {
        user u = new user(1, "Initial Name", "init@example.com", "111");
        service.create(u);

        user updateReq = new user(1, "Updated Name", "updated@example.com", "222");
        user updated = service.update(1, updateReq);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("222", updated.getMobile());

        // Verify state in DB
        user inDb = service.getuserbyid(1);
        assertEquals("Updated Name", inDb.getName());
    }

    @Test
    public void testPartialUpdate() {
        user u = new user(1, "Keep Name", "old@example.com", "999");
        service.create(u);

        // Update only email
        user updateReq = new user(1, null, "new@example.com", null);
        user updated = service.update(1, updateReq);

        assertNotNull(updated);
        assertEquals("Keep Name", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
        assertEquals("999", updated.getMobile());
    }

    @Test
    public void testUpdateNonExistentUser() {
        user updateReq = new user(99, "Ghost", "ghost@example.com", "000");
        assertNull(service.update(99, updateReq));
        assertNull(service.update(null, updateReq));
        assertNull(service.update(1, null));
    }

    @Test
    public void testDeleteUser() {
        user u = new user(1, "To Delete", "delete@example.com", "000");
        service.create(u);

        assertTrue(service.exists(1));
        boolean deleted = service.delete(1);
        assertTrue(deleted);
        assertFalse(service.exists(1));
        assertNull(service.getuserbyid(1));
        assertEquals(0, service.count());

        // Deleting non-existent returns false
        assertFalse(service.delete(1));
        assertFalse(service.delete(null));
    }

    @Test
    public void testDeleteUserByIdReturningUser() {
        user u = new user(10, "David", "david@example.com", "12345");
        service.create(u);

        user removed = service.deleteuserbyid(10);
        assertNotNull(removed);
        assertEquals(10, removed.getId());
        assertNull(service.deleteuserbyid(10));
        assertNull(service.deleteuserbyid(null));
    }

    @Test
    public void testClear() {
        service.create(new user(1, "A", "a@a.com", "1"));
        service.create(new user(2, "B", "b@b.com", "2"));
        assertEquals(2, service.count());

        service.clear();
        assertEquals(0, service.count());
        assertTrue(service.getalluser().isEmpty());
    }
}

