package org.example.Repository;

import org.example.Entity.student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class studentrepository {
    private Map<Long,student> mp;

    public studentrepository() {
        mp=new HashMap<>();
    }
    public student saveStudent(student req){
        student res=mp.put(req.getId(),req);
        return res;
    }
    public student getbyid(Long id){
        student res=mp.get(id);
        if(res==null){
            return null;
        }
        return res;
    }
    public List<student> getall(){
        return new ArrayList<>(mp.values());
    }
}
