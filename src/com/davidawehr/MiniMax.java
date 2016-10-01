package com.davidawehr;

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
        double best = Double.NEGATIVE_INFINITY;
        Board.Move bestMove = null;
        for (Board.Move move : board.possibleActions(who)) {
            double quality;
            if (who == 0) {
                quality = minVal(move, 1, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
            } else {
                quality = -maxVal(move, 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
            }
            if (quality > best) {
                best = quality;
                bestMove = move;
            }
        }
        if (bestMove == null) {
            throw new NoMovesException(String.format("No moves for %d", who));
        }
        System.out.println("Best outcome: " + bestMove.resultBoard.heuristic());
        return bestMove.placement;
    }

    private static double maxVal(Board.Move state, int who, int depth, double alpha, double beta) {
        if (depth == CUTOFF_DEPTH) {
            return state.resultBoard.heuristic();
        }
        double max_val = Double.NEGATIVE_INFINITY;
        for (Board.Move move : state.resultBoard.possibleActions(who)) {
            max_val = Math.max(max_val, minVal(move, 1-who, depth+1, alpha, beta));
            if (max_val >= beta) {
                return max_val;
            }
            alpha = Math.max(alpha, max_val);
        }
        if (max_val == Double.NEGATIVE_INFINITY) {
            return state.resultBoard.heuristic();
        } else {
            return max_val;
        }
    }

    private static double minVal(Board.Move state, int who, int depth, double alpha, double beta) {
        if (depth == CUTOFF_DEPTH) {
            return state.resultBoard.heuristic();
        }
        double min_val = Double.POSITIVE_INFINITY;
        for (Board.Move move : state.resultBoard.possibleActions(who)) {
            min_val = Math.min(min_val, maxVal(move, 1-who, depth+1, alpha, beta));
            if (min_val <= alpha) {
                return min_val;
            }
            beta = Math.min(beta, min_val);
        }
        if (min_val == Double.POSITIVE_INFINITY) {
            return state.resultBoard.heuristic();
        } else {
            return min_val;
        }
    }
}
