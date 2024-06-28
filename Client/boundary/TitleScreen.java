package boundary;

import controller.BoundaryController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class TitleScreen extends JPanel {

    private BufferedImage bg;
    public TitleScreen() {
        setLayout(null);

        JLabel titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(100, 120, 600, 100);
        titleLabel.setText("Hit & Blow");
        titleLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 80));
        add(titleLabel);

        JButton startButton = new JButton("ログイン画面へ");
        startButton.setBounds(420, 400, 180, 50);
        startButton.addActionListener(e-> BoundaryController.getInstance().changeScreen("Login"));

        add(startButton);

        JButton exitButton = new JButton("終了");
        exitButton.setBounds(200, 400, 180, 50);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);

        //デバッグ用
//        JButton debugButton1 = new JButton("デバッグ１");
//        debugButton1.setBounds(200, 310, 100, 50);
//        debugButton1.addActionListener(e -> BoundaryController.getInstance().debug("admin1", "password1"));
//        add(debugButton1);
//
//        JButton debugButton2 = new JButton("デバッグ２");
//        debugButton2.setBounds(300, 310, 100, 50);
//        debugButton2.addActionListener(e -> BoundaryController.getInstance().debug("admin2", "password2"));
//        add(debugButton2);
//
//        JButton debugButton3 = new JButton("デバッグ３");
//        debugButton3.setBounds(400, 310, 100, 50);
//        debugButton3.addActionListener(e -> BoundaryController.getInstance().debug("admin3", "password3"));
//        add(debugButton3);
//
//        JButton debugButton4 = new JButton("デバッグ４");
//        debugButton4.setBounds(500, 310, 100, 50);
//        debugButton4.addActionListener(e -> BoundaryController.getInstance().debug("admin4", "password4"));
//        add(debugButton4);

        try {
            this.bg = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/title.jpg")));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.bg = null;
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        AffineTransform af = AffineTransform.getScaleInstance(0.5,0.5);
        g2D.drawImage(bg, af, this);
    }

}