package org.burgas.communityservice.entity;

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
public class Community implements Persistable<Long> {

    @Id
    private Long id;
    private String title;
    private String description;

    @Column("is_public")
    private Boolean isPublic;

    @Column("open_post")
    private Boolean openPost;

    @Column("open_comment")
    private Boolean openComment;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
