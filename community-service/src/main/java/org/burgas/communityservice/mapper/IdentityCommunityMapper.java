package org.burgas.communityservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.CommunityResponse;
import org.burgas.communityservice.dto.IdentityCommunityNotification;
import org.burgas.communityservice.dto.IdentityCommunityRequest;
import org.burgas.communityservice.dto.IdentityResponse;
import org.burgas.communityservice.handler.WebClientHandler;
import org.burgas.communityservice.service.CommunityService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class IdentityCommunityMapper {

    private final CommunityService communityService;
    private final WebClientHandler webClientHandler;

    public Mono<IdentityCommunityNotification> toIdentityCommunityNotification(
            Mono<IdentityCommunityRequest> identityCommunityRequestMono, String authValue
    ) {
        return identityCommunityRequestMono.flatMap(
                identityCommunityRequest ->
                        Mono.zip(
                                webClientHandler.getIdentityById(String.valueOf(identityCommunityRequest.getIdentityId()), authValue),
                                communityService.findById(String.valueOf(identityCommunityRequest.getCommunityId()))
                        )
                                .flatMap(
                                        objects -> {
                                            IdentityResponse identityResponse = objects.getT1();
                                            CommunityResponse communityResponse = objects.getT2();
                                            return Mono.fromCallable(
                                                    () -> IdentityCommunityNotification.builder()
                                                            .receiverId(identityCommunityRequest.getReceiverId())
                                                            .identityResponse(identityResponse)
                                                            .communityResponse(communityResponse)
                                                            .build()
                                            );
                                        }
                                )
        );
    }
}
