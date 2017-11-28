package sk.liptovzije.model.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import sk.liptovzije.model.DO.EventDO;
import sk.liptovzije.utils.LocalDateToJsJsonDateDeserializer;
import sk.liptovzije.utils.LocalDateToJsJsonDateSerializer;

public class EventDTO {
    private Long id;
    private Long userId;
    private String title;
    @JsonSerialize(using = LocalDateToJsJsonDateSerializer.class)
    @JsonDeserialize(using = LocalDateToJsJsonDateDeserializer.class)
    private LocalDateTime startDateTime;
    @JsonSerialize(using = LocalDateToJsJsonDateSerializer.class)
    @JsonDeserialize(using = LocalDateToJsJsonDateDeserializer.class)
    private LocalDateTime endDateTime;
    //    private LocalDate startDate;
//    private LocalTime startTime;
//    private LocalDate endDate;
//    private LocalTime endTime;
    private String content;

    public EventDTO() {}

    public EventDTO(EventDO event) {
        id     = event.getId();
        userId = event.getUserId();
        title  = event.getTitle();
        content = event.getContent();
        startDateTime = new LocalDateTime(event.getStartDate().toDate().getTime() +event.getStartTime().getMillisOfDay());
        endDateTime   = new LocalDateTime(event.getEndDate().toDate().getTime() +event.getEndTime().getMillisOfDay());
    }

    public EventDO toDo() {
        EventDO event = new EventDO();
        event.setId(id);
        event.setUserId(userId);
        event.setTitle(title);
        event.setStartDate(startDateTime.toLocalDate());
        event.setStartTime(startDateTime.toLocalTime());
        event.setEndDate(endDateTime.toLocalDate());
        event.setEndTime(endDateTime.toLocalTime());
        event.setContent(content);

        return event;
    }

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

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

//    public LocalDate getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(LocalDate startDate) {
//        this.startDate = startDate;
//    }
//
//    public LocalTime getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalDate getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = endDate;
//    }
//
//    public LocalTime getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(LocalTime endTime) {
//        this.endTime = endTime;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
