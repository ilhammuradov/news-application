package az.abb.news.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_post")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
     String title;
     String content;
    @ManyToMany
     List<Author> authors;
    @OneToMany
     List<File> files;
    @ManyToMany
     List<Category> categories;
}
