package tn.esprit.tpfoyer.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idChambre;

    long numeroChambre;

    @Enumerated(EnumType.STRING)
    TypeChambre typeC;



    @OneToMany
    @ToString.Exclude
    Set<Reservation> reservations;

    @ManyToOne(cascade = CascadeType.ALL)
    Bloc bloc;

}
