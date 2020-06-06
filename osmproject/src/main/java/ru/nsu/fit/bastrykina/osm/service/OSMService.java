package ru.nsu.fit.bastrykina.osm.service;
import org.springframework.stereotype.Service;
import ru.nsu.fit.bastrykina.osm.SpeedTest;
import ru.nsu.fit.bastrykina.osm.loader.OSMLoader;


@Service
public class OSMService {

    private SpeedTest tester;
    private OSMLoader loader;

    public OSMService(SpeedTest tester, OSMLoader loader) {
        this.tester = tester;
        this.loader = loader;
    }

    public String performTesting() {
        return tester.test();
    }

    public void doLoad() {
        new Thread(() -> loader.load()).start();
    }
}
