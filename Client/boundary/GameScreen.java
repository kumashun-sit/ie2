package boundary;

import boundary.element.AnswerLogCellRenderer;
import controller.BoundaryController;
import entity.Answer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.LineBorder;


public class GameScreen extends JPanel implements ActionListener {

    final int playerW = 196;
    final int playerH = 60;
    final int answerX = 451;
    final int answerY = 160;
    final int answerW = 66;
    final int answerH = 66;
    final int buttonX = 450;
    final int buttonY = 420;
    final int buttonW = 90;
    final int buttonH = 50;

    public static ArrayList<String> myAnswer = new ArrayList<>();
    public static ArrayList<JLabel> answerLabelList = new ArrayList<>();
    public static ArrayList<JLabel> playerLabelList = new ArrayList<>();
    public static ArrayList<JButton> buttonList = new ArrayList<>();
    public static ArrayList<Color> colorList = new ArrayList<>();

    public static JLabel announceLabel = new JLabel();
    public static JLabel timerLabel = new JLabel();
    public static Timer timer;
    public static int timerCounter;
    public static int numCounter = 0;

    public GameScreen() {
        setLayout(null);

        int setX = 1;
        int setY;

        //配列の初期化
        myAnswer.clear();
        answerLabelList.clear();
        playerLabelList.clear();
        buttonList.clear();
        colorList.clear();

        colorList.add(new Color(255,224,224));
        colorList.add(new Color(255,255,224));
        colorList.add(new Color(239,255,224));
        colorList.add(new Color(224,239,255));

        colorList.add(new Color(255, 70, 70));
        colorList.add(new Color(255, 255, 70));
        colorList.add(new Color(70, 255, 70));
        colorList.add(new Color(70, 70, 255));

        //プレイヤー表示ラベルの作成
        for (int i=0; i<4; i++) {
            JLabel playerLabel = new JLabel();
            playerLabel.setText("P1 admin1");
            playerLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 24));
            playerLabel.setHorizontalAlignment(JLabel.CENTER);
            playerLabel.setOpaque(true);
            playerLabel.setBackground(colorList.get(i));
            playerLabel.setForeground(Color.DARK_GRAY);
            playerLabel.setBounds(setX, 0, playerW, playerH);
            playerLabelList.add(playerLabel);
            add(playerLabel);
            setX += playerW;
            if (i==1) setX+=1;
        }

        setX = answerX;

        //入力された数字を表示するラベルの作成
        for (int i=0; i<4; i++) {
            JLabel answerLabel = new JLabel();
            answerLabel.setFont(new Font("Arial", Font.PLAIN, 50));
            answerLabel.setOpaque(true);
            answerLabel.setBackground(Color.WHITE);
            answerLabel.setHorizontalAlignment(JLabel.CENTER);
            answerLabel.setBounds(setX, answerY, answerW, answerH);
            answerLabel.setBorder(new LineBorder(new Color(100, 100, 100), 2, true));
            answerLabelList.add(answerLabel);
            add(answerLabel);
            setX += answerW+5;
        }

        //ログの作成
        DefaultListModel<Answer> model = BoundaryController.getInstance().logModel;
        JList<Answer> answerLogList = new JList<>(model);
        AnswerLogCellRenderer renderer = new AnswerLogCellRenderer();
        answerLogList.setCellRenderer(renderer);

        //スクロール領域の作成
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setView(answerLogList);
        scrollPane.setBounds(0,60,playerW*2,502);
        add(scrollPane);

        setX = buttonX;
        setY = buttonY;

        //ターンのアナウンスをするラベルの作成
        announceLabel.setText("あなたのターンです");
        announceLabel.setHorizontalAlignment(JLabel.CENTER);
        announceLabel.setFont(new Font("Meiryo UI", Font.PLAIN, 32));
        announceLabel.setBounds(380, answerY-70, 420, 40);
        add(announceLabel);

        //0から9までの数字のボタンを作成
        int buttonCounter = 0;

        JButton button = new JButton(String.valueOf(buttonCounter));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBounds(setX+buttonW+5, setY, buttonW, buttonH);
        button.setActionCommand(String.valueOf(buttonCounter));
        button.addActionListener(this);
        buttonList.add(button);
        add(button);

        setY-=buttonH+5;
        buttonCounter++;

        for (int i=0; i<3; i++) {
            setX = buttonX;
            for (int j=0; j<3; j++) {
                button = new JButton(String.valueOf(buttonCounter));
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                button.setBounds(setX, setY, buttonW, buttonH);
                button.setActionCommand(String.valueOf(buttonCounter));
                button.addActionListener(this);
                buttonList.add(button);
                add(button);

                setX+=buttonW+5;
                buttonCounter++;
            }
            setY-=buttonH+5;
        }

        //確定ボタンの作成
        JButton enterButton = new JButton("確定");
        enterButton.setFont(new Font("Meiryo UI", Font.PLAIN, 14));
        enterButton.setBounds(buttonX+buttonW*2+10, buttonY, buttonW, buttonH);
        buttonList.add(enterButton);
        add(enterButton);

        enterButton.addActionListener(e -> {

            /** デバッグ用 **/
//            if (numCounter == 4) {
//                numCounter = 0;
//                for (int i = 0; i < 4; i++) answerLabelList.get(i).setText(null);
//                BoundaryController.getInstance().model.add(0,
//                        new Answer(99, 99,
//                                Integer.parseInt(myAnswer.get(0)),
//                                Integer.parseInt(myAnswer.get(1)),
//                                Integer.parseInt(myAnswer.get(2)),
//                                Integer.parseInt(myAnswer.get(3)),
//                                0
//                        ));
//                myAnswer.clear();
//            }

            if (numCounter == 4) {
                numCounter = 0;
                BoundaryController.getInstance().inputNumber(
                        myAnswer.stream().mapToInt(Integer::parseInt).toArray());
                //自分の入力を削除
                for (int i = 0; i < 4; i++) answerLabelList.get(i).setText(null);
                myAnswer.clear();
            }

        });

        //リセットボタンの作成
        JButton resetButton = new JButton("リセット");
        resetButton.setFont(new Font("Meiryo UI", Font.PLAIN, 14));
        resetButton.setBounds(buttonX, buttonY, buttonW, buttonH);
        buttonList.add(resetButton);
        add(resetButton);

        resetButton.addActionListener(e -> {
            for (int i = 0; i < 4; i++) answerLabelList.get(i).setText(null);
            myAnswer.clear();
            numCounter = 0;
        });

        //ダミーボタン
        JButton dummyButton = new JButton();
        dummyButton.setBounds(0,0,0,0);
        add(dummyButton);

        //タイマーとタイマーラベルの作成
        timerLabel.setText("30");
        timerLabel.setHorizontalAlignment(JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN,60));
        timerLabel.setBounds(685,470,100,100);
        add(timerLabel);

        timer = new Timer(1000, this);
        timer.setActionCommand("timer");
    }

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();

        //タイマーの処理
        if (Objects.equals(command, "timer")) {
            timerCounter--;
            timerLabel.setText(String.valueOf(timerCounter));

            if (timerCounter == 10) {
                timerLabel.setForeground(Color.RED);
            }

            if (timerCounter == 0) {
                timer.stop();

                for (int i=0; i<12; i++) buttonList.get(i).setEnabled(false);
            }
        } else {
            //数字ボタンの処理
            if (numCounter < 4) {
                answerLabelList.get(numCounter).setText(command);
                myAnswer.add(command);
                numCounter++;
            }
        }
    }

}