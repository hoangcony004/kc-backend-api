package kho_cang.api.core.gencode;

public class TokenResponse {
    private String token;
    private String menu;

    public TokenResponse(String token, String menu) {
        this.token = token;
        this.menu = menu;
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
}
