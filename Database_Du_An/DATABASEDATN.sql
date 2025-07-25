﻿CREATE DATABASE DATN;
GO

USE DATN;
GO

-- 1. VaiTro
CREATE TABLE VaiTro (
    MaVaiTro INT PRIMARY KEY,
    TenVaiTro NVARCHAR(50)
);
GO 
-- 2. TaiKhoan
CREATE TABLE TaiKhoan (
    MaTK INT PRIMARY KEY IDENTITY(1,1),
    TenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    MatKhau VARCHAR(100) NOT NULL,
    HoTen NVARCHAR(100),
    GioiTinh BIT,
    NgaySinh DATE,
    SoDT VARCHAR(20),
    Email VARCHAR(100),
    Avatar VARCHAR(255),
    MaVaiTro INT,
    FOREIGN KEY (MaVaiTro) REFERENCES VaiTro(MaVaiTro)
);
GO
CREATE TABLE ThongBao (
    MaThongBao INT PRIMARY KEY IDENTITY(1,1),
    TieuDe NVARCHAR(200),
    NoiDung NVARCHAR(MAX),
    Url NVARCHAR(500), -- URL chuyển hướng khi nhấn thông báo
    DaDoc BIT DEFAULT 0, -- 0: chưa đọc, 1: đã đọc
    ThoiGianTao DATETIME DEFAULT GETDATE(),
    MaTK INT,
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK)
);
GO
CREATE TABLE DiaChi (
    MaDiaChi INT PRIMARY KEY IDENTITY(1,1),
    MaTK INT NOT NULL, -- khóa ngoại đến người dùng
    HoTen NVARCHAR(100), -- tên người nhận (có thể khác với chủ tài khoản)
    SoDienThoai NVARCHAR(20), -- số điện thoại người nhận
    Tinh NVARCHAR(100),
    Quan NVARCHAR(100),
    Phuong NVARCHAR(100),
    DiaChiChiTiet NVARCHAR(255),
    MacDinh BIT DEFAULT 0,
    CONSTRAINT FK_DiaChi_TaiKhoan FOREIGN KEY (MaTK)
        REFERENCES TaiKhoan(MaTK)
        ON DELETE CASCADE -- xóa tài khoản sẽ xóa luôn địa chỉ
);


GO 
-- 3. LoaiSanPham
CREATE TABLE LoaiSanPham (
    MaLoaiSanPham INT PRIMARY KEY IDENTITY(1,1),
    TenLoaiSanPham NVARCHAR(100),
    TenLoaiKhongDau NVARCHAR(100)
);

GO 
-- 4. SanPham
CREATE TABLE SanPham (
    MaSP INT PRIMARY KEY IDENTITY(1,1),
    TenSP NVARCHAR(100),
    TenKhongDau NVARCHAR(255),  -- thêm cột này ngay khi tạo bảng
    MoTaChung NVARCHAR(MAX),
    ThuongHieu NVARCHAR(100),
    MaLoaiSanPham INT,
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaLoaiSanPham) REFERENCES LoaiSanPham(MaLoaiSanPham)
);
GO 




-- 5. GiamGiaSP
CREATE TABLE GiamGiaSP (
    MaGiamGia INT PRIMARY KEY IDENTITY(1,1),
    TenGG NVARCHAR(100) NOT NULL,
    PhanTramGiam INT NULL,
    GiaGiam DECIMAL(18,2) NULL,
    ThoiGianBatDau DATETIME NOT NULL,
    ThoiGianKetThuc DATETIME NOT NULL,
    TrangThai NVARCHAR(50) NOT NULL
);
GO 
-- 6. TrangThaiKH
CREATE TABLE TrangThaiKH (
    MaTrangThaiKH INT PRIMARY KEY IDENTITY(1,1),
    TenTrangThai NVARCHAR(100)
);
GO 
-- 7. BienTheSanPham (bỏ MaGiamGia đi vì chuyển sang bảng phụ)
CREATE TABLE BienTheSanPham (
    MaCTSP INT PRIMARY KEY IDENTITY(1,1),
    MaSP INT,
    Gia DECIMAL(18,2),
    MoTaChiTiet NVARCHAR(MAX),
    SoLuongTonKho INT,
    PhienBan NVARCHAR(50),
    MaTrangThaiKH INT,
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP),
    FOREIGN KEY (MaTrangThaiKH) REFERENCES TrangThaiKH(MaTrangThaiKH)
);
GO 
CREATE TABLE BienThe_GiamGiaSP (
    MaCTSP INT NOT NULL,
    MaGiamGia INT NOT NULL,
    PRIMARY KEY (MaCTSP, MaGiamGia),
    FOREIGN KEY (MaCTSP) REFERENCES BienTheSanPham(MaCTSP),
    FOREIGN KEY (MaGiamGia) REFERENCES GiamGiaSP(MaGiamGia)
);


GO 
-- 8. AnhSanPham
CREATE TABLE AnhSanPham (
    MaAnhSanPham INT PRIMARY KEY IDENTITY(1,1),
    MaSP INT,
    URLAnhSP VARCHAR(255),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
);
GO 
-- 9. AnhChiTiet
CREATE TABLE AnhChiTiet (
    MaAnhChiTiet INT PRIMARY KEY IDENTITY(1,1),
    MaCTSP INT,
    URLAnhCT VARCHAR(255),
    FOREIGN KEY (MaCTSP) REFERENCES BienTheSanPham(MaCTSP)
);
GO 
-- 10. TrangThaiDH
CREATE TABLE TrangThaiDH (
    MaTTDH INT PRIMARY KEY IDENTITY(1,1),
    TenTTDH NVARCHAR(100)
);
GO 

CREATE TABLE Voucher (
    MaVoucher INT PRIMARY KEY IDENTITY(1,1),
    MaCode VARCHAR(20) UNIQUE NOT NULL,
    TenGiamGia NVARCHAR(255),
    PhanTramGiam INT NOT NULL CHECK (PhanTramGiam BETWEEN 0 AND 100),
    SoLuong INT NOT NULL,
    DieuKien DECIMAL(18,2) DEFAULT 0, -- Giá trị tối thiểu đơn hàng
    NgayBatDau DATE NOT NULL,
    NgayKetThuc DATE NOT NULL,
    MoTa NVARCHAR(MAX),
    TrangThai BIT DEFAULT 1 -- 1: hoạt động, 0: vô hiệu
);
GO 
-- 11. DonHang
CREATE TABLE DonHang (
    MaDH INT PRIMARY KEY IDENTITY(1,1),
    MaTK INT,
    MaTTDH INT,
 --   MaGiamGia INT NULL,
	MaVoucher INT NULL,
    DiaChiGiaoHang NVARCHAR(255),
    NgayDat DATETIME,
    NgayGiaoDuKien NVARCHAR(50),
    NgayGiaoThucTe DATETIME,
	NgayXacNhanNhanHang DATETIME NULL,
    TongTienCTT DECIMAL(18,2),
    GiamGiaThucTe DECIMAL(18,2),
    PhiVanChuyen DECIMAL(18,2),
    ThanhTien DECIMAL(18,2),
    GhiChu NVARCHAR(MAX),
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK),
    FOREIGN KEY (MaTTDH) REFERENCES TrangThaiDH(MaTTDH),
