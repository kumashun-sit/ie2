package boundary;

import boundary.element.RankCellRenderer;
import controller.BoundaryController;
import entity.Rank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class RankingScreen extends JPanel {

    private BufferedImage bg;
    public RankingScreen() {
        setLayout(null);

        DefaultListModel<Rank> model = BoundaryController.getInstance().rankModel;
        JList<Rank> rankList = new JList<>(model);
        RankCellRenderer renderer = new RankCellRenderer();
        rankList.setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setView(rankList);
        scrollPane.setBounds(200,135,400,267);
        scrollPane.setBackground(new Color(230, 230,230));
        scrollPane.setOpaque(true);
        add(scrollPane);

        JButton backButton = new JButton("ロビー画面へ");
        backButton.setBounds(260, 440, 280, 60);
        backButton.addActionListener(e -> {
            BoundaryController.getInstance().rankModel.clear();
            BoundaryController.getInstance().changeScreen("Lobby");
        });
        add(backButton);

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