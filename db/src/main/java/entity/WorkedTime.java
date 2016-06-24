package entity;

import dao.Factory;

import javax.persistence.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "worked_time")
public class WorkedTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 1024)
    private String login;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private int time;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public WorkedTime() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Worked time {" +
                " id: " + id +
                ", login: " + login +
                ", date: " + new SimpleDateFormat("dd.MM.yyy").format(date) +
                " }";
    }
}
