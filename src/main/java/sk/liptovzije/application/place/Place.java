package sk.liptovzije.application.place;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
}
