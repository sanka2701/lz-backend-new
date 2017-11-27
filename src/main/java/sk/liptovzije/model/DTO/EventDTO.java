package sk.liptovzije.model.DTO;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import sk.liptovzije.model.DO.EventDO;

public class EventDTO {
    private Long id;
    private Long userId;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String content;

    public EventDTO() {}

    public EventDTO(EventDO event) {
        id     = event.getId();
        userId = event.getUserId();
        title  = event.getTitle();
        startDate = event.getStartDate();
        startTime = event.getStartTime();
        endDate = event.getEndDate();
        endTime = event.getEndTime();
        content = event.getContent();
    }

    public EventDO toDo() {
        EventDO event = new EventDO();
        event.setId(id);
        event.setUserId(userId);
        event.setTitle(title);
        event.setStartDate(startDate);
        event.setStartTime(startTime);
        event.setEndDate(endDate);
        event.setEndTime(endTime);
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
