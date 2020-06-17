package com.bhhan.restapi.service.dto;

import com.bhhan.restapi.domain.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hbh5274@gmail.com on 2020-06-17
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
public class AccountAdapter extends User {

    private Account account;

    public AccountAdapter(Account account) {
        super(account.getEmail(), account.getPassword(), getAuthorities(account));
        this.account = account;
    }

    private static List<SimpleGrantedAuthority> getAuthorities(Account account) {
        return account.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }
}
