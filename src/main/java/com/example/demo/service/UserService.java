package com.example.demo.service;

import com.example.demo.entity.AccountEntity;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final AccountRepository accountRepository;

    public List<String> findIbanByUser(String userId) {
        return accountRepository.findByUserId(userId)
                                .stream()
                                .map(AccountEntity::getIban)
                                .toList();
    }
}
