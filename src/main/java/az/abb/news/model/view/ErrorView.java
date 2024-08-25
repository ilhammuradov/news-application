package az.abb.news.model.view;

import lombok.Builder;

@Builder
public record ErrorView(
        Integer errorCode,
        String errorMessage
) {
}