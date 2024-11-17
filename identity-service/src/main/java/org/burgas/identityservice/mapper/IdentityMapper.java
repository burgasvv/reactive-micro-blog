package org.burgas.identityservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.IdentityRequestCreate;
import org.burgas.identityservice.dto.IdentityRequestUpdate;
import org.burgas.identityservice.dto.IdentityResponse;
import org.burgas.identityservice.entity.Identity;
import org.burgas.identityservice.repository.IdentityRepository;
import org.burgas.identityservice.service.AuthorityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class IdentityMapper {

    private final AuthorityService authorityService;
    private final IdentityRepository identityRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<Identity> toIdentityCreate(Mono<IdentityRequestCreate> identityRequestCreateMono) {
        return identityRequestCreateMono
                .flatMap(
                        identityRequestCreate -> Mono.just(
                                Identity.builder()
                                        .id(identityRequestCreate.getId())
                                        .username(identityRequestCreate.getUsername())
                                        .password(passwordEncoder.encode(identityRequestCreate.getPassword()))
                                        .email(identityRequestCreate.getEmail())
                                        .firstname(identityRequestCreate.getFirstname())
                                        .lastname(identityRequestCreate.getLastname())
                                        .patronymic(identityRequestCreate.getPatronymic())
                                        .authorityId(2L)
                                        .enabled(true)
                                        .isNew(true)
                                        .build()
                        )
                );
    }

    public Mono<Identity> toIdentityUpdate(Mono<IdentityRequestUpdate> identityRequestUpdateMono) {
        return identityRequestUpdateMono
                .flatMap(
                        identityRequestUpdate -> identityRepository.findById(identityRequestUpdate.getId())
                                .flatMap(
                                        identity -> Mono.just(
                                                Identity.builder()
                                                        .id(identity.getId())
                                                        .username(identityRequestUpdate.getUsername())
                                                        .password(identity.getPassword())
                                                        .email(identityRequestUpdate.getEmail())
                                                        .firstname(identityRequestUpdate.getFirstname())
                                                        .lastname(identityRequestUpdate.getLastname())
                                                        .patronymic(identityRequestUpdate.getPatronymic())
                                                        .authorityId(identity.getAuthorityId())
                                                        .enabled(identity.getEnabled())
                                                        .isNew(false)
                                                        .build()
                                        )
                                )
                );
    }

    public Mono<IdentityResponse> toIdentityResponse(Mono<Identity> identityMono) {
        return identityMono
                .flatMap(
                        identity -> authorityService.findById(String.valueOf(identity.getAuthorityId()))
                                .flatMap(
                                        authorityResponse -> Mono.just(
                                                IdentityResponse.builder()
                                                        .id(identity.getId())
                                                        .username(identity.getUsername())
                                                        .password(identity.getPassword())
                                                        .email(identity.getEmail())
                                                        .firstname(identity.getFirstname())
                                                        .lastname(identity.getLastname())
                                                        .patronymic(identity.getPatronymic())
                                                        .enabled(identity.getEnabled())
                                                        .authorityResponse(authorityResponse)
                                                        .build()
                                        )
                                )
                );
    }
}
