/**
 * Quản lý 1 session bash sống xuyên suốt — giống mở Terminal Linux thật
 * Có khả năng phát hiện khi nào lệnh chạy xong (dựa vào prompt bash quay lại)
 * Mở tool chính ngay sau khi Login thành công bằng cách source run.tcl
 *
 * @since   9.0.0
 * @package com.snst
 */
package com.snst;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Consumer;

public class ToolLauncher {

    private static Process     bashProcess  = null;
    private static PrintWriter bashInput    = null;
    private static Thread      readerThread = null;
    private static boolean     sessionAlive = false;

    private static StringBuilder tailBuffer = new StringBuilder();

    // ═══════════════════════════════════════════════════════════════
    //  BASH SESSION — terminal nhúng sống xuyên suốt
    // ═══════════════════════════════════════════════════════════════

    public static synchronized void startSession(
        String            workingDir,
        Consumer<String>  onLog,
        Consumer<Boolean> onBusyChanged,
        Runnable          onClosed
    ) {
        if (sessionAlive) return;

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-i");
            pb.redirectErrorStream(true);

            if (workingDir != null && !workingDir.isEmpty()) {
                File dir = new File(workingDir);
                if (dir.exists() && dir.isDirectory()) {
                    pb.directory(dir);
                }
            }

            bashProcess = pb.start();
            sessionAlive = true;
            tailBuffer.setLength(0);

            OutputStream stdin = bashProcess.getOutputStream();
            bashInput = new PrintWriter(stdin, true);

            readerThread = new Thread(() -> {
                try {
                    InputStream is = bashProcess.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    char[] buffer = new char[1024];
                    int charsRead;

                    while ((charsRead = reader.read(buffer)) != -1) {
                        final String outputChunk = new String(buffer, 0, charsRead);
                        javafx.application.Platform.runLater(() -> onLog.accept(outputChunk));

                        tailBuffer.append(outputChunk);
                        if (tailBuffer.length() > 200) {
                            tailBuffer.delete(0, tailBuffer.length() - 200);
                        }

                        boolean looksLikePrompt = isPromptEnding(tailBuffer.toString());
                        javafx.application.Platform.runLater(() -> onBusyChanged.accept(!looksLikePrompt));
                    }
                } catch (Exception e) {
                    // Bình thường khi session đóng
                } finally {
                    sessionAlive = false;
                    javafx.application.Platform.runLater(onClosed);
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

        } catch (Exception e) {
            javafx.application.Platform.runLater(() ->
                onLog.accept("ERROR: Không thể mở session: " + e.getMessage())
            );
        }
    }

    private static boolean isPromptEnding(String text) {
        String trimmed = text.replaceAll("\\s+$", "");
        if (trimmed.isEmpty()) return false;
        String lastLine = trimmed.substring(trimmed.lastIndexOf('\n') + 1);
        return lastLine.matches(".*[\\$#]\\s*$") && lastLine.length() < 100;
    }

    public static synchronized void sendCommand(String command) {
        if (!sessionAlive || bashInput == null) return;
        bashInput.print(command + "\n");
        bashInput.flush();
    }

    public static boolean isSessionAlive() {
        return sessionAlive;
    }

    public static synchronized void stopSession() {
        if (bashProcess != null && bashProcess.isAlive()) {
            bashProcess.destroyForcibly();
        }
        sessionAlive = false;
        bashProcess  = null;
        bashInput    = null;
    }

    // ═══════════════════════════════════════════════════════════════
    //  LAUNCH MAIN TOOL — mở GUI chính ngay sau khi Login
    //  Tiến trình độc lập, tách khỏi bash session (tool tự mở cửa sổ riêng)
    // ═══════════════════════════════════════════════════════════════

    /**
     * Mở tool chính ngay sau khi Login:
     *   - Dev mode  : chạy lệnh giả lập (VD: calc.exe trên Windows)
     *   - Prod mode : chạy "source run.tcl" trong bash (KHÔNG dùng ic/icc2_shell,
     *                 vì môi trường bash trên Linux đã setup sẵn từ trước)
     *
     * @since 9.0.0
     * @param onLog   Callback nhận log text (để hiển thị nếu cần, có thể bỏ qua)
     * @param onDone  Callback gọi khi đã start tiến trình thành công (không đợi tool đóng)
     * @param onError Callback gọi khi có lỗi khi start tiến trình
     */
    public static void launchMainTool(
        Consumer<String>  onLog,
        Runnable          onDone,
        Consumer<String>  onError
    ) {
        Thread thread = new Thread(() -> {
            try {
                ProcessBuilder pb;

                if (AppConfig.isDevMode()) {
                    // ── DEV: chạy lệnh giả lập, VD "calc.exe" ──────
                    String devCmd = AppConfig.getDevLaunchCommand();
                    javafx.application.Platform.runLater(() ->
                        onLog.accept("$ " + devCmd + "\n")
                    );
                    pb = new ProcessBuilder(devCmd.trim().split("\\s+"));

                } else {
                    // ── PROD: chỉ "source run.tcl" trong bash, KHÔNG mở ic/icc2_shell ────
                    String runTcl = AppConfig.getRunTclPath();

                    String fullCmd = "source " + runTcl;
                    javafx.application.Platform.runLater(() ->
                        onLog.accept("$ " + fullCmd + "\n")
                    );
                    pb = new ProcessBuilder("bash", "-c", fullCmd);
                }

                pb.inheritIO();
                pb.start();

                javafx.application.Platform.runLater(onDone);

            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                    onError.accept("Không thể mở tool: " + e.getMessage())
                );
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}