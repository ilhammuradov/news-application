package az.abb.news.service;

import az.abb.news.entity.File;
import az.abb.news.exceptions.FileNotFoundException;
import az.abb.news.mapper.FileMapper;
import az.abb.news.model.view.FileView;
import az.abb.news.repository.FileRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTests {
    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileService fileService;

    private EasyRandom easyRandom;
    private File fileEntity;
    private FileView fileView;

   @Value("${upload.dir}")
    private String uploadDir;

    @BeforeEach
    void setUp() {
        fileEntity = File.builder()
                .id(1L)
                .type("text/plain")
                .url("test" + "/test.txt")
                .build();
        fileView = FileView.builder()
                .id(1L)
                .type("text/plain")
                .url("test" + "/test.txt")
                .build();

        ReflectionTestUtils.setField(fileService, "uploadDir", uploadDir);
    }

//    @BeforeEach
//    void setUp() {
//        EasyRandomParameters parameters = new EasyRandomParameters();
//        easyRandom = new EasyRandom(parameters);
//
//        fileEntity = easyRandom.nextObject(File.class);
//        fileView = createFileView();
//    }
//
//    private FileView createFileView() {
//        return FileView.builder()
//                .id(easyRandom.nextObject(Long.class))
//                .type(easyRandom.nextObject(String.class))
//                .url(easyRandom.nextObject(String.class))
//                .build();
//    }

    @Test
    public void testGetAll(){
        given(fileRepository.findAll()).willReturn(List.of(fileEntity));
        given(fileMapper.toFileViewList(List.of(fileEntity))).willReturn(List.of(fileView));

        ResponseEntity<List<FileView>> response=fileService.getAll();

        assertThat(response.getBody()).isEqualTo(List.of(fileView));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(fileRepository).should().findAll();
        then(fileMapper).should().toFileViewList(List.of(fileEntity));
    }

    @Test
    public void testGetByIdSuccess() {
        given(fileRepository.findById(anyLong())).willReturn(Optional.of(fileEntity));
        given(fileMapper.toFileView(any(File.class))).willReturn(fileView);

        ResponseEntity<FileView> response = fileService.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileView, response.getBody());
        then(fileRepository).should().findById(anyLong());
        then(fileMapper).should().toFileView(any(File.class));
    }

    @Test
    public void testGetByIdNotFound() {
        given(fileRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(FileNotFoundException.class, () -> fileService.getById(1L));
        then(fileMapper).shouldHaveNoInteractions();
    }

//    @Test
//    public void testUploadFile() throws IOException {
//        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
//                "text/plain", "Spring Framework".getBytes());
//        given(fileRepository.save(any(File.class))).willReturn(fileEntity);
//        given(fileMapper.toFileView(any(File.class))).willReturn(fileView);
//
//        ResponseEntity<FileView> response = fileService.uploadFile(multipartFile);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(fileView, response.getBody());
//        then(fileRepository).should().save(any(File.class));
//        then(fileMapper).should().toFileView(any(File.class));
//    }
}
