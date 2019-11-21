package ru.otus.study.spring.librarymvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private String id;
    private String role;

    public Role(String role) {
        this.role = role;
    }
}
