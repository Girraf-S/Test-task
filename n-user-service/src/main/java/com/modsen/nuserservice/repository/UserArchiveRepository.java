package com.modsen.nuserservice.repository;

import com.modsen.nuserservice.entity.UserArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserArchiveRepository extends JpaRepository<UserArchive, Long> {
}
