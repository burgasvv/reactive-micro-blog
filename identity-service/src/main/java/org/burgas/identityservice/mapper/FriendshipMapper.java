package org.burgas.identityservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.FriendshipNotification;
import org.burgas.identityservice.dto.FriendshipRequest;
import org.burgas.identityservice.entity.Friendship;
import org.burgas.identityservice.repository.IdentityRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FriendshipMapper {

    private final IdentityRepository identityRepository;

    public Mono<Friendship> requestToFriendship(Mono<FriendshipRequest> friendshipRequestMono, Boolean accepted) {
        return friendshipRequestMono.flatMap(
                friendshipRequest -> Mono.fromCallable(
                        () -> Friendship.builder()
                                .identityId(friendshipRequest.getIdentityId())
                                .friendId(friendshipRequest.getFriendId())
                                .accepted(accepted)
                                .build()
                )
        );
    }

    public Mono<FriendshipNotification> toFriendshipNotification(Mono<Friendship> friendshipMono) {
        return friendshipMono.flatMap(
                friendship -> identityRepository.findById(friendship.getIdentityId())
                        .flatMap(
                                identity -> Mono.fromCallable(
                                        () -> FriendshipNotification.builder()
                                                .identityId(identity.getId())
                                                .friendId(friendship.getFriendId())
                                                .username(identity.getUsername())
                                                .firstname(identity.getFirstname())
                                                .lastname(identity.getLastname())
                                                .patronymic(identity.getPatronymic())
                                                .accepted(friendship.getAccepted())
                                                .build()
                                )
                        )
        );
    }
}
