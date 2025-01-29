package com.user_management.dao;

import com.user_management.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileDao extends JpaRepository<Profile, Long> {
    @Override
    Optional<Profile> findById(Long aLong);

}
