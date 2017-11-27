package sk.liptovzije.model.DO;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import sk.liptovzije.model.DTO.EventDTO;
import sk.liptovzije.utils.LocalDatePersistenceConverter;
import sk.liptovzije.utils.LocalTimePersistenceConverter;

import javax.persistence.*;

@Entity
@Table(name = "events")
public class EventDO {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate startDate;

    @Column(name = "start_time")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime startTime;

    @Column(name = "end_date")
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate endDate;

    @Column(name = "end_time")
    @Convert(converter = LocalTimePersistenceConverter.class)
    private LocalTime endTime;

    @Column(name = "content")
    private String content;

    public EventDO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
