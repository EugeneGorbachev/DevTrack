package entity;

import dao.Factory;

import javax.persistence.*;
import java.sql.SQLException;

@Entity
@Table(name = "program")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1024)
    private String whitelist;

    @Column(nullable = false, length = 1024)
    private String blacklist;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public boolean validate(String string) {
        if (!blacklist.isEmpty() && string.matches(blacklist)) {
            return false;
        } else {
            return !whitelist.isEmpty() && string.matches(whitelist);
        }
    }

    public Program() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }

    public String getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String blacklist) {
        this.blacklist = blacklist;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Program {" +
                " id: " + id +
                ", name: " + name +
                ", whitelist: " + whitelist +
                ", blacklist: " + blacklist +
                " }";
    }
}
