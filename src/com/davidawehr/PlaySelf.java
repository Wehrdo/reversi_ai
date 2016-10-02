package com.davidawehr;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by dawehr on 10/1/2016.
 */
public class PlaySelf {

    public static void main(String[] args) {
//	    Board board = new Board(new long[] {0x007871F44C850000L, 0x3E068E0BB37AFC3EL});
        Board board = new Board();
        ArrayList<Board.Move> moves = board.possibleActions(0);

        boolean blackHasMoves = true;
        boolean whiteHasMoves = true;
        while (true) {
            System.out.println(board);
            System.out.println();
            try {
                board.setWho(0);
                int ai_move = MiniMax.minimax(board, 0);
                blackHasMoves = true;
                board.placePiece(ai_move, 0);
            } catch (MiniMax.NoMovesException e) {
                System.out.println("Black has no moves");
                blackHasMoves = false;
            }

            try {
                board.setWho(1);
                int ai_move = MiniMax.minimax(board, 1);
                whiteHasMoves = true;
                board.placePiece(ai_move, 1);
            } catch (MiniMax.NoMovesException e) {
                System.out.println("White has no moves");
                whiteHasMoves = false;
            }

            if (!blackHasMoves && !whiteHasMoves) {
                System.out.println("Game over");
                System.out.println("Black score: " + Long.bitCount(board.positions[0]));
                System.out.println("White score: " + Long.bitCount(board.positions[1]));
                break;
            }
        }

    }
}
