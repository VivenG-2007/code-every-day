package day_6;

import java.time.*;
import java.util.*;


class task implements Comparable<task>{
    int id;
    String title;
    int priority;
    LocalDate dueDate;
    Set<String> tags;
    boolean completed;

    public task(int id, String title, int priority, LocalDate dueDate, Set<String> tags, boolean completed) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
        this.tags = tags;
        this.completed = completed;
    }

    @Override
    public int compareTo(task o) {
        if(this.priority!=o.priority) {
            return Integer.compare(this.priority, o.priority);
        }
        if (!this.dueDate.equals(o.dueDate)) {
            return this.dueDate.compareTo(o.dueDate);
        }
        return this.title.compareTo(o.title);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj){
            return true;
        }
        if(obj==null){
            return false;
        }
        task t=(task)obj;
        if(this.id==t.id){
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
}

class TaskNotFoundException extends Exception{
    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
class InvalidTaskException extends Exception{
    public InvalidTaskException(String message) {
        super(message);
    }
    public InvalidTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
class DuplicateTaskException extends Exception{
    public DuplicateTaskException(String message) {
        super(message);
    }
    public DuplicateTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
class TaskAlreadyCompletedException extends Exception{
    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
    public TaskAlreadyCompletedException(String message, Throwable cause) {
        super(message, cause);
    }
}

class scheduler{
    Map<Integer,task> tasks=new HashMap<>();
    PriorityQueue<task> pending=new PriorityQueue<>();
    TreeMap<LocalDate,List<task>> byduedate=new TreeMap<>();
    Map<String,Set<Integer>> tagindex=new HashMap<>();
    List<task> history=new ArrayList<>();

    void addtask(String title, int priority, LocalDate dueDate, Set<String> tags) throws InvalidTaskException, DuplicateTaskException {
        if(title==null||title.isBlank()){
            throw new InvalidTaskException("Title is blank, Please fill it also");
        }
        if(priority<1||priority>5){
            throw new InvalidTaskException("Priority is not in range");
        }
        if(dueDate.isBefore(LocalDate.now())){
            throw new InvalidTaskException("Invalid input time");
        }
        for(task t: tasks.values()){
            if (t.title.equalsIgnoreCase(title) &&
                    t.dueDate.equals(dueDate)) {
                throw new DuplicateTaskException("Task already exists");
                }
        }
        int id = tasks.size() + 1;
        task t=new task(id, title, priority, dueDate, tags, false);
        tasks.put(id,t);
        pending.offer(t);
        for (String tag : tags) {
            tagindex.computeIfAbsent(tag, k -> new HashSet<>()).add(id);
        }
        byduedate.computeIfAbsent(dueDate, d -> new ArrayList<>()).add(t);
    }
}