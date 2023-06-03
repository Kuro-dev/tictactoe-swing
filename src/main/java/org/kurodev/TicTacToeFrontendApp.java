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
    CompletableFuture<TicTacToeButtonEvent> currentMove = null;
    private final Player player1 = new SwingPlayer(this::awaitPlayerMove);
    JButton[] buttons = new JButton[9];
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
            var btn = new JButton("Start the game you idiot");
            FieldPosition pos = FieldPosition.values()[i];
            btn.addActionListener(e -> buttonPressed(pos, btn));
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.WHITE);
            ticTacToeButtons.add(btn);
            btn.setEnabled(false);
            buttons[i] = btn;
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
            //new thread to avoid blocking the GUI thread, which would lead to the application Freezing/being unusable
            Thread thread = new Thread(() -> {
                System.out.println("Playing with a friend!");
                player2 = new SwingPlayer(this::awaitPlayerMove);
                game = new TicTacToeGame(player1, player2);
                resetButtons();
                //TODO figure out who starts
                game.start();
                //Game is over
                for (JButton button : buttons) {
                    button.setEnabled(false);
                }
                if (game.getWinner() == player1) {
                    player1Wins++;
                } else if (game.getWinner() == player2) {
                    player2Wins++;
                }
                scoreLabel.setText("Player 1 | " + player1Wins + " : " + player2Wins + " | Player 2");
            });
            thread.setDaemon(true);
            thread.start();
        });

        topBar.add(playWithFriendBtn);
        JButton playWithComBtn = new JButton("Play against PC");
        playWithComBtn.setEnabled(false);
        topBar.add(playWithComBtn);
        return topBar;
    }

    private void resetButtons() {
        for (JButton button : buttons) {
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setText("click me!");
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.setEnabled(true);
        }
    }

    private void buttonPressed(FieldPosition position, JButton btn) {
        if (Objects.nonNull(currentMove)) {
            currentMove.complete(new TicTacToeButtonEvent(position, btn));
        }
    }

    public CompletableFuture<TicTacToeButtonEvent> awaitPlayerMove() {
        currentMove = new CompletableFuture<>();
        return currentMove;
    }
}
