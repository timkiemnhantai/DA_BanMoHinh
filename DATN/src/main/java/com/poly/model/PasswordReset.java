package com.poly.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

@IdClass(PasswordResetId.class)
@Entity
@Table(name = "password_resets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordReset {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TaiKhoan", referencedColumnName = "MaTK") // Khớp với tên cột trong DB
    private TaiKhoan taiKhoan;

    @Id
    @Nationalized
    @Column(name = "token")
    private String token;

    @Column(name = "expires_at")
    private Date expiresAt;
}
