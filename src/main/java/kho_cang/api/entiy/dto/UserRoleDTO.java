package kho_cang.api.entiy.dto;

public class UserRoleDTO {
    private Long userId;
    private String username;
    private String roleName;

    public UserRoleDTO(Long userId, String username, String roleName) {
        this.userId = userId;
        this.username = username;
        this.roleName = roleName;
    }

    // Getters và setters (hoặc dùng Lombok @Data nếu có)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
