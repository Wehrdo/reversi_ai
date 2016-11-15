package com.davidawehr;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by dawehr on 10/1/2016.
 */
public class PlaySelf {
    public static void main(String[] args) {
        LinkedList<Integer> positions;

//	    Board board = new Board(new long[] {0x007871F44C850000L, 0x3E068E0BB37AFC3EL});
        Board board = new Board();

        PrintWriter writer;
        try {
            writer = new PrintWriter("self_games.txt", "UTF-8");
        } catch (Exception e) {
            System.out.println("PW failed");
            return;
        }
//        boolean blackHasMoves = true;
//        boolean whiteHasMoves = true;
        for (int i = 0; i < 3; i++) {
            System.out.println(i);
            positions = new LinkedList<>();
            board = new Board();
            while (true) {
                boolean blackHasMoves = true;
                boolean whiteHasMoves = true;

//                System.out.println(board);
//                System.out.println();
                try {
//                    board.setWho(0);
                    int ai_move = MiniMax.minimax(board, 0);
                    board.placePiece(ai_move, 0);
                    positions.add(ai_move);
                } catch (MiniMax.NoMovesException e) {
                    System.out.println("Black has no moves");
                    blackHasMoves = false;
                }

                try {
//                    board.setWho(1);
                    int ai_move = MiniMax.minimax(board, 1);
                    board.placePiece(ai_move, 1);
                    positions.add(-ai_move);
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

            int score = Long.bitCount(board.positions[0]) - Long.bitCount(board.positions[1]);
            for (int placement: positions) {
                writer.println("" + placement + " " + score);
            }

        }
        writer.close();
    }


}
