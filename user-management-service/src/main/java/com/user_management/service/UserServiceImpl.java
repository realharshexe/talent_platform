package com.user_management.service;

import com.user_management.dao.ProfileDao;
import com.user_management.dao.UserDao;
import com.user_management.model.UserRoleEnum;
import com.user_management.entity.Profiles;
import com.user_management.entity.Users;
import com.user_management.model.ProfileDto;
import com.user_management.model.UserDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;


    @Override
    public void registerUser(UserDto userDto) {
        Users users = Users.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(UserRoleEnum.valueOf(userDto.getRole())).build();
        dao.save(users);
    }

    public String authenticateUser(UserDto userDTO) {
        Users users = dao.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userDTO.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT Token
        return jwtService.generateToken(users.getEmail());
    }

    @Override
    public ProfileDto getProfile(Long profileId) {
        Profiles profiles = profileDao.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return ProfileDto.builder()
                .id(profiles.getId())
                .bio(profiles.getBio())
                .skills(profiles.getSkills())
                .location(profiles.getLocation())
                .experience(profiles.getExperience())
                .languages(profiles.getLanguages())
                .build();
    }

    @Override
    public void updateProfile(Long profileId, ProfileDto profileDTO) {
        Profiles profiles = profileDao.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profiles.setBio(profileDTO.getBio());
        profiles.setSkills(profileDTO.getSkills());
        profiles.setLocation(profileDTO.getLocation());
        profiles.setExperience(profileDTO.getExperience());
        profiles.setLanguages(profileDTO.getLanguages());
        profileDao.save(profiles);
    }
}
