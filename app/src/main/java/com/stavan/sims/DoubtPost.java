package com.stavan.sims;

public class DoubtPost {

    private String name;
    private String cleared;

    public DoubtPost(String name, String cleared) {
        this.name = name;
        this.cleared = cleared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCleared() {
        return cleared;
    }

    public void setCleared(String cleared) {
        this.cleared = cleared;
    }
}
