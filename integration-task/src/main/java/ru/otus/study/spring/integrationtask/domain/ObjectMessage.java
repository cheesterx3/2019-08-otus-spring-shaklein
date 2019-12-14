package ru.otus.study.spring.integrationtask.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "object_events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @OneToOne(targetEntity = MonitoringObject.class)
    @JoinColumn(name="object_id")
    private MonitoringObject object;
    private String message;

    public ObjectMessage(MessageType type, MonitoringObject object, String message) {
        this.type = type;
        this.object = object;
        this.message = message;
    }

    public static ObjectMessage fromMessage(SensorMessage sensorMessage, MonitoringObject object) {
        return new ObjectMessage(sensorMessage.getType(),
                object,
                sensorMessage.getMessage());
    }
}
