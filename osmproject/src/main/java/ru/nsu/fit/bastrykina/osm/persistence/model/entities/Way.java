package ru.nsu.fit.bastrykina.osm.persistence.model.entities;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.nsu.fit.bastrykina.osm.persistence.model.converter.MyHstoreConverter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "way")
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
public class Way {
    @Id
    long id;

    @Column(name = "username")
    String user;

    long uid;

    long version;

    long changeset;

    @Column(name = "date_time")
    Date timestamp;

    @Convert(disableConversion = true)
    @Type(type = "hstore")
    @Column(columnDefinition = "hstore")
    Map<String, String> tags;
}
