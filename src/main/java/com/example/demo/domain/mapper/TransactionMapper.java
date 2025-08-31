package com.example.demo.domain.mapper;


import com.example.demo.api.model.TransactionDto;
import com.example.demo.api.model.TransactionSummaryDto;
import com.example.demo.domain.Transaction;
import com.example.demo.entity.TransactionEntity;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toDomain(TransactionEntity entity);
}
