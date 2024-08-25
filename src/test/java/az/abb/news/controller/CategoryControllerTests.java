package az.abb.news.controller;

import az.abb.news.exceptions.CategoryNotFoundException;
import az.abb.news.model.request.CategoryRequest;
import az.abb.news.model.view.CategoryView;
import az.abb.news.service.CategoryService;
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
public class CategoryControllerTests {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryView categoryView;
    private CategoryRequest categoryRequest;

    @BeforeEach
    public void setUp() {
        categoryView = CategoryView.builder().id(1L).name("TestCategory").build();
        categoryRequest = CategoryRequest.builder().name("TestCategory").postsId(List.of(1L)).build();
    }

    @Test
    public void testGetAll() throws Exception {
        given(categoryService.getAll()).willReturn(ResponseEntity.ok(List.of(categoryView)));

        mockMvc.perform(get("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(categoryView.id().intValue())))
                .andExpect(jsonPath("$[0].name", is(categoryView.name())));
    }

    @Test
    public void testGetById() throws Exception {
        given(categoryService.getById(1L)).willReturn(ResponseEntity.ok(categoryView));

        mockMvc.perform(get("/api/v1/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryView.id().intValue())))
                .andExpect(jsonPath("$.name", is(categoryView.name())));
    }

    @Test
    public void testGetByIdFail() throws Exception {
        given(categoryService.getById(anyLong())).willThrow(CategoryNotFoundException.class);

        mockMvc.perform(get("/api/v1/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        given(categoryService.create(categoryRequest)).willReturn(ResponseEntity.ok(categoryView));

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryView.id().intValue())))
                .andExpect(jsonPath("$.name", is(categoryView.name())));
    }

    @Test
    public void testUpdate() throws Exception {
        given(categoryService.update(1L, categoryRequest)).willReturn(ResponseEntity.ok(categoryView));

        mockMvc.perform(put("/api/v1/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryView.id().intValue())))
                .andExpect(jsonPath("$.name", is(categoryView.name())));
    }

    @Test
    public void testDelete() throws Exception {
        given(categoryService.delete(1L)).willReturn(ResponseEntity.ok("Category with id " + 1L + " has been deleted."));

        mockMvc.perform(delete("/api/v1/categories/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Category with id " + 1L + " has been deleted."));
    }
}

