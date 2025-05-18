package kho_cang.api.entiy.dto;

public class SysUserRoleDTO {
    private Long userId;
    private Long roleId;

    public SysUserRoleDTO() {
    }

    public SysUserRoleDTO(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
