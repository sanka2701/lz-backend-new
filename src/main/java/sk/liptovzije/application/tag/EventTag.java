package sk.liptovzije.application.tag;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "event_tag")
public class EventTag {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;
}
