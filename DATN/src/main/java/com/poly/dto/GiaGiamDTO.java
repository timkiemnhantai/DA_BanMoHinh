package com.poly.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiaGiamDTO {
    private Integer phanTramGiam;
    private Long giaGiam;
    private BigDecimal giaSauGiam;
}
