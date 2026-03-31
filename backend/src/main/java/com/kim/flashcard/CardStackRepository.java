package com.kim.flashcard;

import com.kim.flashcard.Model.CardStack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CardStackRepository extends JpaRepository<CardStack, Long> {
    Page<CardStack> findAllByOrderByLastUsedDesc(Pageable pageable);
}