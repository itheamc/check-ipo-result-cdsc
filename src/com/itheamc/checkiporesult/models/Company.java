package com.itheamc.checkiporesult.models;

public class Company {
    private int id;
    private String name;
    private String scrip;
    private boolean isFileUploaded;

    // Constructor
    public Company() {
    }

    // Constructor
    public Company(int id, String name, String scrip, boolean isFileUploaded) {
        this.id = id;
        this.name = name;
        this.scrip = scrip;
        this.isFileUploaded = isFileUploaded;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScrip() {
        return scrip;
    }

    public void setScrip(String scrip) {
        this.scrip = scrip;
    }

    public boolean isFileUploaded() {
        return isFileUploaded;
    }

    public void setFileUploaded(boolean fileUploaded) {
        isFileUploaded = fileUploaded;
    }

    // Overriding toString() method
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", scrip='" + scrip + '\'' +
                ", isFileUploaded=" + isFileUploaded +
                '}';
    }
}
