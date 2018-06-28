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
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Long id;

    @Column(name = "owner")
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
