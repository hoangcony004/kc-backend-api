package kho_cang.api.repository.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kho_cang.api.entiy.system.SysRoleMenu;

public interface SysRoleMenuRepository extends JpaRepository<SysRoleMenu, Long> {

    @Query("""
                SELECT m, rm.permissionType, m.link, m.icon, m.label
                FROM SysUser u
                JOIN SysUserRole ur ON ur.user.id = u.id
                JOIN SysRole r ON ur.role.id = r.id
                JOIN SysRoleMenu rm ON rm.role.id = r.id
                JOIN SysMenu m ON m.id = rm.menu.id
                WHERE u.id = :userId
            """)
    List<Object[]> findMenusWithPermissionByUserId(@Param("userId") Long userId);

}
