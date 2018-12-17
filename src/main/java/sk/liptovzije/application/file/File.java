package sk.liptovzije.application.file;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class File {
    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @Column(name = "replace_key")
    private Long replaceKey;

    @Column(name = "path")
    private String path;
}
