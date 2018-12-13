package sk.liptovzije.application.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import sk.liptovzije.application.post.Post;
import sk.liptovzije.application.tag.Tag;
import sk.liptovzije.utils.LocalDatePersistenceConverter;
import sk.liptovzije.utils.LocalTimePersistenceConverter;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "events")
public class Event extends Post {
    @Column(name = "place")
    private Long placeId;

    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @Convert(converter = LocalTimePersistenceConverter.class)
    @Column(name = "start_time")
    private LocalTime startTime;

    @Convert(converter = LocalDatePersistenceConverter.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Convert(converter = LocalTimePersistenceConverter.class)
    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "approved")
    private Boolean approved;

    //todo: not working while saving
//    @ManyToMany(cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//    })
//    @JoinTable(
//            name = "event_tags",
//            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
//    )
    @OneToMany
    @JoinTable(
            name = "event_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    private Event(){
        super();
    }

    public static class Builder {

        private Long id;
        private Long placeId;
        private Long ownerId;
        private String title;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private String thumbnail;
        private String content;
        private Boolean approved;
        private Set<Tag> tags;

        public Builder(long ownerId, String title, String content) {
            this.ownerId = ownerId;
            this.title = title;
            this.content = content;
            this.approved = false;
        }

        public Builder startDate(LocalDate date){
            this.startDate = date;
            return this;
        }

        public Builder startDate(Long millis){
            this.startDate = new LocalDate(millis);
            return this;
        }

        public Builder endDate(LocalDate date){
            this.endDate = date;
            return this;
        }

        public Builder endDate(Long millis){
            this.endDate = new LocalDate(millis);
            return this;
        }

        public Builder startTime(LocalTime time){
            this.startTime = time;
            return this;
        }

        public Builder startTime(Long millis){
            this.startTime = new LocalTime(millis);
            return this;
        }

        public Builder endTime(LocalTime time){
            this.endTime = time;
            return this;
        }

        public Builder endTime(Long millis){
            this.endTime = new LocalTime(millis);
            return this;
        }

        public Builder thumbnail(String thumbnail){
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder placeId(Long placeId){
            this.placeId = placeId;
            return this;
        }

        public Builder approved(Boolean approved){
            this.approved = approved;
            return this;
        }

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Builder tags(Set<Tag> tags){
            this.tags = tags;
            return this;
        }

        public Event build() {
            Event event = new Event();

            event.setId(id);
            event.setApproved(approved);
            event.setTags(tags);
            event.setPlaceId(placeId);
            event.setOwnerId(ownerId);
            event.setTitle(title);
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
