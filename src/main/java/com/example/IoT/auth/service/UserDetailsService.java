package com.example.IoT.auth.service;

import com.example.IoT.auth.domain.UserEntity;
import com.example.IoT.auth.repository.AuthRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = authRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .disabled(!userEntity.isEnabled())
                .build();
    }
}
