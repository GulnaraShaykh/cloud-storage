package com.example.cloudstorage.service;

import com.example.cloudstorage.model.File;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.FileRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void uploadFile(MultipartFile file, String fileName) throws IOException {
        File newFile = new File();
        newFile.setFileName(fileName); // Название файла
        newFile.setFileData(file.getSize()); // Бинарные данные
        newFile.setUploadedAt(new Date()); // Дата загрузки
        fileRepository.save(newFile);

    }

    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public boolean deleteFile(String fileName) {
        if (fileRepository.existsByFileName(fileName)) {
            fileRepository.deleteByFileName(fileName);
            return true;
        }
        return false;
    }

//    public byte[] downloadFile(String fileName) {
//        Optional<File> file = fileRepository.findByFileName(fileName);
//        return file.map(File::getFileData).orElse(null);
//    }

    public boolean renameFile(String filename, String newName) {
        fileRepository.findByFileName(filename)
                .map(file -> {
                    file.setFileName(newName);
                    return fileRepository.save(file);
                });
        return true;
    }
}