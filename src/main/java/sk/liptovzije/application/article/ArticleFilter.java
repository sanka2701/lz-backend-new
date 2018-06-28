package sk.liptovzije.application.article;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ArticleFilter {
    private Long ownerId;
    private String title;
    private LocalDate dateAddedFrom;
    private LocalDate dateAddedUntil;
}
