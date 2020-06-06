package ru.nsu.fit.bastrykina.osm.persistence.model.entities;

import lombok.*;
import ru.nsu.fit.bastrykina.osm.persistence.model.converter.MyHstoreConverter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "relation")
public class Relation {
    @Id
    long id;

    @Column(name = "username")
    String user;

    long uid;

    long version;

    long changeset;

    @Column(name = "date_time")
    Date timestamp;

    @Convert(converter = MyHstoreConverter.class)
    Map<String, String> tags;

    @OneToMany(mappedBy = "relation", cascade = CascadeType.ALL)
    List<Member> members;
}
