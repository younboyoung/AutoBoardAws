package com.autoboardaws.autoboard.users.controller;

import com.autoboardaws.autoboard.domain.dto.AccountDto;
import com.autoboardaws.autoboard.domain.entity.Account;
import com.autoboardaws.autoboard.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @PostMapping(value = "/api/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody AccountDto accountDto) {
        ModelMapper mapper = new ModelMapper();
        Account account = mapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Account user = userService.createUser(account);

        return ResponseEntity.ok("signup successful");
    }
}
