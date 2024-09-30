package com.autoboardaws.autoboard.security.service;

import com.autoboardaws.autoboard.domain.dto.AccountContext;
import com.autoboardaws.autoboard.domain.dto.AccountDto;
import com.autoboardaws.autoboard.domain.entity.Account;
import com.autoboardaws.autoboard.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
@AllArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        Account account = userRepository.findByEmailAddress(emailAddress);
        if(account == null) {
            throw new UsernameNotFoundException("No User found with emailAddress: " + emailAddress);
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(account.getRoles()));
        ModelMapper mapper = new ModelMapper();
        AccountDto accountDto = mapper.map(account, AccountDto.class);

        return new AccountContext(accountDto, authorities);
    }
}
