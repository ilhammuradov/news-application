package az.abb.news.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_file")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class File extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
     String type;
     String url;
    @ManyToOne
     Post post;

     @Column(name = "post_id",insertable = false,updatable = false)
    Long postId;
}

