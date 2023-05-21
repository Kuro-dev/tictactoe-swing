package org.kurodev;

import logic.FieldPosition;
import logic.Player;
import logic.TicTacToeGame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class TicTacToeFrontendApp {
    private final JFrame window;
    CompletableFuture<FieldPosition> currentMove = null;
    private final Player player1 = new SwingPlayer(this::awaitPlayerMove);
    private TicTacToeGame game;
    private Player player2;

    private int player1Wins = 0;
    private int player2Wins = 0;

    public TicTacToeFrontendApp(JFrame window) {
        this.window = window;
    }

    public void setup() {
        window.setTitle("Tic Tac toe Game");
        window.setSize(800, 600);

        var baseLayout = new BorderLayout(5, 5);
        var mainView = new JPanel(baseLayout);

        JPanel topBar = setupTopBar();
        mainView.add(topBar, BorderLayout.NORTH);

        var grid = new GridLayout(3, 3);
        var ticTacToeButtons = new JPanel(grid);
        mainView.add(ticTacToeButtons, BorderLayout.CENTER);

        for (int i = 0; i < 9; i++) {
            var btn = new JButton("click me!");
            FieldPosition pos = FieldPosition.values()[i];
            btn.addActionListener(e -> buttonPressed(pos));
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.WHITE);
            ticTacToeButtons.add(btn);
        }
        window.add(mainView);
        window.setVisible(true);
    }

    private JPanel setupTopBar() {
        JPanel topBar = new JPanel(new GridLayout(1, 3));
        JLabel scoreLabel = new JLabel("Player 1 | 0 : 0 | Player 2");
        topBar.add(scoreLabel);
        JButton playWithFriendBtn = new JButton("Play with friend");
        playWithFriendBtn.addActionListener(e -> {
            Thread t = new Thread(() -> {
                System.out.println("Playing with a friend!");
                player2 = new SwingPlayer(this::awaitPlayerMove);
                game = new TicTacToeGame(player1, player2);
                game.start();
                if (game.getWinner() == player1) {
                    player1Wins++;
                } else if (game.getWinner() == player2) {
                    player2Wins++;
                }
                scoreLabel.setText("Player 1 | " + player1Wins + " : " + player2Wins + " | Player 2");
            });
            t.setDaemon(true);
            t.start();
        });

        topBar.add(playWithFriendBtn);
        JButton playWithComBtn = new JButton("Play against PC");
        playWithComBtn.setEnabled(false);
        topBar.add(playWithComBtn);
        return topBar;
    }

    private void buttonPressed(FieldPosition position) {
        if (Objects.nonNull(currentMove)) {
            currentMove.complete(position);
        }
    }

    public CompletableFuture<FieldPosition> awaitPlayerMove() {
        currentMove = new CompletableFuture<>();
        return currentMove;
    }
}
