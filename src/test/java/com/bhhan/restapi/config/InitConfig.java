package com.bhhan.restapi.config;

import com.bhhan.restapi.domain.Account;
import com.bhhan.restapi.service.AccountService;
import org.assertj.core.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hbh5274@gmail.com on 2020-06-17
 * Github : http://github.com/bhhan5274
 */

@Configuration
public class InitConfig {
    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                final Account account = Account.builder()
                        .email(appProperties.getUserName())
                        .password(appProperties.getUserPassword())
                        .roles(Sets.newLinkedHashSet(Account.AccountRole.USER, Account.AccountRole.ADMIN))
                        .build();
                accountService.saveAccount(account);
            }
        };
    }
}
