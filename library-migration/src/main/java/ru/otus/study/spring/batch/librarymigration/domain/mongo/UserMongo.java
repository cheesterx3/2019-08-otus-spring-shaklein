package ru.otus.study.spring.batch.librarymigration.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
public class UserMongo {
    @Id
    private String id;
    @Indexed
    private long migrateId;
    private String name;
    private String login;
    private String password;
    @DBRef
    private List<RoleMongo> roles;

    public UserMongo(long migrateId, String name, String login, String password, List<RoleMongo> roles) {
        this.migrateId = migrateId;
        this.name = name;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }
}
