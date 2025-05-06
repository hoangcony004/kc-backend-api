package kho_cang.api.entiy.system;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sys_token")
public class SysToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_sys_token_user"))
    private SysUser user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String token;

    @Column(length = 20)
    private String type = "access"; // Giá trị mặc định 'access'

    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked = false; // Giá trị mặc định là false

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}
