package boundary;

import controller.BoundaryController;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LoginScreen extends JPanel {

    public static JTextField userIDField;
    public static JPasswordField passwordField;
    public static JButton loginButton;
    private BufferedImage image;

    public LoginScreen() {
        setLayout(null);

        JLabel titleLabel = new JLabel();
        titleLabel.setText("ログイン");
        titleLabel.setBounds(300, 130, 200, 60);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 44));
        add(titleLabel);

        JLabel userIDLabel = new JLabel();
        userIDLabel.setText("ユーザID");
        userIDLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
        userIDLabel.setBounds(250, 245, 100, 30);
        add(userIDLabel);

        JLabel passwordLabel = new JLabel();
        passwordLabel.setText("パスワード");
        passwordLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
        passwordLabel.setBounds(250, 305, 100, 30);
        add(passwordLabel);

        userIDField = new JTextField();
        userIDField.setBounds(360, 245, 200, 30);
        add(userIDField);

        passwordField = new JPasswordField();
        passwordField.setBounds(360, 305, 200, 30);
        add(passwordField);

        loginButton = new JButton("ログイン");
        loginButton.setBounds(420, 400, 180, 50);
        loginButton.addActionListener(e -> {
            loginButton.setEnabled(false);
            BoundaryController.getInstance().loginRequest(userIDField.getText(), String.valueOf(passwordField.getPassword()));
        });

        add(loginButton);

        JButton createAccountButton = new JButton("アカウント新規作成画面へ");
        createAccountButton.setBounds(200, 400, 180, 50);
        createAccountButton.addActionListener(e -> {
            CreateAccountScreen.userIDField.setText(null);
            CreateAccountScreen.passwordField1.setText(null);
            CreateAccountScreen.passwordField2.setText(null);

            BoundaryController.getInstance().changeScreen("CreateAccount");
        });
        add(createAccountButton);

        JButton backButton = new JButton("タイトル画面へ");
        backButton.setBounds(40, 480, 120, 50);
        backButton.addActionListener(e -> {
            userIDField.setText(null);
            passwordField.setText(null);

            BoundaryController.getInstance().changeScreen("Title");
        });
        add(backButton);

        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/title.jpg")));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.image = null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        AffineTransform af = AffineTransform.getScaleInstance(0.5,0.5);
        g2D.drawImage(image, af, this);
    }

}