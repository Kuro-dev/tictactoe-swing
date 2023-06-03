package org.kurodev;

import logic.FieldPosition;

import javax.swing.*;

public record TicTacToeButtonEvent(FieldPosition pos, JButton button) {
}
