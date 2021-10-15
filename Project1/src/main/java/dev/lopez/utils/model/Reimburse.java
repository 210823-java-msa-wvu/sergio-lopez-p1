package dev.lopez.utils.model;

import java.time.LocalDate;

public class Reimburse {
    private int id;
    private int type;
    private int applicant;
    private float amount;
    private LocalDate start_date;
    private LocalDate submit_date;
    private int pass_grade;
    private int grade_scheme;
    private int level1;
    private int level2;
    private int level3;
    private int next_signer;
    private LocalDate sign_date;
    private int status;
    private String note;
    private String location;
    private boolean exceed = false;
    private int score = -1;
    private int next_info = -1;

    public int getNext_info() {
        return next_info;
    }

    public void setNext_info(int next_info) {
        this.next_info = next_info;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getExceed() {
        return exceed;
    }

    public void setExceed(boolean exceed) {
        this.exceed = exceed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getApplicant() {
        return applicant;
    }

    public void setApplicant(int applicant) {
        this.applicant = applicant;
    }

    public int getNext_signer() {
        return next_signer;
    }

    public void setNext_signer(int next_signer) {
        this.next_signer = next_signer;
    }

    public LocalDate getSign_date() {
        return sign_date;
    }

    public void setSign_date(LocalDate sign_date) {
        this.sign_date = sign_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getSubmit_date() {
        return submit_date;
    }

    public void setSubmit_date(LocalDate submit_date) {
        this.submit_date = submit_date;
    }

    public int getPass_grade() {
        return pass_grade;
    }

    public void setPass_grade(int pass_grade) {
        this.pass_grade = pass_grade;
    }

    public int getGrade_scheme() {
        return grade_scheme;
    }

    public void setGrade_scheme(int grade_scheme) {
        this.grade_scheme = grade_scheme;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel3() {
        return level3;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reimburse{" +
                "id=" + id +
                ", type=" + type +
                ", applicant=" + applicant +
                ", amount=" + amount +
                ", start_date=" + start_date +
                ", submit_date=" + submit_date +
                ", pass_grade=" + pass_grade +
                ", grade_scheme=" + grade_scheme +
                ", level1=" + level1 +
                ", level2=" + level2 +
                ", level3=" + level3 +
                ", next_signer=" + next_signer +
                ", sign_date=" + sign_date +
                ", status=" + status +
                ", note='" + note + '\'' +
                ", location='" + location + '\'' +
                ", exceed=" + exceed +
                ", score=" + score +
                ", next_info=" + next_info +
                '}';
    }
}
