package org.burgas.chatservice.entity;

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
public class Chat implements Persistable<Long> {

    @Id
    private Long id;

    @Column("sender_id")
    private Long senderId;

    @Column("receiver_id")
    private Long receiverId;

    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
