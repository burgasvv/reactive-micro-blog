package org.burgas.identityservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {

    @Column("identity_id")
    private Long identityId;

    @Column("friend_id")
    private Long friendId;

    private Boolean accepted;
}
