package org.kurodev;

import logic.PlayField;
import logic.Player;
import logic.PlayerSymbol;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SwingPlayer implements Player {


    private final Supplier<CompletableFuture<TicTacToeButtonEvent>> playerMoveListener;
    private PlayerSymbol symbol;

    public SwingPlayer(Supplier<CompletableFuture<TicTacToeButtonEvent>> playerMoveListener) {

        this.playerMoveListener = playerMoveListener;
    }

    @Override
    public void setSymbol(PlayerSymbol playerSymbol) {
        this.symbol = playerSymbol;
    }

    @Override
    public void doMove(PlayField playField) {
        System.out.println("its " + symbol + " turn");
        CompletableFuture<TicTacToeButtonEvent> playerChoice = playerMoveListener.get();
        Color playerColor = symbol == PlayerSymbol.XSymbol ? Color.BLUE.darker() : Color.RED.darker().darker();
        try {
            TicTacToeButtonEvent event = playerChoice.get();
            playField.placeSymbol(symbol, event.pos());
            JButton button = event.button();
            button.setBackground(playerColor);
            button.setText(symbol == PlayerSymbol.XSymbol ? "X" : "O");
            button.setEnabled(false);
            button.setFont(new Font("Arial", Font.PLAIN, 100));
            //here
            System.out.println(symbol + " put at " + event.pos());
        } catch (InterruptedException | ExecutionException e) {
            //should never happen.
            throw new RuntimeException(e);
        }

    }
}
