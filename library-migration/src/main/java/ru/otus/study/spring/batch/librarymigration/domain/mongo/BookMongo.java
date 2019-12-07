package ru.otus.study.spring.batch.librarymigration.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMongo {
    @Id
    private String id;
    @Indexed
    private long migrateId;
    private String name;
    @DBRef
    private List<AuthorMongo> authors;
    private List<GenreMongo> genres;

    public BookMongo(long migrateId, String name, List<AuthorMongo> authorMongos, List<GenreMongo> genres) {
        this.migrateId = migrateId;
        this.name = name;
        this.authors = authorMongos;
        this.genres = genres;
    }
}
