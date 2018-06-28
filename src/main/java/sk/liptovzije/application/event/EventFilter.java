package sk.liptovzije.application.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class EventFilter {
    private Long ownerId;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Boolean approved;
}
