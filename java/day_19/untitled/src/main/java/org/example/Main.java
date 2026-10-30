package org.example;

import org.example.model.user;
import org.example.service.USerService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== User CRUD Service Demonstration ===");
        USerService userService = new USerService();

        // 1. Create Users
        System.out.println("\n1. Creating Users:");
        user user1 = new user(1, "Rohit Sharma", "rohit@example.com", "9876543210");
        user user2 = new user(2, "Virat Kohli", "virat@example.com", "9876543211");
        userService.create(user1);
        userService.create(user2);
        System.out.println("Created: " + user1.toJson());
        System.out.println("Created: " + user2.toJson());

        // 2. Read All Users
        System.out.println("\n2. Getting All Users:");
        List<user> allUsers = userService.getalluser();
        for (user u : allUsers) {
            System.out.println(u);
        }

        // 3. Read User By ID
        System.out.println("\n3. Getting User By ID (id = 1):");
        user fetched = userService.getuserbyid(1);
        System.out.println("Found: " + (fetched != null ? fetched.toJson() : "null"));

        // 4. Update User
        System.out.println("\n4. Updating User (id = 1):");
        user updateReq = new user(1, "Rohit G. Sharma", "rohit.sharma@example.com", "9999999999");
        user updated = userService.update(1, updateReq);
        System.out.println("Updated: " + (updated != null ? updated.toJson() : "null"));

        // 5. Delete User
        System.out.println("\n5. Deleting User (id = 2):");
        boolean deleted = userService.delete(2);
        System.out.println("Deleted id 2? " + deleted);

        // 6. Verify Final State
        System.out.println("\n6. Final Users in DB:");
        for (user u : userService.getalluser()) {
            System.out.println(u);
        }

        System.out.println("\n=== All Operations Completed Successfully ===");
    }
}
