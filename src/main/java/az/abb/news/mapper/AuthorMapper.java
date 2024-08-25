package az.abb.news.mapper;

import az.abb.news.entity.Author;
import az.abb.news.model.request.AuthorRequest;
import az.abb.news.model.view.AuthorView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorView toAuthorView(Author author);

    Author toAuthor(AuthorView authorView);

    Author toAuthor(AuthorRequest authorRequest);

    List<AuthorView> toAuthorViewList(List<Author> authors);

    List<Author> toAuthorList(List<AuthorView> authorViews);
}
