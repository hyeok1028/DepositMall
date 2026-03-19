package com.hana8.hanaro.service;

import com.hana8.hanaro.common.util.AccountNumberFormatter;
import com.hana8.hanaro.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountNumberGeneratorService {

    private static final int MAX_RETRY = 10;

    private final AccountRepository accountRepository;

    public String generate(String preferred) {
        if (preferred != null && !preferred.isBlank()) {
            String normalized = AccountNumberFormatter.normalize(preferred);
            if (normalized != null && normalized.matches("\\d{11}")
                    && !accountRepository.existsByAccountNumber(normalized)) {
                return normalized;
            }
        }

        for (int i = 0; i < MAX_RETRY; i++) {
            String generated = generateRandom11Digits();
            if (!accountRepository.existsByAccountNumber(generated)) {
                return generated;
            }
        }

        throw new IllegalStateException("account number generation failed");
    }

    private String generateRandom11Digits() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        for (int i = 1; i < 11; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
