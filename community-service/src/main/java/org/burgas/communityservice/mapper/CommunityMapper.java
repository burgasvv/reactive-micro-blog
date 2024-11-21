package org.burgas.communityservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.CommunityRequest;
import org.burgas.communityservice.dto.CommunityResponse;
import org.burgas.communityservice.entity.Community;
import org.burgas.communityservice.repository.CommunityRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class CommunityMapper {

    private final CommunityRepository communityRepository;

    public Mono<Community> toCommunity(Mono<CommunityRequest> communityRequestMono) {
        return communityRequestMono.flatMap(
                communityRequest -> {
                    Long communityId = communityRequest.getId() == null ? 0L : communityRequest.getId();
                    return communityRepository.findById(communityId).mapNotNull(community -> community)
                            .flatMap(
                                    community -> Mono.fromCallable(
                                            () -> Community.builder()
                                                    .id(communityRequest.getId())
                                                    .title(communityRequest.getTitle())
                                                    .description(communityRequest.getDescription())
                                                    .isPublic(communityRequest.getIsPublic())
                                                    .openPost(communityRequest.getOpenPost())
                                                    .openComment(communityRequest.getOpenComment())
                                                    .createdAt(community.getCreatedAt())
                                                    .isNew(false)
                                                    .build()
                                    )
                            )
                            .switchIfEmpty(
                                    Mono.fromCallable(
                                            () -> Community.builder()
                                                    .id(communityRequest.getId())
                                                    .title(communityRequest.getTitle())
                                                    .description(communityRequest.getDescription())
                                                    .isPublic(communityRequest.getIsPublic())
                                                    .openPost(communityRequest.getOpenPost())
                                                    .openComment(communityRequest.getOpenComment())
                                                    .createdAt(LocalDateTime.now())
                                                    .isNew(true)
                                                    .build()
                                    )
                            );
                }
        );
    }

    public Mono<CommunityResponse> toCommunityResponse(Mono<Community> communityMono) {
        return communityMono.flatMap(
                community -> Mono.fromCallable(
                        () -> CommunityResponse.builder()
                                .id(community.getId())
                                .title(community.getTitle())
                                .description(community.getDescription())
                                .isPublic(community.getIsPublic())
                                .openPost(community.getOpenPost())
                                .openComment(community.getOpenComment())
                                .createdAt(
                                        community.getCreatedAt().format(
                                                DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")
                                        )
                                )
                                .build()
                )
        );
    }
}
