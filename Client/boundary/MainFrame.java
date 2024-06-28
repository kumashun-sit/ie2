package boundary;

import controller.BoundaryController;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Hit & Blow");

        setSize(800 + getInsets().left + getInsets().right, 600 + getInsets().top * getInsets().bottom);

        setLocationRelativeTo(null);
        setResizable(false);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout());

        cardPanel.add(new TitleScreen(), "Title");
        cardPanel.add(new LoginScreen(), "Login");
        cardPanel.add(new CreateAccountScreen(), "CreateAccount");
        cardPanel.add(new LobbyScreen(), "Lobby");
        cardPanel.add(new RankingScreen(),"Ranking");
        cardPanel.add(new MatchingScreen(), "Matching");
        cardPanel.add(new GameScreen(), "Game");
        cardPanel.add(new ResultScreen(), "Result");

        add(cardPanel);
        BoundaryController.getInstance().setCardPanel(cardPanel);

        addWindowListener(new WinListener());
    }

    static class WinListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) { System.exit(0); }
    }

}