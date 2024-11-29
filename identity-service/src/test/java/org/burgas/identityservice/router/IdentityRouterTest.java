package org.burgas.identityservice.router;

import org.burgas.identityservice.dto.IdentityRequestCreate;
import org.burgas.identityservice.dto.IdentityResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@AutoConfigureWebFlux
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IdentityRouterTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testGetIdentities() {
        webTestClient.get()
                .uri("/identities")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(IdentityResponse.class);
    }

    @Test
    public void testGetIdentity() {
        webTestClient.get()
                .uri("/identities/admin")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdentityResponse.class);
    }

    @Test
    public void testCreateIdentity() {
        webTestClient.post()
                .uri("/identities/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(
                                IdentityRequestCreate.builder()
                                        .username("mouretter")
                                        .firstname("Sapov")
                                        .lastname("Alexander")
                                        .patronymic("Sergeevich")
                                        .email("mouretter@gmail.com")
                                        .password("asdqwe")
                                        .wallIsOpened(true)
                                        .build()
                        ),
                        IdentityRequestCreate.class
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(IdentityResponse.class);
    }
}