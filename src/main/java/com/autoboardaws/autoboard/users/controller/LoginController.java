package com.autoboardaws.autoboard.users.controller;

import com.autoboardaws.autoboard.domain.dto.AccountDto;
import com.autoboardaws.autoboard.security.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private JWTService jwtService;

    @GetMapping("/denied")
    public String accessDenied(@RequestParam(value="exception", required=false) String exception,
                               @AuthenticationPrincipal AccountDto accountDto, Model model) {
        model.addAttribute("username", accountDto.getUsername());
        model.addAttribute("exception", exception);

        return "login/denied";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "logout";
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication(HttpSession session) {
        String jwt = (String) session.getAttribute("jwt");
        if (jwt != null && jwtService.validateToken(jwt)) {
            AccountDto user = jwtService.getUserFromToken(jwt);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
