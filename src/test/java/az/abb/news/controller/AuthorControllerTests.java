package az.abb.news.controller;

import az.abb.news.exceptions.AuthorNotFoundException;
import az.abb.news.model.request.AuthorRequest;
import az.abb.news.model.view.AuthorView;
import az.abb.news.service.AuthorService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTests {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthorRequest authorRequest;
    private AuthorView authorView;

    @BeforeEach
    void setUp() {
        authorRequest = AuthorRequest.builder().name("Ilham Muradov").postsId(List.of(1L)).build();
        authorView = AuthorView.builder().id(1L).name("Ilham Muradov").posts_id(List.of(1L)).build();
    }

    @Test
    public void testGetAll() throws Exception {
        given(authorService.getAll()).willReturn(ResponseEntity.ok(List.of(authorView)));

        mockMvc.perform(get("/api/v1/authors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(authorView.id().intValue())))
                .andExpect(jsonPath("$[0].name", is(authorView.name())))
                .andExpect(jsonPath("$[0].posts_id", hasSize(1)))
                .andExpect(jsonPath("$[0].posts_id[0]", is(authorView.posts_id().getFirst().intValue())));
    }

    @Test
    public void testGetById() throws Exception {
        given(authorService.getById(1L)).willReturn(ResponseEntity.ok(authorView));

        mockMvc.perform(get("/api/v1/authors/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorView.id().intValue())))
                .andExpect(jsonPath("$.name", is(authorView.name())))
                .andExpect(jsonPath("$.posts_id", hasSize(1)))
                .andExpect(jsonPath("$.posts_id[0]", is(authorView.posts_id().getFirst().intValue())));
    }

    @Test
    public void testGetByIdFail() throws Exception {
        given(authorService.getById(anyLong())).willThrow(AuthorNotFoundException.class);

        mockMvc.perform(get("/api/v1/authors/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        given(authorService.create(authorRequest)).willReturn(ResponseEntity.ok(authorView));

        mockMvc.perform(post("/api/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorView.id().intValue())))
                .andExpect(jsonPath("$.name", is(authorView.name())))
                .andExpect(jsonPath("$.posts_id", hasSize(1)))
                .andExpect(jsonPath("$.posts_id[0]", is(authorView.posts_id().getFirst().intValue())));
    }

    @Test
    public void testUpdate() throws Exception {
        given(authorService.update(1L, authorRequest)).willReturn(ResponseEntity.ok(authorView));

        mockMvc.perform(put("/api/v1/authors/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(authorView.id().intValue())))
                .andExpect(jsonPath("$.name", is(authorView.name())))
                .andExpect(jsonPath("$.posts_id", hasSize(1)))
                .andExpect(jsonPath("$.posts_id[0]", is(authorView.posts_id().getFirst().intValue())));
    }

    @Test
    public void testDelete() throws Exception {
        given(authorService.delete(1L)).willReturn(ResponseEntity.ok("Author with id " + 1L + " has been deleted."));

        mockMvc.perform(delete("/api/v1/authors/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Author with id " + 1L + " has been deleted."));
    }
}
//feignclient
//EasyRandom