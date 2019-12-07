package ru.otus.study.spring.batch.librarymigration.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "role")
@NoArgsConstructor
@AllArgsConstructor
public class RoleMongo {
    @Id
    private String id;
    @Indexed
    private long migrateId;
    private String role;

    public RoleMongo(long migrateId, String role) {
        this.migrateId = migrateId;
        this.role = role;
    }
}
