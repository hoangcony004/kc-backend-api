package kho_cang.api.controller.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kho_cang.api.core.gencode.ApiResponse;
import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.service.user.UserService;


@RestController
@RequestMapping("/api/private/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody SysUser user) {
        SysUser createdUser = userService.createUser(user);
        ApiResponse response = new ApiResponse(ApiResponse.Status.SUCCESS, "User created successfully", 201, createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SysUser>>> getAllUsers() {
    
        List<SysUser> users = userService.getAllUsers();
    
        ApiResponse<List<SysUser>> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Users fetched successfully", 200, users);
        return ResponseEntity.ok(response);
    }
    
    // // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SysUser>> getUserById(@PathVariable Long id) {
        Optional<SysUser> user = userService.getUserById(id);
        if (user.isPresent()) {
            ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "User found", 200, user.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.ERROR, "User not found", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // // Update user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SysUser>> updateUser(@PathVariable Long id, @RequestBody SysUser userDetails) {
        SysUser updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "User updated successfully", 200, updatedUser);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.ERROR, "User not found", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            ApiResponse<Void> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "User deleted successfully", 200, null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Void> response = new ApiResponse<>(ApiResponse.Status.ERROR, "User not found", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
