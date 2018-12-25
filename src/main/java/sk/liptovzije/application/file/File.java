package sk.liptovzije.application.file;

import lombok.*;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "extension")
    private String extension;

    @Column(name = "directory")
    private String directory;

    public Path getPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append('.');
        sb.append(this.extension);

        return Paths.get(this.directory, sb.toString());
    }
}
