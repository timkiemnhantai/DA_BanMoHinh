package com.poly.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String email;            // thêm nếu cần xác định user qua email
    private String oldPassword;      // đổi tên cho khớp với getOldPassword()
    private String newPassword;
    private String confirmPassword;
}
