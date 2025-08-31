package com.example.demo.repository;

import com.example.demo.entity.TransactionEntity;
import com.example.demo.domain.TransactionInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID>, PagingAndSortingRepository<TransactionEntity, UUID> {
    Page<TransactionInfo> findByIbanAndValueDateBetween(String iban, LocalDate valueDateStart, LocalDate valueDateEnd, Pageable pageable);
}
