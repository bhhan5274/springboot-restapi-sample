package com.bhhan.restapi.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Created by hbh5274@gmail.com on 2020-06-17
 * Github : http://github.com/bhhan5274
 */

@Component
@ConfigurationProperties(prefix = "my-app")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Validated
public class AppProperties {
    @NotEmpty
    private String userName = "bhhan@email.com";
    @NotEmpty
    private String userPassword = "bhhan";
    @NotEmpty
    private String clientId = "myApp";
    @NotEmpty
    private String clientSecret = "pass";
}
