package kho_cang.api.controller.system;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kho_cang.api.core.gencode.ApiResponse;
import kho_cang.api.entiy.dto.SysUserRoleDTO;
import kho_cang.api.entiy.dto.UserRoleDTO;
import kho_cang.api.entiy.system.SysRole;
import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.entiy.system.SysUserRole;
import kho_cang.api.repository.system.SysRoleRepository;
import kho_cang.api.repository.user.UserRepository;
import kho_cang.api.service.system.SysUserRoleService;

@RestController
@RequestMapping("/api/private/users-roles")
public class SysUserRoleController {
    private final SysUserRoleService sysUserRoleService;
    private final UserRepository userRepository;
    private final SysRoleRepository roleRepository;

    public SysUserRoleController(SysUserRoleService sysUserRoleService, UserRepository userRepository,
            SysRoleRepository roleRepository) {
        this.sysUserRoleService = sysUserRoleService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse> assignRole(@RequestBody SysUserRoleDTO request) {
        try {
            SysUser user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            SysRole role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại"));

            SysUserRole userRole = new SysUserRole();
            userRole.setUser(user);
            userRole.setRole(role);

            SysUserRole saved = sysUserRoleService.createOrUpdateUserRole(userRole);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ApiResponse.Status.SUCCESS, "Gán role thành công", 200, saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500));
        }
    }

    // Example method to get all user roles
    @PostMapping("/post-all")
    public ResponseEntity<ApiResponse> getAllUserRoles() {
        try {
            List<SysUserRole> userRoles = sysUserRoleService.getAllUserRoles();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ApiResponse.Status.SUCCESS, "Lấy danh sách thành công", 200,
                            userRoles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserRoleByUserId(@PathVariable Long userId) {
        try {
            UserRoleDTO userRole = sysUserRoleService.getUserRoleByUserId(userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ApiResponse.Status.SUCCESS, "Lấy user role thành công", 200, userRole));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 404));
        }
    }

}
