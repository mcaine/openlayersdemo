package com.mikeycaine.db;

import com.mikeycaine.controllers.JsonController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ThingService {
    private final ThingRepository thingRepository;

    public void insertThings() {

        for (Polygon p: JsonController.japanPolygons()) {
            Thing t = new Thing();
            t.setPolygon(p);
            thingRepository.save(t);
            log.info("Saved " + t);
        };
    }
}
