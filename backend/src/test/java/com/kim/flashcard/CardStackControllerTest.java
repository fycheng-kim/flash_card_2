package com.kim.flashcard;

import com.kim.flashcard.Model.CardStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardStackController.class)
class CardStackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardStackRepository cardStackRepository;

//    @MockitoBean
//    private CardRepository cardRepository;

    @Test
    void shouldReturnStacksFromMock() throws Exception {
        // Arrange
        int numOfCardStacks = 2;
        List<CardStack> testCardStacks = TestDataFactory.createStacks(numOfCardStacks);
        given(cardStackRepository.findAllByOrderByLastUsedDesc())
                .willReturn(testCardStacks);

        // Act & Assert
        mockMvc.perform(get("/api/stacks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name")
                        .value(testCardStacks.get(0).getName()));
    }

//    @Test
//    void shouldCreateStackSuccessfully() throws Exception {
//        // Arrange
//        CardStack savedStack = new CardStack("Kotlin 2.3", LocalDateTime.now());
//        given(stackRepository.save(any(CardStack.class))).willReturn(savedStack);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/stacks")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\": \"Kotlin 2.3\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Kotlin 2.3"));
//    }
}