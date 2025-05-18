package kho_cang.api.repository.system;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kho_cang.api.entiy.system.SysRole;


@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {

}
