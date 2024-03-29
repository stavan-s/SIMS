package com.stavan.sims;

import java.io.Serializable;


// Model class for Student
public class Student implements Serializable {

    private String id;
    private String fName;
    private String mName;
    private String lName;
    private String department;
    private String className;
    private String div;
    private String rollNo;
    private String ownNumber;
    private String parentNumber;
    private String initialPass;
    private String dateOfJoining;
    private String UID;

    public Student(String id, String fName, String mName, String lName, String department, String className, String div, String rollNo, String ownNumber, String parentNumber, String dateOfJoining) {
        this.id = id;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.department = department;
        this.className = className;
        this.div = div;
        this.rollNo = rollNo;
        this.ownNumber = ownNumber;
        this.parentNumber = parentNumber;
        this.dateOfJoining = dateOfJoining;
    }

    public Student() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getOwnNumber() {
        return ownNumber;
    }

    public void setOwnNumber(String ownNumber) {
        this.ownNumber = ownNumber;
    }

    public String getParentNumber() {
        return parentNumber;
    }

    public void setParentNumber(String parentNumber) {
        this.parentNumber = parentNumber;
    }

    public String getInitialPass() {
        return initialPass;
    }

    public void setInitialPass(String initialPass) {
        this.initialPass = initialPass;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
