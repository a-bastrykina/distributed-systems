
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OSMProcessor {

    private final Path dataPath;
    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    public OSMProcessor(Path dataPath) {
        this.dataPath = dataPath;
    }

    // For task1
    public void printStatistics() {
        XMLStreamReader reader = null;
        Map<String, Integer> userChanges = new HashMap<>();
        Map<String, Integer> uniqueTagKeys = new HashMap<>();
        try {
            var inputStream = new BZip2CompressorInputStream(Files.newInputStream(dataPath));
            reader = XML_INPUT_FACTORY.createXMLStreamReader(inputStream);
            while (reader.hasNext()) {
                var event = reader.next();
                if (event == XMLEvent.START_ELEMENT) {
                    var elementName = reader.getLocalName();
                    if ("node".equals(elementName)) {
                        var user = reader.getAttributeValue(null, "user");
                        if (user != null) {
                            if (!userChanges.containsKey(user)) {
                                userChanges.put(user, 1);
                            } else {
                                userChanges.replace(user, userChanges.get(user) + 1);
                            }
                        }
                        // Check node tags
                        while (reader.hasNext()) {
                            var nestedEvent = reader.next();
                            var nodeTagKeys = new HashSet<String>();
                            if (nestedEvent == XMLEvent.END_ELEMENT && "node".equals(reader.getLocalName())) break;
                            if (nestedEvent == XMLEvent.START_ELEMENT && "tag".equals(reader.getLocalName())) {
                                var tagKey = reader.getAttributeValue(null, "k");
                                if (tagKey != null && !nodeTagKeys.contains(tagKey)) {
                                    nodeTagKeys.add(tagKey);
                                    if (!uniqueTagKeys.containsKey(tagKey)) {
                                        uniqueTagKeys.put(tagKey, 1);
                                    } else {
                                        uniqueTagKeys.replace(tagKey, uniqueTagKeys.get(tagKey) + 1);
                                    }
                                }
                            }
                        }
                    } else if ("way".equals("elementName")) break;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(getClass()).error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    Logger.getLogger(getClass()).error("Error closing xml stream reader: " + e.getMessage());
                }
            }
        }
        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.println("User changes:");
        System.out.println("----------------------------------------------------");
        userChanges.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .forEach((e) -> System.out.println(e.getKey() + ": " + e.getValue()));

        System.out.println();
        System.out.println("----------------------------------------------------");
        System.out.println("Unique tag keys:");
        System.out.println("----------------------------------------------------");
        uniqueTagKeys.entrySet().stream().forEach((e) -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
}
