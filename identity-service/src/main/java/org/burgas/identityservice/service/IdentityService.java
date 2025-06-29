package org.burgas.identityservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.IdentityPrincipal;
import org.burgas.identityservice.dto.IdentityRequestCreate;
import org.burgas.identityservice.dto.IdentityRequestUpdate;
import org.burgas.identityservice.dto.IdentityResponse;
import org.burgas.identityservice.handler.WebClientHandler;
import org.burgas.identityservice.mapper.IdentityMapper;
import org.burgas.identityservice.repository.IdentityRepository;
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
@Transactional(
        readOnly = true, propagation = SUPPORTS
)
public class IdentityService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final WebClientHandler webClientHandler;

    public Flux<IdentityResponse> findAll() {
        return identityRepository.findAll()
                .flatMap(identity -> identityMapper.toIdentityResponse(Mono.just(identity)))
                .log("IDENTITY-SERVICE::findAll");
    }

    public Mono<IdentityResponse> findById(String id) {
        return identityRepository.findById(Long.valueOf(id))
                .flatMap(identity -> identityMapper.toIdentityResponse(Mono.just(identity))
                )
                .log("IDENTITY-SERVICE::findById");
    }

    public Mono<IdentityResponse> findByUsername(String username) {
        return identityRepository.findIdentityByUsername(username)
                .flatMap(identity -> identityMapper.toIdentityResponse(Mono.just(identity))
                )
                .log("IDENTITY-SERVICE::findByUsername");
    }

    @Transactional(
            isolation = REPEATABLE_READ,
            propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> createIdentityWall(Mono<IdentityRequestCreate> identityRequestCreateMono, String authValue) {
        return Mono.zip(webClientHandler.getPrincipal(authValue), identityRequestCreateMono)
                .flatMap(
                        objects -> {
                            IdentityPrincipal identityPrincipal = objects.getT1();
                            IdentityRequestCreate identityRequestCreate = objects.getT2();
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    Objects.equals(identityPrincipal.getId(), identityRequestCreate.getId())
                            ) {
                                return identityRepository.createIdentityWallByIdentityId(
                                        identityRequestCreate.getId(), identityRequestCreate.getWallIsOpened()
                                )
                                        .then(Mono.just("Стена активирована"));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ,
            propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<IdentityResponse> createIdentity(
            Mono<IdentityRequestCreate> identityRequestCreateMono, String authValue
    ) {
        return webClientHandler.getPrincipal(authValue)
                .flatMap(
                        identityPrincipal ->
                                Optional.of(identityPrincipal)
                                        .filter(principal -> !principal.getAuthenticated())
                                        .map(ip ->
                                                        identityMapper.toIdentityCreate(identityRequestCreateMono)
                                                                .flatMap(identityRepository::save)
                                                                .flatMap(identity -> identityMapper
                                                                        .toIdentityResponse(Mono.just(identity))
                                                                )
                                        )
                                        .orElseGet(
                                                () -> Mono.error(
                                                        new RuntimeException("Выйдите из аккаунта, чтобы создать новый")
                                                )
                                        )
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ,
            propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<IdentityResponse> updateIdentity(
            Mono<IdentityRequestUpdate> identityRequestUpdateMono, String authValue
    ) {
        return identityRequestUpdateMono.flatMap(
                identityRequestUpdate -> webClientHandler.getPrincipal(authValue)
                        .flatMap(
                                identityPrincipal -> {
                                    if (identityPrincipal.getAuthenticated() &&
                                        Objects.equals(identityRequestUpdate.getId(), identityPrincipal.getId())) {
                                        return identityMapper.toIdentityUpdate(Mono.just(identityRequestUpdate))
                                                .flatMap(identityRepository::save)
                                                .flatMap(identity -> identityMapper
                                                        .toIdentityResponse(Mono.just(identity))
                                                )
                                                .log("IDENTITY-SERVICE::createIdentity");
                                    } else
                                        return Mono.error(
                                                new RuntimeException("Пользователь не авторизован и не имеет прав₽")
                                        );
                                }
                        )
        );
    }

    @Transactional(
            isolation = REPEATABLE_READ,
            propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public Mono<String> deleteIdentity(String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .flatMap(
                        identityPrincipal -> {
                            if (identityPrincipal.getAuthenticated() &&
                                identityPrincipal.getId() == Long.parseLong(identityId)) {
                                return identityRepository.deleteById(Long.valueOf(identityId))
                                        .then(
                                                Mono.just(
                                                "Пользователь с идентификатором " + identityId + " успешно удален")
                                        )
                                        .log("IDENTITY-SERVICE::deleteIdentity");
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован или не имеет доступа")
                                );
                        }
                );
    }

    public Flux<IdentityResponse> findFriendsByIdentityId(String identityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId() == Long.parseLong(identityId)
                            ) {
                                return identityRepository.findFriendsByIdentityId(Long.valueOf(identityId))
                                        .flatMap(identity -> identityMapper.toIdentityResponse(Mono.just(identity)))
                                        .log("IDENTITY-SERVICE::findFriendsByIdentityId");
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }

    public Flux<IdentityResponse> findIdentitiesByCommunityId(String identityId, String communityId, String authValue) {
        return webClientHandler.getPrincipal(authValue)
                .flux()
                .flatMap(
                        identityPrincipal -> {
                            if (
                                    identityPrincipal.getAuthenticated() &&
                                    identityPrincipal.getId() == Long.parseLong(identityId)
                            ) {
                                return identityRepository.findIdentitiesByCommunityId(Long.valueOf(communityId))
                                        .flatMap(identity -> identityMapper.toIdentityResponse(Mono.just(identity)));
                            } else
                                return Mono.error(
                                        new RuntimeException("Пользователь не авторизован и не имеет прав доступа")
                                );
                        }
                );
    }
}
