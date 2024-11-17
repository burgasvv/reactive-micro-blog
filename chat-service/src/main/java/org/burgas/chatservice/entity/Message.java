package org.burgas.chatservice.entity;

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
public class Message implements Persistable<Long> {

    @Id
    private Long id;

    @Column("chat_id")
    private Long chatId;

    @Column("sender_id")
    private Long senderId;

    @Column("receiver_id")
    private Long receiverId;

    private String content;

    @Column("received_at")
    private LocalDateTime receivedAt;

    @Transient
    private Boolean isNew;

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }
}
