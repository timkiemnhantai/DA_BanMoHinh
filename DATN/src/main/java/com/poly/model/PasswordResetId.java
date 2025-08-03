package com.poly.model;

import java.io.Serializable;
import java.util.Objects;

public class PasswordResetId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer taiKhoan; // ID của TaiKhoan (chính là MaTK)
    private String token;

    public PasswordResetId() {}

    public PasswordResetId(Integer taiKhoan, String token) {
        this.taiKhoan = taiKhoan;
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordResetId)) return false;
        PasswordResetId that = (PasswordResetId) o;
        return Objects.equals(taiKhoan, that.taiKhoan) &&
                Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taiKhoan, token);
    }
}
