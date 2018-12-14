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
@Table(name = "place")
public class Place {

    @Id
    @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "label")
    private String label;

    @Column(name = "address")
    private String address;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "approved")
    private Boolean approved;


    public Place(Long id, Long ownerId, String label, String address, double longitude, double latitude) {
        this.id = id;
        this.ownerId = ownerId;
        this.label = label;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.approved = false;
    }
}
