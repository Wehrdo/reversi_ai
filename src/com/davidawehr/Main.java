package com.davidawehr;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	    Game reversi = new Game();

        ArrayList<Game.Move> moves = reversi.possibleActions(reversi.board, 0);
//        moves = reversi.possibleActions(moves.get(0).resultBoard, 1);
//        moves = reversi.possibleActions(moves.get(1).resultBoard, 0);
        for (Game.Move move : moves) {
            System.out.println(move.resultBoard);
        }
    }
}
