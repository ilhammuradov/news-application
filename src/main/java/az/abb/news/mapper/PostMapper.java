package az.abb.news.mapper;

import az.abb.news.entity.Post;
import az.abb.news.model.request.PostRequest;
import az.abb.news.model.view.PostView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostView toPostView(Post post);

    Post toPost(PostView postView);

    Post toPost(PostRequest postRequest);

    List<PostView> toPostViewList(List<Post> posts);

    List<Post> toPostList(List<PostView> postViews);
}