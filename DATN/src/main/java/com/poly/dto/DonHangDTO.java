package com.poly.dto;

import java.util.List;

import com.poly.entity.ChiTietDonHang;
import com.poly.entity.DonHang;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonHangDTO {
    private DonHang donHang;
    private List<ChiTietDonHang> chiTietDonHangs;

}
