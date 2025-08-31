package com.example.demo.domain.mapper;

import com.example.demo.api.model.TransactionDto;
import com.example.demo.domain.Transaction;
import com.example.demo.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toDomain(TransactionEntity entity);

    TransactionDto toDto(Transaction transaction);
}
