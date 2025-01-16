package com.example.cloudstorage.model;
import jakarta.persistence.*;
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


    @Column(name = "file-size", nullable = true)
    private long fileSize;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "uploaded_at", nullable = true)
    private Date uploadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public File() {

    }

    public File(String filePath) {
        this.filePath = filePath;
    }

    public File(String fileName, long fileSize, String filePath, Date uploadedAt) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }
}