package com.kim.flashcard;

import com.kim.flashcard.Model.Card;
import com.kim.flashcard.Model.CardStack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public Page<CardStack> getAllStacks(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "lastUsed",
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return repository.findAllByOrderByLastUsedDesc(pageable);
    }

    @PostMapping
    public ResponseEntity<CardStack> createStack(@RequestBody CardStack stack) {
        // Ensure the time is set to now if the frontend didn't provide it
        if (stack.getLastUsed() == null) {
            stack.setLastUsed(LocalDateTime.now());
        }
        repository.save(stack);
        return ResponseEntity.ok(stack);
    }

    @PutMapping("/{cardStackId}")
    public ResponseEntity<CardStack> updateStackName(
            @PathVariable Long cardStackId,
            @RequestBody CardStack stackDetails
    ) {
        return repository.findById(cardStackId)
                .map(existingStack -> {
                    existingStack.setName(stackDetails.getName());
                    // lastUsed remains unchanged or can be updated here if desired
                    CardStack updated = repository.save(existingStack);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{cardStackId}/cards")
    public List<Card> getCardsByStack(@PathVariable Long cardStackId) {

        return cardRepository.findByStackId(cardStackId);
    }

//    @PostMapping("/{cardStackId}")
//    public CardStack addCardIntoCardStack(
//            @PathVariable Long cardStackId
//    ) {
//        return cardRepository.findByStackId(cardStackId);
//    }
}