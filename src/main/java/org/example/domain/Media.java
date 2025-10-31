package org.example.domain;

import java.io.Serializable;

public abstract class Media implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String title;
    protected boolean available = true;

    public Media(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract int getLoanDays();
    public abstract double calculateFine(long overdueDays);

    @Override
    public String toString() {
        return title + (available ? " (Available)" : " (Borrowed)");
    }
}