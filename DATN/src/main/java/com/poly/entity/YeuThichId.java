package com.poly.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YeuThichId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer sanPham;
    private Integer taiKhoan;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YeuThichId)) return false;
        YeuThichId that = (YeuThichId) o;
        return Objects.equals(sanPham, that.sanPham) && Objects.equals(taiKhoan, that.taiKhoan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sanPham, taiKhoan);
    }
}
