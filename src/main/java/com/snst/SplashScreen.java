/**
 * Splash Screen — DFT AI Agent
 *
 * @since   2.2.0
 * @package com.snst
 * @author  triti
 */
package com.snst;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SplashScreen {

    private double progress = 0.0;

    public void show(Image appIcon, Runnable onFinished) {
        Stage stage = new Stage();
        stage.getIcons().add(appIcon);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setWidth(600);
        stage.setHeight(380);
        stage.centerOnScreen();

        StackPane root = buildRoot(stage, onFinished);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private StackPane buildRoot(Stage stage, Runnable onFinished) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #0D1117;");

        VBox content = buildContent(stage, onFinished);
        StackPane.setAlignment(content, Pos.CENTER);
        root.getChildren().add(content);

        return root;
    }

    private VBox buildContent(Stage stage, Runnable onFinished) {
        VBox content = new VBox(16);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40, 40, 40, 40));
        content.setMaxWidth(Double.MAX_VALUE);

        // ── Logo to hơn ──────────────────────────────────────────
        ImageView logo = new ImageView(
            new Image(getClass().getResourceAsStream("/images/logo_rm_bkgr_white.png"))
        );
        logo.setFitWidth(300);
        logo.setFitHeight(300);
        logo.setPreserveRatio(true);

        // ── Loading label ─────────────────────────────────────────
        Label loadingLabel = new Label("Loading...");
        loadingLabel.setFont(Font.font("Arial", 15));
        loadingLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.4);");

        // ── Progress bar full width + label % ────────────────────
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(6);
        progressBar.setStyle(
            "-fx-accent: #C62828;" +
            "-fx-background-color: rgba(255,255,255,0.1);" +
            "-fx-background-radius: 3;" +
            "-fx-padding: 0;"
        );

        // Label hiện % tiến trình
        Label percentLabel = new Label("0%");
        percentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        percentLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.5);");

        // Ghép progress bar + % vào 1 hàng
        HBox progressRow = new HBox(10);
        progressRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(progressBar, Priority.ALWAYS);
        progressRow.getChildren().addAll(progressBar, percentLabel);

        content.getChildren().addAll(
            logo,
            loadingLabel, progressRow
        );

        // ── Animate progress ──────────────────────────────────────
        Timeline[] timelineRef = new Timeline[1];
        timelineRef[0] = new Timeline(
            new KeyFrame(Duration.millis(60), e -> {
                progress += 0.02;
                if (progress > 1.0) progress = 1.0;
                progressBar.setProgress(progress);
                percentLabel.setText((int)(progress * 100) + "%");

                if (progress >= 1.0) {
                    timelineRef[0].stop();
                    stage.close();
                    onFinished.run();
                }
            })
        );
        timelineRef[0].setCycleCount(Timeline.INDEFINITE);
        timelineRef[0].play();

        return content;
    }
}