--    FOREIGN KEY (MaGiamGia) REFERENCES GiamGiaSP(MaGiamGia),
	FOREIGN KEY (MaVoucher) REFERENCES Voucher(MaVoucher)

);
GO 
-- 12. ThanhToan
CREATE TABLE ThanhToan (
    MaThanhToan INT PRIMARY KEY IDENTITY(1,1),
    MaDH INT,
    SoTien DECIMAL(18,2),
    NgayThanhToan DATETIME,
    PhuongThucTT NVARCHAR(100),
    TrangThai NVARCHAR(50),
    GhiChuTT NVARCHAR(MAX),
    FOREIGN KEY (MaDH) REFERENCES DonHang(MaDH)
);
GO 
-- 13. DanhGiaSP
CREATE TABLE DanhGiaSP (
    MaDG INT PRIMARY KEY IDENTITY(1,1),
    MaSP INT NOT NULL, -- Sản phẩm bắt buộc
    MaCTSP INT NULL,   -- Biến thể có thể NULL
    MaTK INT NOT NULL,
    SoSao INT CHECK (SoSao BETWEEN 1 AND 5), -- Ràng buộc số sao
    BinhLuan NVARCHAR(MAX),
    NgayDang DATETIME DEFAULT GETDATE(), -- Tự động lấy ngày đăng

    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP),
    FOREIGN KEY (MaCTSP) REFERENCES BienTheSanPham(MaCTSP),
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK)
);
GO 
-- 14. GioHang
CREATE TABLE GioHang (
    MaGH INT PRIMARY KEY IDENTITY(1,1),
    MaTK INT UNIQUE,
    TongTien DECIMAL(18,2),
    TrangThaiGH NVARCHAR(50),
    NgayTao DATETIME,

    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK)
);
GO 
-- 15. ChiTietGioHang
CREATE TABLE ChiTietGioHang (
    MaCTGH INT PRIMARY KEY IDENTITY(1,1),
    MaGH INT,
    MaCTSP INT,
    SoLuong INT,
    GiamGiaThucTe DECIMAL(18,2),
    GiaTienThucTe DECIMAL(18,2),
	NgayThem DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (MaGH) REFERENCES GioHang(MaGH),
    FOREIGN KEY (MaCTSP) REFERENCES BienTheSanPham(MaCTSP)
);
GO 
-- 16. ChiTietDonHang
CREATE TABLE ChiTietDonHang (
    MaCTDH INT PRIMARY KEY IDENTITY(1,1),
    MaDH INT,
    MaCTSP INT,
    SoLuongSP INT,
    DonGia DECIMAL(18,2),
    GiamGiaThucTe DECIMAL(18,2),
    ThanhTien DECIMAL(18,2),
    GhiChu NVARCHAR(MAX),
    FOREIGN KEY (MaDH) REFERENCES DonHang(MaDH),
    FOREIGN KEY (MaCTSP) REFERENCES BienTheSanPham(MaCTSP)
);
GO 
-- 17. YeuThich
CREATE TABLE YeuThich (
    MaSP INT,
    MaTK INT,
    NgayYeuThich DATETIME,
    PRIMARY KEY (MaSP, MaTK),
    FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP),
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK)
);
GO 
-- 18. Banner
CREATE TABLE Banner (
    MaBanner INT PRIMARY KEY IDENTITY(1,1),
    TieuDeBanner NVARCHAR(100),
    AnhBanner VARCHAR(255),
    Link VARCHAR(255),
    LoaiBanner NVARCHAR(100),
    NgayTao DATE,
    NgayKetThuc DATE
);
GO 
CREATE TABLE [dbo].[password_resets] (
    TaiKhoan INT NOT NULL,
    token NVARCHAR(255) NOT NULL,
    expires_at DATETIME,
    CONSTRAINT PK_password_resets PRIMARY KEY (TaiKhoan, token),
    CONSTRAINT FK_password_resets_TaiKhoan FOREIGN KEY (TaiKhoan) REFERENCES TaiKhoan(MaTK)
);
GO 
CREATE TABLE PhanHoiDanhGia (
    MaPhanHoi INT PRIMARY KEY IDENTITY,
    MaDanhGia INT FOREIGN KEY REFERENCES DanhGiaSP(MaDG),
    MaNV INT FOREIGN KEY REFERENCES TaiKhoan(MaTK),
    NoiDung NVARCHAR(MAX),
    NgayPhanHoi DATETIME DEFAULT GETDATE()
);

GO
CREATE TABLE TaiKhoan_Voucher (
    MaTK INT,
    MaVoucher INT,
    NgaySuDung DATETIME DEFAULT GETDATE(),
    TrangThaiSuDung BIT DEFAULT 1, -- 1 = đã dùng, 0 = chưa dùng
    PRIMARY KEY (MaTK, MaVoucher),
    FOREIGN KEY (MaTK) REFERENCES TaiKhoan(MaTK),
    FOREIGN KEY (MaVoucher) REFERENCES Voucher(MaVoucher)
);

GO

CREATE FUNCTION dbo.udf_ChuyenKhongDau(@str NVARCHAR(255))
RETURNS NVARCHAR(255)
AS
BEGIN
    DECLARE @result NVARCHAR(255) = @str;
    
    SET @result = LOWER(@result);

    -- Thay thế các dấu
    SET @result = REPLACE(@result, N'á', 'a');
    SET @result = REPLACE(@result, N'à', 'a');
    SET @result = REPLACE(@result, N'ả', 'a');
    SET @result = REPLACE(@result, N'ã', 'a');
    SET @result = REPLACE(@result, N'ạ', 'a');
    SET @result = REPLACE(@result, N'ă', 'a');
    SET @result = REPLACE(@result, N'ắ', 'a');
    SET @result = REPLACE(@result, N'ằ', 'a');
    SET @result = REPLACE(@result, N'ẳ', 'a');
    SET @result = REPLACE(@result, N'ẵ', 'a');
    SET @result = REPLACE(@result, N'ặ', 'a');
    SET @result = REPLACE(@result, N'â', 'a');
    SET @result = REPLACE(@result, N'ấ', 'a');
    SET @result = REPLACE(@result, N'ầ', 'a');
    SET @result = REPLACE(@result, N'ẩ', 'a');
    SET @result = REPLACE(@result, N'ẫ', 'a');
    SET @result = REPLACE(@result, N'ậ', 'a');

    SET @result = REPLACE(@result, N'é', 'e');
    SET @result = REPLACE(@result, N'è', 'e');
    SET @result = REPLACE(@result, N'ẻ', 'e');
    SET @result = REPLACE(@result, N'ẽ', 'e');
    SET @result = REPLACE(@result, N'ụ', 'u');
    SET @result = REPLACE(@result, N'ẹ', 'e');
    SET @result = REPLACE(@result, N'ê', 'e');
    SET @result = REPLACE(@result, N'ế', 'e');
    SET @result = REPLACE(@result, N'ề', 'e');
    SET @result = REPLACE(@result, N'ể', 'e');
    SET @result = REPLACE(@result, N'ễ', 'e');
    SET @result = REPLACE(@result, N'ệ', 'e');

    SET @result = REPLACE(@result, N'í', 'i');
    SET @result = REPLACE(@result, N'ì', 'i');
    SET @result = REPLACE(@result, N'ỉ', 'i');
    SET @result = REPLACE(@result, N'ĩ', 'i');
    SET @result = REPLACE(@result, N'ị', 'i');

    SET @result = REPLACE(@result, N'ó', 'o');
    SET @result = REPLACE(@result, N'ò', 'o');
    SET @result = REPLACE(@result, N'ỏ', 'o');
    SET @result = REPLACE(@result, N'õ', 'o');
    SET @result = REPLACE(@result, N'ọ', 'o');
    SET @result = REPLACE(@result, N'ô', 'o');
    SET @result = REPLACE(@result, N'ố', 'o');
    SET @result = REPLACE(@result, N'ồ', 'o');
    SET @result = REPLACE(@result, N'ổ', 'o');
    SET @result = REPLACE(@result, N'ỗ', 'o');
    SET @result = REPLACE(@result, N'ộ', 'o');
    SET @result = REPLACE(@result, N'ơ', 'o');
    SET @result = REPLACE(@result, N'ớ', 'o');
    SET @result = REPLACE(@result, N'ờ', 'o');
    SET @result = REPLACE(@result, N'ở', 'o');
    SET @result = REPLACE(@result, N'ỡ', 'o');
    SET @result = REPLACE(@result, N'ợ', 'o');

    SET @result = REPLACE(@result, N'ú', 'u');
    SET @result = REPLACE(@result, N'ù', 'u');
    SET @result = REPLACE(@result, N'ủ', 'u');
    SET @result = REPLACE(@result, N'ũ', 'u');
    SET @result = REPLACE(@result, N'ụ', 'u');
    SET @result = REPLACE(@result, N'ư', 'u');
    SET @result = REPLACE(@result, N'ứ', 'u');
    SET @result = REPLACE(@result, N'ừ', 'u');
    SET @result = REPLACE(@result, N'ử', 'u');
    SET @result = REPLACE(@result, N'ữ', 'u');
    SET @result = REPLACE(@result, N'ự', 'u');

    SET @result = REPLACE(@result, N'ý', 'y');
    SET @result = REPLACE(@result, N'ỳ', 'y');
    SET @result = REPLACE(@result, N'ỷ', 'y');
    SET @result = REPLACE(@result, N'ỹ', 'y');
    SET @result = REPLACE(@result, N'ỵ', 'y');

    SET @result = REPLACE(@result, N'đ', 'd');

    RETURN @result;
