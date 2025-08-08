package com.poly.dto;

import java.util.List;

import com.poly.model.ChiTietDonHang;
import com.poly.model.DonHang;

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
