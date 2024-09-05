CREATE DATABASE IF NOT EXISTS QuanLyGiaoTrinh;
USE QuanLyGiaoTrinh;

-- Tạo bảng taikhoan
CREATE TABLE taikhoan (
  nhanvien_id int not null PRIMARY KEY,
  username VARCHAR(50) NOT NULL unique,
  password VARCHAR(50) NOT NULL
);

-- Tạo bảng quyen
CREATE TABLE quyen (
  id int auto_increment not null primary key,
  nhanvien_id int not null,
  username VARCHAR(50) NOT NULL,
  ten VARCHAR(50) NOT NULL,
  UNIQUE KEY `authorities_idx_1` (username, ten)
);

-- Tạo bảng nhanvien
CREATE TABLE nhanvien (
  id INT AUTO_INCREMENT PRIMARY KEY,
  mssv VARCHAR(50) UNIQUE NOT NULL,
  ten VARCHAR(50) NOT NULL,
  sdt VARCHAR(15) NOT NULL
);

-- Tạo bảng nhacungcap (nhà cung cấp)
CREATE TABLE nhacungcap (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten VARCHAR(50) NOT NULL,
  diachi VARCHAR(300),
  phuongxa varchar(100),
  quanhuyen varchar(100),
  tinhthanh varchar(100),
  sdt VARCHAR(15),
  email VARCHAR(50)
);

create table chuyennganh(
	id int auto_increment not null primary key,
    tenchuyennganh varchar(50) not null unique
);

-- Tạo bảng giaotrinh
CREATE TABLE giaotrinh (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten VARCHAR(50) NOT NULL,
  chuyennganh_id int,
  tacgia VARCHAR(50),
  nhaxuatban VARCHAR(50),
  namxuatban INT,
  ngaytao date,
  gia double NOT NULL,
  soluong int not null,
  hinhanh varchar(500)
);


-- Tạo bảng lop
CREATE TABLE lop (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ten VARCHAR(50) NOT NULL,
  monhoc VARCHAR(50),
  giangvien VARCHAR(50),
  phonghoc VARCHAR(50),
  thoigianhoc VARCHAR(50),
  namhoc VARCHAR(50)

);

-- Tạo bảng phieunhap
CREATE TABLE phieunhap (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ngay DATE NOT NULL,
  tongtien double NOT NULL,
  nhanvien_id INT,
  nhacungcap_id INT,
  tinhtrang varchar(100) default 'Chưa duyệt'
);

-- Tạo bảng chitietphieunhap
CREATE TABLE chitietphieunhap (
  id int auto_increment primary key,
  soluong INT NOT NULL,
  dongia double NOT NULL,
  phieunhap_id INT,
  giaotrinh_id INT
);

-- Tạo bảng phieuxuat
CREATE TABLE phieuxuat (
  id INT AUTO_INCREMENT PRIMARY KEY,
  ngay DATE NOT NULL,
  tongtien double NOT NULL,
  trangthai bool default false,
  nhanvien_id INT,
  lop_id INT,
  tennguoinhan varchar(200),
  sdtnguoinhan varchar(20)
);

-- Tạo bảng chitietphieuxuat
CREATE TABLE chitietphieuxuat (
  id int auto_increment primary key,
  soluong INT NOT NULL,
  dongia double NOT NULL,
  phieuxuat_id INT,
  giaotrinh_id INT
);


-- Thiết lập khóa ngoại cho các bảng
ALTER TABLE quyen ADD FOREIGN KEY (nhanvien_id) REFERENCES taikhoan(nhanvien_id) on delete cascade;

-- Khóa ngoại cho bảng nhanvien: taikhoan_id tham chiếu đến taikhoan.username
ALTER TABLE taikhoan ADD FOREIGN KEY (nhanvien_id) REFERENCES nhanvien(id) on delete cascade;


-- Khóa ngoại cho bảng giaotrinh: loai_id tham chiếu đến loaigiaotrinh.id
alter table giaotrinh add constraint FK_giaotrinh_chuyennganh_id foreign key(chuyennganh_id) references chuyennganh(id);

-- Khóa ngoại cho bảng phieunhap: nhanvien_id tham chiếu đến nhanvien.id, nhacungcap_id tham chiếu đến nhacungcap.id
ALTER TABLE phieunhap ADD FOREIGN KEY (nhanvien_id) REFERENCES nhanvien(id) on delete set null;
ALTER TABLE phieunhap ADD FOREIGN KEY (nhacungcap_id) REFERENCES nhacungcap(id) on delete set null;

-- Khóa ngoại cho bảng chitietphieunhap: phieunhap_id tham chiếu đến phieunhap.id, giaotrinh_id tham chiếu đến giaotrinh.id
ALTER TABLE chitietphieunhap ADD FOREIGN KEY (phieunhap_id) REFERENCES phieunhap(id) on delete set null;
ALTER TABLE chitietphieunhap ADD FOREIGN KEY (giaotrinh_id) REFERENCES giaotrinh(id) on delete set null;

