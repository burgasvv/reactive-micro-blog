package org.burgas.identityservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.identityservice.dto.AuthorityResponse;
import org.burgas.identityservice.mapper.AuthorityMapper;
import org.burgas.identityservice.repository.AuthorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(
        readOnly = true, propagation = SUPPORTS
)
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final AuthorityMapper authorityMapper;

    public Mono<AuthorityResponse> findById(String authorityId) {
        return authorityRepository.findById(Long.valueOf(authorityId))
                .flatMap(authority -> authorityMapper.toAuthorityResponse(Mono.just(authority)))
                .log("IDENTITY-SERVICE::findById");
    }
}
