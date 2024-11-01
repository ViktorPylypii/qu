package com.bot.quotes.repository;

import com.bot.quotes.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote,Long>, JpaSpecificationExecutor<Quote> {
    Optional<Quote> findByQuote(String quote);
}