END;
GO 
-- 1. VaiTro (không có IDENTITY)
INSERT INTO VaiTro (MaVaiTro, TenVaiTro) VALUES 
(1, N'Admin'), 
(2, N'Nhân viên'), 
(3, N'Khách hàng');
GO 
-- 2. TaiKhoan (có IDENTITY nên bỏ MaTK)
INSERT INTO TaiKhoan (
    TenDangNhap, MatKhau, HoTen, GioiTinh, NgaySinh, SoDT, Email,  Avatar, MaVaiTro
) VALUES 
('admin', '123456', N'Quản trị viên', 1, '1990-01-01', '0900000001', 'admin@example.com', 'admin.jpg', 1),
('nhanvien1', '123456', N'Lê Thị L', 1, '1995-01-01', '0905400001', 'staff@example.com', 'staff.jpg', 2),
('user1', '123456', N'Nguyễn Văn A', 1, '2000-05-20', '0900000002', 'user1@example.com',  'user1.jpg', 3),
('user2', '123456', N'Trần Thị B', 0, '1995-08-12', '0900000003', 'user2@example.com',  'user2.jpg', 3),
('user3', '123456', N'Lê Văn C', 1, '1992-11-03', '0900000004', 'user3@example.com',  'user3.jpg', 3),
('user4', '123456', N'Phạm Thị D', 0, '1998-03-28', '0900000005', 'user4@example.com',  'user4.jpg', 3),
('user5', '123456', N'Hoàng Văn E', 1, '1994-09-15', '0900000006', 'user5@example.com',  'user5.jpg', 3),
('nhanvien2', '123456', N'Ngô Văn H', 1, '1990-04-10', '0900000007', 'nhanvien1@example.com',  'nhanvien1.jpg', 2),
('nhanvien3', '123456', N'Lưu Thị I', 0, '1991-07-22', '0900000008', 'nhanvien2@example.com',  'nhanvien2.jpg', 2),
('nhanvien4', '123456', N'Trịnh Văn K', 1, '1989-12-05', '0900000009', 'nhanvien3@example.com',  'nhanvien3.jpg', 2);
GO 
INSERT INTO DiaChi (MaTK, Tinh, Quan, Phuong, DiaChiChiTiet, MacDinh) VALUES
-- Admin
(1, N'TP Hồ Chí Minh', N'Quận 1', N'Phường Bến Nghé', N'123 Lê Lợi', 1),

-- Nhân viên 1
(2, N'TP Hồ Chí Minh', N'Quận 3', N'Phường 3', N'45 Nguyễn Thị Minh Khai', 1),
(2, N'TP Hồ Chí Minh', N'Quận 5', N'Phường 4', N'90 Trần Hưng Đạo', 0),

-- User 1
(3, N'Hà Nội', N'Quận Cầu Giấy', N'Phường Dịch Vọng', N'12 Trần Đăng Ninh', 1),

-- User 2
(4, N'Hà Nội', N'Quận Thanh Xuân', N'Phường Khương Đình', N'56 Nguyễn Trãi', 1),

-- User 3
(5, N'Đà Nẵng', N'Hải Châu', N'Thạch Thang', N'78 Bạch Đằng', 1),
(5, N'Đà Nẵng', N'Thanh Khê', N'Tân Chính', N'23 Điện Biên Phủ', 0),

-- User 4
(6, N'Cần Thơ', N'Ninh Kiều', N'An Khánh', N'21 Mậu Thân', 1),

-- User 5
(7, N'Hải Phòng', N'Lê Chân', N'An Biên', N'34 Tô Hiệu', 1),

-- Nhân viên 2
(8, N'TP Hồ Chí Minh', N'Quận Bình Thạnh', N'Phường 25', N'145 Phan Văn Trị', 1),

-- Nhân viên 3
(9, N'TP Hồ Chí Minh', N'Quận Tân Bình', N'Phường 2', N'67 Trường Chinh', 1),

