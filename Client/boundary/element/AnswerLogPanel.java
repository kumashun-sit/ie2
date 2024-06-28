package boundary.element;

import boundary.GameScreen;
import entity.Answer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class AnswerLogPanel extends JPanel {

    public AnswerLogPanel(Answer answer) {

        setLayout(null);
        setPreferredSize(new Dimension(370,99));
        setOpaque(true);
        setBackground(Color.WHITE);

        JPanel p = new JPanel();
        p.setBounds(40,15,300,70);
        p.setBorder(new LineBorder(Color.BLACK, 2,true));
        p.setBackground(GameScreen.colorList.get(answer.getColorNum()));

        add(p);

        JLabel answerLabel1 = new JLabel();
        answerLabel1.setText(String.valueOf(answer.getAnswer1()));
        answerLabel1.setHorizontalAlignment(JLabel.CENTER);
        answerLabel1.setFont(new Font("Arial", Font.PLAIN, 45));
        answerLabel1.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        p.add(answerLabel1);

        JLabel answerLabel2 = new JLabel();
        answerLabel2.setText(String.valueOf(answer.getAnswer2()));
        answerLabel2.setHorizontalAlignment(JLabel.CENTER);
        answerLabel2.setFont(new Font("Arial", Font.PLAIN, 45));
        answerLabel2.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        p.add(answerLabel2);

        JLabel answerLabel3 = new JLabel();
        answerLabel3.setText(String.valueOf(answer.getAnswer3()));
        answerLabel3.setHorizontalAlignment(JLabel.CENTER);
        answerLabel3.setFont(new Font("Arial", Font.PLAIN, 45));
        answerLabel3.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        p.add(answerLabel3);

        JLabel answerLabel4 = new JLabel();
        answerLabel4.setText(String.valueOf(answer.getAnswer4()));
        answerLabel4.setHorizontalAlignment(JLabel.CENTER);
        answerLabel4.setFont(new Font("Arial", Font.PLAIN, 45));
        answerLabel4.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        p.add(answerLabel4);

        JLabel hitBlowLabel = new JLabel();
        hitBlowLabel.setText("<html><body>Hit : " + answer.getHit() + "<br/>Blow : " + answer.getBlow() + "</body></html>");
        hitBlowLabel.setHorizontalAlignment(JLabel.CENTER);
        hitBlowLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        hitBlowLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        p.add(hitBlowLabel);

    }

}