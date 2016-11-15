package com.davidawehr;

import java.util.*;
import java.util.concurrent.Callable;
import com.davidawehr.Board.Move;

/**
 * Created by dawehr on 9/24/2016.
 */
public class MiniMax {
    private static final int CUTOFF_DEPTH = 9;
    private static int expanded;

    static class NoMovesException extends Exception {
        NoMovesException(String message) {
            super(message);
        }
    }

    // Find the best move to make
    // who: who to find the best move for. 0 for black, 1 for white
    public static int minimax(Board board, int who) throws NoMovesException {
        expanded = 0;
        int best = Integer.MIN_VALUE;
        Board.Move bestMove = null;

        ArrayList<Board.Move> moves = (board.possibleActions(who));
        Collections.sort(moves);
        if (who == 0) {
            Collections.reverse(moves);
        }
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        for (Board.Move move : moves) {
            int quality;
            if (who == 0) {
                quality = minVal(move, 1, 1, alpha, beta);
                alpha = Math.max(alpha, quality);
            } else {
                quality = -maxVal(move, 0, 1, alpha, beta);
                beta = Math.min(beta, -quality);
            }
            if (quality > best) {
                best = quality;
                bestMove = move;
            }
        }
        if (bestMove == null) {
            throw new NoMovesException(String.format("No moves for %d", who));
        }
//        System.out.println("" + expanded );
//        System.out.println("Worst case: " + best * (who == 0 ? 1 : -1));
        return bestMove.placement;
    }


    private static int maxVal(Board.Move state, int who, int depth, int alpha, int beta) {
        if (depth == CUTOFF_DEPTH) {
            expanded += 1;
            return state.resultBoard.heuristic(true);
        }
        int max_val = Integer.MIN_VALUE;
        ArrayList<Move> moves = state.resultBoard.possibleActions(who);
        Collections.sort(moves);
        if (who == 0) {
            Collections.reverse(moves);
        }
        for (Board.Move move : moves) {
            max_val = Math.max(max_val, minVal(move, 1-who, depth+1, alpha, beta));
            if (max_val >= beta) {
                return max_val;
            }
            alpha = Math.max(alpha, max_val);
        }
        if (max_val == Integer.MIN_VALUE) {
            expanded += 1;
            return state.resultBoard.heuristic(false);
        } else {
            return max_val;
        }
    }

    private static int minVal(Board.Move state, int who, int depth, int alpha, int beta) {
        if (depth == CUTOFF_DEPTH) {
            expanded += 1;
            return state.resultBoard.heuristic(true);
        }
        int min_val = Integer.MAX_VALUE;
        ArrayList<Move> moves = state.resultBoard.possibleActions(who);
        Collections.sort(moves);
        if (who == 0) {
            Collections.reverse(moves);
        }
        for (Board.Move move : moves) {
            min_val = Math.min(min_val, maxVal(move, 1-who, depth+1, alpha, beta));
            if (min_val <= alpha) {
                return min_val;
            }
            beta = Math.min(beta, min_val);
        }
        if (min_val == Integer.MAX_VALUE) {
            expanded += 1;
            return state.resultBoard.heuristic(false);
        } else {
            return min_val;
        }
    }
}
