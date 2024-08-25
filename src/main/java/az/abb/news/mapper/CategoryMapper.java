package az.abb.news.mapper;

import az.abb.news.entity.Category;
import az.abb.news.model.request.CategoryRequest;
import az.abb.news.model.view.CategoryView;
import az.abb.news.repository.CategoryRepository;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryView toCategoryView(Category category);

    Category toCategory(CategoryView categoryView);

    Category toCategory(CategoryRequest categoryRequest);

    List<CategoryView> toCategoryViewList(List<Category> categories);

    List<Category> toCategoryList(List<CategoryView> categoryViews);
}
