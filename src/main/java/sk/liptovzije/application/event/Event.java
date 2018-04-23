package sk.liptovzije.application.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import sk.liptovzije.utils.LocalDatePersistenceConverter;
import sk.liptovzije.utils.LocalTimePersistenceConverter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "owner")
    private Long ownerId;

    @Column(name = "heading")
    private String heading;

    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @Convert(converter = LocalTimePersistenceConverter.class)
    @Column(name = "end_time")
    private LocalTime startTime;

    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Convert(converter = LocalTimePersistenceConverter.class)
    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "content")
    private String content;

    @Column(name = "approved")
    private Boolean approved;
}
