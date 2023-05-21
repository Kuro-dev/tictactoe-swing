package org.kurodev;

import logic.FieldPosition;
import logic.PlayField;
import logic.Player;
import logic.PlayerSymbol;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SwingPlayer implements Player {


    private final Supplier<CompletableFuture<FieldPosition>> playerMoveListener;
    private PlayerSymbol symbol;

    public SwingPlayer(Supplier<CompletableFuture<FieldPosition>> playerMoveListener) {

        this.playerMoveListener = playerMoveListener;
    }

    @Override
    public void setSymbol(PlayerSymbol playerSymbol) {
        this.symbol = playerSymbol;
    }

    @Override
    public void doMove(PlayField playField) {
        System.out.println("its " + symbol + " turn");
        CompletableFuture<FieldPosition> playerChoice = playerMoveListener.get();
        try {
            FieldPosition position = playerChoice.get();
            playField.placeSymbol(symbol, position);
            //here
            System.out.println(symbol + " put at " + position);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
