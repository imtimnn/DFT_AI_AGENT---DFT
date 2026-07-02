/**
 * Đọc config từ file config.properties
 * Tự động tìm file run.tcl nằm CÙNG THƯ MỤC với file .jar khi chạy ở prod mode
 *
 * @since   1.0.0
 * @package com.snst
 */
package com.snst;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class AppConfig {

    private static final Properties props = new Properties();

    // Tự động load khi class được dùng lần đầu
    static {
        try {
            InputStream is = AppConfig.class
                .getResourceAsStream("/config.properties");
            if (is == null) {
                System.err.println("[AppConfig] KHÔNG TÌM THẤY config.properties!");
            } else {
                props.load(is);
                System.out.println("[AppConfig] Load thành công!");
                System.out.println("[AppConfig] app.mode = " + props.getProperty("app.mode"));
            }
        } catch (Exception e) {
            System.err.println("[AppConfig] Lỗi: " + e.getMessage());
        }
    }

    /**
     * Lấy giá trị theo key
     */
    public static String get(String key) {
        return props.getProperty(key, "");
    }

    /**
     * Kiểm tra đang chạy chế độ dev không
     */
    public static boolean isDevMode() {
        return "dev".equals(get("app.mode"));
    }

    /**
     * Lấy lệnh ic phù hợp với môi trường
     */
    public static String getIcCommand() {
        return isDevMode() ? get("dev.tool.ic") : get("tool.ic");
    }

    /**
     * Lấy lệnh icc2_shell phù hợp với môi trường
     */
    public static String getIcc2Command() {
        return isDevMode() ? get("dev.tool.icc2") : get("tool.icc2");
    }

    /**
     * Lấy đường dẫn file Tcl khuôn mẫu (gốc, không đổi)
     */
    public static String getTclTemplatePath() {
        return isDevMode() ? get("dev.tcl.template") : get("tcl.template");
    }

    /**
     * Lấy đường dẫn file Tcl tạm (sẽ bị ghi đè mỗi lần Run)
     */
    public static String getTclRuntimePath() {
        return isDevMode() ? get("dev.tcl.runtime") : get("tcl.runtime");
    }

    /**
     * Lấy lệnh giả lập mở tool khi ở chế độ dev (VD: calc.exe trên Windows)
     *
     * @since 8.0.0
     */
    public static String getDevLaunchCommand() {
        return get("dev.tool.launch");
    }

    /**
     * [MỚI] Lấy thư mục chứa file .jar đang chạy (prod) hoặc thư mục project (dev)
     * Dùng để tìm các file đi kèm nằm CÙNG CẤP với .jar, ví dụ run.tcl
     *
     * @since 9.0.0
     */
    public static String getAppDirectory() {
        try {
            File jarFile = new File(
                AppConfig.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            );
            // Nếu đang chạy từ .jar → jarFile là chính file .jar → lấy thư mục cha
            // Nếu đang chạy từ IDE (chưa build .jar) → jarFile là thư mục target/classes
            if (jarFile.isFile()) {
                return jarFile.getParentFile().getAbsolutePath();
            } else {
                return jarFile.getAbsolutePath();
            }
        } catch (URISyntaxException e) {
            System.err.println("[AppConfig] Không lấy được thư mục app: " + e.getMessage());
            return ".";
        }
    }

    /**
     * [SỬA] Lấy đường dẫn file run.tcl:
     *   - PROD: tự động ghép getAppDirectory() + "/run.tcl" (CÙNG THƯ MỤC với .jar)
     *   - DEV : lấy từ config.properties như cũ (vì dev mode không thật sự đọc file này,
     *           chỉ dùng lệnh giả lập calc.exe qua getDevLaunchCommand())
     *
     * @since 8.0.0 — sửa lại 9.0.0 để tự tìm cùng thư mục .jar
     */
    public static String getRunTclPath() {
        if (isDevMode()) {
            return get("dev.tcl.run");
        }
        File runTclFile = new File(getAppDirectory(), "run");
        return runTclFile.getAbsolutePath();
    }
}