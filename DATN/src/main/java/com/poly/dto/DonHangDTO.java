package com.poly.dto;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    // Danh sách sản phẩm dạng Map cho JS dễ dùng
    private List<ChiTietDonHangDTO> chiTietDTOs;

    private String productsDataJson;

    public DonHangDTO(DonHang donHang, List<ChiTietDonHang> chiTietDonHangs) {
        this.donHang = donHang;
        this.chiTietDTOs = chiTietDonHangs.stream()
            .map(ChiTietDonHangDTO::new)
            .collect(Collectors.toList());

        try {
            ObjectMapper mapper = new ObjectMapper();
            this.productsDataJson = mapper.writeValueAsString(this.chiTietDTOs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.productsDataJson = "[]";
        }
    }
}
