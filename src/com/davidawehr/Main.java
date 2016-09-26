package com.davidawehr;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	    Game reversi = new Game();

        ArrayList<Game.Move> moves = Game.possibleActions(reversi.board, 0);
        moves = Game.possibleActions(moves.get(0).resultBoard, 1);
        moves = Game.possibleActions(moves.get(0).resultBoard, 0);
        moves = Game.possibleActions(moves.get(0).resultBoard, 1);
        moves = Game.possibleActions(moves.get(2).resultBoard, 0);
        moves = Game.possibleActions(moves.get(2).resultBoard, 1);

        if (true) {
            try {
                System.out.println(MiniMax.minimax(moves.get(3).resultBoard, 0));
            }
            catch (MiniMax.NoMovesException e) {System.out.println("No moves");}
        }

        moves = Game.possibleActions(moves.get(3).resultBoard, 0);
        int i = 0;
        for (Game.Move move : moves) {
            System.out.println(i++);
            System.out.println(move.resultBoard);
        }


    }
}
