package com.autoboardaws.autoboard.users.service;

import com.autoboardaws.autoboard.domain.entity.Account;
import com.autoboardaws.autoboard.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Account createUser(Account account) {
        return userRepository.save(account);
    }

    public Account getUserByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }
}
