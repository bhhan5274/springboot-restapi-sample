package com.bhhan.restapi.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hbh5274@gmail.com on 2020-06-17
 * Github : http://github.com/bhhan5274
 */

@Entity
@Table(name = "ACCOUNTS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Account {

    public enum AccountRole {
        ADMIN("ADMIN"), USER("USER");

        private String role;

        AccountRole(String role){
            this.role = role;
        }

        public String getRole(){
            return "ROLE_" + role;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles = new HashSet<>();

    @Builder
    public Account(Long id, String email, String password, Collection<AccountRole> roles){
        this.id = id;
        this.password = password;
        this.email = email;
        this.roles.addAll(roles);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
