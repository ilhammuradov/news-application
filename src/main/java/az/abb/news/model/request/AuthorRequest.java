package az.abb.news.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record AuthorRequest(
        @NotBlank
        String name,
        List<Long> postsId
) {
}
