package sk.liptovzije.application.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import sk.liptovzije.utils.LocalDatePersistenceConverter;
import sk.liptovzije.utils.LocalTimePersistenceConverter;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "owner")
    private Long ownerId;

    @Column(name = "place")
    private Long placeId;

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

    private Event(){}

    public static class Builder {

        private Long id;
        private Long placeId;
        private Long ownerId;
        private String heading;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private String thumbnail;
        private String content;

        // todo: remove, just for offline testing purposes
        private static AtomicInteger idGenerator=new AtomicInteger();

        public Builder(long ownerId, String heading, String content) {
            this.ownerId = ownerId;
            this.heading = heading;
            this.content = content;
        }

        public Builder startDate(LocalDate date){
            this.startDate = date;
            return this;
        }

        public Builder endDate(LocalDate date){
            this.endDate = date;
            return this;
        }

        public Builder startTime(LocalTime time){
            this.startTime = time;
            return this;
        }

        public Builder endTime(LocalTime time){
            this.endTime = time;
            return this;
        }

        public Builder thumbnail(String thumbnail){
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder placeId(long placeId){
            this.placeId = placeId;
            return this;
        }

        public Event build() {
            Event event = new Event();

            event.setId((long) idGenerator.incrementAndGet());
            event.setApproved(false);

            event.setPlaceId(placeId);
            event.setOwnerId(ownerId);
            event.setHeading(heading);
            event.setStartDate(startDate);
            event.setStartTime(startTime);
            event.setEndDate(endDate);
            event.setEndTime(endTime);
            event.setThumbnail(thumbnail);
            event.setContent(content);

            return event;
        }
    }
}
