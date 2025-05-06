package kho_cang.api.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kho_cang.api.entiy.system.SysUser;


public interface UserRepository extends JpaRepository<SysUser, Integer> {
    Optional<SysUser> findByUsername(String username);
}
