package ru.otus.study.spring.integrationtask.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.study.spring.integrationtask.domain.AbstractAccident;
import ru.otus.study.spring.integrationtask.domain.MessageType;
import ru.otus.study.spring.integrationtask.exceptions.AlreadyStartedException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Service
@RequiredArgsConstructor
public class DefaultAppStarterService implements AppStarterService {
    private final static String[] SENSORS = new String[]{"0010", "0011", "0012", "0020", "0021", "0022", "0030", "0031", "0040", "0050", "0060", "0061", "0062"};
    private final static MessageType[] MESSAGE_TYPES = new MessageType[]{MessageType.Unknown, MessageType.Fire, MessageType.Alarm, MessageType.Service};

    private final SensorDataTransducer dataTransducer;
    private final AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public void start(int threadCount) throws AlreadyStartedException {
        if (started.get())
            throw new AlreadyStartedException("Already started");
        started.set(true);
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> genEvent(dataTransducer));
        }
        executorService.shutdown();
    }

    @Override
    public void stop() {
        started.set(false);
    }

    private void genEvent(SensorDataTransducer dataTransducer) {
        while (started.get()) {
            final AbstractAccident accident = dataTransducer.process(generateMessage());
            log.info(accident.getInfo());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String generateMessage() {
        return String.format("{\"sensorId\":\"%s\",\"type\":\"%s\",\"message\":\"MESSAGE\"}", randomSensor(), randomMessageType().name());
    }

    private static String randomSensor() {
        return SENSORS[RandomUtils.nextInt(0, SENSORS.length)];
    }

    private static MessageType randomMessageType() {
        return MESSAGE_TYPES[RandomUtils.nextInt(0, 3)];
    }
}
