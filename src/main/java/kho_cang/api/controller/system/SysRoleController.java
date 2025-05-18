package kho_cang.api.controller.system;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kho_cang.api.core.gencode.ApiResponse;
import kho_cang.api.entiy.system.SysRole;
import kho_cang.api.service.system.SysRoleService;

@RestController
@RequestMapping("/api/private/roles")
public class SysRoleController {
    private final SysRoleService sysRoleService;

    public SysRoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @PostMapping("/post-all")
    public ResponseEntity<ApiResponse> getAllUserRoles() {
        try {
            List<SysRole> userRoles = sysRoleService.getAllRoles();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ApiResponse.Status.SUCCESS, "Lấy danh sách thành công", 200,
                            userRoles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500));
        }
    }

    // insert role
    @PostMapping("/insert-update")
    public ResponseEntity<ApiResponse> insertOrUpdateRole(@RequestBody SysRole role) {
        try {
            SysRole savedRole;
            if (role.getId() != null) {
                // Update
                savedRole = sysRoleService.createOrUpdateRole(role);
            } else {
                // Insert
                savedRole = sysRoleService.createOrUpdateRole(role);
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(ApiResponse.Status.SUCCESS, "Phân quyền thành công", 200, savedRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500));
        }
    }

}
