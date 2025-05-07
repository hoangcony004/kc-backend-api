package kho_cang.api.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kho_cang.api.entiy.system.SysUser;

public interface UserRepository extends JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);

    @Query("SELECT r.name FROM SysUserRole ur " +
            "JOIN ur.user u " +
            "JOIN ur.role r " +
            "WHERE u.username = :username")
    List<String> findRolesByUsername(@Param("username") String username);

}
