package ru.otus.study.spring.integrationtask.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "accidents")

@NoArgsConstructor
public class Accident extends AbstractAccident{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String info;
    @OneToOne(targetEntity = MonitoringObject.class)
    @JoinColumn(name = "object_id")
    private MonitoringObject object;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    @OneToOne(targetEntity = ObjectMessage.class)
    @JoinColumn(name = "message_id")
    private ObjectMessage message;

    public Accident(String info) {
        this.info = info;
    }

    public Accident(String info, ObjectMessage message) {
        this.info = info;
        this.object = message.getObject();
        this.type = message.getType();
        this.message = message;
    }

    public static Accident nullAccident() {
        return new Accident("Empty accident");
    }

    public String getInfo() {
        return object != null ? String.format("%s for object %s", info, object.getName()) : info;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public MonitoringObject getObject() {
        return object;
    }

    public String getId(){
        return String.valueOf(id);
    }

}
