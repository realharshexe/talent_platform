package com.user_management.service;

import com.user_management.dao.ProfileDao;
import com.user_management.dao.RoleDao;
import com.user_management.dao.UserDao;
import com.user_management.entity.Role;
import com.user_management.entity.Profiles;
import com.user_management.entity.Users;
import com.user_management.model.ProfileDto;
import com.user_management.model.UserDto;
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
    private RoleDao roleDao;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;


    @Override
    public void registerUser(UserDto userDto) {
        // 1️⃣ Check if user already exists
        if (dao.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // 2️⃣ Fetch role_id from role table
        Role role = roleDao.findByRoles(userDto.getRoles())
                .orElseThrow(() -> new RuntimeException("Invalid role: " + userDto.getRoles()));

        // 3️⃣ Hash password
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // 4️⃣ Save user with role_id
        Users user = new Users();
        user.setEmail(userDto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(role);

        dao.save(user);
    }


    public String authenticateUser(UserDto userDTO) {
        Users users = dao.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userDTO.getPassword(), users.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
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
