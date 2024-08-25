package az.abb.news.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record PostRequest(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotNull
        List<Long> authorsId,
        @NotNull
        List<Long> filesId,
        @NotNull
        List<Long> categoriesId
) {
}
