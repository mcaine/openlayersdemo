package com.mikeycaine;

import com.mikeycaine.db.ThingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final ThingService thingService;

    @Override
    public void run(String...args) throws Exception {
        log.info("Calling thing service...");
        thingService.insertThings();
    }
}