-- Nhân viên 4
(10, N'Đồng Nai', N'Biên Hòa', N'Tân Tiến', N'100 Nguyễn Ái Quốc', 1);
GO
-- 3. LoaiSanPham (có IDENTITY)
INSERT INTO LoaiSanPham (TenLoaiSanPham) VALUES 
(N'Bandai HG'), 
(N'Bandai MG'), 
(N'Phụ kiện'),
(N'Bandai RG'),
(N'Bandai PG'),
(N'In Era+'),
(N'Moshow Toys'),
(N'Motor Nuclear'),
(N'SNAA'),
(N'Dụng cụ'),
(N'Khác');
GO 
UPDATE LoaiSanPham
SET TenLoaiKhongDau = dbo.udf_ChuyenKhongDau(TenLoaiSanPham);
GO 
-- 4. SanPham (có IDENTITY)
INSERT INTO SanPham (TenSP, MoTaChung, ThuongHieu, MaLoaiSanPham, NgayTao) VALUES 
(N'Gundam Aerial', N'Mô hình Gundam Aerial tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'Gundam Barbatos Lupus', N'Mô hình Gundam Barbatos Lupus tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'Cục chà nhám mô hình 4 mặt', N'Dụng cụ chà nhám 4 mặt dành cho mô hình', N'', 10, '2025-06-27'),
(N'Kìm Plato', N'Kìm cắt mô hình Plato chuyên dụng', N'Plato', 10, '2025-06-27'),
(N'Kìm 1 Lưỡi Nanye', N'Kìm 1 lưỡi cắt mô hình cao cấp của Nanye', N'Nanye', 10, '2025-06-27'),
(N'Nhíp', N'Nhíp gắp linh kiện mô hình', N'', 10, '2025-06-27'),
(N'Tách part DSPIAE', N'Dụng cụ tách part DSPIAE chính hãng', N'DSPIAE', 10, '2025-06-27'),
(N'Gundam Aerial Rebuild', N'Mô hình Gundam Aerial Rebuild tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'Gundam Calibarn', N'Mô hình Gundam Calibarn tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'Gundam Gusion Rebake Full City', N'Mô hình Gundam Gusion Rebake Full City tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'Gundvolva', N'Mô hình Gundvolva tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'Shin Burning Gundam', N'Mô hình Shin Burning Gundam tỉ lệ HG của Bandai', N'Bandai', 1, '2025-06-27'),
(N'00 Quan[T] Full Saber', N'Mô hình 00 Quan[T] Full Saber tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'ASW-G-XX Gundam Vidar', N'Mô hình ASW-G-XX Gundam Vidar tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'Eclipse Gundam', N'Mô hình Eclipse Gundam tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'GN-002 Gundam Dynames', N'Mô hình GN-002 Gundam Dynames tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'GN-003 Gundam Kyrios', N'Mô hình GN-003 Gundam Kyrios tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'Gundam Barbaros', N'Mô hình Gundam Barbaros tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'Unicorn Gundam Perfectibility', N'Mô hình Unicorn Gundam Perfectibility tỉ lệ MG của Bandai', N'Bandai', 2, '2025-06-27'),
(N'00 Gundam Seven Sword/G', N'Mô hình 00 Gundam Seven Sword/G tỉ lệ PG của Bandai', N'Bandai', 5, '2025-06-27'),
(N'00-Raiser', N'Mô hình 00-Raiser tỉ lệ PG của Bandai', N'Bandai', 5, '2025-06-27'),
(N'Banshee Norn Full Psycho-Frame Prototype Mobile Suit', N'Mô hình Banshee Norn Full Psycho-Frame Prototype Mobile Suit tỉ lệ PG của Bandai', N'Bandai', 5, '2025-06-27'),
(N'RX-78-2 Gundam Unleashed', N'Mô hình RX-78-2 Gundam Unleashed tỉ lệ PG của Bandai', N'Bandai', 5, '2025-06-27'),
(N'Unicorn Gundam Full Psycho-Frame Prototype Mobile Suit', N'Mô hình Unicorn Gundam Full Psycho-Frame Prototype Mobile Suit tỉ lệ PG của Bandai', N'Bandai', 5, '2025-06-27'),
(N'Destiny Gundam', N'Mô hình Destiny Gundam tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'Freedom Gundam', N'Mô hình Freedom Gundam tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'God Gundam', N'Mô hình God Gundam tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'Gundam Exia', N'Mô hình Gundam Exia tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'MS-06S Zaku II', N'Mô hình MS-06S Zaku II tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'MSM-07S Z''Gok', N'Mô hình MSM-07S Z''Gok tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'RX-78-2 Gundam', N'Mô hình RX-78-2 Gundam tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'Strike Freedoom Gundam', N'Mô hình Strike Freedoom Gundam tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'Zeta Gundam', N'Mô hình Zeta Gundam tỉ lệ RG của Bandai', N'Bandai', 4, '2025-06-27'),
(N'AURORA', N'Mô hình AURORA đến từ thương hiệu IN ERA+', N'IN ERA+', 6, '2025-06-27'),
(N'Genesis', N'Mô hình Genesis đến từ thương hiệu IN ERA+', N'IN ERA+', 6, '2025-06-27'),
(N'THUNDERBOLT', N'Mô hình THUNDERBOLT đến từ thương hiệu IN ERA+', N'IN ERA+', 6, '2025-06-27'),
(N'THUNDERBOLT 2.0', N'Mô hình THUNDERBOLT 2.0 đến từ thương hiệu IN ERA+', N'IN ERA+', 6, '2025-06-27'),
(N'Trailblazer', N'Mô hình Trailblazer đến từ thương hiệu IN ERA+', N'IN ERA+', 6, '2025-06-27'),
(N'Date Masamune', N'Mô hình Date Masamune thuộc thương hiệu Moshow Toys', N'Moshow Toys', 7, '2025-06-27'),
(N'Illustrious Class Date Masamune', N'Mô hình Illustrious Class Date Masamune thuộc thương hiệu Moshow Toys', N'Moshow Toys', 7, '2025-06-27'),
(N'Illustrious Class Takeda Shingen', N'Mô hình Illustrious Class Takeda Shingen thuộc thương hiệu Moshow Toys', N'Moshow Toys', 7, '2025-06-27'),
(N'Jingwei', N'Mô hình Jingwei thuộc thương hiệu Moshow Toys', N'Moshow Toys', 7, '2025-06-27'),
(N'Lancelot', N'Mô hình Lancelot thuộc thương hiệu Moshow Toys', N'Moshow Toys', 7, '2025-06-27'),
(N'Takeda Shingen', N'Mô hình Takeda Shingen thuộc thương hiệu Moshow Toys', N'Moshow Toys', 7, '2025-06-27'),
(N'HiRM MNP-XH02 Cao Ren', N'Mô hình HiRM MNP-XH02 Cao Ren đến từ Motor Nuclear', N'Motor Nuclear', 8, '2025-06-27'),
(N'MG HiRM MNP-XH04 NEZHA', N'Mô hình MG HiRM MNP-XH04 NEZHA đến từ Motor Nuclear', N'Motor Nuclear', 8, '2025-06-27'),
(N'MNP-XH05 Zhao Yun + Magnolia White Dragon Horse', N'Mô hình MNP-XH05 Zhao Yun + Magnolia White Dragon Horse đến từ Motor Nuclear', N'Motor Nuclear', 8, '2025-06-27'),
(N'MNP-XH06 Wei Yuan Trainee', N'Mô hình MNP-XH06 Wei Yuan Trainee đến từ Motor Nuclear', N'Motor Nuclear', 8, '2025-06-27'),
(N'MNP-XH07 Wei Yuan Trainee', N'Mô hình MNP-XH07 Wei Yuan Trainee đến từ Motor Nuclear', N'Motor Nuclear', 8, '2025-06-27'),
(N'Gods Guardian GAWAIN Special Edition ver.KK', N'Mô hình Gods Guardian GAWAIN Special Edition ver.KK thuộc thương hiệu SNAA', N'SNAA', 9, '2025-06-27'),
(N'Iron Sickle (The Round Table Knights)', N'Mô hình Iron Sickle (The Round Table Knights) thuộc thương hiệu SNAA', N'SNAA', 9, '2025-06-27'),
(N'Knights of the Round Table - Giant Axe LANCELOT', N'Mô hình Knights of the Round Table - Giant Axe LANCELOT thuộc thương hiệu SNAA', N'SNAA', 9, '2025-06-27'),
(N'YR-03 Martial Emperor', N'Mô hình YR-03 Martial Emperor thuộc thương hiệu SNAA', N'SNAA', 9, '2025-06-27'),
(N'YR-04 FIRELORD (F.P.A.A)', N'Mô hình YR-04 FIRELORD (F.P.A.A) thuộc thương hiệu SNAA', N'SNAA', 9, '2025-06-27'),
(N'Twelve''s War Little White Snake SAMAEL', N'Mô hình Twelve''s War Little White Snake SAMAEL thuộc dòng sản phẩm đặc biệt khác', N'Khác', 11, '2025-06-27'),
(N'Action Base MG RG HG', N'Phụ kiện Action Base dành cho MG, RG, HG', N'Bandai', 3, '2025-06-27'),
(N'Áo Choàng cho MNP-XH02 Caoren', N'Phụ kiện Áo Choàng cho mô hình MNP-XH02 Cao Ren', N'Motor Nuclear', 3, '2025-06-27'),
(N'Base Stage Act', N'Phụ kiện Base Stage Act cho trưng bày mô hình', N'Khác', 3, '2025-06-27'),
(N'Led Unit (for RX-0)', N'Phụ kiện Led Unit dành cho RX-0 Unicorn Gundam', N'Bandai', 3, '2025-06-27'),
(N'The Witch From Mercury Weapon Display Base', N'Phụ kiện Display Base vũ khí The Witch From Mercury', N'Bandai', 3, '2025-06-27');
GO 
UPDATE SanPham
SET TenKhongDau = dbo.udf_ChuyenKhongDau(TenSP);
GO 
INSERT INTO AnhSanPham (MaSP, URLAnhSP) VALUES 
(1,  '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial1.png'),
(2, '/AnhSanPham/MoHinh/BanDai/HG/GundamBarbatosLupus/GundamBarbatosLupus1.png'),
(3, '/AnhSanPham/DungCu/Cucchanhammohinh4mat/Cucchanhammohinh4mat1.png'),
(4, '/AnhSanPham/DungCu/KimPlato/KimPlato1.png'),
(5, '/AnhSanPham/DungCu/Kim1LuoiNanye/Kim1LuoiNanye1.png'),
(6, '/AnhSanPham/DungCu/Nhip/Nhip1.png'),
(7, '/AnhSanPham/DungCu/TachPartDSPIAE/TachPartDSPIAE1.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild1.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn1.png'),
(10, '/AnhSanPham/MoHinh/BanDai/HG/GundamGusionRebakeFullCity/GundamGusionRebakeFullCity1.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva1.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam1.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber1.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar1.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam1.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames1.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios1.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros1.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility1.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG1.png'),
(21, '/AnhSanPham/MoHinh/BanDai/PG/00-Raiser/00-Raiser1.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit1.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed1.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit1.png'),
(25, '/AnhSanPham/MoHinh/BanDai/RG/DestinyGundam/DestinyGundam1.png'),
(26, '/AnhSanPham/MoHinh/BanDai/RG/FreedomGundam/FreedomGundam1.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam1.png'),
(28, '/AnhSanPham/MoHinh/BanDai/RG/GundamExia/GundamExia1.png'),
(29, '/AnhSanPham/MoHinh/BanDai/RG/MS-06SZakuII/MS-06SZakuII1.png'),
(30, '/AnhSanPham/MoHinh/BanDai/RG/MSM-07SZGok/MSM-07SZGok1.png'),
(31, '/AnhSanPham/MoHinh/BanDai/RG/RX-78-2Gundam/RX-78-2Gundam1.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam1.png'),
(33, '/AnhSanPham/MoHinh/BanDai/RG/ZetaGundam/ZetaGundam1.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA1.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis1.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT1.png'),
(37, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT2.0/THUNDERBOLT2.01.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer1.png'),
(39, '/AnhSanPham/MoHinh/MoshowToys/DateMasamune/DateMasamune1.png'),
(40, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassDateMasamune/IllustriousClassDateMasamune1.png'),
(41, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassTakedaShingen/IllustriousClassTakedaShingen1.png'),
(42, '/AnhSanPham/MoHinh/MoshowToys/Jingwei/Jingwei1.png'),
(43, '/AnhSanPham/MoHinh/MoshowToys/Lancelot/Lancelot1.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen1.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen1.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA1.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse1.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee1.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee1.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK1.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)1.png'),
(52, '/AnhSanPham/MoHinh/SNAA/Knightsofth%20RoundTable-GiantAxeLANCELOT/Knightsofth%20RoundTable-GiantAxeLANCELOT1.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor1.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)1.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL1.png'),
(56, '/AnhSanPham/PhuKien/ActionBaseMGRGHG/ActionBaseMGRGHG1.png'),
(57, '/AnhSanPham/PhuKien/AoChoangchoMNP-XH02%20Caoren/AoChoangchoMNP-XH02%20Caoren1.png'),
(58, '/AnhSanPham/PhuKien/BaseStageAct/BaseStageAct1.png'),
(59, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)1.png'),
(60, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase1.png');
GO 
INSERT INTO TrangThaiKH (TenTrangThai) VALUES
(N'Còn hàng'),
(N'Hết hàng'),
(N'Sắp về');
GO 

INSERT INTO BienTheSanPham (MaSP, Gia, SoLuongTonKho, PhienBan, MoTaChiTiet, MaTrangThaiKH) VALUES
(1, 250000, 50, N'Phiên bản chuẩn', NULL, 1),
(2, 280000, 50, N'Phiên bản chuẩn', NULL, 1),
(3, 150000, 100, N'Phiên bản chuẩn', NULL, 1),
(4, 130000, 80, N'Phiên bản chuẩn', NULL, 1),
(5, 170000, 70, N'Phiên bản chuẩn', NULL, 1),
(6, 120000, 60, N'Phiên bản chuẩn', NULL, 1),
(7, 140000, 60, N'Phiên bản chuẩn', NULL, 1),
(8, 260000, 50, N'Phiên bản chuẩn', NULL, 1),
(9, 270000, 50, N'Phiên bản chuẩn', NULL, 1),
(10, 250000, 50, N'Phiên bản chuẩn', NULL, 1),
(11, 240000, 50, N'Phiên bản chuẩn', NULL, 1),
(12, 260000, 50, N'Phiên bản chuẩn', NULL, 1),
(13, 320000, 40, N'Phiên bản chuẩn', NULL, 1),
(14, 310000, 40, N'Phiên bản chuẩn', NULL, 1),
(15, 330000, 40, N'Phiên bản chuẩn', NULL, 1),
(16, 300000, 40, N'Phiên bản chuẩn', NULL, 1),
(17, 290000, 40, N'Phiên bản chuẩn', NULL, 1),
(18, 280000, 40, N'Phiên bản chuẩn', NULL, 1),
(19, 350000, 30, N'Phiên bản chuẩn', NULL, 1),
(20, 360000, 30, N'Phiên bản chuẩn', NULL, 1),
(21, 370000, 30, N'Phiên bản chuẩn', NULL, 1),
(22, 400000, 30, N'Phiên bản chuẩn', NULL, 1),
(23, 420000, 30, N'Phiên bản chuẩn', NULL, 1),
(24, 280000, 50, N'Phiên bản chuẩn', NULL, 1),
(25, 300000, 50, N'Phiên bản chuẩn', NULL, 1),
(26, 310000, 50, N'Phiên bản chuẩn', NULL, 1),
(27, 320000, 50, N'Phiên bản chuẩn', NULL, 1),
(28, 330000, 50, N'Phiên bản chuẩn', NULL, 1),
(29, 340000, 50, N'Phiên bản chuẩn', NULL, 1),
(30, 350000, 50, N'Phiên bản chuẩn', NULL, 1),
(31, 360000, 50, N'Phiên bản chuẩn', NULL, 1),
(32, 370000, 50, N'Phiên bản chuẩn', NULL, 1),
(33, 380000, 50, N'Phiên bản chuẩn', NULL, 1),
(34, 390000, 50, N'Phiên bản chuẩn', NULL, 1),
(35, 400000, 50, N'Phiên bản chuẩn', NULL, 1),
(36, 410000, 50, N'Phiên bản chuẩn', NULL, 1),
(37, 420000, 50, N'Phiên bản chuẩn', NULL, 1),
(38, 430000, 50, N'Phiên bản chuẩn', NULL, 1),
(39, 440000, 50, N'Phiên bản chuẩn', NULL, 1),
(40, 450000, 50, N'Phiên bản chuẩn', NULL, 1),
(41, 460000, 50, N'Phiên bản chuẩn', NULL, 1),
(42, 470000, 50, N'Phiên bản chuẩn', NULL, 1),
(43, 480000, 50, N'Phiên bản chuẩn', NULL, 1),
(44, 490000, 50, N'Phiên bản chuẩn', NULL, 1),
(45, 500000, 50, N'Phiên bản chuẩn', NULL, 1),
(46, 510000, 50, N'Phiên bản chuẩn', NULL, 1),
(47, 520000, 50, N'Phiên bản chuẩn', NULL, 1),
(48, 530000, 50, N'Phiên bản chuẩn', NULL, 1),
(49, 540000, 50, N'Phiên bản chuẩn', NULL, 1),
(50, 550000, 50, N'Phiên bản chuẩn', NULL, 1),
(51, 560000, 50, N'Phiên bản chuẩn', NULL, 1),
(52, 570000, 50, N'Phiên bản chuẩn', NULL, 1),
(53, 580000, 50, N'Phiên bản chuẩn', NULL, 1),
(54, 590000, 50, N'Phiên bản chuẩn', NULL, 1),
(55, 600000, 50, N'Phiên bản chuẩn', NULL, 1),
(56, 110000, 50, N'Phiên bản HG, RG đen', NULL, 1),
(56, 150000, 50, N'Phiên bản HG, RG clear trong suốt', NULL, 1),
(56, 170000, 50, N'Phiên bản MG clear trong suốt', NULL, 1),
(57, 620000, 50, N'Phiên bản chuẩn', NULL, 1),
(58, 630000, 50, N'Phiên bản chuẩn', NULL, 1),
(59, 640000, 50, N'Phiên bản chuẩn', NULL, 1),
(60, 650000, 50, N'Phiên bản chuẩn', NULL, 1);
GO 




INSERT INTO AnhChiTiet (MaCTSP, URLAnhCT) VALUES
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial2.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial3.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial4.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial5.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial6.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial7.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial8.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial9.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial10.png'),
(1, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerial/GundamAerial11.png'),
(2, '/AnhSanPham/MoHinh/BanDai/HG/GundamBarbatosLupus/GundamBarbatosLupus2.png'),
(2, '/AnhSanPham/MoHinh/BanDai/HG/GundamBarbatosLupus/GundamBarbatosLupus3.png'),
(3, '/AnhSanPham/DungCu/Cucchanhammohinh4mat/Cucchanhammohinh4mat2.png'),
(3, '/AnhSanPham/DungCu/Cucchanhammohinh4mat/Cucchanhammohinh4mat3.png'),
(3, '/AnhSanPham/DungCu/Cucchanhammohinh4mat/Cucchanhammohinh4mat4.png'),
(3, '/AnhSanPham/DungCu/Cucchanhammohinh4mat/Cucchanhammohinh4mat5.png'),
(3, '/AnhSanPham/DungCu/Cucchanhammohinh4mat/Cucchanhammohinh4mat6.png'),
(4, '/AnhSanPham/DungCu/KimPlato/KimPlato2.png'),
(5, '/AnhSanPham/DungCu/Kim1LuoiNanye/Kim1LuoiNanye2.png'),
(5, '/AnhSanPham/DungCu/Kim1LuoiNanye/Kim1LuoiNanye3.png'),
(5, '/AnhSanPham/DungCu/Kim1LuoiNanye/Kim1LuoiNanye4.png'),
(6, '/AnhSanPham/DungCu/Nhip/Nhip2.png'),
(7, '/AnhSanPham/DungCu/TachPartDSPIAE/TachPartDSPIAE2.png'),
(7, '/AnhSanPham/DungCu/TachPartDSPIAE/TachPartDSPIAE3.png'),
(7, '/AnhSanPham/DungCu/TachPartDSPIAE/TachPartDSPIAE4.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild2.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild3.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild4.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild5.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild6.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild7.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild8.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild9.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild10.png'),
(8, '/AnhSanPham/MoHinh/BanDai/HG/GundamAerialRebuild/GundamAerialRebuild11.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn2.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn3.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn4.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn5.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn6.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn7.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn8.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn9.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn10.png'),
(9, '/AnhSanPham/MoHinh/BanDai/HG/GundamCalibarn/GundamCalibarn11.png'),
(10, '/AnhSanPham/MoHinh/BanDai/HG/GundamGusionRebakeFullCity/GundamGusionRebakeFullCity2.png'),
(10, '/AnhSanPham/MoHinh/BanDai/HG/GundamGusionRebakeFullCity/GundamGusionRebakeFullCity3.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva2.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva3.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva4.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva5.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva6.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva7.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva8.png'),
(11, '/AnhSanPham/MoHinh/BanDai/HG/Gundvolva/Gundvolva9.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam2.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam3.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam4.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam5.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam6.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam7.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam8.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam9.png'),
(12, '/AnhSanPham/MoHinh/BanDai/HG/ShinBurningGundam/ShinBurningGundam10.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber2.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber3.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber4.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber5.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber6.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber7.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber8.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber9.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber10.png'),
(13, '/AnhSanPham/MoHinh/BanDai/MG/00-QuanTFullSaber/00-QuanTFullSaber11.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar2.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar3.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar4.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar5.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar6.png'),
(14, '/AnhSanPham/MoHinh/BanDai/MG/ASW-G-XXGundamVidar/ASW-G-XXGundamVidar7.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam2.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam3.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam4.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam5.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam6.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam7.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam8.png'),
(15, '/AnhSanPham/MoHinh/BanDai/MG/EclipseGundam/EclipseGundam9.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames2.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames3.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames4.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames5.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames6.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames7.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames8.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames9.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames10.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames11.png'),
(16, '/AnhSanPham/MoHinh/BanDai/MG/GN-002GundamDynames/GN-002GundamDynames12.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios2.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios3.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios4.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios5.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios6.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios7.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios8.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios9.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios10.png'),
(17, '/AnhSanPham/MoHinh/BanDai/MG/GN-003GundamKyrios/GN-003GundamKyrios11.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros2.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros3.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros4.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros5.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros6.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros7.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros8.png'),
(18, '/AnhSanPham/MoHinh/BanDai/MG/GundamBarbaros/GundamBarbaros9.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility2.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility3.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility4.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility5.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility6.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility7.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility8.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility9.png'),
(19, '/AnhSanPham/MoHinh/BanDai/MG/UnicornGundamPerfectibility/UnicornGundamPerfectibility10.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG2.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG3.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG4.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG5.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG6.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG7.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG8.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG9.png'),
(20, '/AnhSanPham/MoHinh/BanDai/PG/00GundamSevenSwordG/00GundamSevenSwordG10.png'),
(21, '/AnhSanPham/MoHinh/BanDai/PG/00-Raiser/00-Raiser2.png'),
(21, '/AnhSanPham/MoHinh/BanDai/PG/00-Raiser/00-Raiser3.png'),
(21, '/AnhSanPham/MoHinh/BanDai/PG/00-Raiser/00-Raiser4.png'),
(21, '/AnhSanPham/MoHinh/BanDai/PG/00-Raiser/00-Raiser5.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit2.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit3.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit4.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit5.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit6.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit7.png'),
(22, '/AnhSanPham/MoHinh/BanDai/PG/BansheeNornFullPsycho-FramePrototyeMobileSuit/BansheeNornFullPsycho-FramePrototyeMobileSuit8.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed2.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed3.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed4.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed5.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed6.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed7.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed8.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed9.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed10.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed11.png'),
(23, '/AnhSanPham/MoHinh/BanDai/PG/RX-78-2GundamUnleashed/RX-78-2GundamUnleashed12.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit2.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit3.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit4.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit5.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit6.png'),
(24, '/AnhSanPham/MoHinh/BanDai/PG/UnicornGundamFullPsycho-FramePrototyeMobileSuit/UnicornGundamFullPsycho-FramePrototyeMobileSuit7.png'),
(25, '/AnhSanPham/MoHinh/BanDai/RG/DestinyGundam/DestinyGundam2.png'),
(25, '/AnhSanPham/MoHinh/BanDai/RG/DestinyGundam/DestinyGundam3.png'),
(26, '/AnhSanPham/MoHinh/BanDai/RG/FreedomGundam/FreedomGundam2.png'),
(26, '/AnhSanPham/MoHinh/BanDai/RG/FreedomGundam/FreedomGundam3.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam2.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam3.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam4.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam5.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam6.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam7.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam8.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam9.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam10.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam11.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam12.png'),
(27, '/AnhSanPham/MoHinh/BanDai/RG/GodGundam/GodGundam13.png'),
(28, '/AnhSanPham/MoHinh/BanDai/RG/GundamExia/GundamExia2.png'),
(28, '/AnhSanPham/MoHinh/BanDai/RG/GundamExia/GundamExia3.png'),
(28, '/AnhSanPham/MoHinh/BanDai/RG/GundamExia/GundamExia4.png'),
(28, '/AnhSanPham/MoHinh/BanDai/RG/GundamExia/GundamExia5.png'),
(29, '/AnhSanPham/MoHinh/BanDai/RG/MS-06SZakuII/MS-06SZakuII2.png'),
(29, '/AnhSanPham/MoHinh/BanDai/RG/MS-06SZakuII/MS-06SZakuII3.png'),
(30, '/AnhSanPham/MoHinh/BanDai/RG/MSM-07SZGok/MSM-07SZGok2.png'),
(30, '/AnhSanPham/MoHinh/BanDai/RG/MSM-07SZGok/MSM-07SZGok3.png'),
(31, '/AnhSanPham/MoHinh/BanDai/RG/RX-78-2Gundam/RX-78-2Gundam2.png'),
(31, '/AnhSanPham/MoHinh/BanDai/RG/RX-78-2Gundam/RX-78-2Gundam3.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam2.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam3.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam4.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam5.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam6.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam7.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam8.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam9.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam10.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam11.png'),
(32, '/AnhSanPham/MoHinh/BanDai/RG/StrikeFreedoomGundam/StrikeFreedoomGundam12.png'),
(33, '/AnhSanPham/MoHinh/BanDai/RG/ZetaGundam/ZetaGundam2.png'),
(33, '/AnhSanPham/MoHinh/BanDai/RG/ZetaGundam/ZetaGundam3.png'),
(33, '/AnhSanPham/MoHinh/BanDai/RG/ZetaGundam/ZetaGundam4.png'),
(33, '/AnhSanPham/MoHinh/BanDai/RG/ZetaGundam/ZetaGundam5.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA2.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA3.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA4.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA5.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA6.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA7.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA8.png'),
(34, '/AnhSanPham/MoHinh/INERA+/AURORA/AURORA9.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis2.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis3.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis4.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis5.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis6.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis7.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis8.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis9.png'),
(35, '/AnhSanPham/MoHinh/INERA+/Genesis/Genesis10.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT2.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT3.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT4.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT5.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT6.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT7.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT8.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT9.png'),
(36, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT/THUNDERBOLT10.png'),
(37, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT2.0/THUNDERBOLT2.02.png'),
(37, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT2.0/THUNDERBOLT2.03.png'),
(37, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT2.0/THUNDERBOLT2.04.png'),
(37, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT2.0/THUNDERBOLT2.05.png'),
(37, '/AnhSanPham/MoHinh/INERA+/THUNDERBOLT2.0/THUNDERBOLT2.06.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer2.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer3.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer4.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer5.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer6.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer7.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer8.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer9.png'),
(38, '/AnhSanPham/MoHinh/INERA+/Trailblazer/Trailblazer10.png'),
(39, '/AnhSanPham/MoHinh/MoshowToys/DateMasamune/DateMasamune2.png'),
(39, '/AnhSanPham/MoHinh/MoshowToys/DateMasamune/DateMasamune3.png'),
(39, '/AnhSanPham/MoHinh/MoshowToys/DateMasamune/DateMasamune4.png'),
(39, '/AnhSanPham/MoHinh/MoshowToys/DateMasamune/DateMasamune5.png'),
(39, '/AnhSanPham/MoHinh/MoshowToys/DateMasamune/DateMasamune6.png'),
(40, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassDateMasamune/IllustriousClassDateMasamune2.png'),
(40, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassDateMasamune/IllustriousClassDateMasamune3.png'),
(40, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassDateMasamune/IllustriousClassDateMasamune4.png'),
(40, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassDateMasamune/IllustriousClassDateMasamune5.png'),
(41, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassTakedaShingen/IllustriousClassTakedaShingen2.png'),
(41, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassTakedaShingen/IllustriousClassTakedaShingen3.png'),
(41, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassTakedaShingen/IllustriousClassTakedaShingen4.png'),
(41, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassTakedaShingen/IllustriousClassTakedaShingen5.png'),
(41, '/AnhSanPham/MoHinh/MoshowToys/IllustriousClassTakedaShingen/IllustriousClassTakedaShingen6.png'),
(42, '/AnhSanPham/MoHinh/MoshowToys/Jingwei/Jingwei2.png'),
(42, '/AnhSanPham/MoHinh/MoshowToys/Jingwei/Jingwei3.png'),
(42, '/AnhSanPham/MoHinh/MoshowToys/Jingwei/Jingwei4.png'),
(42, '/AnhSanPham/MoHinh/MoshowToys/Jingwei/Jingwei5.png'),
(42, '/AnhSanPham/MoHinh/MoshowToys/Jingwei/Jingwei6.png'),
(43, '/AnhSanPham/MoHinh/MoshowToys/Lancelot/Lancelot2.png'),
(43, '/AnhSanPham/MoHinh/MoshowToys/Lancelot/Lancelot3.png'),
(43, '/AnhSanPham/MoHinh/MoshowToys/Lancelot/Lancelot4.png'),
(43, '/AnhSanPham/MoHinh/MoshowToys/Lancelot/Lancelot5.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen2.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen3.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen4.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen5.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen6.png'),
(44, '/AnhSanPham/MoHinh/MoshowToys/TakedaShingen/TakedaShingen7.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen2.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen3.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen4.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen5.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen6.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen7.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen8.png'),
(45, '/AnhSanPham/MoHinh/MotorNuclear/HiRMMNP-XH02CaoRen/HiRMMNP-XH02CaoRen9.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA2.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA3.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA4.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA5.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA6.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA7.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA8.png'),
(46, '/AnhSanPham/MoHinh/MotorNuclear/MGHiRMMNP-XH04NEZHA/MGHiRMMNP-XH04NEZHA9.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse2.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse3.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse4.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse5.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse6.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse7.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse8.png'),
(47, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse/MNP-XH05ZhaoYun+MagnoliaWhiteDragonHorse9.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee2.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee3.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee4.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee5.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee6.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee7.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee8.png'),
(48, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH06WeiYuanTrainee/MNP-XH06WeiYuanTrainee9.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee2.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee3.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee4.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee5.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee6.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee7.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee8.png'),
(49, '/AnhSanPham/MoHinh/MotorNuclear/MNP-XH07WeiYuanTrainee/MNP-XH07WeiYuanTrainee9.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK2.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK3.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK4.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK5.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK6.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK7.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK8.png'),
(50, '/AnhSanPham/MoHinh/SNAA/GodsGuardianGAWAINSpecialEditionver.KK/GodsGuardianGAWAINSpecialEditionver.KK9.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)2.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)3.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)4.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)5.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)6.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)7.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)8.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)9.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)10.png'),
(51, '/AnhSanPham/MoHinh/SNAA/IronSickle(TheRoundTableKnights)/IronSickle(TheRoundTableKnights)11.png'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT2.jpg'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT3.jpg'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT4.jpg'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT5.jpg'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT6.jpg'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT7.jpg'),
(52, '/AnhSanPham/MoHinh/SNAA/KnightsofthRoundTable-GiantAxeLANCELOT/KnightsofthRoundTable-GiantAxeLANCELOT8.jpg'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor2.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor3.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor4.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor5.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor6.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor7.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor8.png'),
(53, '/AnhSanPham/MoHinh/SNAA/YR-03MartialEmperor/YR-03MartialEmperor9.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)2.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)3.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)4.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)5.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)6.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)7.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)8.png'),
(54, '/AnhSanPham/MoHinh/SNAA/YR-04FIRELORD(F.P.A.A)/YR-04FIRELORD(F.P.A.A)9.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL2.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL3.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL4.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL5.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL6.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL7.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL8.png'),
(55, '/AnhSanPham/MoHinh/Khac/Twelve''sWarLittleWhiteSnakeSAMAEL/Twelve''sWarLittleWhiteSnakeSAMAEL9.png'),
(56, '/AnhSanPham/PhuKien/ActionBaseMGRGHG/ActionBaseMGRGHG3.png'),
(57, '/AnhSanPham/PhuKien/ActionBaseMGRGHG/ActionBaseMGRGHG4.png'),
(58, '/AnhSanPham/PhuKien/ActionBaseMGRGHG/ActionBaseMGRGHG2.png'),
(59, '/AnhSanPham/PhuKien/AoChoangchoMNP-XH02%20Caoren/AoChoangchoMNP-XH02%20Caoren2.png'),
(59, '/AnhSanPham/PhuKien/AoChoangchoMNP-XH02%20Caoren/AoChoangchoMNP-XH02%20Caoren3.png'),
(59, '/AnhSanPham/PhuKien/AoChoangchoMNP-XH02%20Caoren/AoChoangchoMNP-XH02%20Caoren4.png'),
(59, '/AnhSanPham/PhuKien/AoChoangchoMNP-XH02%20Caoren/AoChoangchoMNP-XH02%20Caoren5.png'),
(60, '/AnhSanPham/PhuKien/BaseStageAct/BaseStageAct2.png'),
(60, '/AnhSanPham/PhuKien/BaseStageAct/BaseStageAct3.png'),
(60, '/AnhSanPham/PhuKien/BaseStageAct/BaseStageAct4.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)2.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)3.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)4.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)5.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)6.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)7.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)8.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)9.png'),
(61, '/AnhSanPham/PhuKien/LedUnit(forRX-0)/LedUnit(forRX-0)10.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase2.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase3.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase4.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase5.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase6.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase7.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase8.png'),
(62, '/AnhSanPham/PhuKien/TheWitchFromMercuryWeaponDisplayBase/TheWitchFromMercuryWeaponDisplayBase9.png');
GO 
INSERT INTO Banner (TieuDeBanner, AnhBanner, Link, LoaiBanner, NgayTao, NgayKetThuc) VALUES
(N'Khuyến mãi tháng 6', N'/AnhBanner/SummerSale.png', N'/khuyen-mai/thang-6', N'Khuyến Mãi', '2025-06-01', '2025-06-30'),
(N'Ra mắt sản phẩm mới', N'/AnhBanner/NewProduct.png', N'/san-pham/mo-hinh-moi', N'Sản Phẩm', '2025-06-15', '2025-07-15'),
(N'Flash Sale cuối tuần', N'/AnhBanner/FlashSale.png', N'/khuyen-mai/flash-sale', N'Khuyến Mãi', '2025-06-20', '2025-06-22'),
(N'Giảm giá Phụ Kiện', N'/AnhBanner/AccessoryDiscount.png', N'/phu-kien', N'Phụ Kiện', '2025-06-10', '2025-07-10'),
(N'Ưu đãi VIP', N'/AnhBanner/VIPOffer.png', N'/vip', N'Ưu Đãi', '2025-06-05', '2025-07-05');
GO 
INSERT INTO TrangThaiDH (TenTTDH) VALUES
(N'Chờ xác nhận'),
(N'Đã xác nhận'),
(N'Đang giao hàng'),
(N'Đã giao hàng'),
(N'Đã hủy'),
(N'Giao không thành công'),
(N'Hoàn trả');
GO 
INSERT INTO GiamGiaSP (TenGG, PhanTramGiam, GiaGiam, ThoiGianBatDau, ThoiGianKetThuc, TrangThai)
VALUES
(N'Giảm 10% toàn bộ đơn hàng', 10, NULL, '2025-06-01 00:00:00', '2025-08-01 23:59:59', N'Đang hoạt động'),
(N'Giảm 50.000đ cho đơn từ 300.000đ', NULL, 50000, '2025-06-15 00:00:00', '2025-07-15 23:59:59', N'Đang hoạt động'),
(N'Giảm 20% cho sản phẩm mới', 20, NULL, '2025-06-10 00:00:00', '2025-06-30 23:59:59', N'Đã kết thúc'),
(N'Flash Sale cuối tuần', NULL, 100000, '2025-06-28 00:00:00', '2025-06-30 23:59:59', N'Đã kết thúc'),
(N'Ưu đãi thành viên VIP', 15, NULL, '2025-06-01 00:00:00', '2025-12-31 23:59:59', N'Sắp diễn ra');
GO 

INSERT INTO DonHang (
    MaTK, MaTTDH, DiaChiGiaoHang, NgayDat, NgayGiaoDuKien, NgayGiaoThucTe,
    TongTienCTT, GiamGiaThucTe, PhiVanChuyen, ThanhTien, GhiChu, NgayXacNhanNhanHang
) VALUES
-- Đơn hàng 1 - Chờ xác nhận
(6, 1, N'123 Đường ABC, Q1, TP.HCM', '2025-06-27', '2025-07-01', NULL,
480000, 0, 30000, 510000, N'Khách mới, chờ xác nhận', NULL),

-- Đơn hàng 2 - Đang giao
(3, 3, N'45 Lý Tự Trọng, Q3, TP.HCM', '2025-06-25', '2025-06-28', NULL,
620000, 50000, 25000, 595000, N'Sử dụng mã giảm giá 50K', NULL),

-- Đơn hàng 3 - Đã giao
(4, 4, N'78 Hoàng Văn Thụ, Q.Tân Bình, TP.HCM', '2025-06-22', '2025-06-25', '2025-06-25',
700000, 0, 30000, 730000, N'Giao thành công', '2025-06-26'),

-- Đơn hàng 4 - Đã hủy
(5, 5, N'90 Nguyễn Trãi, Q5, TP.HCM', '2025-06-15', '2025-06-17', NULL,
400000, 20000, 30000, 410000, N'Khách huỷ do thay đổi địa chỉ', NULL),

-- Đơn hàng 5 - Đã giao
(3, 4, N'25 Nguyễn Đình Chiểu, Q.3, TP.HCM', '2025-06-10', '2025-06-13', '2025-06-13',
300000, 0, 25000, 325000, N'Đã giao, không sử dụng mã giảm', '2025-06-14'),

-- Đơn hàng 6 - Đã giao nhưng chưa xác nhận
(6, 4, N'23 Pasteur, Q1, TP.HCM', '2025-06-18', '2025-06-21', NULL,
650000, 30000, 20000, 640000, N'Giao vào buổi sáng, gọi trước khi giao', NULL),

-- Đơn hàng 7 - Đã giao
(7, 4, N'151 Hai Bà Trưng, Q3, TP.HCM', '2025-06-10', '2025-06-13', '2025-06-12',
750000, 50000, 30000, 730000, N'Khách thanh toán tiền mặt, đã nhận đủ hàng', '2025-06-13');
GO
-- ChiTietDonHang cho 5 đơn hàng tương ứng với MaDH từ 1 đến 5
INSERT INTO ChiTietDonHang (MaDH, MaCTSP, SoLuongSP, DonGia, GiamGiaThucTe, ThanhTien, GhiChu) VALUES
-- Đơn hàng 1
(1, 1, 2, 240000, 0, 480000, N'Khách mua 2 sản phẩm'),
-- Đơn hàng 2
(2, 2, 1, 350000, 50000, 300000, N'Dùng mã giảm giá'),
(2, 3, 1, 320000, 0, 320000, NULL),
-- Đơn hàng 3
(3, 4, 1, 700000, 0, 700000, NULL),
-- Đơn hàng 4
(4, 5, 2, 200000, 20000, 380000, N'Mua combo 2 sản phẩm'),
-- Đơn hàng 5
(5, 6, 1, 300000, 0, 300000, N'Sản phẩm giao sớm'),

(6, 56, 1, 110000, 0, 110000, N''),
(6, 9, 1, 270000, 0, 270000, N''),

(7, 57, 1, 110000, 0, 110000, N''),
(7, 10, 1, 250000, 0, 250000, N'');
GO 
INSERT INTO GioHang (MaTK, TongTien, TrangThaiGH, NgayTao) VALUES
(2, 450000, N'Đang chọn hàng', '2025-06-28 10:15:00'),
(3, 890000, N'Chờ thanh toán', '2025-06-28 11:00:00'),
(4, 0, N'Trống', '2025-06-28 12:30:00'),
(5, 250000, N'Đã lưu', '2025-06-28 13:45:00'),
(6, 1290000, N'Chờ thanh toán', '2025-06-28 14:10:00');
GO 
INSERT INTO ChiTietGioHang (MaGH, MaCTSP, SoLuong, GiamGiaThucTe, GiaTienThucTe, NgayThem) VALUES
-- Giỏ hàng 1
(1, 1, 2, 0, 480000, GETDATE()),         -- 2 x 240000
(1, 2, 1, 10000, 230000, GETDATE()),     -- giảm giá 10k

-- Giỏ hàng 2
(2, 3, 1, 20000, 300000, GETDATE()),
(2, 4, 2, 0, 400000, GETDATE()),         -- 2 x 200000

-- Giỏ hàng 3 (trống hoặc đã bị xóa sp)
-- (3, ...) — Không có chi tiết vì trạng thái "Trống"

-- Giỏ hàng 4
(4, 5, 1, 0, 250000, GETDATE()),         -- giá gốc

-- Giỏ hàng 5
(5, 6, 2, 10000, 580000, GETDATE());     -- 2 x 295000 - giảm 10k

GO 
INSERT INTO DanhGiaSP (MaSP, MaTK, MaCTSP , SoSao, BinhLuan, NgayDang) VALUES
(1, 2, NULL,5, N'Sản phẩm rất đẹp, chi tiết sắc nét.', '2025-06-25 10:00:00'),
(1, 3, NULL,4, N'Khá hài lòng, giao hàng nhanh.', '2025-06-26 14:30:00'),
(2, 4, NULL,5, N'Gundam cực ngầu, đúng mô tả.', '2025-06-24 08:45:00'),
(3, 5, NULL,3, N'Chất lượng tạm ổn, giá hợp lý.', '2025-06-23 18:20:00'),
(4, 6, NULL,4, N'Kìm cắt tốt, nhưng giao thiếu phụ kiện.', '2025-06-22 11:15:00'),
(5, 2, NULL,5, N'Sản phẩm cao cấp, đáng tiền.', '2025-06-20 16:50:00'),
(6, 3, NULL,2, N'Không đúng như kỳ vọng, hơi lỏng lẻo.', '2025-06-21 13:40:00'),
(7, 4, NULL,5, N'Rất chắc chắn, giao hàng nhanh.', '2025-06-20 09:00:00'),
(8, 5, NULL,4, N'Đẹp, dễ lắp ráp.', '2025-06-19 17:10:00'),
(9, 6, NULL,5, N'Mẫu mới đẹp hơn bản cũ.', '2025-06-18 12:30:00');
GO 
INSERT INTO ThanhToan (MaDH, SoTien, NgayThanhToan, PhuongThucTT, TrangThai, GhiChuTT) VALUES
(1, 510000, '2025-06-27 10:15:00', N'Thanh toán khi nhận hàng', N'Chờ xác nhận', N'Chờ xác nhận thanh toán từ shipper'),

(2, 595000, '2025-06-26 09:30:00', N'Ví điện tử MoMo', N'Đã thanh toán', N'Thanh toán thành công bằng MoMo'),

(3, 730000, '2025-06-25 11:45:00', N'Chuyển khoản ngân hàng', N'Đã thanh toán', N'Thanh toán đầy đủ qua Vietcombank'),

(4, 410000, '2025-06-16 14:00:00', N'Thanh toán khi nhận hàng', N'Đã hủy', N'Hủy đơn, không thực hiện thanh toán'),

(5, 325000, '2025-06-13 08:20:00', N'Ví ZaloPay', N'Đã thanh toán', N'Sử dụng mã giảm giá của ZaloPay'),

(6, 325000, '2025-06-13 08:20:00', N'Ví ZaloPay', N'Đã thanh toán', N'Sử dụng mã giảm giá của ZaloPay'),

(7, 325000, '2025-06-13 08:20:00', N'Ví ZaloPay', N'Đã thanh toán', N'Sử dụng mã giảm giá của ZaloPay');
GO 
INSERT INTO YeuThich (MaSP, MaTK, NgayYeuThich) VALUES
(1, 2, '2025-06-20 10:15:00'),
(2, 2, '2025-06-21 09:00:00'),
(3, 3, '2025-06-22 14:45:00'),
(5, 4, '2025-06-23 11:30:00'),
(8, 5, '2025-06-24 08:50:00'),
(10, 6, '2025-06-25 13:15:00'),
(12, 3, '2025-06-25 15:10:00'),
(15, 2, '2025-06-26 09:20:00'),
(20, 4, '2025-06-26 10:05:00'),
(25, 5, '2025-06-27 17:45:00');
GO 
INSERT INTO BienThe_GiamGiaSP (MaCTSP, MaGiamGia) VALUES
(1, 1),
(56, 3),
(2, 1),
(3, 2),
(5, 2),
(57, 3),
(8, 4),
(10, 5);
GO 
INSERT INTO PhanHoiDanhGia (MaDanhGia, MaNV, NoiDung, NgayPhanHoi) VALUES
(1, 8, N'Cảm ơn bạn đã đánh giá. Sản phẩm này đang được nhiều người yêu thích!', '2025-06-25 12:00:00'),
(2, 9, N'Chúng tôi rất vui khi bạn hài lòng. Hẹn bạn lần mua sau!', '2025-06-26 16:00:00'),
(3, 10, N'Gundam này là hàng hot, cảm ơn bạn đã tin tưởng.', '2025-06-24 10:00:00'),
(4, 8, N'Cảm ơn bạn đã góp ý. Chúng tôi sẽ cải thiện chất lượng.', '2025-06-23 20:00:00'),
(5, 9, N'Rất xin lỗi vì thiếu phụ kiện, chúng tôi sẽ liên hệ để bổ sung.', '2025-06-22 13:00:00'),
(6, 10, N'Cảm ơn đánh giá tích cực của bạn. Mong được phục vụ thêm!', '2025-06-20 18:00:00'),
(7, 8, N'Rất tiếc vì bạn chưa hài lòng. Chúng tôi sẽ kiểm tra lại sản phẩm.', '2025-06-21 15:00:00'),
(8, 9, N'Cảm ơn bạn, mong bạn có trải nghiệm lắp ráp thú vị.', '2025-06-19 19:00:00'),
(9, 10, N'Phiên bản mới này đang được ưu đãi đặc biệt, cảm ơn bạn đã ủng hộ.', '2025-06-18 14:00:00'),
(10, 8, N'Chúng tôi rất trân trọng phản hồi từ bạn. Hẹn bạn lần tới!', '2025-06-18 16:00:00');


INSERT INTO Voucher (MaCode, TenGiamGia, PhanTramGiam, SoLuong, DieuKien, NgayBatDau, NgayKetThuc, MoTa, TrangThai)
VALUES 
('SALE10', N'Giảm 10%', 10, 100, 50000, '2025-07-01', '2025-08-01', N'Dùng cho đơn hàng từ 50k', 1),
('FREESHIP', N'Freeship toàn quốc', 0, 50, 0, '2025-07-01', '2025-07-31', N'Miễn phí vận chuyển', 1),
('NEWUSER20', N'Ưu đãi người dùng mới', 20, 30, 100000, '2025-07-01', '2025-12-31', N'Chỉ dùng lần đầu', 1);
-- User 4 đã dùng mã SALE10
-- User 4 đã dùng SALE10
INSERT INTO TaiKhoan_Voucher (MaTK, MaVoucher, NgaySuDung, TrangThaiSuDung)
VALUES (4, 1, '2025-07-05 10:00:00', 1);

-- User 5 chưa dùng NEWUSER20
INSERT INTO TaiKhoan_Voucher (MaTK, MaVoucher, TrangThaiSuDung)
VALUES (5, 3, 0);

-- User 6 đã dùng FREESHIP
INSERT INTO TaiKhoan_Voucher (MaTK, MaVoucher, NgaySuDung, TrangThaiSuDung)
VALUES (6, 2, '2025-07-10 09:30:00', 1);

