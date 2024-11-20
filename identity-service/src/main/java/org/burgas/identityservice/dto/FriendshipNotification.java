package org.burgas.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipNotification {

    private Long identityId;
    private Long friendId;
    private String username;
    private String firstname;
    private String lastname;
    private String patronymic;
    private Boolean accepted;
}
