package ru.otus.study.spring.batch.librarymigration.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "genre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreMongo {
    @Id
    private String id;
    @Indexed
    private long migrateId;
    private String name;

    public GenreMongo(long migrateId, String name) {
        this.migrateId = migrateId;
        this.name = name;
    }
}
