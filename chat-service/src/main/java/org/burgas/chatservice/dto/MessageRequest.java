package org.burgas.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private Long id;
    private Long chatId;
    private String content;
    private Long senderId;
    private Long receiverId;
}
