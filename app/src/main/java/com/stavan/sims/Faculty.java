package com.stavan.sims;

public class Faculty {

    private String fName;
    private String mName;
    private String lName;
    private String email;
    private String number;
    private String initialPass;


    public Faculty(String fName, String mName, String lName, String email, String number) {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.email = email;
        this.number = number;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInitialPass() {
        return initialPass;
    }

    public void setInitialPass(String initialPass) {
        this.initialPass = initialPass;
    }

}
