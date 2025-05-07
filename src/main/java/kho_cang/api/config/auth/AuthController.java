package kho_cang.api.config.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import kho_cang.api.core.gencode.ApiResponse;
import kho_cang.api.core.gencode.TokenResponse;
import kho_cang.api.entiy.dto.LoginRequest;
import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.repository.user.UserRepository;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();

        // Tìm user trong DB
        SysUser user = userRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            ApiResponse<TokenResponse> response = new ApiResponse<>(
                    ApiResponse.Status.ERROR,
                    "Sai tài khoản hoặc mật khẩu",
                    401);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Lấy danh sách vai trò từ repository
        List<String> roles = userRepository.findRolesByUsername(username);

        // Tạo token với username và roles
        String token = jwtService.generateToken(user.getUsername(), roles);
        TokenResponse tokenData = new TokenResponse(token, "Bearer", 86400); // 1 ngày

        // Trả về kết quả thành công
        ApiResponse<TokenResponse> response = new ApiResponse<>(
                ApiResponse.Status.SUCCESS,
                "Đăng nhập thành công",
                200,
                tokenData // Gửi kèm dữ liệu token
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/private/test")
    public ResponseEntity<ApiResponse<String>> testPrivate() {
        ApiResponse<String> response = new ApiResponse<>(
                ApiResponse.Status.SUCCESS,
                "Đây là API riêng tư",
                200,
                "Hello, private world!");
        return ResponseEntity.ok(response);
    }

}
