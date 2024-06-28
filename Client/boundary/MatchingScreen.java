package boundary;

import controller.BoundaryController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;


public class MatchingScreen extends JPanel implements ActionListener {

    public static JButton stopMatchingButton;
    public static JLabel stateLabel;
    public static JLabel playerLabel;
    public static JLabel rateLabel;
    public static ArrayList<JLabel> gameInfoLabelList = new ArrayList<>();
    public static Timer buttonTimer;
    public static Timer textTimer;
    public static int textTimerCounter;

    private BufferedImage image;

    public MatchingScreen() {
        int setY;

        setLayout(null);

        try {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getResource("img/title.jpg")));
        } catch (IOException ex) {
            ex.printStackTrace();
            this.image = null;
        }

        stateLabel = new JLabel();
        stateLabel.setText("マッチング中です...");
        stateLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 50));
        stateLabel.setHorizontalAlignment(JLabel.CENTER);
        stateLabel.setBounds(100, 160, 600, 50);
        add(stateLabel);

        textTimer = new Timer(600, this);
        textTimer.setActionCommand("textTimer");

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

        setY = 225;

        for (int i=0; i<4; i++) {
            JLabel gameInfoLabel = new JLabel();
            gameInfoLabel.setText("admin1 : 1000 → 1020");
            gameInfoLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 24));
            gameInfoLabel.setHorizontalAlignment(JLabel.CENTER);
            gameInfoLabel.setBounds(200, setY, 400, 40);
            gameInfoLabelList.add(gameInfoLabel);
            add(gameInfoLabel);
            setY += 40;
        }

        stopMatchingButton = new JButton("マッチング中断");
        stopMatchingButton.setBounds(260, 410, 280, 60);
        stopMatchingButton.addActionListener(e -> BoundaryController.getInstance().stopMatchingRequest());
        add(stopMatchingButton);

        buttonTimer = new Timer(2000, this);
        buttonTimer.setActionCommand("buttonTimer");

        /** デバッグ用 **/
//        JButton gameButton = new JButton("ゲーム画面へ");
//        gameButton.setBounds(300, 300, 200, 50);
//        gameButton.addActionListener(e -> {
//            BoundaryController.getInstance().changeScreen("Game");
//        });
//        add(gameButton);
//
//        JButton resultButton = new JButton("リザルト画面へ");
//        resultButton.setBounds(300, 400, 200, 50);
//        resultButton.addActionListener(e -> {
//            BoundaryController.getInstance().changeScreen("Result");
//        });
//        add(resultButton);
    }

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        //タイマーの処理
        if (Objects.equals(command, "buttonTimer")) {
            stopMatchingButton.setEnabled(true);
        }

        if (Objects.equals(command, "textTimer")) {
            if (textTimerCounter == 0) {
                stateLabel.setText("マッチング中です.");
                textTimerCounter++;
            } else if (textTimerCounter == 1) {
                stateLabel.setText("マッチング中です..");
                textTimerCounter++;
            } else if (textTimerCounter == 2) {
                stateLabel.setText("マッチング中です...");
                textTimerCounter = 0;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        AffineTransform af = AffineTransform.getScaleInstance(0.5,0.5);
        g2D.drawImage(image, af, this);
    }

}