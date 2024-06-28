package boundary;

import controller.BoundaryController;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

public class CreateAccountScreen extends JPanel {

        public static JTextField userIDField;
        public static JPasswordField passwordField1;
        public static JPasswordField passwordField2;
        public static JButton createAccountButton;
        private BufferedImage image;

        public CreateAccountScreen() {
                setLayout(null);

                JLabel titleLabel = new JLabel();
                titleLabel.setText("アカウント新規作成");
                titleLabel.setBounds(100, 130, 600, 60);
                titleLabel.setHorizontalAlignment(JLabel.CENTER);
                titleLabel.setVerticalAlignment(JLabel.TOP);
                titleLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 40));
                add(titleLabel);

                JLabel userIDLabel = new JLabel();
                userIDLabel.setText("ユーザID");
                userIDLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
                userIDLabel.setBounds(205, 215, 150, 30);
                add(userIDLabel);

                JLabel passwordLabel = new JLabel();
                passwordLabel.setText("パスワード");
                passwordLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
                passwordLabel.setBounds(205, 275, 150, 30);
                add(passwordLabel);

                JLabel passwordLabel2 = new JLabel();
                passwordLabel2.setText("パスワード（確認）");
                passwordLabel2.setFont(new Font("Meiryo UI", Font.PLAIN, 20));
                passwordLabel2.setBounds(205, 335, 180, 30);
                add(passwordLabel2);

                userIDField = new JTextField();
                userIDField.setBounds(400, 215, 200, 30);
                add(userIDField);

                passwordField1 = new JPasswordField();
                passwordField1.setBounds(400, 275, 200, 30);
                add(passwordField1);

                passwordField2 = new JPasswordField();
                passwordField2.setBounds(400, 335, 200, 30);
                add(passwordField2);

                createAccountButton = new JButton("新規作成");
                createAccountButton.setBounds(420, 400, 180, 50);
                createAccountButton.addActionListener(e -> {
                        //文字列が空でないかを確認する
                        if (!userIDField.getText().isEmpty() &&
                                !String.valueOf(passwordField1.getPassword()).isEmpty() &&
                                !String.valueOf(passwordField2.getPassword()).isEmpty()) {
                                //パスワードの一致を確認する
                                if ( String.valueOf(passwordField1.getPassword()).equals(String.valueOf(passwordField2.getPassword())) ) {
                                        createAccountButton.setEnabled(false);
                                        BoundaryController.getInstance().createAccountRequest(userIDField.getText(), String.valueOf(passwordField1.getPassword()));
                                } else {
                                        BoundaryController.getInstance().showErrorMessage("エラー：パスワードが一致しません．");
                                }
                        } else {
                                BoundaryController.getInstance().showErrorMessage("エラー：文字列が入力されていません．");
                        }
                });

                add(createAccountButton);

                JButton backButton = new JButton("ログイン画面へ");
                backButton.setBounds(200, 400, 180, 50);
                backButton.addActionListener(e -> {
                        LoginScreen.userIDField.setText(null);
                        LoginScreen.passwordField.setText(null);

                        BoundaryController.getInstance().changeScreen("Login");
                });
                add(backButton);

                JButton backButton2 = new JButton("タイトル画面へ");
                backButton2.setBounds(40, 480, 120, 50);
                backButton2.addActionListener(e -> {
                        userIDField.setText(null);
                        passwordField1.setText(null);
                        passwordField2.setText(null);

                        BoundaryController.getInstance().changeScreen("Title");
                });
                add(backButton2);

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