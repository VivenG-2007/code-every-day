package org.example.model;

import java.util.Objects;

public class user {
    private Integer id;
    private String name;
    private String email;
    private String mobile;

    public user() {
    }

    public user(Integer id, String name, String email, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String toJson() {
        return "{\n" +
                "  \"id\": " + (id != null ? id : "null") + ",\n" +
                "  \"name\": " + (name != null ? "\"" + escapeJson(name) + "\"" : "null") + ",\n" +
                "  \"email\": " + (email != null ? "\"" + escapeJson(email) + "\"" : "null") + ",\n" +
                "  \"mobile\": " + (mobile != null ? "\"" + escapeJson(mobile) + "\"" : "null") + "\n" +
                "}";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        user user = (user) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
