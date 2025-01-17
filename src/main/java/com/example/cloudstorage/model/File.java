package com.example.cloudstorage.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_size", nullable = true)
    private Long fileSize;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "uploaded_at", nullable = true)
    private Date uploadedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public File(String fileName, long fileSize, String filePath, Date uploadedAt) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }
}