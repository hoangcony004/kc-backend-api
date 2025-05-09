package kho_cang.api.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kho_cang.api.entiy.system.SysToken;

@Repository
public interface SysTokenRepository extends JpaRepository<SysToken, Long> {
    boolean existsByTokenAndIsRevokedTrue(String token);
} 