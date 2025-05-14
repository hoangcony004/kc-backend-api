package kho_cang.api.repository.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        @Query("SELECT u FROM SysUser u WHERE " +
                        "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(u.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(u.unitcode) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                        "ORDER BY u.createdAt DESC")
        Page<SysUser> searchAllFields(@Param("keyword") String keyword, Pageable pageable);

}
