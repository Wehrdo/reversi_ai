package com.davidawehr;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        int best = Integer.MIN_VALUE;
        Board.Move bestMove = null;

        ExecutorService pool = Executors.newFixedThreadPool(7);
        HashMap<Board.Move, Future<Integer>> moveQuality = new HashMap<>();

        ArrayList<Board.Move> moves = (board.possibleActions(who));
        Collections.shuffle(moves);
        for (Board.Move move : moves) {
//            double quality;
//            if (who == 0) {
//                quality = minVal(move, 1, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
//            } else {
//                quality = -maxVal(move, 0, 1, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
//            }
//            if (quality > best) {
//                best = quality;
//                bestMove = move;
//            }
            Callable<Integer> callable = new DoSearch(who, move);
            Future<Integer> future = pool.submit(callable);
            moveQuality.put(move, future);
        }
        for (Map.Entry<Board.Move, Future<Integer>> entry : moveQuality.entrySet()) {
            Board.Move move = entry.getKey();
            int quality = Integer.MIN_VALUE;
            try {
                quality = entry.getValue().get();
            } catch (Exception e) {
                System.out.println("Exception running thread");
            }
            if (quality > best) {
                best = quality;
                bestMove = move;
            }
        }
        if (bestMove == null) {
            throw new NoMovesException(String.format("No moves for %d", who));
        }
        System.out.println("Worst case: " + best * (who == 0 ? 1 : -1));
        return bestMove.placement;
    }

    private static class DoSearch implements Callable<Integer> {
        private int who;
        private Board.Move testMove;
        public DoSearch(int who, Board.Move move) {
            this.who = who;
            testMove = move;
        }

        @Override
        public Integer call() {
            if (who == 0) {
                return minVal(testMove, 1, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else {
                return -maxVal(testMove, 0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
        }
    }

    private static int maxVal(Board.Move state, int who, int depth, int alpha, int beta) {
        if (depth == CUTOFF_DEPTH) {
            return state.resultBoard.heuristic();
        }
        int max_val = Integer.MIN_VALUE;
        for (Board.Move move : state.resultBoard.possibleActions(who)) {
            max_val = Math.max(max_val, minVal(move, 1-who, depth+1, alpha, beta));
            if (max_val >= beta) {
                return max_val;
            }
            alpha = Math.max(alpha, max_val);
        }
        if (max_val == Integer.MIN_VALUE) {
            return state.resultBoard.heuristic();
        } else {
            return max_val;
        }
    }

    private static int minVal(Board.Move state, int who, int depth, int alpha, int beta) {
        if (depth == CUTOFF_DEPTH) {
            return state.resultBoard.heuristic();
        }
        int min_val = Integer.MAX_VALUE;
        for (Board.Move move : state.resultBoard.possibleActions(who)) {
            min_val = Math.min(min_val, maxVal(move, 1-who, depth+1, alpha, beta));
            if (min_val <= alpha) {
                return min_val;
            }
            beta = Math.min(beta, min_val);
        }
        if (min_val == Integer.MAX_VALUE) {
            return state.resultBoard.heuristic();
        } else {
            return min_val;
        }
    }
}
