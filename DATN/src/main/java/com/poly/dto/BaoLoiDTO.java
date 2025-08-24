package com.poly.dto;

import java.util.List;

import com.poly.model.BaoLoi;
import com.poly.model.BaoLoiMedia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaoLoiDTO {
    private BaoLoi baoLoi;
    private List<BaoLoiMedia> dsMedia;
}
