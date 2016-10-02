/**
 * Created by dawehr on 9/30/2016.
 */
package com.davidawehr;

import java.util.ArrayList;


public class Board {
    final long[] positions;
    int whoSearching;

    public void setWho(int who) {
        whoSearching = who;
    }

    private static final byte[] checkDirections = {7, 8, 9, -1, 1, -9, -8, -7};
    private static final byte[] rowChanges = {
            -1, -1, -1,   // indices 0-2 (maps -9, -8, -7)
            9, 9, 9, 9, 9, // indices 3-7
            0, 9, 0, // indices 8-10 (maps -1, 1)
            9, 9, 9, 9, 9, // indices 11-15
            1, 1, 1 // indices 16-18 (maps (7, 8, 9)
    };

    private static final byte[] weights = {
            32, 5, 10, 5, 5, 10, 5, 32,
            5, -2, -3, -3, -3, -3 ,-2, 5,
            10, -3, 2, 1, 1, 2, -3, 10,
            5, -3, 1, 1, 1, 1, -3, 5,
            5, -3, 1, 1, 1, 1, -3, 5,
            10, -3, 2, 1, 1, 2, 3, 10,
            5, -2, -3, -3, -3, -3, -2, 5,
            32, 5, 10, 5, 5, 10, 5, 32
    };

    Board(long[] poss) {
        positions = poss;
    }

    Board() {
        this(new long[] {0x0000001008000000L, 0x0000000810000000L});
    }

    // Heuristic of how "good" the board is for black
    int heuristic() {
        if (whoSearching == 0) {
            long blackPositions = positions[0];
            long whitePositions = positions[1];
            int blackScore = 0;
            int whiteScore = 0;
            for (byte i = 0; i < 64; i++) {
                byte weight = weights[i];
                blackScore += ((blackPositions >> i) & 1) * weight;
                whiteScore += ((whitePositions >> i) & 1) * weight;
            }
            return blackScore - whiteScore;
        }
        else {
            return Long.bitCount(positions[0]) - Long.bitCount(positions[1]);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            long mask = (1L << i);
            if (i % 8 == 0) {
                out.append(Integer.toString(8 - (i/8)));
                out.append("|");
            }
            if ((positions[0] & mask) != 0) {
                out.append("X  ");
            } else if ((positions[1] & mask) != 0) {
                out.append("O  ");
            } else {
                out.append("-  ");
            }
            if (i % 8 == 7) {
                out.append("\n");
            }
        }
        out.append("  1  2  3  4  5  6  7  8");
        return out.toString();
    }


    // who: 0 if it is black's move, 1 if it is white's move
    ArrayList<Move> possibleActions(int who) {
        long myPieces = positions[who];
        long theirPieces = positions[1-who];

        ArrayList<Move> moves = new ArrayList<>();
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

                int expectedRows = rowChanges[checkDir+9] * (flipLength+1);
                int actualRows = (compareWith >> 3) - (i >> 3); // compareWith/3 - i/3
                long compareMask = (1L << compareWith);
                // Valid move
                if (flipLength > 0 && (compareMask & myPieces) != 0 && compareWith >= 0 && compareWith < 64 && expectedRows == actualRows) {
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

    boolean placePiece(int loc, int who) {
        long myPieces = positions[who];
        long theirPieces = positions[1-who];


        // If there is a piece at this location, skip it
        if ( ((myPieces | theirPieces) & (1L << loc)) != 0) {
            return false;
        }

        long theirNew = theirPieces;
        long myNew = myPieces;
        boolean validMove = false;

        for (byte checkDir : checkDirections) {
            int compareWith = loc + checkDir;
            int flipLength = 0;
            while (compareWith >= 0 && compareWith < 64 &&      // Within the board limits
                    ((1L << compareWith) & theirPieces) != 0 && // The opponent has a piece there
                    ((1L << compareWith) & myPieces) == 0) {    // I don't have a piece there
                flipLength += 1;
                compareWith += checkDir;
            }

            int expectedRows = rowChanges[checkDir+9] * (flipLength+1);
            int actualRows = (compareWith >> 3) - (loc >> 3); // compareWith/3 - loc/3
            long compareMask = (1L << compareWith);
            // Valid move
            if (flipLength > 0 && (compareMask & myPieces) != 0 && compareWith >= 0 && compareWith < 64 && expectedRows == actualRows) {
                validMove = true;
                int f, c;
                for (f = loc+checkDir, c = 0; c < flipLength; c += 1, f += checkDir) {
                    // Remove opponent's piece
                    theirNew &= ~(1L << f);
                    // Add my piece
                    myNew |= (1L << f);
                }
            }

            if (validMove) {
                // Add the newly placed piece
                myNew |= (1L << loc);
                positions[who] = myNew;
                positions[1-who] = theirNew;
            }
        }
        return validMove;
    }


    class Move {
        final Board resultBoard;
        final int placement;
        Move(Board b, int p) {
            resultBoard = b;
            placement = p;
        }
    }
}
