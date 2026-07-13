/**
 * Đọc config từ file config.properties
 * Tự động tìm file "run" nằm CÙNG THƯ MỤC với file .jar khi chạy ở prod mode
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
                System.err.println("[AppConfig] config.properties NOT FOUND!");
            } else {
                props.load(is);
                System.out.println("[AppConfig] Loaded successfully.");
            }
        } catch (Exception e) {
            System.err.println("[AppConfig] Error: " + e.getMessage());
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
     * Lấy thư mục chứa file .jar đang chạy (prod) hoặc thư mục project
     * (dev). Dùng để tìm các file đi kèm nằm cùng cấp với .jar, ví dụ
     * file "run".
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
     * Lấy đường dẫn file "run":
     *   - PROD: tự động ghép getAppDirectory() + "/run" (cùng thư mục với .jar)
     *   - DEV : lấy từ config.properties (dev mode dùng lệnh giả lập
     *           calc.exe qua getDevLaunchCommand(), không đọc file này)
     *
     * @since 8.0.0
     */
    public static String getRunTclPath() {
        if (isDevMode()) {
            return get("dev.tcl.run");
        }
        File runFile = new File(getAppDirectory(), "run");
        return runFile.getAbsolutePath();
    }

    /**
     * Kiểm tra file "run" có thực sự tồn tại trên đĩa không — dùng để
     * báo lỗi rõ ràng trước khi chạy, thay vì để bash tự chạy
     * "source <file không tồn tại>" rồi lỗi lặng lẽ trong console.
     *
     * @since 9.1.0
     */
    public static boolean runFileExists() {
        if (isDevMode()) return true; // dev mode không đọc file này, luôn coi là OK
        return new File(getRunTclPath()).isFile();
    }
}