package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
