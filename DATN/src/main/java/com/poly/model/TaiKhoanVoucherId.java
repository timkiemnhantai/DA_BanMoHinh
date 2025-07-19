package com.poly.model;


import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanVoucherId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer taiKhoan;
    private Integer voucher;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaiKhoanVoucherId that)) return false;
        return Objects.equals(taiKhoan, that.taiKhoan) && Objects.equals(voucher, that.voucher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taiKhoan, voucher);
    }
}

