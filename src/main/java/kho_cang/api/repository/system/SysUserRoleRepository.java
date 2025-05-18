package kho_cang.api.repository.system;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kho_cang.api.entiy.system.SysUserRole;

@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    Optional<SysUserRole> findByUser_Id(Long userId);


}