package az.abb.news.model.view;

import lombok.Builder;

@Builder
public record CategoryView(
        Long id,
        String name
) {
}
