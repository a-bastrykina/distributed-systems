package ru.nsu.fit.bastrykina.osm.statistics;

import ru.nsu.fit.bastrykina.osm.generated.Node;
import ru.nsu.fit.bastrykina.osm.loader.OSMProcessor;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class StatisticsPrinter {


    public static void printNodeStatistics() {
        Map<String, Integer> userChanges = new HashMap<>();
        Map<String, Integer> uniqueTagKeys = new HashMap<>();
        Node curNode;
        try (var processor = new OSMProcessor()) {
            while ((curNode = processor.getNextNode()) != null) {
                var user = curNode.getUser();
                if (user != null) {
//                    System.out.println("user change: " + user);
                    if (!userChanges.containsKey(user)) {
                        userChanges.put(user, 1);
                    } else {
                        userChanges.replace(user, userChanges.get(user) + 1);
                    }
                }
                for (var tag : curNode.getTag()) {
                    var tagKey = tag.getK();
//                    System.out.println("tag: " + tagKey);
                    if (!uniqueTagKeys.containsKey(tagKey)) {
                        uniqueTagKeys.put(tagKey, 1);
                    } else {
                        uniqueTagKeys.replace(tagKey, uniqueTagKeys.get(tagKey) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
