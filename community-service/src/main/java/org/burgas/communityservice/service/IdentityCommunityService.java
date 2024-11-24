package org.burgas.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.IdentityCommunityRequest;
import org.burgas.communityservice.dto.IdentityPrincipal;
import org.burgas.communityservice.handler.WebClientHandler;
import org.burgas.communityservice.kafka.KafkaProducer;
import org.burgas.communityservice.mapper.IdentityCommunityMapper;
import org.burgas.communityservice.repository.CommunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
@RequiredArgsConstructor
public class IdentityCommunityService {

    private final CommunityRepository communityRepository;
    private final IdentityCommunityMapper identityCommunityMapper;
    private final WebClientHandler webClientHandler;
    private final KafkaProducer kafkaProducer;

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
}