package model;

import java.io.Serializable;

public class Shift implements Serializable {
    private int id;
    private String employeeName;
    private String shiftTime;
    private String role;

    public Shift(int id, String employeeName, String shiftTime, String role) {
        this.id = id;
        this.employeeName = employeeName;
        this.shiftTime = shiftTime;
        this.role = role;
    }

    public Shift(String employeeName, String shiftTime, String role) {
        this.employeeName = employeeName;
        this.shiftTime = shiftTime;
        this.role = role;
    }

    public int getId() { return id; }
    public String getEmployeeName() { return employeeName; }
    public String getShiftTime() { return shiftTime; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return employeeName + " - " + shiftTime + " - " + role;
    }
}
