package kho_cang.api.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kho_cang.api.entiy.system.SysToken;
import kho_cang.api.entiy.system.SysUser;
import kho_cang.api.repository.system.SysTokenRepository;
import kho_cang.api.repository.user.UserRepository;
import java.sql.Timestamp;

@Service
public class TokenBlacklistService {
    
    @Autowired
    private SysTokenRepository sysTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public void blacklistToken(String token, String username) {
        SysUser user = userRepository.findByUsername(username);
        
        SysToken sysToken = new SysToken();
        sysToken.setToken(token);
        sysToken.setIsRevoked(true);
        sysToken.setUser(user);
        sysToken.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        sysTokenRepository.save(sysToken);
    }
    
    public boolean isTokenBlacklisted(String token) {
        return sysTokenRepository.existsByTokenAndIsRevokedTrue(token);
    }
}