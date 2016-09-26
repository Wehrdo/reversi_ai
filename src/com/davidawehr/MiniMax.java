package com.davidawehr;
import com.davidawehr.Game.Board;
import com.davidawehr.Game.Move;

/**
 * Created by dawehr on 9/24/2016.
 */
public class MiniMax {
    private static final int CUTOFF_DEPTH = 8;

    static class NoMovesException extends Exception {
        public NoMovesException(String message) {
            super(message);
        }
    }

    // Find the best move to make
    // who: who to find the best move for. 0 for black, 1 for white
    public static int minimax(Board board, int who) throws NoMovesException {
        if (who == 0) {
            double best = Double.NEGATIVE_INFINITY;
            Move bestMove = null;
            for (Move move : Game.possibleActions(board, 0)) {
                double quality = minVal(move, 1, 1);
                if (quality > best) {
                    best = quality;
                    bestMove = move;
                }
            }
            if (bestMove == null) {
                throw new NoMovesException(String.format("No moves for %d", who));
            }
            return bestMove.placement;
        }
        // TODO; Implement for black
        return -1;
    }

    private static double maxVal(Move state, int who, int depth) {
        if (depth == CUTOFF_DEPTH) {
            return state.resultBoard.heuristic();
        }
        double max_val = Double.NEGATIVE_INFINITY;
        for (Move move : Game.possibleActions(state.resultBoard, who)) {
            max_val = Math.max(max_val, minVal(move, 1-who, depth+1));
        }
        return max_val;
    }

    private static double minVal(Move state, int who, int depth) {
        if (depth == CUTOFF_DEPTH) {
            return state.resultBoard.heuristic();
        }
        double min_val = Double.POSITIVE_INFINITY;
        for (Move move : Game.possibleActions(state.resultBoard, who)) {
            min_val = Math.min(min_val, maxVal(move, 1-who, depth+1));
        }
        return min_val;
    }
}
