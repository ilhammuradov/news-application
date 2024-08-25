package az.abb.news.controller;

import az.abb.news.exceptions.CategoryNotFoundException;
import az.abb.news.model.request.PostRequest;
import az.abb.news.model.view.PostView;
import az.abb.news.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTests {

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private PostRequest postRequest;
    private PostView postView;

    @BeforeEach
    public void setUp() {
        postView = PostView.builder().id(1L).title("title").content("content").build();
        postRequest = PostRequest.builder().title("title").content("content").authorsId(List.of(1L)).categoriesId(List.of(1L)).filesId(List.of(1L)).build();
    }

    @Test
    public void testGetAll() throws Exception {
        given(postService.getAll()).willReturn(ResponseEntity.ok(List.of(postView)));

        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(postView.id().intValue())))
                .andExpect(jsonPath("$[0].title", is(postView.title())))
                .andExpect(jsonPath("$[0].content", is(postView.content())));
    }

    @Test
    public void testGetById() throws Exception {
        given(postService.getById(1L)).willReturn(ResponseEntity.ok(postView));

        mockMvc.perform(get("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postView.id().intValue())))
                .andExpect(jsonPath("$.title", is(postView.title())))
                .andExpect(jsonPath("$.content", is(postView.content())));
    }

    @Test
    public void testGetByIdFail() throws Exception {
        given(postService.getById(anyLong())).willThrow(CategoryNotFoundException.class);

        mockMvc.perform(get("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        given(postService.create(postRequest)).willReturn(ResponseEntity.ok(postView));

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postView.id().intValue())))
                .andExpect(jsonPath("$.title", is(postView.title())))
                .andExpect(jsonPath("$.content", is(postView.content())));
    }

    @Test
    public void testUpdate() throws Exception {
        given(postService.update(1L, postRequest)).willReturn(ResponseEntity.ok(postView));

        mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postView.id().intValue())))
                .andExpect(jsonPath("$.title", is(postView.title())))
                .andExpect(jsonPath("$.content", is(postView.content())));
    }

    @Test
    public void testDelete() throws Exception {
        given(postService.delete(1L)).willReturn(ResponseEntity.ok("Post with id " + 1L + " has been deleted."));

        mockMvc.perform(delete("/api/v1/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Post with id " + 1L + " has been deleted."));
    }
}

