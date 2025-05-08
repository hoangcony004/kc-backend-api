package kho_cang.api.config.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        Long userId = user.getId();
        List<Object[]> menusWithPermission = sysRoleMenuRepository.findMenuByUserId(userId);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode menuArray = mapper.createArrayNode();

        if (!menusWithPermission.isEmpty()) {
            for (Object[] result : menusWithPermission) {
                SysMenu menu = (SysMenu) result[0];
                String permissionType = (String) result[1];
                String link = result.length > 2 ? (String) result[2] : "";
                String icon = result.length > 3 ? (String) result[3] : "";
                String label = result.length > 4 ? (String) result[4] : "";
                Long parent = (result.length > 5 && result[5] != null) ? (Long) result[5] : null;

                ObjectNode menuNode = mapper.createObjectNode();
                menuNode.put("id", menu.getId());
                menuNode.put("label", label);
                menuNode.put("link", link);
                menuNode.put("icon", icon);
                menuNode.put("permission", permissionType);
                if (parent != null) {
                    menuNode.put("parent", parent);
                } else {
                    menuNode.putNull("parent");
                }

                menuArray.add(menuNode);
            }
        }

        try {
            menuUser = mapper.writeValueAsString(menuArray);
        } catch (JsonProcessingException e) {
            System.err.println("Error while converting menuArray to JSON: " + e.getMessage());
        }
        // Tạo token với username và roles
        String token = jwtService.generateToken(user.getUsername(), roles);
        TokenResponse tokenData = new TokenResponse(token, menuUser);

        // Trả về kết quả thành công
        ApiResponse<TokenResponse> response = new ApiResponse<>(
                ApiResponse.Status.SUCCESS,
                "Đăng nhập thành công",
                200,
                tokenData);
        return ResponseEntity.ok(response);
    }

}
