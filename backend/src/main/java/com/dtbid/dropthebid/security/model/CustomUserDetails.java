package com.dtbid.dropthebid.security.model;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
  
  private Long id;
  private String username;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;
  
  public CustomUserDetails(
      Long id, String username, String password,
      Collection<? extends GrantedAuthority> authorities) {

    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
