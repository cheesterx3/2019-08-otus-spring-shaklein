package ru.otus.study.spring.integrationtask.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "sensors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    @Id
    private long id;
    private String code;
    @OneToOne(targetEntity = MonitoringObject.class)
    @JoinColumn(name = "object_id")
    private MonitoringObject object;
}
