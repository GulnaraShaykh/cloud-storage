package com.example.cloudstorage.repository;

import com.example.cloudstorage.model.File;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    boolean existsByFileName(String fileName);

    void deleteByFileName(String fileName);

    Optional<File> findByFileName(String fileName);
}