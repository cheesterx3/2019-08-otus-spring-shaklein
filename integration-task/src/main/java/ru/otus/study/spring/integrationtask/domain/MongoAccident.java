package ru.otus.study.spring.integrationtask.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document(collection = "accidents")
@AllArgsConstructor
@NoArgsConstructor
public class MongoAccident extends AbstractAccident {
    @Id
    private String id;
    private String info;
    private MonitoringObject object;
    private MessageType type;
    private ObjectMessage message;

    public MongoAccident(String message, ObjectMessage objectMessage) {
        this.info = message;
        this.object = objectMessage.getObject();
        this.type = objectMessage.getType();
        this.message = objectMessage;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getInfo() {
        return object != null ? String.format("Mongo %s for object %s", info, object.getName()) : info;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public MonitoringObject getObject() {
        return object;
    }
}
