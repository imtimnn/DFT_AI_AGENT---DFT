/**
 * Màn hình Login
 *
 * @since   3.2.0
 * @package com.snst
 * @author  triti
 */
package com.snst;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen {

    private static final String COLOR_RED = "#C62828";

    private TextField     usernameField;
    private PasswordField passwordField;
    private TextField     passwordVisible;
    private boolean       isPasswordShown = false;

    public void show(Stage stage) {
        stage.setTitle("DFT AI AGENT - SNST VIETNAM");
        stage.setWidth(1280);
        stage.setHeight(800);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.centerOnScreen();

        StackPane root = buildRoot();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ═══════════════════════════════════════════════════════════════
    //  ROOT
    // ═══════════════════════════════════════════════════════════════
    private StackPane buildRoot() {
        StackPane root = new StackPane();

        // Lớp 1 — Ảnh nền
        Image bgImage = new Image(getClass().getResourceAsStream("/images/login_background.jpg"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(false);
        bgView.setSmooth(true);
        bgView.fitWidthProperty().bind(root.widthProperty());
        bgView.fitHeightProperty().bind(root.heightProperty());

        // Lớp 2 — Overlay tối
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.30);");

        // Lớp 3 — Header: chỉ logo, góc trên trái
        HBox header = buildHeader();
        header.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        StackPane.setAlignment(header, Pos.TOP_LEFT);
        StackPane.setMargin(header, new Insets(16, 0, 0, 20));

        // Lớp 4 — Nội dung giữa (title PNR + form)
        VBox centerContent = buildCenterContent();
        StackPane.setAlignment(centerContent, Pos.CENTER_LEFT);
        StackPane.setMargin(centerContent, new Insets(0, 0, 0, 100));

        root.getChildren().addAll(bgView, overlay, header, centerContent);
        return root;
    }

    // ═══════════════════════════════════════════════════════════════
    //  HEADER — Chỉ logo, góc trên trái
    // ═══════════════════════════════════════════════════════════════
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 0, 0));
        header.setAlignment(Pos.TOP_LEFT);

        ImageView logo = new ImageView(
            new Image(getClass().getResourceAsStream("/images/logo_rm_bkgr_white.png"))
        );
        logo.setFitWidth(160);
        logo.setFitHeight(160);
        logo.setPreserveRatio(true);

        header.getChildren().add(logo);
        return header;
    }

    // ═══════════════════════════════════════════════════════════════
    //  CENTER — Title PNR + Form login
    // ═══════════════════════════════════════════════════════════════
    private VBox buildCenterContent() {
        VBox center = new VBox(28);
        center.setAlignment(Pos.CENTER);
        center.setMaxWidth(440);

        center.getChildren().addAll(buildTitleSection(), buildLoginForm());
        return center;
    }

    // ── Title: chỉ PNR TIMING DEBUGGER ───────────────────────────
    private VBox buildTitleSection() {
        VBox section = new VBox(6);
        section.setAlignment(Pos.CENTER);

        HBox tagRow = new HBox(10);
        tagRow.setAlignment(Pos.CENTER);

        Label tagLabel = new Label("DFT AI AGENT");
        tagLabel.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        tagLabel.setStyle(
            "-fx-text-fill: #ffffff;" +
            "-fx-letter-spacing: 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(252, 182, 182, 0.8), 10, 0, 0, 0);"
        );

        tagRow.getChildren().addAll(tagLabel);
        section.getChildren().addAll(tagRow);
        return section;
    }

    // ═══════════════════════════════════════════════════════════════
    //  FORM LOGIN
    // ═══════════════════════════════════════════════════════════════
    private VBox buildLoginForm() {
        VBox form = new VBox(16);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(32, 36, 28, 36));
        form.setMaxWidth(440);
        form.setStyle(
            "-fx-background-color: rgba(35,42,56,0.85);" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: rgba(255,255,255,0.14);" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 32, 0, 0, 8);"
        );

        Label usernameLabel = new Label("USERNAME");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        usernameLabel.setStyle(
            "-fx-text-fill: rgba(255,255,255,0.6);" +
            "-fx-letter-spacing: 1;"
        );

        usernameField = buildDarkTextField("your.name");
        usernameField.setOnAction(e -> passwordField.requestFocus());

        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        passwordLabel.setStyle(
            "-fx-text-fill: rgba(255,255,255,0.6);" +
            "-fx-letter-spacing: 1;"
        );

        StackPane pwBox = buildPasswordBox();

        Button loginBtn = buildLoginButton();

        Label versionLabel = new Label("v2.4.1  ·  © 2026 ADTechnology");
        versionLabel.setFont(Font.font("Arial", 11));
        versionLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.25);");
        VBox.setMargin(versionLabel, new Insets(4, 0, 0, 0));

        HBox versionRow = new HBox();
        versionRow.setAlignment(Pos.CENTER);
        versionRow.getChildren().add(versionLabel);

        form.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, pwBox,
            loginBtn,
            versionRow
        );
        return form;
    }

    private TextField buildDarkTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(46);
        field.setFont(Font.font("Arial", 13));
        field.setMaxWidth(Double.MAX_VALUE);
        field.setStyle(
            "-fx-background-color: rgba(255,255,255,0.14);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: rgba(255,255,255,0.22);" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 0 16 0 16;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255,255,255,0.45);"
        );
        return field;
    }

    private StackPane buildPasswordBox() {
        String fieldStyle =
            "-fx-background-color: rgba(255,255,255,0.14);" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: rgba(255,255,255,0.22);" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 0 44 0 16;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255,255,255,0.45);";

        passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setPrefHeight(46);
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passwordField.setFont(Font.font("Arial", 13));
        passwordField.setStyle(fieldStyle);
        passwordField.setOnAction(e -> handleLogin());

        passwordVisible = new TextField();
        passwordVisible.setPromptText("••••••••");
        passwordVisible.setPrefHeight(46);
        passwordVisible.setMaxWidth(Double.MAX_VALUE);
        passwordVisible.setFont(Font.font("Arial", 13));
        passwordVisible.setVisible(false);
        passwordVisible.setManaged(false);
        passwordVisible.setStyle(fieldStyle);
        passwordVisible.setOnAction(e -> handleLogin());

        Button toggleBtn = new Button("👁");
        toggleBtn.setFont(Font.font(13));
        toggleBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: rgba(255,255,255,0.5);" +
            "-fx-cursor: hand;" +
            "-fx-padding: 0;"
        );
        toggleBtn.setOnAction(e -> togglePassword(toggleBtn));

        StackPane box = new StackPane();
        StackPane.setAlignment(toggleBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggleBtn, new Insets(0, 14, 0, 0));
        box.getChildren().addAll(passwordField, passwordVisible, toggleBtn);
        return box;
    }

    private void togglePassword(Button toggleBtn) {
        if (isPasswordShown) {
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisible.setVisible(false);
            passwordVisible.setManaged(false);
            toggleBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: rgba(255,255,255,0.5);" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
            );
            isPasswordShown = false;
        } else {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordVisible.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            toggleBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 0;"
            );
            isPasswordShown = true;
        }
    }

    private Button buildLoginButton() {
        Button btn = new Button("LOG IN  →");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(50);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        btn.setStyle(
            "-fx-background-color: " + COLOR_RED + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #B71C1C;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + COLOR_RED + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnAction(e -> handleLogin());
        return btn;
    }

    // ═══════════════════════════════════════════════════════════════
    //  LOGIC
    // ═══════════════════════════════════════════════════════════════
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = isPasswordShown
            ? passwordVisible.getText().trim()
            : passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please enter your username and password!");
            return;
        }

        if (username.equals("admin") && password.equals("admin")) {
            Stage stage = (Stage) usernameField.getScene().getWindow();

            ToolLauncher.launchMainTool(
                log -> System.out.print(log),   // in log ra console Java để debug
                () -> stage.close(),             // start tool thành công → đóng cửa sổ Login
                error -> showAlert(error)        // lỗi → hiện popup, giữ Login để thử lại
            );
        } else {
            showAlert("Invalid username or password!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}