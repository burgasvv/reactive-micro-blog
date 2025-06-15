package org.burgas.identityservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.FriendshipNotification;
import org.burgas.identityservice.dto.FriendshipRequest;
import org.burgas.identityservice.dto.IdentityPrincipal;
import org.burgas.identityservice.handler.WebClientHandler;
import org.burgas.identityservice.kafka.KafkaProducer;
import org.burgas.identityservice.mapper.FriendshipMapper;
import org.burgas.identityservice.repository.FriendshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    private final WebClientHandler webClientHandler;
    private final KafkaProducer kafkaProducer;

    public Flux<FriendshipNotification> findFriendshipNotificationsByFriendId(
            String friendId, String authValue, Boolean accepted
    ) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId().equals(Long.valueOf(friendId))
                            ) {
                                return Optional.of(accepted)
                                        .filter(accept -> !accept)
                                        .map(
                                                aBool -> friendshipRepository
                                                        .findFriendshipsByFriendIdAndAcceptedIsFalse(identityPrincipal.getId())
                                                        .flatMap(friendship -> friendshipMapper
                                                                .toFriendshipNotification(Mono.just(friendship))
                                                        )
                                        )
                                        .orElseGet(
                                                () -> friendshipRepository
                                                        .findFriendshipsByFriendIdAndAcceptedIsTrue(identityPrincipal.getId())
                                                        .flatMap(friendship -> friendshipMapper
                                                                .toFriendshipNotification(Mono.just(friendship))
                                                        )
                                        );
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                )
                .log("FRIENDSHIP_SERVICE::findFriendshipNotificationsByFriendId");
    }

    @Transactional(isolation = REPEATABLE_READ, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> sendFriendRequest(Mono<FriendshipRequest> friendshipRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), friendshipRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            FriendshipRequest friendshipRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), friendshipRequest.getIdentityId())
                            ) {
                                return kafkaProducer.sendIdentityFriend(
                                                friendshipMapper.requestToFriendship(Mono.just(friendshipRequest), false)
                                                        .flatMap(friendshipRepository::save)
                                                        .flatMap(friendship -> friendshipMapper
                                                                .toFriendshipNotification(Mono.just(friendship))
                                                        ), false
                                        )
                                        .then(Mono.just("Заявка в друзья успешно отправлена"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                )
                .log("FRIENDSHIP_SERVICE::sendFriendRequest");
    }

    @Transactional(isolation = REPEATABLE_READ, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> acceptFriendship(Mono<FriendshipRequest> friendshipRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), friendshipRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            FriendshipRequest friendshipRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), friendshipRequest.getIdentityId())
                            ) {
                                return kafkaProducer.sendIdentityFriend(
                                                friendshipRepository.acceptFriendshipByIdentityIdAndFriendId(
                                                        friendshipRequest.getFriendId(), friendshipRequest.getIdentityId()
                                                )
                                                        .then(
                                                                friendshipMapper.requestToFriendship(Mono.just(friendshipRequest), true)
                                                                        .flatMap(friendshipRepository::save)
                                                                        .flatMap(friendship -> friendshipMapper
                                                                                .toFriendshipNotification(Mono.just(friendship))
                                                                        )
                                                        ), true
                                        )
                                        .then(Mono.just("Заявка в друзья успешно принята"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                )
                .log("FRIENDSHIP_SERVICE::acceptFriendship");
    }

    @Transactional(isolation = REPEATABLE_READ, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> declineFriendship(Mono<FriendshipRequest> friendshipRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), friendshipRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            FriendshipRequest friendshipRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), friendshipRequest.getIdentityId())
                            ) {
                                return friendshipRepository.findFriendshipByIdentityIdAndFriendIdAndAcceptedIsFalse(
                                        friendshipRequest.getFriendId(), friendshipRequest.getIdentityId()
                                )
                                        .mapNotNull(friendship -> friendship)
                                        .flatMap(
                                                friendship -> friendshipRepository
                                                        .deleteFriendshipByIdentityIdAndFriendIdAndAcceptedIsFalse(
                                                                friendship.getIdentityId(), friendship.getFriendId()
                                                        )
                                                        .then(Mono.just("Заявка в друзья успешно отклонена"))
                                        )
                                        .switchIfEmpty(
                                                Mono.error(
                                                        new RuntimeException("Заявка в друзья не найдена")
                                                )
                                        );
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                )
                .log("FRIENDSHIP_SERVICE::declineFriendship");
    }

    @Transactional(isolation = REPEATABLE_READ, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> deleteFromFriendship(Mono<FriendshipRequest> friendshipRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), friendshipRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            FriendshipRequest friendshipRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), friendshipRequest.getIdentityId())
                            ) {
                                return friendshipRepository.deleteFriendshipByIdentityIdAndFriendIdAndAcceptedIsTrue(
                                        friendshipRequest.getIdentityId(), friendshipRequest.getFriendId()
                                )
                                        .then(
                                                Mono.just("Пользователь с идентификатором " + friendshipRequest.getFriendId()
                                                + " удален из друзей")
                                        );
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                )
                .log("FRIENDSHIP_SERVICE::declineFriendship");
    }
}
