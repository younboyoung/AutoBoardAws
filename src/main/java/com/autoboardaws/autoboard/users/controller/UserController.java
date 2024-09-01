package com.autoboardaws.autoboard.users.controller;

import com.autoboardaws.autoboard.domain.dto.AccountDto;
import com.autoboardaws.autoboard.domain.entity.Account;
import com.autoboardaws.autoboard.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @PostMapping("/api/signup")
    public String signup(AccountDto accountDto) {
        ModelMapper mapper = new ModelMapper();
        Account account = mapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        userService.createUser(account);

        return "redirect:/";
    }
}
