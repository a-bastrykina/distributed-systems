package ru.nsu.fit.bastrykina.osm.persistence.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {
    @Id
    long id;

    String type;

    String role;

    long ref;

    @ManyToOne
    @JoinColumn(name = "relation_id")
    @JsonBackReference
    Relation relation;
}
