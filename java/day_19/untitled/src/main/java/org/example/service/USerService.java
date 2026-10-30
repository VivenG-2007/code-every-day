package org.example.service;

import org.example.model.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class USerService {
    private final Map<Integer, user> userdb;

    public USerService() {
        this.userdb = new ConcurrentHashMap<>();
    }

    public user create(user req) {
        if (req == null || req.getId() == null) {
            return null;
        }
        userdb.put(req.getId(), req);
        return req;
    }

    public List<user> getalluser() {
        return new ArrayList<>(userdb.values());
    }

    public user getuserbyid(Integer id) {
        if (id == null) {
            return null;
        }
        return userdb.get(id);
    }

    public user update(Integer id, user req) {
        if (id == null || req == null || !userdb.containsKey(id)) {
            return null;
        }
        user existing = userdb.get(id);
        if (req.getName() != null && !req.getName().trim().isEmpty()) {
            existing.setName(req.getName());
        }
        if (req.getEmail() != null && !req.getEmail().trim().isEmpty()) {
            existing.setEmail(req.getEmail());
        }
        if (req.getMobile() != null && !req.getMobile().trim().isEmpty()) {
            existing.setMobile(req.getMobile());
        }
        userdb.put(id, existing);
        return existing;
    }

    public user update(user req) {
        if (req == null || req.getId() == null) {
            return null;
        }
        return update(req.getId(), req);
    }

    public boolean delete(Integer id) {
        if (id == null) {
            return false;
        }
        return userdb.remove(id) != null;
    }

    public user deleteuserbyid(Integer id) {
        if (id == null) {
            return null;
        }
        return userdb.remove(id);
    }

    public boolean exists(Integer id) {
        return id != null && userdb.containsKey(id);
    }

    public int count() {
        return userdb.size();
    }

    public void clear() {
        userdb.clear();
    }
}
