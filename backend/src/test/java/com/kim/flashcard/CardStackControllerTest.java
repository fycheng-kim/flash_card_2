package com.kim.flashcard;

import com.kim.flashcard.Model.CardStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(CardStackController.class)
class CardStackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardStackRepository cardStackRepository;

    @MockitoBean
    private CardRepository cardRepository;

    @Test
    void shouldGetAllStacks() throws Exception {
        // Arrange
        int numOfCardStacks = 2;
        List<CardStack> testCardStacks = TestDataFactory.createStacks(numOfCardStacks);
        Page<CardStack> page = new PageImpl<>(testCardStacks);
        given(cardStackRepository.findAllByOrderByLastUsedDesc(any(Pageable.class)))
                .willReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/stacks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name")
                        .value(testCardStacks.get(0).getName()));
    }

    @Test
    void shouldCreateStackSuccessfully() throws Exception {
        // Arrange
        CardStack savedStack = TestDataFactory.createStack("testCardStack");
        given(cardStackRepository.save(any(CardStack.class))).willReturn(savedStack);

        // Act & Assert
        mockMvc.perform(post("/api/stacks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"testCardStack\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testCardStack"));
    }

    @Test
    void shouldUpdateCardStackName() throws Exception {
        Long cardStackId = 1L;
        CardStack existing = TestDataFactory.createStack("Old Name");
        CardStack updated = TestDataFactory.createStack("New Name");

        given(cardStackRepository.findById(cardStackId))
                .willReturn(java.util.Optional.of(existing));
        given(cardStackRepository.save(any(CardStack.class)))
                .willReturn(updated);


        // 2. Act & Assert
        mockMvc.perform(put("/api/stacks/{id}", cardStackId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.toJson(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }
}