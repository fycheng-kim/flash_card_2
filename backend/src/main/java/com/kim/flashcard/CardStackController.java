package com.kim.flashcard;

import com.kim.flashcard.Model.Card;
import com.kim.flashcard.Model.CardStack;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stacks")
@CrossOrigin(origins = "http://localhost:5173")
public class CardStackController {

    private final CardStackRepository repository;
    private final CardRepository cardRepository;

    public CardStackController(CardStackRepository repository, CardRepository cardRepository) {
        this.repository = repository;
        this.cardRepository = cardRepository;
    }

    @GetMapping
    public List<CardStack> getAllStacks() {
        return repository.findAllByOrderByLastUsedDesc();
    }

    // NEW: Handle creating a stack
    @PostMapping
    public ResponseEntity<CardStack> createStack(@RequestBody CardStack stack) {
        // Ensure the time is set to now if the frontend didn't provide it
        if (stack.getLastUsed() == null) {
            stack.setLastUsed(LocalDateTime.now());
        }
        repository.save(stack);
        return ResponseEntity.ok(stack);
    }
    
    @GetMapping("/{id}/cards")
    public List<Card> getCardsByStack(@PathVariable Long id) {
        // This assumes you have a CardRepository and a relationship set up
        return cardRepository.findByStackId(id);
    }
}