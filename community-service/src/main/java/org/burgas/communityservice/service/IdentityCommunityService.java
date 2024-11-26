package org.burgas.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.IdentityCommunityNotification;
import org.burgas.communityservice.dto.IdentityCommunityRequest;
import org.burgas.communityservice.dto.IdentityPrincipal;
import org.burgas.communityservice.handler.WebClientHandler;
import org.burgas.communityservice.kafka.KafkaProducer;
import org.burgas.communityservice.mapper.IdentityCommunityMapper;
import org.burgas.communityservice.repository.CommunityInvitationRepository;
import org.burgas.communityservice.repository.CommunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@RequiredArgsConstructor
public class IdentityCommunityService {

    private final CommunityInvitationRepository communityInvitationRepository;
    private final CommunityRepository communityRepository;
    private final IdentityCommunityMapper identityCommunityMapper;
    private final WebClientHandler webClientHandler;
    private final KafkaProducer kafkaProducer;

    public Flux<IdentityCommunityNotification> getNotAcceptedNotificationsByReceiver(
            String receiverId, String authValue
    ) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), Long.valueOf(receiverId))
                            ) {
                                return communityInvitationRepository.
                                        findCommunityInvitationsByReceiverIdAndIsAcceptedFalse(Long.valueOf(receiverId))
                                        .flatMap(
                                                communityInvitation -> identityCommunityMapper
                                                        .toInvitationIdentityCommunityNotification(
                                                                Mono.just(communityInvitation), authValue
                                                        )
                                        );
                            } else
                                return Flux.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    public Flux<IdentityCommunityNotification> getAcceptedNotificationsByReceiver(
            String receiverId, String authValue
    ) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), Long.valueOf(receiverId))
                            ) {
                                return communityInvitationRepository.
                                        findCommunityInvitationsByReceiverIdAndIsAcceptedTrue(Long.valueOf(receiverId))
                                        .flatMap(
                                                communityInvitation -> identityCommunityMapper
                                                        .toInvitationIdentityCommunityNotification(
                                                                Mono.just(communityInvitation), authValue
                                                        )
                                        );
                            } else
                                return Flux.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> sendInvitationToCommunityAdministration(
            Mono<IdentityCommunityRequest> identityCommunityRequestMono, String authValue
    ) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), identityCommunityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            IdentityCommunityRequest identityCommunityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId().equals(identityCommunityRequest.getIdentityId())
                            ) {
                                return communityRepository.isOwnerOfCommunity(
                                        identityCommunityRequest.getCommunityId(), identityCommunityRequest.getIdentityId()
                                )
                                        .filter(isOwner -> isOwner)
                                        .flatMap(
                                                _ ->
                                                        communityRepository.makeInvitationFromCommunityToIdentity(
                                                                identityCommunityRequest.getCommunityId(),
                                                                identityCommunityRequest.getIdentityId(),
                                                                identityCommunityRequest.getReceiverId(), false
                                                        ).then(
                                                                kafkaProducer.sendIdentityCommunityNotification(
                                                                        identityCommunityMapper.toIdentityCommunityNotification(
                                                                                Mono.just(identityCommunityRequest), authValue
                                                                        )
                                                                )
                                                        ).then(Mono.just("Приглашение успешно отправлено"))
                                        )
                                        .switchIfEmpty(
                                                Mono.error(
                                                        new RuntimeException("Пользователь, не являющийся администратором сообщества " +
                                                                             "не может отправлять приглашения")
                                                )
                                        );
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> acceptInvitationToCommunityAdministration(
            Mono<IdentityCommunityRequest> identityCommunityRequestMono, String authValue
    ) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), identityCommunityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            IdentityCommunityRequest identityCommunityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId().equals(identityCommunityRequest.getReceiverId())
                            ) {
                                return communityRepository.makeInvitationAccepted(
                                                identityCommunityRequest.getCommunityId(),
                                                identityCommunityRequest.getIdentityId(),
                                                identityCommunityRequest.getReceiverId(), true
                                        )
                                        .then(
                                                communityRepository.makeInvitationFromCommunityToIdentity(
                                                        identityCommunityRequest.getCommunityId(),
                                                        identityCommunityRequest.getReceiverId(),
                                                        identityCommunityRequest.getIdentityId(), true
                                                )
                                        )
                                        .then(
                                                communityRepository.insertCommunityAdmin(
                                                        identityCommunityRequest.getReceiverId(),
                                                        identityCommunityRequest.getCommunityId()
                                                )
                                        )
                                        .then(Mono.just("Вы успешно приняли заявку в сообщество"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> declineInvitationToCommunityAdministration(
            Mono<IdentityCommunityRequest> identityCommunityRequestMono, String authValue
    ) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), identityCommunityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            IdentityCommunityRequest identityCommunityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId().equals(identityCommunityRequest.getReceiverId())
                            ) {
                                return communityRepository.declineInvitationToCommunity(
                                        identityCommunityRequest.getCommunityId(),
                                        identityCommunityRequest.getIdentityId(),
                                        identityCommunityRequest.getReceiverId()
                                ).then(Mono.just("Вы успешно отклонили заявку о вступлении в сообщество"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }
}
