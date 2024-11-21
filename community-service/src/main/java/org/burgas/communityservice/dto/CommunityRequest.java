package org.burgas.communityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRequest {

    private Long id;
    private String title;
    private String description;
    private Boolean isPublic;
    private Boolean openPost;
    private Boolean openComment;
}
