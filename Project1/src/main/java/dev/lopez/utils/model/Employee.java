package dev.lopez.utils.model;

public class Employee {
    private int id; //id serial primary key
    private String username;
    private String password;
    private String first;
    private String last;
    private int report_to;
    private float pending;
    private float awarded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public int getReport_to() {
        return report_to;
    }

    public void setReport_to(int report_to) {
        this.report_to = report_to;
    }

    public float getPending() {
        return pending;
    }

    public void setPending(float pending) {
        this.pending = pending;
    }

    public float getAwarded() {
        return awarded;
    }

    public void setAwarded(float awarded) {
        this.awarded = awarded;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", report_to=" + report_to +
                ", pending=" + pending +
                ", awarded=" + awarded +
                '}';
    }
}
