package com.autoboardaws.autoboard.domain.dto;

import com.autoboardaws.autoboard.domain.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String id;
    private String username;
    private String password;
    private String emailAddress;
    private String roles;

    public AccountDto(Account account) {
        this.id = String.valueOf(account.getId());
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.emailAddress = account.getEmailAddress();
        this.roles = account.getRoles();
    }

    @Override
    public String toString() {
        return "AccountDto{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", roles='" + roles + '\'' +
                '}';
    }
}
