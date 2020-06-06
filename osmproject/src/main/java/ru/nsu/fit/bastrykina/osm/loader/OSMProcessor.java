package ru.nsu.fit.bastrykina.osm.loader;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.log4j.Logger;
import ru.nsu.fit.bastrykina.osm.generated.Node;
import ru.nsu.fit.bastrykina.osm.generated.Relation;
import ru.nsu.fit.bastrykina.osm.generated.Way;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class OSMProcessor implements Closeable {
    private static final String OSM_RESOURCE = "/RU-NVS.osm.bz2";
    private static final String EXAMPLE = "/example.xml";

    private int event;

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();
    XMLStreamReader reader;

    public OSMProcessor() {
        try {
            var inputStream = new BZip2CompressorInputStream(getClass().getResourceAsStream(OSM_RESOURCE));
//            var inputStream = getClass().getResourceAsStream(EXAMPLE);
            reader = XML_INPUT_FACTORY.createXMLStreamReader(inputStream);
        } catch (Exception e) {
            Logger.getLogger(getClass()).error("Failed to create OSM processor, cause: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                Logger.getLogger(getClass()).error("Error closing xml stream reader: " + e.getMessage());
            }
        }
    }

    public OSMType getCurrentType() {
        try {
            while (reader.hasNext()) {
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    var elementName = reader.getLocalName();
                    switch (elementName) {
                        case "node":
                            reader.next();
                            return OSMType.NODE;
                        case "way":
                            reader.next();
                            return OSMType.WAY;
                        case "relation":
                            return OSMType.RELATION;
                        default:
                            reader.next();
                            return OSMType.UNKNOWN;
                    }
                }
                reader.next();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return OSMType.UNKNOWN;
    }

    public Node getNextNode() {
           try {
               while (reader.hasNext()) {
                   if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                       var elementName = reader.getLocalName();
                       if ("node".equals(elementName)) {
                           JAXBContext ctx = JAXBContext.newInstance(Node.class);
                           Unmarshaller unmarshaller = ctx.createUnmarshaller();
                           Object obj = unmarshaller.unmarshal(reader);
                           return (Node) obj;
                       }
                   }
                   reader.next();
               }
           } catch (Exception e) {
               Logger.getLogger(getClass()).error(e.getMessage());
               e.printStackTrace();
               throw new RuntimeException(e);
           }
           return null;
    }

    public Way getNextWay() {
        try {
            while (reader.hasNext()) {
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    var elementName = reader.getLocalName();
                    if ("way".equals(elementName)) {
                        JAXBContext ctx = JAXBContext.newInstance(Way.class);
                        Unmarshaller unmarshaller = ctx.createUnmarshaller();
                        Object obj = unmarshaller.unmarshal(reader);
                        return (Way) obj;
                    }
                }
                reader.next();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public Relation getNextRelation() {
        try {
            while (reader.hasNext()) {
                if (reader.getEventType() == XMLEvent.START_ELEMENT) {
                    var elementName = reader.getLocalName();
                    if ("relation".equals(elementName)) {
                        JAXBContext ctx = JAXBContext.newInstance(Relation.class);
                        Unmarshaller unmarshaller = ctx.createUnmarshaller();
                        Object obj = unmarshaller.unmarshal(reader);
                        return (Relation) obj;
                    }
                }
                reader.next();
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean isEof() throws XMLStreamException {
        return !reader.hasNext();
    }
}
