package kho_cang.api.service.system;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kho_cang.api.entiy.system.SysRole;
import kho_cang.api.repository.system.SysRoleRepository;

@Service
public class SysRoleService {

    private final SysRoleRepository sysRoleRepository;

    public SysRoleService(SysRoleRepository sysRoleRepository) {
        this.sysRoleRepository = sysRoleRepository;
    }

    public List<SysRole> getAllRoles() {
        return sysRoleRepository.findAll();
    }

    public SysRole getRoleById(Long id) {
        return sysRoleRepository.findById(id).orElse(null);
    }

    public SysRole createOrUpdateRole(SysRole role) {
        if (role.getId() != null) {
            // Thử tìm role đã tồn tại trong DB
            Optional<SysRole> existingRoleOpt = sysRoleRepository.findById(role.getId());
            if (existingRoleOpt.isPresent()) {
                SysRole existingRole = existingRoleOpt.get();
                // Cập nhật các trường cần thiết, ví dụ:
                existingRole.setName(role.getName());
                existingRole.setDescription(role.getDescription());
                // ... cập nhật thêm các trường khác nếu có
                return sysRoleRepository.save(existingRole);
            } else {
                // Nếu không tìm thấy role với id đó, có thể chọn tạo mới hoặc ném lỗi
                // Ở đây mình chọn ném lỗi
                throw new RuntimeException("Role với id " + role.getId() + " không tồn tại.");
            }
        } else {
            // Insert mới
            return sysRoleRepository.save(role);
        }
    }

    public void deleteRole(Long id) {
        sysRoleRepository.deleteById(id);
    }

}
