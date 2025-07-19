package com.poly.dto;


import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnhSanPhamDTO {

    private Integer maSanPham;

    // URL từ bảng AnhSanPham
    private List<String> anhSanPhamURLs;

    // URL từ bảng AnhChiTiet
    private List<String> anhChiTietURLs;

}
