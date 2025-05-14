package kho_cang.api.controller.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kho_cang.api.core.gencode.ApiResponse;
import kho_cang.api.core.pages.PageModel;
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
    @PostMapping("/insert")
    public ResponseEntity<ApiResponse> createUser(@RequestBody SysUser user) {
        try {
            SysUser createdUser = userService.createUser(user);
            ApiResponse response = new ApiResponse(ApiResponse.Status.SUCCESS, "Thành công!", 201, createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Post all users
    @PostMapping("/search-paging")
    public ResponseEntity<ApiResponse<List<SysUser>>> postAllUsers(@RequestBody PageModel pageModel) {
        try {
            int currentPage = pageModel.getCurrentPage();
            int pageSize = pageModel.getPageSize();
            String strKey = pageModel.getStrKey();

            Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
            Page<SysUser> pageResult = userService.postUsersWithSearch_Paging(strKey, pageable);

            ApiResponse<List<SysUser>> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Thành công!", 200,
                    pageResult.getContent());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<SysUser>> response = new ApiResponse<>(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(),
                    500, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get user by ID
    @GetMapping("/getOne/{id}")
    public ResponseEntity<ApiResponse<SysUser>> getUserById(@PathVariable Long id) {
        try {
            Optional<SysUser> user = userService.getUserById(id);
            if (user.isPresent()) {
                ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Thành công!", 200,
                        user.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.ERROR,
                        "Không tìm thấy người dùng!", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Update user
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<SysUser>> updateUser(@PathVariable Long id, @RequestBody SysUser userDetails) {
        try {
            SysUser updatedUser = userService.updateUser(id, userDetails);
            if (updatedUser != null) {
                ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Thành công!", 200,
                        updatedUser);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.ERROR,
                        "Không thể cập nhật người dùng", 404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<SysUser> response = new ApiResponse<>(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Delete user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            boolean isDeleted = userService.deleteUser(id);
            if (isDeleted) {
                ApiResponse<Void> response = new ApiResponse<>(ApiResponse.Status.SUCCESS, "Thành công!", 200, null);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Void> response = new ApiResponse<>(ApiResponse.Status.ERROR, "Không thể xóa người dùng",
                        404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(ApiResponse.Status.ERROR, "Lỗi: " + e.getMessage(), 500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
