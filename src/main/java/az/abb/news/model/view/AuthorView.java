package az.abb.news.model.view;

import lombok.Builder;

import java.util.List;

@Builder
public record AuthorView(
        Long id,
        String name,
        List<Long> posts_id
) {
}
