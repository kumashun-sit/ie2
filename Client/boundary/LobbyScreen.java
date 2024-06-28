package boundary;

import controller.BoundaryController;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LobbyScreen extends JPanel {

    public static JLabel playerLabel;
    public static JLabel rateLabel;
    public static JButton rankingButton;
    private BufferedImage image;

    public LobbyScreen() {
        setLayout(null);

        JLabel titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(100, 120, 600, 100);
        titleLabel.setText("Hit & Blow");
        titleLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 80));
        add(titleLabel);

        playerLabel = new JLabel();
        playerLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 30));
        playerLabel.setHorizontalAlignment(JLabel.CENTER);
        playerLabel.setBounds(200,250,400,60);
        add(playerLabel);

        rateLabel = new JLabel();
        rateLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 30));
        rateLabel.setHorizontalAlignment(JLabel.CENTER);
        rateLabel.setBounds(200,300,400,60);
        add(rateLabel);

        rankingButton = new JButton("ランキング画面へ");
        rankingButton.setBounds(200, 400, 180, 50);
        rankingButton.addActionListener(e -> {
            rankingButton.setEnabled(false);
            BoundaryController.getInstance().getRankRequest();
        });
        add(rankingButton);

        JButton startMatchingButton = new JButton("マッチング開始");
        startMatchingButton.setBounds(420, 400, 180, 50);
        startMatchingButton.addActionListener(e -> BoundaryController.getInstance().startMatchingRequest());
        add(startMatchingButton);

        JButton btn1 = new JButton("ログアウト");
        btn1.setBounds(40, 480, 120, 50);
        btn1.addActionListener(e -> BoundaryController.getInstance().logout());
        add(btn1);

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