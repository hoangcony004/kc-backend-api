package kho_cang.api.core.gencode;

import kho_cang.api.entiy.dto.UserDTO;

public class TokenResponse {
    private String token;
    private String menu;
    private UserDTO user;

    public TokenResponse(String token, String menu, UserDTO user) {
        this.token = token;
        this.menu = menu;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
