package com.example.cloudstorage.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.Date;

@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_data", nullable = true)
    private long fileData;

    @Column(name = "uploaded_at", nullable = true)
    private Date uploadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;


    public File( String fileName, long fileData, Date uploadedAt) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.uploadedAt = uploadedAt;
    }

    public File() {

    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileData() {
        return fileData;
    }

    public void setFileData(long fileData) {
        this.fileData = fileData;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}