package org.burgas.communityservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityInvitation {

    @Column("identity_id")
    private Long identityId;

    @Column("receiver_id")
    private Long receiverId;

    @Column("community_id")
    private Long communityId;
}
