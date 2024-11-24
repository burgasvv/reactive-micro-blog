package org.burgas.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityCommunityNotification {

    private Long receiverId;
    private IdentityResponse identityResponse;
    private CommunityResponse communityResponse;
}
