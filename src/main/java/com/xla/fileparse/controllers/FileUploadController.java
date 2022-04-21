package com.xla.fileparse.controllers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.xla.fileparse.model.FileList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    // Для присвоения идентификатора
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }

    // Загружаем необходимый нам файл и парсим его и сразу же выдаем результат после POST процесса
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody FileList fileList(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                InputStream inputStream1 = file.getInputStream();
                List<String> partitions = new BufferedReader(new InputStreamReader(inputStream1))
                        .lines().parallel()
                        .filter(s -> s.startsWith("#"))
                        .collect(Collectors.toList());

                InputStream inputStream2 = file.getInputStream();
                List<String> stringList = new BufferedReader(new InputStreamReader(inputStream2))
                        .lines().parallel()
                        .filter(s -> !s.contains("#"))
                        .collect(Collectors.toList());

                partitions.addAll(stringList);

                // также записываем распарсенный файл в корневой каталог проекта
                Files.write(Paths.get("demo.txt"), partitions, StandardOpenOption.CREATE);

                return new FileList(counter.incrementAndGet(), partitions);

            } catch (Exception e) {
                return new FileList("Вам не удалось загрузить " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            return new FileList("Вам не удалось загрузить " + file.getOriginalFilename() + " потому что файл пустой.");
        }
    }

}
