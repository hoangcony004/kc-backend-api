package kho_cang.api.config.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kho_cang.api.core.gencode.ApiResponse;
import kho_cang.api.core.gencode.TokenResponse;
import kho_cang.api.entiy.dto.LoginRequest;
import kho_cang.api.entiy.system.SysMenu;
import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.repository.system.SysRoleMenuRepository;
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
    private SysRoleMenuRepository sysRoleMenuRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String rawPassword = loginRequest.getPassword();
        String menuUser = "";

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

        if (user != null) {
            Long userId = user.getId();
            List<Object[]> menusWithPermission = sysRoleMenuRepository.findMenusWithPermissionByUserId(userId);

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode menuArray = mapper.createArrayNode();

            if (!menusWithPermission.isEmpty()) {
                for (Object[] result : menusWithPermission) {
                    SysMenu menu = (SysMenu) result[0];
                    String permissionType = (String) result[1];
                    String link = result.length > 2 ? (String) result[2] : "";
                    String icon = result.length > 3 ? (String) result[3] : "";
                    String label = result.length > 4 ? (String) result[4] : "";

                    ObjectNode menuNode = mapper.createObjectNode();
                    menuNode.put("id", menu.getId());
                    menuNode.put("label", label);
                    menuNode.put("link", link);
                    menuNode.put("icon", icon);
                    menuNode.put("permission", permissionType);

                    menuArray.add(menuNode); // Thêm từng menu vào mảng
                }
            } 

            try {
                menuUser = mapper.writeValueAsString(menuArray);
                System.out.println("menuUser: " + menuUser); // In ra menuUser để kiểm tra
            } catch (JsonProcessingException e) {
                System.err.println("Error while converting menuArray to JSON: " + e.getMessage());
            }            
        }

        // Tạo token với username và roles
        String token = jwtService.generateToken(user.getUsername(), roles);
        TokenResponse tokenData = new TokenResponse(token, menuUser);

        // Trả về kết quả thành công
        ApiResponse<TokenResponse> response = new ApiResponse<>(
                ApiResponse.Status.SUCCESS,
                "Đăng nhập thành công",
                200,
                tokenData
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
