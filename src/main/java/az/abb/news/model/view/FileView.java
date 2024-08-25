package az.abb.news.model.view;

import lombok.Builder;

@Builder
public record FileView(
        Long id,
        String type,
        String url
) {
}
