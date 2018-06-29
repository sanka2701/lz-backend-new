package sk.liptovzije.application.place;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "owner")
    private Long ownerId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "approved")
    private Boolean approved;

    // todo: remove, just for offline testing purposes
    private static AtomicInteger idGenerator=new AtomicInteger(19);

    public Place(Long id, Long ownerId, String name, String address, double longitude, double latitude) {
        this.id = id!= null ? id : (long) idGenerator.incrementAndGet();
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.approved = false;
    }
}