-- Khóa ngoại cho bảng phieuxuat: nhanvien_id tham chiếu đến nhanvien.id, lop_id tham chiếu đến lop.id
ALTER TABLE phieuxuat ADD FOREIGN KEY (nhanvien_id) REFERENCES nhanvien(id) on delete set null;
ALTER TABLE phieuxuat ADD FOREIGN KEY (lop_id) REFERENCES lop(id) on delete set null;

-- Khóa ngoại cho bảng chitietphieuxuat: phieuxuat_id tham chiếu đến phieuxuat.id, giaotrinh_id tham chiếu đến giaotrinh.id
ALTER TABLE chitietphieuxuat ADD FOREIGN KEY (phieuxuat_id) REFERENCES phieuxuat(id) on delete set null;
ALTER TABLE chitietphieuxuat ADD FOREIGN KEY (giaotrinh_id) REFERENCES giaotrinh(id) on delete set null;


-- procedure cap nhat so luong trinh khi mot phieu lien quan duoc xuat
delimiter $$
create trigger quanlygiaotrinh.update_count_book_from_phieu_xuat
after update on quanlygiaotrinh.phieuxuat
for each row
begin
	-- xu ly --
	if new.trangthai != old.trangthai and new.trangthai = true
    then
		call update_count_book_xuat(new.id, 1=1);
	else
		call update_count_book_xuat(new.id, 1=2);
    end if;
end$$
delimiter ;

delimiter $$
create procedure update_count_book_xuat(
	in id_px int,
    in status bool
)
begin
	-- Khai bao
	declare soluong_ton int;
    declare soluong_ct int;
    declare giaotrinh_id_ct int;
    declare done bool default false;
	declare ctpx_cur cursor
    for select soluong, giaotrinh_id from quanlygiaotrinh.chitietphieuxuat
    where phieuxuat_id = id_px;
    declare continue handler for not found set done = true;
    -- Xu ly
    open ctpx_cur;
    loop_start : loop
		fetch ctpx_cur into soluong_ct, giaotrinh_id_ct;
		if done then
			-- Out loop
            leave loop_start;
        end if;

        -- Cap nhat so luong giao trinh
        if status = true
        then
			update quanlygiaotrinh.giaotrinh
			set soluong = soluong - soluong_ct
			where id = giaotrinh_id_ct;
		else
			update quanlygiaotrinh.giaotrinh
			set soluong = soluong + soluong_ct
			where id = giaotrinh_id_ct;
        end if;
    end loop loop_start;
    close ctpx_cur;
end$$
delimiter ;

delimiter $$
create trigger quanlygiaotrinh.update_count_book_from_phieu_xuat
after update on quanlygiaotrinh.phieuxuat
for each row
begin
	-- xu ly --
	if new.trangthai != old.trangthai and new.trangthai = true
    then
		call update_count_book_xuat(new.id, 1=1);
	else
		call update_count_book_xuat(new.id, 1=2);
    end if;
end$$
delimiter ;

INSERT INTO nhanvien (mssv, ten, sdt)
VALUES
('2001200218', 'admin', '0123456789'),
('2001207307', 'Nguyễn Hữu Nam', '0987654321'),
('2001207182', 'Nguyễn Tuấn Anh', '0912345678'),
('2001200219', 'Phạm Thị Diễm', '0908765432'),
('2001200220', 'Nguyễn Thị Ngọc', '0934567890');


INSERT INTO taikhoan (nhanvien_id, username, password)
VALUES
(1, 'nguyenvana', '123'),
(2, 'tranb', '123'),
(3, 'levanc', '123'),
(4, 'phamd', '123'),
(5, 'dovane', '123');

INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (7, 1, 'nguyenvana', 'ADMIN');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (8, 1, 'nguyenvana', 'USER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (9, 2, 'tranb', 'USER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (10, 3, 'levanc', 'USER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (11, 4, 'phamd', 'USER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (12, 5, 'dovane', 'USER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (13, 1, 'nguyenvana', 'MANAGER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (14, 5, 'dovane', 'STUDENT');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (15, 4, 'phamd', 'STUDENT');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (16, 3, 'levanc', 'STUDENT');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (17, 2, 'tranb', 'MANAGER');
INSERT INTO quanlygiaotrinh.quyen (id, nhanvien_id, username, ten) VALUES (20, 1, 'nguyenvana', 'STUDENT');


-- Tạo 5 bản ghi cho bảng nhacungcap
INSERT INTO nhacungcap (ten, diachi, phuongxa, quanhuyen, tinhthanh, sdt, email)
VALUES
('Công ty TNHH ABC', '14 Đinh Tiên Hoàng', 'Phường 1', 'Quận 1', 'Hồ Chí Minh', '0281234567', 'abc@gmail.com'),
('Công ty CP XYZ', '25 Lê Thánh Tôn', 'Phường 2', 'Quận 2', 'Hồ Chí Minh', '0282345678', 'xyz@gmail.com'),
('Công ty TNHH MNP', '36 Nguyễn Huệ', 'Phường 3', 'Quận 3', 'Hồ Chí Minh', '0283456789', 'mnp@gmail.com'),
('Công ty CP QRS', '47 Lý Tự Trọng', 'Phường 4', 'Quận 4', 'Hồ Chí Minh', '0284567890', 'qrs@gmail.com'),
('Công ty TNHH TUV', '58 Nguyễn Thị Minh Khai', 'Phường 5', 'Quận 5', 'Hồ Chí Minh', '0285678901', 'tuv@gmail.com');

-- Tạo 5 bản ghi cho bảng chuyennganh
INSERT INTO chuyennganh (tenchuyennganh)
VALUES
('Công nghệ phần mềm'),
('Hệ thống thông tin'),
('Phân tích dữ liệu'),
('Mạng máy tính');

INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (1, 'Công nghệ phần mềm nâng cao', 1, 'Nguyễn Văn Ánh', 'NXB Giáo Dục', 2020, '2021-01-01', 100000, 49, 'CNPM.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (2, 'Kiểm định phần mềm', 1, 'Trần Thị Bốn', 'NXB Đại Học Quốc Gia', 2020, '2021-02-01', 120000, 15, 'KDPM.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (3, 'Cơ sở dữ liệu nâng cao', 2, 'Lê Văn Châu', 'NXB Kinh Tế', 2019, '2021-03-01', 80000, 21, 'CSDLNC.png');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (4, 'Khai thác dữ liệu', 3, 'Phạm Thị Danh', 'NXB Kinh Tế', 2019, '2021-04-01', 90000, 79, 'KPDL.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (5, 'Xây dựng hạ tầng mậng', 4, 'Đỗ Văn Linh', 'NXB Tài Chính', 2018, '2021-05-01', 150000, 64, 'XDHTM.png');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (6, 'Hệ quản trị cơ sở dữ liệu', 1, 'HUIT', 'HUIT', 2020, '2023-12-01', 80000, 32, 'HQTCDSL.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (7, 'Hướng đối tượng', 1, 'Đại học Khoa học tư nhiên', 'Đại học Khoa học tư nhiên', 2021, '2023-12-06', 75000, 4, 'HuongDoiTuong.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (8, 'Lập trình ứng dụng Web', 1, 'Giảng viên', 'Không xác định', 2015, '2023-12-06', 46000, 32, 'laptrinhungdung.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (9, 'Giáo trình NoSQL', 2, 'Giảng viên', 'Không xác định', 2014, '2023-12-06', 100000, 32, 'NOSQL.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (10, 'Giáo trình kho dữ liệu OLAP', 2, 'Giảng viên', 'Trường đại học HUIT', 2022, '2023-12-06', 123000, 32, 'OLAP.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (11, 'Phân tích thiết kế hệ thống thông tin', 1, 'Sở giáo dục và đào tạo', 'Sở giáo dục và đào tạo', 2020, '2023-12-06', 80000, 32, 'Phantichthietke.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (12, 'Công nghệ .NET', 1, 'Đại học Công nghệ thông tin', 'Đại học Công nghệ thông tin', 2021, '2023-12-06', 60000, 32, 'DOTNET.png');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (13, 'Mạng máy tính', 4, 'Sở giáo dục và đào tạo', 'Sở giáo dục và đào tạo', 2023, '2023-12-06', 234000, 32, 'mangmaytinh.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (14, 'Hệ điều hành', 1, 'HUIT', 'Không xác định', 2018, '2023-12-06', 35000, 32, 'HDH.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (15, 'Toán rời rạc', 2, 'Giảng viên', 'Không xác định', 2019, '2023-12-06', 50000, 32, 'toanroirac.jpeg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (17, 'Ngôn ngữ lập trình C++ ', 1, 'Nguyễn Minh', 'NXB Giáo Dục', 2020, '2021-01-01', 100000, 10, '1.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (18, 'Lập trình căn bản', 1, 'Trần Thị Ngọc', 'NXB Đại Học Quốc Gia', 2020, '2021-02-01', 120000, 15, '6.png');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (19, 'Khoa học máy tính', 2, 'Lê Văn Tâm', 'NXB Kinh Tế', 2019, '2021-03-01', 80000, 20, 'a.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (21, 'Cấu trúc cơ sở dữ liệu và giải thuật', 4, 'Đỗ Văn Phong', 'NXB Tài Chính', 2018, '2021-05-01', 150000, 30, 'ctdlgt.jpg');
INSERT INTO quanlygiaotrinh.giaotrinh (id, ten, chuyennganh_id, tacgia, nhaxuatban, namxuatban, ngaytao, gia, soluong, hinhanh) VALUES (27, 'Cơ sở dữ liệu', 1, 'NXB Trẻ', 'NXB Trẻ', 2014, '2023-12-24', 0, 5, 'csdl.jpg');


INSERT INTO lop (ten, monhoc, giangvien, phonghoc, thoigianhoc, namhoc) VALUES
('11DHTH1', 'Toán rời rạc', 'ThS. Nguyễn Văn Tùng', 'A101', 'Thứ 2, 7h30 - 9h30', '2023 - 2024'),
('11DHTH2', 'Lập trình căn bản', 'ThS. Đinh Nguyễn Trọng Nghĩa', 'A102', 'Thứ 3, 9h30 - 11h30', '2023 - 2024'),
('11DHTH3', 'Cấu trúc dữ liệu và giải thuật', 'ThS. Nguyễn Thanh Long', 'A103', 'Thứ 4, 13h30 - 15h30', '2023 - 2024'),
('11DHTH4', 'Ngôn ngữ lập trình C++', 'ThS. Mạnh Thiên Lý', 'A104', 'Thứ 5, 15h30 - 17h30', '2023 - 2024'),
('11DHTH5', 'Hệ điều hành', 'ThS. Nguyễn Thị Thùy Trang', 'A105', 'Thứ 6, 17h30 - 19h30', '2023 - 2024'),
('11DHTH6', 'Mạng máy tính', 'ThS. Nguyễn Thị Thủy', 'A106', 'Thứ 2, 7h30 - 9h30', '2023 - 2024'),
('11DHTH7', 'Khoa học máy', 'ThS. Nguyễn Thị Thu Tâm', 'A107', 'Thứ 5, 9h30 - 11h30', '2023 - 2024'),
('11DHTH8', 'Trí tuệ nhân tạo', 'ThS. Nguyễn Văn Hải', 'A108', 'Thứ 4, 13h30 - 15h30', '2023 - 2024'),
('11DHTH9', 'Cơ sở dữ liệu', 'ThS. Bùi Công Danh', 'A109', 'Thứ 2, 15h30 - 17h30', '2023 - 2024'),
('11DHTH10', 'Nhập môn lập trình web', 'ThS. Nguyễn Thị Thu Tâm', 'A110', 'Thứ 2, 17h30 - 19h30', '2023 - 2024');


-- -- Tạo 5 bản ghi cho bảng phieunhap
-- INSERT INTO phieunhap (ngay, tongtien, nhanvien_id, nhacungcap_id)
-- VALUES
-- ('2023-11-01', 1000000, 1, 1),
-- ('2023-11-01', 1200000, 2, 2),
-- ('2023-10-01', 800000, 3, 3),
-- ('2023-10-01', 900000, 4, 4),
-- ('2023-10-01', 1500000, 5, 5);
--
-- -- Tạo 5 bản ghi cho bảng chitietphieunhap
-- INSERT INTO chitietphieunhap (soluong, dongia, phieunhap_id, giaotrinh_id)
-- VALUES
-- (10, 100000, 1, 1),
-- (15, 120000, 2, 2),
-- (20, 80000, 3, 3),
-- (25, 90000, 4, 4),
-- (30, 150000, 5, 5);
--
-- -- Tạo 5 bản ghi cho bảng phieuxuat
-- INSERT INTO phieuxuat (ngay, tongtien, trangthai, nhanvien_id, lop_id, tennguoinhan)
-- VALUES
-- ('2023-11-02', 1100000, true, 1, 1, 'Nguyễn Văn A'),
-- ('2023-11-02', 1320000, true, 2, 2, 'Trần Thị B'),
-- ('2023-10-02', 880000, true, 3, 3, 'Lê Văn C'),
-- ('2023-10-02', 990000, true, 4, 4, 'Phạm Thị D'),
-- ('2023-10-02', 1650000, true, 5, 5, 'Đỗ Văn E');
--
-- -- Tạo 5 bản ghi cho bảng chitietphieuxuat
-- INSERT INTO chitietphieuxuat (soluong, dongia, phieuxuat_id, giaotrinh_id)
-- VALUES
-- (10, 110000, 1, 1),
-- (15, 132000, 2, 2),
-- (20, 88000, 3, 3),
-- (25, 99000, 4, 4),
-- (30, 165000, 5, 5);

SET SQL_SAFE_UPDATES = 0;

alter table phieunhap
add column ghichu varchar(500);

alter table phieuxuat
add column ghichu varchar(500);


