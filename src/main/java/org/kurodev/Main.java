package org.kurodev;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final JFrame window = new JFrame();

    public static void main(String[] args) {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("Tic Tac toe Game");
        window.setSize(800, 600);
        window.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 9; i++) {
            window.add(new JButton("Click me"));
        }
        window.setVisible(true);

//        Scanner in = new Scanner(System.in);
//        TicTacToeGame game = new TicTacToeGame(new HumanPlayer(in), new HumanPlayer(in));
//        game.start();
    }
}
