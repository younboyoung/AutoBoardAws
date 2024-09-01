package com.autoboardaws.autoboard.users.repository;

import com.autoboardaws.autoboard.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {
}
