package com.davidawehr;

import java.util.ArrayList;

/**
 * Created by dawehr on 9/24/2016.
 */
public class Game {
    private class Board {
        final long[] positions;
        Board(long[] poss) {
            positions = poss;
        }

        // Heuristic of how "good" the board is for black
        double heuristic() {
            return Long.bitCount(positions[0]) - Long.bitCount(positions[1]);
        }

        @Override
        public String toString() {
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < 64; i++) {
                long mask = (1L << i);
                if ((positions[0] & mask) != 0) {
                    out.append("X ");
                } else if ((positions[1] & mask) != 0) {
                    out.append("O ");
                } else {
                    out.append("- ");
                }
                if (i % 8 == 7) {
                    out.append("\n");
                }
            }
            return out.toString();
        }
    }

    class Move {
        final Board resultBoard;
        final int placement;
        Move(Board b, int p) {
            resultBoard = b;
            placement = p;
        }
    }

    public Board board;
    public Game() {
        board = new Board(new long[]{0x0000001008000000L, 0x0000000810000000L});
    }

    // who: 0 if it is black's move, 1 if it is white's move
    public ArrayList<Move> possibleActions(Board curBoard, int who) {
        long myPieces = curBoard.positions[who];
        long theirPieces = curBoard.positions[1-who];

        ArrayList<Move> moves = new ArrayList<>();
        byte[] checkDirections = {7, 8, 9, -1, 0, 1, -9, -8, -7};
        // Look at all positions on board
        for (int i = 0; i < 64; i++) {
            // If there is a piece at this location, skip it
            if ( ((myPieces | theirPieces) & (1L << i)) != 0) {
                continue;
            }


            long theirNew = theirPieces;
            long myNew = myPieces;
            boolean validMove = false;

            for (byte checkDir : checkDirections) {
                int compareWith = i + checkDir;
                int flipLength = 0;
                while (compareWith >= 0 && compareWith < 64 &&      // Within the board limits
                        ((1L << compareWith) & theirPieces) != 0 && // The opponent has a piece there
                        ((1L << compareWith) & myPieces) == 0) {    // I don't have a piece there
                    flipLength += 1;
                    compareWith += checkDir;
                }

                long compareMask = (1L << compareWith);
                // Valid move
                if (flipLength > 0 && (compareMask & myPieces) != 0 && compareWith >= 0 && compareWith < 64) {
                    validMove = true;
                    int f, c;
                    for (f = i+checkDir, c = 0; c < flipLength; c += 1, f += checkDir) {
                        // Remove opponent's piece
                        theirNew &= ~(1L << f);
                        // Add my piece
                        myNew |= (1L << f);
                    }
                }
            }

            if (validMove) {
                // Add the newly placed piece
                myNew |= (1L << i);
                long[] resultPositions = new long[] {0,0};
                resultPositions[who] = myNew; resultPositions[1-who] = theirNew;
                moves.add(new Move(new Board(resultPositions), i));
            }
        }

        return moves;
    }
}
