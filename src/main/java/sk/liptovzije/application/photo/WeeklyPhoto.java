package sk.liptovzije.application.photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import sk.liptovzije.application.file.File;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weekly_photo")
public class WeeklyPhoto {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "thumbnail")
    private File photo;

    @Column(name = "date_added")
    private LocalDate dateAdded;
}
