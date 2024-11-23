package org.burgas.postservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post implements Persistable<Long> {

    @Id
    private Long id;

    @Column("identity_id")
    private Long identityId;

    @Column("wall_id")
    private Long wallId;

    private String content;

    @Column("published_at")
    private LocalDateTime publishedAt;

    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
