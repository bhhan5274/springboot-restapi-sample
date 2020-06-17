package com.bhhan.restapi.config;

import com.bhhan.restapi.domain.Account;
import com.bhhan.restapi.service.AccountService;
import com.bhhan.restapi.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by hbh5274@gmail.com on 2020-06-17
 * Github : http://github.com/bhhan5274
 */
class AuthServerConfigTest extends BaseControllerTest {
    @Autowired
    AccountService accountService;

    @DisplayName("인증 토큰 발급 테스트")
    @Test
    void getAuthToken() throws Exception {

        final String email = "bhhan5274@email.com";
        final String password = "bhhan5274";

        final Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(Sets.newSet(Account.AccountRole.ADMIN, Account.AccountRole.USER))
                .build();

        accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", email)
                .param("password", password)
                .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }
}