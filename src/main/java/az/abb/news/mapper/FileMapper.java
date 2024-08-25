package az.abb.news.mapper;

import az.abb.news.entity.File;
import az.abb.news.model.view.FileView;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileView toFileView(File file);

    File toFile(FileView fileView);

    List<FileView> toFileViewList(List<File> files);

    List<File> toFileList(List<FileView> fileViews);
}
