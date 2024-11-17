package org.burgas.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityRequestCreate {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String patronymic;
}
