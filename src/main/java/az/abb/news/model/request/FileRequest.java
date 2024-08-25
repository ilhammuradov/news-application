package az.abb.news.model.request;

import az.abb.news.model.view.PostView;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record FileRequest(
        @NotBlank
        String type,
        PostView post
) {
}
