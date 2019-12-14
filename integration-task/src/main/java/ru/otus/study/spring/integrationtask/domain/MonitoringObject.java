package ru.otus.study.spring.integrationtask.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "objects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringObject {
    @Id
    private long id;
    private String name;
}
