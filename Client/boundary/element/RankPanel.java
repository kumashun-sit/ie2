package boundary.element;

import entity.Rank;

import javax.swing.*;
import java.awt.*;

public class RankPanel extends JPanel {

    RankPanel(Rank rank) {

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(370,37));
        setOpaque(true);
        setBackground(Color.WHITE);

        JLabel rankLabel = new JLabel();
        rankLabel.setText("    " + rank.getRank());
        rankLabel.setFont(new Font("Meiryo UI", rank.getRankStyle(), 25));
        rankLabel.setForeground(rank.getRankColor());
        add(rankLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel();
        userLabel.setText(rank.getUserID() + " : " + rank.getRate() + "    ");
        userLabel.setFont(new Font("Meiryo UI", rank.getUserStyle(), 25));
        userLabel.setForeground(rank.getUserColor());
        add(userLabel, BorderLayout.EAST);

    }

}