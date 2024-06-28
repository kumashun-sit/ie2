package boundary;

import controller.BoundaryController;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class ResultScreen extends JPanel {

    final int answerX = 261;
    final int answerY = 175;
    final int answerW = 66;
    final int answerH = 66;

    public static JLabel winnerLabel = new JLabel();
    public static ArrayList<JLabel> answerLabelList = new ArrayList<>();
    public static ArrayList<JLabel> resultLabelList = new ArrayList<>();
    public static ArrayList<JLabel> deltaRateLabelList = new ArrayList<>();

    private BufferedImage image;

    public ResultScreen() {
        setLayout(null);

        int setX;
        int setY;

        //配列の初期化
        answerLabelList.clear();
        resultLabelList.clear();

        //勝利プレイヤー表示ラベルの作成
        winnerLabel.setText("Winner is admin1 !");
        winnerLabel.setFont(new Font("Meiryo UI", Font.BOLD, 44));
        winnerLabel.setForeground(Color.RED);
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        winnerLabel.setBounds(100, 85, 600, 80);
        add(winnerLabel);

        setX = answerX;

        //正解の数字を表示するラベルの作成
        for (int i=0; i<4; i++) {
            JLabel answerLabel = new JLabel();
            answerLabel.setText("1");
            answerLabel.setFont(new Font("Arial", Font.PLAIN, 45));
            answerLabel.setHorizontalAlignment(JLabel.CENTER);
            answerLabel.setBounds(setX, answerY, answerW, answerH);
            answerLabel.setBorder(new LineBorder(new Color(100, 100, 100), 2, true));
            answerLabel.setOpaque(true);
            answerLabel.setBackground(Color.WHITE);
            answerLabelList.add(answerLabel);
            add(answerLabel);
            setX += answerW+5;
        }

        setY = 260;

        //プレイヤーのレート変動を表示するラベルの作成
        for (int i=0; i<4; i++) {
            JLabel resultLabel = new JLabel();
            resultLabel.setText("admin1 : 1000 → 1020");
            resultLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 24));
            resultLabel.setHorizontalAlignment(JLabel.CENTER);
            resultLabel.setBounds(220, setY, 360, 40);
            resultLabelList.add(resultLabel);
            add(resultLabel);
            setY += 40;
        }

        setY = 260;

        //プレイヤーのレートの変動値を表示するラべルの作成
        for (int i=0; i<4; i++) {
            JLabel deltaRateLabel = new JLabel();
            deltaRateLabel.setText("+20");
            deltaRateLabel.setFont(new Font("Meiryo UI", Font.BOLD, 24));
            deltaRateLabel.setHorizontalAlignment(JLabel.LEFT);
            deltaRateLabel.setBounds(585, setY, 80, 40);
            deltaRateLabelList.add(deltaRateLabel);
            add(deltaRateLabel);
            setY += 40;
        }

        JButton backButton = new JButton("ロビー画面に戻る");
        backButton.setBounds(260, 440, 280, 60);
        backButton.addActionListener(e -> BoundaryController.getInstance().changeScreen("Lobby"));
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