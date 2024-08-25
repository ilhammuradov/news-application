package az.abb.news.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryRequest(
        @NotBlank
        String name,
        List<Long> postsId
) {
}
