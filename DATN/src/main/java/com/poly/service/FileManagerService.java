package com.poly.service;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileManagerService {
    
    private final String rootFolder = "uploads";

    private Path getPath(String folder, String filename) {
        File dir = Paths.get(rootFolder, folder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return Paths.get(dir.getAbsolutePath(), filename);
    }

    public byte[] read(String folder, String filename) {
        Path path = getPath(folder, filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> save(String folder, MultipartFile[] files) {
        List<String> filenames = new ArrayList<>();
        File dir = Paths.get(rootFolder, folder).toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile file : files) {
            String name = System.currentTimeMillis() + file.getOriginalFilename();
            String filename = Integer.toHexString(name.hashCode()) + name.substring(name.lastIndexOf("."));
            Path path = getPath(folder, filename);

            try {
                file.transferTo(path.toFile());
                filenames.add(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filenames;
    }

    public void delete(String folder, String filename) {
        Path path = getPath(folder, filename);
        File file = path.toFile();
        if (file.exists()) {
            file.delete();
        }
    }

    public List<String> list(String folder) {
        File dir = Paths.get(rootFolder, folder).toFile();
        List<String> filenames = new ArrayList<>();

        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                filenames.add(file.getName());
            }
        }
        return filenames;
    }
}