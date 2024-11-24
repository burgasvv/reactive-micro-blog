package org.burgas.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.communityservice.dto.*;
import org.burgas.communityservice.handler.WebClientHandler;
import org.burgas.communityservice.mapper.CommunityMapper;
import org.burgas.communityservice.repository.CommunityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;
    private final WebClientHandler webClientHandler;

    public Mono<CommunityResponse> findById(String communityId) {
        return communityRepository.findById(Long.parseLong(communityId))
                .flatMap(community -> communityMapper.toCommunityResponse(Mono.just(community)));
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> createCommunityWallByCommunityId(
            Mono<CommunityRequest> communityRequestMono, String identityId, String authValue
    ) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), communityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            CommunityRequest communityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId() == Long.parseLong(identityId)
                            ) {
                                return communityRepository.createCommunityWallByCommunityId(
                                        communityRequest.getId(), communityRequest.getWallIsOpened()
                                )
                                        .then(Mono.just("Стена сообщества создана"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    public Flux<CommunityResponse> findCommunitiesByIdentityId(String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId() == Long.parseLong(identityId)
                            ) {
                                return communityRepository.findCommunitiesByIdentityId(identityPrincipal.getId())
                                        .flatMap(
                                                community -> communityMapper
                                                        .toCommunityResponse(Mono.just(community))
                                        );
                            } else
                                return Flux.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<CommunityResponse> createCommunity(
            Mono<CommunityRequest> communityRequestMono, String identityId, String authValue
    ) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), communityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            CommunityRequest communityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId() == Long.parseLong(identityId)
                            ) {
                                return communityMapper.toCommunity(Mono.just(communityRequest))
                                        .flatMap(communityRepository::save)
                                        .flatMap(
                                                community -> communityRepository
                                                        .insertCommunityAdmin(identityPrincipal.getId(), community.getId())
                                                        .then(communityMapper.toCommunityResponse(Mono.just(community)))
                                        );
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> joinTheCommunity(Mono<IdentityCommunityRequest> identityCommunityRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), identityCommunityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            IdentityCommunityRequest identityCommunityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), identityCommunityRequest.getIdentityId())
                            ) {
                                return communityRepository.insertSubscriber(
                                        identityCommunityRequest.getIdentityId(), identityCommunityRequest.getCommunityId())
                                        .then(Mono.just("Пользователь успешно вступил в сообщество"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(isolation = SERIALIZABLE, propagation = REQUIRED, rollbackFor = Exception.class)
    public Mono<String> leaveTheCommunity(Mono<IdentityCommunityRequest> identityCommunityRequestMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), identityCommunityRequestMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            IdentityCommunityRequest identityCommunityRequest = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), identityCommunityRequest.getIdentityId())
                            ) {
                                return communityRepository.deleteSubscriber(
                                        identityCommunityRequest.getIdentityId(), identityCommunityRequest.getCommunityId()
                                ).then(Mono.just("Пользователь успешно вышел из сообщества"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }
}
