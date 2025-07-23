package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.dto.SanPhamDTO;
import com.poly.model.SanPham;
@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
	@Query("""
		    SELECT new com.poly.dto.SanPhamDTO(
		        sp,
		        MAX(gg.phanTramGiam),
		        MAX(gg.giaGiam),
		        MIN(
		            CASE
		                WHEN gg.maGiamGia IS NOT NULL THEN
		                    CASE
		                        WHEN gg.phanTramGiam IS NOT NULL THEN bt.gia * (1 - gg.phanTramGiam / 100.0)
		                        WHEN gg.giaGiam IS NOT NULL THEN bt.gia - gg.giaGiam
		                        ELSE bt.gia
		                    END
		                ELSE bt.gia
		            END
		        ),
		        SUM(CASE WHEN dh.trangThaiDH.maTTDH = 4 THEN ctdh.soLuongSP ELSE 0 END),
		        COALESCE(AVG(DISTINCT dg.soSao), 0)
		    )
		    FROM SanPham sp
		    JOIN sp.bienTheSanPham bt
		    LEFT JOIN BienThe_GiamGiaSP btgg ON btgg.bienTheSanPham = bt
		    LEFT JOIN btgg.giamGiaSP gg ON
		        gg.trangThai = :activeStatus
		        AND gg.thoiGianBatDau <= CURRENT_TIMESTAMP
		        AND gg.thoiGianKetThuc >= CURRENT_TIMESTAMP
		    LEFT JOIN ChiTietDonHang ctdh ON ctdh.bienTheSanPham = bt
		    LEFT JOIN ctdh.donHang dh
		    LEFT JOIN DanhGiaSP dg ON dg.sanPham = sp
		    WHERE (
		        :keyword IS NULL OR
		        LOWER(sp.tenKhongDau) LIKE CONCAT('%', LOWER(:keyword), '%') OR
		        LOWER(sp.moTaChung) LIKE CONCAT('%', LOWER(:keyword), '%') OR
		        LOWER(sp.loaiSanPham.tenLoaiKhongDau) LIKE CONCAT('%', LOWER(:keyword), '%')
		    )
		    AND (
		        :loaiKeyword IS NULL OR
		        LOWER(sp.loaiSanPham.tenLoaiKhongDau) LIKE CONCAT('%', LOWER(:loaiKeyword), '%')
		    )
		    AND (
		        :giamGia IS NULL OR
		        (:giamGia = true AND gg.maGiamGia IS NOT NULL)
		    )
		    GROUP BY sp
		    ORDER BY
		        CASE WHEN :sapXep = 'banchay' THEN SUM(CASE WHEN dh.trangThaiDH.maTTDH = 4 THEN ctdh.soLuongSP ELSE 0 END) END DESC,
		        CASE WHEN :sapXep = 'giathap' THEN
		            MIN(
		                CASE
		                    WHEN gg.maGiamGia IS NOT NULL AND gg.phanTramGiam IS NOT NULL THEN bt.gia * (1 - gg.phanTramGiam / 100.0)
		                    WHEN gg.maGiamGia IS NOT NULL AND gg.giaGiam IS NOT NULL THEN bt.gia - gg.giaGiam
		                    ELSE bt.gia
		                END
		            )
		        END ASC,
		        CASE WHEN :sapXep = 'giacao' THEN
		            MIN(
		                CASE
		                    WHEN gg.maGiamGia IS NOT NULL AND gg.phanTramGiam IS NOT NULL THEN bt.gia * (1 - gg.phanTramGiam / 100.0)
		                    WHEN gg.maGiamGia IS NOT NULL AND gg.giaGiam IS NOT NULL THEN bt.gia - gg.giaGiam
		                    ELSE bt.gia
		                END
		            )
		        END DESC,
		        sp.ngayTao DESC
		""")
		List<SanPhamDTO> locSanPhamDTO(
		    @Param("keyword") String keyword,
		    @Param("loaiKeyword") String loaiKeyword,
		    @Param("sapXep") String sapXep,
		    @Param("giamGia") Boolean giamGia,
		    @Param("activeStatus") String activeStatus
		);

	@Query("""
		    SELECT new com.poly.dto.SanPhamDTO(
		        sp,
		        MAX(gg.phanTramGiam),
		        MAX(gg.giaGiam),
		        MIN(
		            CASE
		                WHEN gg.maGiamGia IS NOT NULL THEN
		                    CASE
		                        WHEN gg.phanTramGiam IS NOT NULL THEN bt.gia * (1 - gg.phanTramGiam / 100.0)
		                        WHEN gg.giaGiam IS NOT NULL THEN bt.gia - gg.giaGiam
		                        ELSE bt.gia
		                    END
		                ELSE bt.gia
		            END
		        ),
		        SUM(CASE WHEN dh.trangThaiDH.maTTDH = 4 THEN ctdh.soLuongSP ELSE 0 END), COALESCE(AVG(DISTINCT dg.soSao), 0)
		    )
		    FROM SanPham sp
		    JOIN sp.bienTheSanPham bt
		    LEFT JOIN BienThe_GiamGiaSP btgg ON btgg.bienTheSanPham = bt
		    LEFT JOIN btgg.giamGiaSP gg ON
		        gg.trangThai = :activeStatus
		        AND gg.thoiGianBatDau <= CURRENT_TIMESTAMP
		        AND gg.thoiGianKetThuc >= CURRENT_TIMESTAMP
		    LEFT JOIN ChiTietDonHang ctdh ON ctdh.bienTheSanPham = bt
		    LEFT JOIN ctdh.donHang dh
		    LEFT JOIN DanhGiaSP dg ON dg.sanPham = sp
		    WHERE dh.trangThaiDH.maTTDH = 4
		    GROUP BY sp
		    HAVING SUM(ctdh.soLuongSP) > 0
		    ORDER BY SUM(ctdh.soLuongSP) DESC
		""")
		List<SanPhamDTO> laySanPhamBanChay(@Param("activeStatus") String activeStatus);
	
	 @Query("""
		        SELECT new com.poly.dto.SanPhamDTO(
		            sp,
		            MAX(gg.phanTramGiam),
		            MAX(gg.giaGiam),
		            MIN(
		                CASE
		                    WHEN gg.maGiamGia IS NOT NULL AND gg.phanTramGiam IS NOT NULL THEN bt.gia * (1 - gg.phanTramGiam / 100.0)
		                    WHEN gg.maGiamGia IS NOT NULL AND gg.giaGiam IS NOT NULL THEN bt.gia - gg.giaGiam
		                    ELSE bt.gia
		                END
		            ),
		            SUM(CASE WHEN dh.trangThaiDH.maTTDH = 4 THEN ctdh.soLuongSP ELSE 0 END), COALESCE(AVG(DISTINCT dg.soSao), 0)
		        )
		        FROM SanPham sp
		        JOIN sp.bienTheSanPham bt
		        LEFT JOIN BienThe_GiamGiaSP btgg ON btgg.bienTheSanPham = bt
		        LEFT JOIN btgg.giamGiaSP gg ON
		            gg.trangThai = :activeStatus
		            AND gg.thoiGianBatDau <= CURRENT_TIMESTAMP
		            AND gg.thoiGianKetThuc >= CURRENT_TIMESTAMP
		        LEFT JOIN ChiTietDonHang ctdh ON ctdh.bienTheSanPham = bt
		        LEFT JOIN ctdh.donHang dh
		        LEFT JOIN DanhGiaSP dg ON dg.sanPham = sp
		        WHERE sp.maSP = :id
		        GROUP BY sp
		    """)
		    SanPhamDTO findSanPhamDTOById(@Param("id") Integer id, @Param("activeStatus") String activeStatus);

}
