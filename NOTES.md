# 📚 Ghi Chú Học Java + JavaFX

---

## 1. Môi Trường Đã Cài

```
[✅] JDK 8u202        → Java Development Kit, bộ công cụ lập trình Java
[✅] Maven 3.9.16     → Công cụ quản lý project và thư viện
[✅] VS Code 1.126.0  → Trình soạn thảo code
[✅] Extension Pack for Java → Plugin giúp VS Code hiểu Java
```

### Kiểm Tra Môi Trường
```cmd
java -version   → kiểm tra Java đã cài chưa
mvn -version    → kiểm tra Maven đã cài chưa
code --version  → kiểm tra VS Code đã cài chưa
```

---

## 2. Java Là Gì? JVM Là Gì?

```
┌─────────────────────────────────────┐
│      Code Java bạn viết             │
│      (file .java)                   │
└──────────────┬──────────────────────┘
               │ Maven biên dịch (compile)
               ▼
┌─────────────────────────────────────┐
│         Bytecode                    │
│         (file .class)               │
│  ⚠️ Không phải code máy thật        │
│  ⚠️ Máy tính KHÔNG chạy trực tiếp  │
└──────────────┬──────────────────────┘
               │ JVM đọc và chạy
               ▼
┌─────────────────────────────────────┐
│   JVM - Java Virtual Machine        │
│   (Máy Ảo Java)                     │
│                                     │
│   → Được viết bằng C++              │
│   → Đã có sẵn khi cài JDK ✅        │
│   → Đây chính là "engine" của Java  │
└──────────────┬──────────────────────┘
               │ gọi xuống
               ▼
┌─────────────────────────────────────┐
│      Hệ điều hành                   │
│   Windows / Linux / macOS           │
│   → Tạo cửa sổ, xử lý chuột...     │
└─────────────────────────────────────┘
```

---

## 3. JavaFX Là Gì?

> **JavaFX** = thư viện vẽ giao diện đồ họa cho Java  
> JDK 8 đã có sẵn JavaFX — không cần cài thêm ✅

---

## 4. Cấu Trúc Project

```
snst-app/
│
├── src/main/java/com/snst/
│   ├── Main.java           ← Điểm khởi động app
│   ├── SplashScreen.java   ← Màn hình splash
│   └── LoginScreen.java    ← Màn hình login
│
├── src/main/resources/
│   └── images/
│       ├── logo_rm_bkgr.png
│       ├── login_background.gif
│       └── app_icon.png
│
├── release/                ← Thư mục gói deploy
│   ├── jdk-8u202-linux-x64.rpm
│   ├── snst-app-1.0.0.jar
│   ├── install.sh
│   └── README.txt
│
├── .clauderules            ← Rule coding cho Claude AI
├── JAVA_RULES.md           ← Tiêu chuẩn code Java
├── NOTES.md                ← File ghi chú này
└── pom.xml                 ← Cấu hình Maven
```

---

## 5. Các Khái Niệm JavaFX Quan Trọng

```
STAGE = Khung cửa sổ
SCENE = Nội dung bên trong cửa sổ

BorderPane — chia 5 vùng:
┌─────────────────────────────┐
│           TOP               │
├───────┬─────────────────────┤
│ LEFT  │      CENTER         │
├───────┴─────────────────────┤
│          BOTTOM             │
└─────────────────────────────┘

VBox  → xếp dọc từ trên xuống
HBox  → xếp ngang từ trái sang
StackPane → xếp chồng lên nhau (dùng cho background + overlay)
```

---

## 6. Lệnh Phát Triển Trên Windows

### Chạy App Để Debug (Dùng Khi Đang Code)
```cmd
cd "C:\Users\triti\Downloads\STUDYING\coding\snst-app"
mvn compile exec:java -Dexec.mainClass=com.snst.Main
```
> Biên dịch lại toàn bộ code rồi chạy luôn
> Dùng khi đang phát triển, test tính năng mới

### Xóa Cache Build Cũ + Chạy Lại Sạch
```cmd
mvn clean compile exec:java -Dexec.mainClass=com.snst.Main
```
> Thêm `clean` khi nghi ngờ có file cũ gây lỗi

### Build File JAR Để Release
```cmd
mvn clean package -Dmaven.test.skip=truek
```
> Tạo file JAR tại `target\snst-app-1.0.0.jar`
> `-Dmaven.test.skip=true` = bỏ qua file test

---

## 7. Lệnh Deploy Sang Linux

### Gửi File JAR Sang Linux Qua SCP
```cmd
scp -oHostKeyAlgorithms=+ssh-rsa "target\snst-app-1.0.0.jar" tritin@192.168.204.129:/opt/snst-app/
```
> Dùng khi update app — chỉ gửi file JAR mới, không cần cài lại JDK

### Gửi Nguyên Folder Release (Lần Đầu Cài)
```cmd
scp -r -oHostKeyAlgorithms=+ssh-rsa "C:\Users\triti\Downloads\STUDYING\coding\snst-app\release" tritin@192.168.204.129:/home/tritin/
```

### Gửi File ZIP
```cmd
scp -oHostKeyAlgorithms=+ssh-rsa "C:\...\snst-app-v1.0.0.zip" tritin@192.168.204.129:/home/tritin/
```

> ⚠️ Lưu ý: Phải thêm `-oHostKeyAlgorithms=+ssh-rsa` vì CentOS 6 dùng mã hóa cũ

