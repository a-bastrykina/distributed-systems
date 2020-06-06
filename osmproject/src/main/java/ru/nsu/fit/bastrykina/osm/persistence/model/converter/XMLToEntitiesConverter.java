package ru.nsu.fit.bastrykina.osm.persistence.model.converter;

import ru.nsu.fit.bastrykina.osm.generated.Tag;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Member;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Node;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Relation;
import ru.nsu.fit.bastrykina.osm.persistence.model.entities.Way;

import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.util.stream.Collectors;

public class XMLToEntitiesConverter {
    public static Node convert(ru.nsu.fit.bastrykina.osm.generated.Node orig) {
        var tags = orig.getTag().stream().collect(Collectors.toMap(Tag::getK, Tag::getV));
        return new Node(orig.getId().longValue(), orig.getUser(), orig.getUid().longValue(),
                orig.getVersion().longValue(), orig.getChangeset().longValue(),
                toSqlDate(orig.getTimestamp()),
                orig.getLat(), orig.getLon(), tags);
    }

    public static Way convert(ru.nsu.fit.bastrykina.osm.generated.Way orig) {
        var tags = orig.getTag().stream().collect(Collectors.toMap(Tag::getK, Tag::getV));
        return new Way(orig.getId().longValue(), orig.getUser(), orig.getUid().longValue(),
                orig.getVersion().longValue(), orig.getChangeset().longValue(),
                toSqlDate(orig.getTimestamp()), tags);
    }

    public static Relation convert(ru.nsu.fit.bastrykina.osm.generated.Relation orig) {
        var tags = orig.getTag().stream().collect(Collectors.toMap(Tag::getK, Tag::getV));
        var members = orig.getMember().stream().map(XMLToEntitiesConverter::convert).collect(Collectors.toList());
        return new Relation(orig.getId().longValue(), orig.getUser(), orig.getUid().longValue(),
                orig.getVersion().longValue(), orig.getChangeset().longValue(),
                toSqlDate(orig.getTimestamp()), tags, members);
    }

    public static Member convert(ru.nsu.fit.bastrykina.osm.generated.Member orig) {
        var mem = new Member();
        mem.setRef(orig.getRef().longValue());
        mem.setRole(orig.getRole());
        mem.setType(orig.getType());
        return mem;
    }

    private static Date toSqlDate(XMLGregorianCalendar cal) {
        return new Date(cal.toGregorianCalendar().getTimeInMillis());
    }
}
