package az.abb.news.model.view;

import lombok.Builder;

@Builder
public record PostView(
        Long id,
        String title,
        String content
) {
}
