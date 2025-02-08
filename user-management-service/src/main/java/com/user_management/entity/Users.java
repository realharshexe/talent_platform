package com.user_management.entity;

import com.user_management.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", unique = true)
    private Profiles profiles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", unique = true)
    private Role roles;

    @Column(nullable = false)
    private LocalDateTime updatedOn = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private UserStatus status;

}
