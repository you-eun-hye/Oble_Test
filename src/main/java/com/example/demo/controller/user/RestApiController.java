package com.example.demo.controller.user;

import com.example.demo.config.error.CustomException;
import com.example.demo.config.error.ErrorCode;
import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.entity.Member;
import com.example.demo.entity.Role;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController{
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/join")
    public UUID register(@RequestBody Map<String, String> user) {
        return userRepository.save(Member.builder()
                .username(user.get("username"))
                .password(passwordEncoder.encode(user.get("password")))
                .role(Role.ROLE_USER)
                .build()).getId();
    }

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = userRepository.findByUsername(user.get("username"))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return jwtTokenProvider.createToken(member.getUsername(), member.getRole());
    }
}
