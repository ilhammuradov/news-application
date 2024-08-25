package az.abb.news.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Auditable {

    @CreatedDate
    @Column(nullable = false, updatable = false)
     LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
     LocalDateTime updatedAt;
}
