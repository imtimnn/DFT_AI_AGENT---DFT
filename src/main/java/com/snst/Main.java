/**
 * Điểm khởi động app — chạy Splash rồi chuyển sang Login
 *
 * @since   1.1.0
 * @package com.snst
 * @author  triti
 */
package com.snst;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Image appIcon = new Image(
            getClass().getResourceAsStream("/images/app_icon.png")
        );
        primaryStage.getIcons().add(appIcon);

        // ── DEV MODE: bỏ qua Splash, vào thẳng Login ──────────
        if (AppConfig.isDevMode()) {
            new LoginScreen().show(primaryStage);
            return;
        }
        // ── PROD MODE: chạy bình thường ─────────────
        SplashScreen splash = new SplashScreen();
        splash.show(appIcon, () -> {
            LoginScreen login = new LoginScreen();
            login.show(primaryStage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}