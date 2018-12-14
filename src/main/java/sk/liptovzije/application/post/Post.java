package sk.liptovzije.application.post;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import sk.liptovzije.utils.LocalDatePersistenceConverter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
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

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "heading")
    protected String title;

    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "date_added")
    protected LocalDate dateAdded;

    public Post () {
        this.dateAdded = LocalDate.now();
    }
}
