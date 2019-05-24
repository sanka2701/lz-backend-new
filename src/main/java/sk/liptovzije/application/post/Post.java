package sk.liptovzije.application.post;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import sk.liptovzije.application.file.File;
import sk.liptovzije.utils.LocalDatePersistenceConverter;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "post_id")
    protected Long id;

    @Column(name = "owner_id")
    protected Long ownerId;

    @Column(name = "content")
    protected String content;

    @Column(name = "heading")
    protected String title;

    @Column(name = "approved")
    private Boolean approved;

    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "date_added")
    protected LocalDate dateAdded;

    @OneToOne
    @JoinColumn(name = "thumbnail")
    private File thumbnail;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name = "content_file",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private Set<File> files = Collections.emptySet();

    public Post () {
        this.dateAdded = LocalDate.now();
    }

    public Set<File> getFiles() {
        return new HashSet<>(this.files);
    }
}



