package com.bhhan.restapi.service;

import com.bhhan.restapi.domain.Account;
import com.bhhan.restapi.domain.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by hbh5274@gmail.com on 2020-06-17
 * Github : http://github.com/bhhan5274
 */

@SpringBootTest
class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("AccountService > loadUserByUsername")
    @Test
    void loadUserByUsername(){

        final String password = "bhhan3";
        final String email = "bhhan3@email.com";

        final Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Sets.newSet(Account.AccountRole.ADMIN, Account.AccountRole.USER))
                .build();

        accountService.saveAccount(account);

        final UserDetails userDetails = accountService.loadUserByUsername(email);
        assertTrue(passwordEncoder.matches(password, userDetails.getPassword()));
    }

    @DisplayName("AccountService > loadUserByUsername > Error")
    @Test
    void loadUserByUsername_fail(){
        assertThrows(UsernameNotFoundException.class, () -> {
            accountService.loadUserByUsername("bhhan");
        });
    }
}