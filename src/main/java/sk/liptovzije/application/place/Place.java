package sk.liptovzije.application.place;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "searchable_name")
    private String searchableName;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    // todo: remove, just for offline testing purposes
    private static AtomicInteger idGenerator=new AtomicInteger();

    public Place(String name, double longitude, double latitude) {
        this.id = (long) idGenerator.incrementAndGet();
        this.name = name;
        this.searchableName = StringUtils.stripAccents(name);
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
