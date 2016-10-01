package com.davidawehr;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	    Board board = new Board(new long[] {0x007871F44C850000L, 0x3E068E0BB37AFC3EL});
//	    Board board = new Board();
        ArrayList<Board.Move> moves = board.possibleActions(0);

        boolean once = false;
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.println(board);
            int col, row;
            System.out.println("Enter column:");
            col = reader.nextInt();
            System.out.println("Enter row:");
            row = reader.nextInt();
            int placement = (8 * (7 - (row - 1))) + (col - 1);
            boolean valid;
            if (!once) {
                once = true;
                valid = true;
            } else {
                valid = board.placePiece(placement, 0);
            }
            if (valid) {
                System.out.println(board);
                System.out.println();
                try {
                    int ai_move = MiniMax.minimax(board, 1);
                    board.placePiece(ai_move, 1);
                } catch (MiniMax.NoMovesException e) {
                    System.out.println("No moves?");
                    break;
                }
            }
            else {
                System.out.println("Invalid move");
            }
        }

//
//        if (false) {
//            try {
//                System.out.println(MiniMax.minimax(board, 0));
//            }
//            catch (MiniMax.NoMovesException e) {System.out.println("No moves");}
//        }
//
//        int i = 0;
//        for (Board.Move move : moves) {
//            System.out.println(i++);
//            System.out.println(move.resultBoard);
//        }


    }
}
