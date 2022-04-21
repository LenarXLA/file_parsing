package com.xla.fileparse.model;

import java.util.List;

public class FileList {
    private long id = 0;
    private List<String> content = null;
    private String err = null;

    // конструктор для вывода сообщений ошибок при загрузке файла
    public FileList(String err) {
        this.err = err;
    }

    // основной конструктор
    public FileList(long id, List<String> content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public List<String> getContent() {
        return content;
    }
}
