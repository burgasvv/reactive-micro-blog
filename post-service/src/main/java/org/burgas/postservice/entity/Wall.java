package org.burgas.postservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wall implements Persistable<Long> {

    @Id
    private Long id;
    private Long identityId;
    private Long communityId;

    @Column("is_opened")
    private Boolean isOpened;

    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
