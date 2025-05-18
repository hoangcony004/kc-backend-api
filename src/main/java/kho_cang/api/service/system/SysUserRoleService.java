package kho_cang.api.service.system;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kho_cang.api.entiy.system.SysUserRole;
import kho_cang.api.repository.system.SysUserRoleRepository;

@Service
public class SysUserRoleService {

    private final SysUserRoleRepository sysUserRoleRepository;

    public SysUserRoleService(SysUserRoleRepository sysUserRoleRepository) {
        this.sysUserRoleRepository = sysUserRoleRepository;
    }

    public SysUserRole createOrUpdateUserRole(SysUserRole newUserRole) {
        Long userId = newUserRole.getUser().getId();

        // Tìm record hiện tại của user
        Optional<SysUserRole> existing = sysUserRoleRepository.findByUser_Id(userId);

        if (existing.isPresent()) {
            SysUserRole current = existing.get();
            current.setRole(newUserRole.getRole()); // cập nhật role
            current.setCreatedAt(LocalDateTime.now()); // cập nhật thời gian nếu muốn
            return sysUserRoleRepository.save(current);
        } else {
            newUserRole.setCreatedAt(LocalDateTime.now());
            return sysUserRoleRepository.save(newUserRole); // insert mới
        }
    }

    // Example method to get a user role by ID
    public SysUserRole getUserRoleById(Long id) {
        // Implement the logic to retrieve a user role by ID
        return new SysUserRole();
    }

    // get all user roles
    public List<SysUserRole> getAllUserRoles() {
        return sysUserRoleRepository.findAll();
    }

}