---

## 8. Lệnh Trên Linux CentOS

### Giải Nén File ZIP
```bash
unzip snst-app-v1.0.0.zip -d snst
cd snst/release
```

### Chuyển Sang Root
```bash
su -
# Nhập password root
```

### Cài Đặt App (Lần Đầu)
```bash
bash install.sh
```

### Thêm Java Vào PATH (Nếu Bị "command not found")
```bash
export PATH=/usr/java/jdk1.8.0_202-amd64/bin:$PATH
# Thêm vĩnh viễn:
echo 'export PATH=/usr/java/jdk1.8.0_202-amd64/bin:$PATH' >> /etc/profile
source /etc/profile
```

### Chạy App
```bash
# Cách 1: Dùng lệnh tắt (sau khi install.sh thành công)
snst-app

# Cách 2: Chạy thẳng JAR
java -jar snst-app-1.0.0.jar

# Cách 3: Chạy từ đường dẫn đầy đủ
java -jar /opt/snst-app/snst-app-1.0.0.jar
```

### Kiểm Tra Java
```bash
java -version
which java
```

---

## 9. Lệnh Update App (Sau Khi Đã Cài)

### Trên Windows — Build + Gửi 1 Lệnh
```cmd
mvn clean package -Dmaven.test.skip=true && scp -oHostKeyAlgorithms=+ssh-rsa "target\snst-app-1.0.0.jar" tritin@192.168.204.129:/opt/snst-app/
```

### Hoặc Dùng File `update.bat` (Double Click Là Xong)
```bat
@echo off
echo Building...
call mvn clean package -Dmaven.test.skip=true
echo Sending to Linux...
scp -oHostKeyAlgorithms=+ssh-rsa "target\snst-app-1.0.0.jar" tritin@192.168.204.129:/opt/snst-app/
echo Done!
pause
```

---

## 10. Gỡ Cài Đặt Trên Linux

```bash
# Xóa app
rm -rf /opt/snst-app
rm /usr/local/bin/snst-app

# Xóa JDK (nếu cần)
rpm -e jdk1.8.0_202
rm -rf /usr/java/jdk1.8.0_202-amd64
rm -f /usr/bin/java
```

---

## 11. Reset Password Root CentOS (Khi Quên)

```
Bước 1: Restart máy → màn hình GRUB hiện ra
Bước 2: Nhấn 'e' để edit
Bước 3: Dùng phím ↓ chọn dòng 'kernel'
Bước 4: Nhấn 'e' để edit dòng kernel
Bước 5: Nhấn 'End' → thêm chữ ' single' vào cuối
Bước 6: Nhấn 'Enter' → nhấn 'b' để boot
Bước 7: Terminal root hiện ra, đặt password mới:
         passwd root
Bước 8: Reboot:
         reboot
```

---

## 12. Lỗi Đã Gặp Và Cách Fix

| Lỗi | Nguyên nhân | Fix |
|---|---|---|
| `java -verion` không chạy | Sai chính tả | Gõ đúng `java -version` |
| Maven archetype lỗi PowerShell | PowerShell hiểu nhầm `-D` | Dùng CMD thay PowerShell |
| `Unknown lifecycle phase` | Chạy lệnh Maven trong PowerShell | Dùng CMD |
| `cannot access file - used by another process` | VS Code đang mở folder | Đóng folder trong VS Code trước |
| `java: command not found` trên Linux | Java chưa có trong PATH | `export PATH=/usr/java/jdk1.8.0_202-amd64/bin:$PATH` |
| `tritin is not in sudoers` | User không có quyền sudo | Dùng `su -` chuyển sang root |
| `no matching host key type` khi SCP | CentOS 6 dùng mã hóa cũ | Thêm `-oHostKeyAlgorithms=+ssh-rsa` |
| `already installed` khi cài JDK | JDK đã có, script dùng `-ivh` | Dùng `-Uvh` thay `-ivh` |

---

## 13. Thông Tin Máy Linux

```
OS      : CentOS 6.10 64-bit
IP      : 192.168.204.129
User    : tritin
App dir : /opt/snst-app/
JDK dir : /usr/java/jdk1.8.0_202-amd64/
```

---

*Ghi chú này được tổng hợp trong quá trình học ngày 25/06/2026*
--------------------------------------------------------------------
step           = "nlib"
  → Tên thư mục cha của file .nlib
  → Lấy bằng: new File(nlibPath).getParentFile().getName()
  → Dùng để thay "set step" trong file Tcl

currentLib     = "/home/.../nlib/routeOpt.nlib"
  → Đường dẫn đầy đủ tới file .nlib người dùng chọn
  → Dùng để thay "set current_lib" trong file Tcl

block          = "ORCA_TOP"
  → Tên block người dùng chọn ở Dropdown BLOCK
  → Dùng để thay "set block_name" trong file Tcl

reportPath     = "/home/.../reports/nlib/nlib.max.timing.rpt" hoặc null
  → null  = không gen report (create_rp = "N")
  → có giá trị = gen report ra đường dẫn này (create_rp = "Y")
  → Dùng để thay "set report_timing_rpt" trong file Tcl

generateReport = true/false
  → Lấy từ trạng thái Checkbox "Generate Timing Report"
  → true  → thay "set create_rp" = "Y" → ICC2 sẽ chạy write_report.tcl
  → false → thay "set create_rp" = "N" → ICC2 bỏ qua bước gen report