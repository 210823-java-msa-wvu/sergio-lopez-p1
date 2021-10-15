package dev.lopez.utils.model;

public class Manager {
    private int id;
    private String username;
    private String password;
    private String first;
    private String last;
    private int report_to;
    private int rank;

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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", report_to=" + report_to +
                ", rank=" + rank +
                '}';
    }
}