package com.iyyish.ums.cloud.common.core.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * @desc: Security#UserDetails实现类
 * @date: 2022年12月12日
 */
@Data
@Builder
public class SecurityUser implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.isNull(authorities)) {
            this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
        }
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
