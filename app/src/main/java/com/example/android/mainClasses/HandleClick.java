package com.example.android.mainClasses;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.chess.AppContextHolder;
import com.example.android.chess.R;
import com.example.android.pieces.Bishop;
import com.example.android.pieces.King;
import com.example.android.pieces.Knight;
import com.example.android.pieces.Pawn;
import com.example.android.pieces.Queen;
import com.example.android.pieces.Rook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandleClick implements View.OnClickListener, Parcelable {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public HandleClick createFromParcel(Parcel source) {
            return new HandleClick(source);
        }

        @Override
        public HandleClick[] newArray(int size) {
            return new HandleClick[size];
        }
    };
    private Board playingBoard;
    private TextView colorView;
    private TextView turnText;
    private Player whitePlayer;
    private Player blackPlayer;
    private int stateOfCells = 1;
    private boolean isWhiteTurn = true;
    private Cell storeClickedCell;
    private TextView pieceImage;

    public HandleClick(Board playingBoard, Player whitePlayer, Player blackPlayer, TextView color, TextView text, TextView pieceImage) {
        this.playingBoard = playingBoard;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.colorView = color;
        this.turnText = text;
        this.pieceImage = pieceImage;
    }

    private HandleClick(Parcel in) {
        this.playingBoard = (Board) in.readValue(Board.class.getClassLoader());
        this.turnText = ((AppCompatActivity) AppContextHolder.getC()).findViewById(R.id.turn_text);
        turnText.setText(in.readString());
        this.pieceImage = ((AppCompatActivity) AppContextHolder.getC()).findViewById(R.id.piece_selected);
        pieceImage.setText(in.readString());
        this.colorView = ((AppCompatActivity) AppContextHolder.getC()).findViewById(R.id.color_view);
        String colorViewStr = in.readString();
        colorView.setText(colorViewStr);
        colorView.setBackgroundResource(colorViewStr.equals("") ? R.color.whiteCell : R.color.blackCell);
        this.whitePlayer = (Player) in.readValue(Player.class.getClassLoader());
        this.blackPlayer = (Player) in.readValue(Player.class.getClassLoader());
        this.storeClickedCell = (Cell) in.readValue(Cell.class.getClassLoader());
        this.stateOfCells = in.readInt();
        this.isWhiteTurn = (boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public void setColorView(TextView colorView) {
        this.colorView = colorView;
    }

    public void setTurnText(TextView turnText) {
        this.turnText = turnText;
    }

    public void setPieceImage(TextView pieceImage) {
        this.pieceImage = pieceImage;
    }

    private void destroyBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                playingBoard.getBoardArr()[i][j].setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (stateOfCells == 1) {
            storeClickedCell = (Cell) v;
            Piece clickedPiece = storeClickedCell.getPieceHolding();
            if (clickedPiece == null) {
                Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.invalid_move), Toast.LENGTH_SHORT).show();
                storeClickedCell = null;
            } else {
                int clickedPieceColor = clickedPiece.getColorID();
                if (isWhiteTurn) {
                    if (clickedPieceColor == R.color.blackCell) {
                        Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.illegal_piece), Toast.LENGTH_SHORT).show();
                        storeClickedCell = null;
                    } else {
                        whitePlayer.setCurrentPiece(clickedPiece);
                        pieceImage.setText(clickedPiece.toString());
                        stateOfCells = 2;
                    }
                } else {
                    if (clickedPieceColor == R.color.whiteCell) {
                        Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.illegal_piece), Toast.LENGTH_SHORT).show();
                        storeClickedCell = null;
                    } else {
                        blackPlayer.setCurrentPiece(clickedPiece);
                        pieceImage.setText(clickedPiece.toString());
                        stateOfCells = 2;
                    }
                }
            }
        } else {
            Cell destCell = (Cell) v;
            pieceImage.setText(" ");
            if (isWhiteTurn) {
                Piece testPiece = whitePlayer.getCurrentPiece();
                if (testPiece.isValidMove(destCell.getLocation(), playingBoard, true)) {
                    playingBoard.getBoardArr()[storeClickedCell.getLocation().getX()][storeClickedCell.getLocation().getY()] = storeClickedCell;
                    playingBoard.getBoardArr()[destCell.getLocation().getX()][destCell.getLocation().getY()] = destCell;
                    Board copyBoard = copyBoard(playingBoard);
                    testPiece.move(destCell.getLocation(), copyBoard, copyBoard.getContext(), true);
                    List<List<Point>> checkingPoints = getCheckingPoints(R.color.whiteCell, copyBoard);
                    testPiece.setPieceLocation(storeClickedCell.getLocation());
                    if (checkingPoints.size() > 0) {
                        Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.king_invalid), Toast.LENGTH_SHORT).show();
                        storeClickedCell = null;
                        stateOfCells = 1;
                        isWhiteTurn = true;
                        whitePlayer.setCurrentPiece(null);
                    } else {
                        testPiece.isValidMove(destCell.getLocation(), playingBoard, false);
                        testPiece.move(destCell.getLocation(), playingBoard, playingBoard.getContext(), false);
                        if (testPiece instanceof King) {
                            whitePlayer.setKing(testPiece);
                        }

                        isWhiteTurn = false;
                        stateOfCells = 1;
                        whitePlayer.setCurrentPiece(null);
                        storeClickedCell = null;
                        colorView.setBackgroundResource(R.color.blackCell);
                        colorView.setText(" ");
                        turnText.setText(R.string.turn_message_black);
                    }
                } else {
                    Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.invalid_move), Toast.LENGTH_SHORT).show();
                    isWhiteTurn = true;
                    storeClickedCell = null;
                    whitePlayer.setCurrentPiece(null);
                    stateOfCells = 1;
                }

            } else {
                Piece testPiece = blackPlayer.getCurrentPiece();
                if (testPiece.isValidMove(destCell.getLocation(), playingBoard, true)) {
                    playingBoard.getBoardArr()[storeClickedCell.getLocation().getX()][storeClickedCell.getLocation().getY()] = storeClickedCell;
                    playingBoard.getBoardArr()[destCell.getLocation().getX()][destCell.getLocation().getY()] = destCell;
                    Board copyBoard = copyBoard(playingBoard);
                    testPiece.move(destCell.getLocation(), copyBoard, copyBoard.getContext(), true);
                    List<List<Point>> checkingPoints = getCheckingPoints(R.color.blackCell, copyBoard);
                    testPiece.setPieceLocation(storeClickedCell.getLocation());
                    if (checkingPoints.size() > 0) {
                        Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.king_invalid), Toast.LENGTH_SHORT).show();
                        isWhiteTurn = false;
                        stateOfCells = 1;
                        blackPlayer.setCurrentPiece(null);
                        storeClickedCell = null;
                    } else {
                        testPiece.isValidMove(destCell.getLocation(), playingBoard, false);
                        testPiece.move(destCell.getLocation(), playingBoard, playingBoard.getContext(), false);
                        if (testPiece instanceof King) {
                            blackPlayer.setKing(testPiece);
                        }

                        isWhiteTurn = true;
                        stateOfCells = 1;
                        blackPlayer.setCurrentPiece(null);
                        storeClickedCell = null;
                        colorView.setBackgroundResource(R.color.whiteCell);
                        colorView.setText("");
                        turnText.setText(R.string.turn_message_white);
                    }
                } else {
                    Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.invalid_move), Toast.LENGTH_SHORT).show();
                    isWhiteTurn = false;
                    storeClickedCell = null;
                    blackPlayer.setCurrentPiece(null);
                    stateOfCells = 1;
                }
            }
            List<List<Point>> currentCheckingPoints = getCheckingPoints(isWhiteTurn ? R.color.whiteCell : R.color.blackCell);
            boolean isStalemate = isStalemate(currentCheckingPoints, isWhiteTurn ? R.color.whiteCell : R.color.blackCell);
            if (isStalemate) {
                if (currentCheckingPoints.size() > 0) {
                    String msg = isWhiteTurn ? AppContextHolder.getC().getResources().getString(R.string.black_wins_message) : AppContextHolder.getC().getResources().getString(R.string.white_wins_message);
                    Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), AppContextHolder.getC().getResources().getString(R.string.draw_game_message), Toast.LENGTH_SHORT).show();
                }
                destroyBoard();
            }
        }
    }

    private List<List<Point>> getCheckingPoints(int color) {
        King kingToCheck = color == R.color.blackCell ? blackPlayer.getMyKing() : whitePlayer.getMyKing();
        List<List<Point>> checkingPoints = new ArrayList<>();
        int justNextX = kingToCheck.getPieceLocation().getX(), justNextY = kingToCheck.getPieceLocation().getY();
        boolean foundNotDiagonal = false;

        //Diagonals and pawn checking
        if (color == R.color.whiteCell) {

            boolean foundInDiagonalWhite = false;
            //Checking upper left pawn && upper right pawn
            Piece upperLeftPawn = null,
                    upperRightPawn = null;

            if (justNextX != 0) {
                if (justNextY == 0) {
                    upperRightPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY + 1].getPieceHolding();
                } else if (justNextY == 7) {
                    upperLeftPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY - 1].getPieceHolding();
                } else {
                    upperLeftPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY - 1].getPieceHolding();
                    upperRightPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY + 1].getPieceHolding();
                }
            }

            //Checking lower left diagonal
            List<Point> lowerLeftDiagonal = new ArrayList<>();
            int lowerLeftX = justNextX + 1;
            for (int y = justNextY - 1; y >= 0; y--) {
                if (lowerLeftX > 7)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[lowerLeftX][y].getPieceHolding();
                lowerLeftDiagonal.add(playingBoard.getBoardArr()[lowerLeftX][y].getLocation());
                if (inLinePiece == null) {
                    lowerLeftX++;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                        && lowerLeftX == justNextX + 1 && y == justNextY - 1) {
                    foundInDiagonalWhite = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                    lowerLeftX++;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.whiteCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                    foundInDiagonalWhite = true;
                    break;
                }

                lowerLeftX++;
            }

            if (foundInDiagonalWhite)
                checkingPoints.add(lowerLeftDiagonal);

            foundInDiagonalWhite = false;

            //Checking lower right diagonal
            List<Point> lowerRightDiagonal = new ArrayList<>();
            int lowerRightX = justNextX + 1;
            for (int y = justNextY + 1; y < 8; y++) {
                if (lowerRightX > 7)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[lowerRightX][y].getPieceHolding();
                lowerRightDiagonal.add(playingBoard.getBoardArr()[lowerRightX][y].getLocation());
                if (inLinePiece == null) {
                    lowerRightX++;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                        && lowerRightX == justNextX + 1 && y == justNextY + 1) {
                    foundInDiagonalWhite = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                    lowerRightX++;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.whiteCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                    foundInDiagonalWhite = true;
                    break;
                }
                lowerRightX++;
            }


            if (foundInDiagonalWhite)
                checkingPoints.add(lowerRightDiagonal);

            foundInDiagonalWhite = false;

            //If there is an opposite pawn attacking then return false and do not check that diagonal

            if (upperLeftPawn instanceof Pawn && upperLeftPawn.getColorID() != R.color.whiteCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(upperLeftPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            } else {
                //Upper left diagonal
                List<Point> upperLeftDiagonal = new ArrayList<>();
                int upperLeftX = justNextX - 1;
                for (int y = justNextY - 1; y >= 0; y--) {
                    if (upperLeftX < 0)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[upperLeftX][y].getPieceHolding();
                    upperLeftDiagonal.add(playingBoard.getBoardArr()[upperLeftX][y].getLocation());
                    if (inLinePiece == null) {
                        upperLeftX--;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                            && upperLeftX == justNextX - 1 && y == justNextY - 1) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                        upperLeftX--;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.whiteCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    upperLeftX--;
                }

                if (foundInDiagonalWhite)
                    checkingPoints.add(upperLeftDiagonal);

                foundInDiagonalWhite = false;
            }

            //If there is an opposite pawn attacking then return false and do not check that diagonal
            if (upperRightPawn instanceof Pawn && upperRightPawn.getColorID() != R.color.whiteCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(upperRightPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            }
            //else check that diagonal for the attack of opposite bishop or queen
            else {
                //Upper right diagonal
                List<Point> upperRightDiagonal = new ArrayList<>();
                int upperRightX = justNextX - 1;
                for (int y = justNextY + 1; y < 8; y++) {
                    if (upperRightX < 0)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[upperRightX][y].getPieceHolding();
                    upperRightDiagonal.add(playingBoard.getBoardArr()[upperRightX][y].getLocation());
                    if (inLinePiece == null) {
                        upperRightX--;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                            && upperRightX == justNextX - 1 && y == justNextY + 1) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                        upperRightX--;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.whiteCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    upperRightX--;
                }

                if (foundInDiagonalWhite)
                    checkingPoints.add(upperRightDiagonal);

            }

        } else {
            //Checking lower left pawn && lower right pawn
            Piece lowerLeftPawn = null,
                    lowerRightPawn = null;

            if (justNextX != 7) {
                if (justNextY == 0)
                    lowerRightPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY + 1].getPieceHolding();
                else if (justNextY == 7)
                    lowerLeftPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY - 1].getPieceHolding();
                else {
                    lowerLeftPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY - 1].getPieceHolding();
                    lowerRightPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY + 1].getPieceHolding();
                }
            }
            boolean foundInDiagonalBlack = false;

            //Upper left diagonal
            List<Point> upperLeftDiagonal = new ArrayList<>();
            int upperLeftX = justNextX - 1;
            for (int y = justNextY - 1; y >= 0; y--) {
                if (upperLeftX < 0)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[upperLeftX][y].getPieceHolding();
                upperLeftDiagonal.add(playingBoard.getBoardArr()[upperLeftX][y].getLocation());
                if (inLinePiece == null) {
                    upperLeftX--;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                        && upperLeftX == justNextX - 1 && y == justNextY - 1) {
                    foundInDiagonalBlack = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                    upperLeftX--;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.blackCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                    foundInDiagonalBlack = true;
                    break;
                }
                upperLeftX--;
            }

            if (foundInDiagonalBlack)
                checkingPoints.add(upperLeftDiagonal);

            foundInDiagonalBlack = false;

            //Checking upper right diagonal
            List<Point> upperRightDiagonal = new ArrayList<>();
            int upperRightX = justNextX - 1;
            for (int y = justNextY + 1; y < 8; y++) {
                if (upperRightX < 0)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[upperRightX][y].getPieceHolding();
                upperRightDiagonal.add(playingBoard.getBoardArr()[upperRightX][y].getLocation());
                if (inLinePiece == null) {
                    upperRightX--;
                    continue;
                }
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                        && upperRightX == justNextX - 1 && y == justNextY + 1) {
                    foundInDiagonalBlack = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                    upperRightX--;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.blackCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                    foundInDiagonalBlack = true;
                    break;
                }
                upperRightX--;
            }
            if (foundInDiagonalBlack)
                checkingPoints.add(upperRightDiagonal);

            foundInDiagonalBlack = false;

            //check if there is an opposite pawn at lower left diagonal
            if (lowerLeftPawn instanceof Pawn && lowerLeftPawn.getColorID() != R.color.blackCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(lowerLeftPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            } else {
                //Lower left diagonal
                List<Point> lowerLeftDiagonal = new ArrayList<>();
                int lowerLeftX = justNextX + 1;
                for (int y = justNextY - 1; y >= 0; y--) {
                    if (lowerLeftX > 7)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[lowerLeftX][y].getPieceHolding();
                    lowerLeftDiagonal.add(playingBoard.getBoardArr()[lowerLeftX][y].getLocation());
                    if (inLinePiece == null) {
                        lowerLeftX++;
                        continue;
                    }
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                            && lowerLeftX == justNextX + 1 && y == justNextY - 1) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                        lowerLeftX++;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.blackCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    lowerLeftX++;
                }
                if (foundInDiagonalBlack)
                    checkingPoints.add(lowerLeftDiagonal);

                foundInDiagonalBlack = false;
            }

            //check if there is an opposite pawn at lower right diagonal
            if (lowerRightPawn instanceof Pawn && lowerRightPawn.getColorID() != R.color.blackCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(lowerRightPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            }

            //if no opposite pawn then check that diagonal for the attack of the opposite queen or bishop
            else {
                //Lower right diagonal
                List<Point> lowerRightDiagonal = new ArrayList<>();
                int lowerRightX = justNextX + 1;
                for (int y = justNextY + 1; y < 8; y++) {
                    if (lowerRightX > 7)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[lowerRightX][y].getPieceHolding();
                    lowerRightDiagonal.add(playingBoard.getBoardArr()[lowerRightX][y].getLocation());
                    if (inLinePiece == null) {
                        lowerRightX++;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                            && lowerRightX == justNextX + 1 && y == justNextY + 1) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                        lowerRightX++;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.blackCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    lowerRightX++;
                }
                if (foundInDiagonalBlack)
                    checkingPoints.add(lowerRightDiagonal);
            }
        }

        //Diagonals checking finished

        //Rows and cols checking
        //Upper front col
        List<Point> upperFrontCol = new ArrayList<>();
        for (int upperFrontX = justNextX - 1; upperFrontX >= 0; upperFrontX--) {
            Piece inLinePiece = playingBoard.getBoardArr()[upperFrontX][justNextY].getPieceHolding();
            upperFrontCol.add(playingBoard.getBoardArr()[upperFrontX][justNextY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && upperFrontX == justNextX - 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(upperFrontCol);

        foundNotDiagonal = false;

        //Lower front col
        List<Point> lowerFrontCol = new ArrayList<>();
        for (int lowerFrontX = justNextX + 1; lowerFrontX < 8; lowerFrontX++) {
            Piece inLinePiece = playingBoard.getBoardArr()[lowerFrontX][justNextY].getPieceHolding();
            lowerFrontCol.add(playingBoard.getBoardArr()[lowerFrontX][justNextY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && lowerFrontX == justNextX + 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(lowerFrontCol);

        foundNotDiagonal = false;

        //Right row
        List<Point> rightRow = new ArrayList<>();
        for (int rightY = justNextY + 1; rightY < 8; rightY++) {
            Piece inLinePiece = playingBoard.getBoardArr()[justNextX][rightY].getPieceHolding();
            rightRow.add(playingBoard.getBoardArr()[justNextX][rightY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && rightY == justNextY + 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(rightRow);

        foundNotDiagonal = false;

        //Left row
        List<Point> leftRow = new ArrayList<>();
        for (int leftY = justNextY - 1; leftY >= 0; leftY--) {
            Piece inLinePiece = playingBoard.getBoardArr()[justNextX][leftY].getPieceHolding();
            leftRow.add(playingBoard.getBoardArr()[justNextX][leftY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && leftY == justNextY - 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(leftRow);

        //Knights check
        Point[] knightsPosition = {new Point(justNextX - 1, justNextY + 2),
                new Point(justNextX - 2, justNextY + 1),
                new Point(justNextX - 2, justNextY - 1),
                new Point(justNextX - 1, justNextY - 2),
                new Point(justNextX + 1, justNextY - 2),
                new Point(justNextX + 2, justNextY - 1),
                new Point(justNextX + 2, justNextY + 1),
                new Point(justNextX + 1, justNextY + 2)};

        for (Point p : knightsPosition) {
            int xCoord = p.getX(), yCoord = p.getY();
            if (xCoord > 7 || xCoord < 0 || yCoord > 7 || yCoord < 0)
                continue;
            Piece locatePiece = playingBoard.getBoardArr()[xCoord][yCoord].getPieceHolding();
            if (locatePiece instanceof Knight && locatePiece.getColorID() != color) {
                List<Point> knightAttack = new ArrayList<>();
                knightAttack.add(locatePiece.getPieceLocation());
                checkingPoints.add(knightAttack);
                break;
            }
        }
        return checkingPoints;
    }

    private List<List<Point>> getCheckingPoints(int color, Board playingBoard) {
        King kingToCheck = color == R.color.blackCell ? blackPlayer.getMyKing() : whitePlayer.getMyKing();
        List<List<Point>> checkingPoints = new ArrayList<>();
        int justNextX = kingToCheck.getPieceLocation().getX(), justNextY = kingToCheck.getPieceLocation().getY();
        boolean foundNotDiagonal = false;

        //Diagonals and pawn checking
        if (color == R.color.whiteCell) {

            boolean foundInDiagonalWhite = false;
            //Checking upper left pawn && upper right pawn
            Piece upperLeftPawn = null,
                    upperRightPawn = null;

            if (justNextX != 0) {
                if (justNextY == 0) {
                    upperRightPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY + 1].getPieceHolding();
                } else if (justNextY == 7) {
                    upperLeftPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY - 1].getPieceHolding();
                } else {
                    upperLeftPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY - 1].getPieceHolding();
                    upperRightPawn = playingBoard.getBoardArr()[justNextX - 1][justNextY + 1].getPieceHolding();
                }
            }

            //Checking lower left diagonal
            List<Point> lowerLeftDiagonal = new ArrayList<>();
            int lowerLeftX = justNextX + 1;
            for (int y = justNextY - 1; y >= 0; y--) {
                if (lowerLeftX > 7)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[lowerLeftX][y].getPieceHolding();
                lowerLeftDiagonal.add(playingBoard.getBoardArr()[lowerLeftX][y].getLocation());
                if (inLinePiece == null) {
                    lowerLeftX++;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                        && lowerLeftX == justNextX + 1 && y == justNextY - 1) {
                    foundInDiagonalWhite = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                    lowerLeftX++;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.whiteCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                    foundInDiagonalWhite = true;
                    break;
                }

                lowerLeftX++;
            }

            if (foundInDiagonalWhite)
                checkingPoints.add(lowerLeftDiagonal);

            foundInDiagonalWhite = false;

            //Checking lower right diagonal
            List<Point> lowerRightDiagonal = new ArrayList<>();
            int lowerRightX = justNextX + 1;
            for (int y = justNextY + 1; y < 8; y++) {
                if (lowerRightX > 7)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[lowerRightX][y].getPieceHolding();
                lowerRightDiagonal.add(playingBoard.getBoardArr()[lowerRightX][y].getLocation());
                if (inLinePiece == null) {
                    lowerRightX++;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                        && lowerRightX == justNextX + 1 && y == justNextY + 1) {
                    foundInDiagonalWhite = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                    lowerRightX++;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.whiteCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                    foundInDiagonalWhite = true;
                    break;
                }
                lowerRightX++;
            }

            if (foundInDiagonalWhite)
                checkingPoints.add(lowerRightDiagonal);

            foundInDiagonalWhite = false;

            //If there is an opposite pawn attacking then return false and do not check that diagonal

            if (upperLeftPawn instanceof Pawn && upperLeftPawn.getColorID() != R.color.whiteCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(upperLeftPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            } else {
                //Upper left diagonal
                List<Point> upperLeftDiagonal = new ArrayList<>();
                int upperLeftX = justNextX - 1;
                for (int y = justNextY - 1; y >= 0; y--) {
                    if (upperLeftX < 0)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[upperLeftX][y].getPieceHolding();
                    upperLeftDiagonal.add(playingBoard.getBoardArr()[upperLeftX][y].getLocation());
                    if (inLinePiece == null) {
                        upperLeftX--;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                            && upperLeftX == justNextX - 1 && y == justNextY - 1) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                        upperLeftX--;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.whiteCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    upperLeftX--;
                }

                if (foundInDiagonalWhite)
                    checkingPoints.add(upperLeftDiagonal);

                foundInDiagonalWhite = false;
            }

            //If there is an opposite pawn attacking then return false and do not check that diagonal
            if (upperRightPawn instanceof Pawn && upperRightPawn.getColorID() != R.color.whiteCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(upperRightPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            }
            //else check that diagonal for the attack of opposite bishop or queen
            else {
                //Upper right diagonal
                List<Point> upperRightDiagonal = new ArrayList<>();
                int upperRightX = justNextX - 1;
                for (int y = justNextY + 1; y < 8; y++) {
                    if (upperRightX < 0)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[upperRightX][y].getPieceHolding();
                    upperRightDiagonal.add(playingBoard.getBoardArr()[upperRightX][y].getLocation());
                    if (inLinePiece == null) {
                        upperRightX--;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                            && upperRightX == justNextX - 1 && y == justNextY + 1) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                        upperRightX--;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.whiteCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell)) {
                        foundInDiagonalWhite = true;
                        break;
                    }
                    upperRightX--;
                }

                if (foundInDiagonalWhite)
                    checkingPoints.add(upperRightDiagonal);

            }

        } else {
            //Checking lower left pawn && lower right pawn
            Piece lowerLeftPawn = null,
                    lowerRightPawn = null;

            if (justNextX != 7) {
                if (justNextY == 0)
                    lowerRightPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY + 1].getPieceHolding();
                else if (justNextY == 7)
                    lowerLeftPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY - 1].getPieceHolding();
                else {
                    lowerLeftPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY - 1].getPieceHolding();
                    lowerRightPawn = playingBoard.getBoardArr()[justNextX + 1][justNextY + 1].getPieceHolding();
                }
            }
            boolean foundInDiagonalBlack = false;

            //Upper left diagonal
            List<Point> upperLeftDiagonal = new ArrayList<>();
            int upperLeftX = justNextX - 1;
            for (int y = justNextY - 1; y >= 0; y--) {
                if (upperLeftX < 0)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[upperLeftX][y].getPieceHolding();
                upperLeftDiagonal.add(playingBoard.getBoardArr()[upperLeftX][y].getLocation());
                if (inLinePiece == null) {
                    upperLeftX--;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                        && upperLeftX == justNextX - 1 && y == justNextY - 1) {
                    foundInDiagonalBlack = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                    upperLeftX--;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.blackCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                    foundInDiagonalBlack = true;
                    break;
                }
                upperLeftX--;
            }

            if (foundInDiagonalBlack)
                checkingPoints.add(upperLeftDiagonal);

            foundInDiagonalBlack = false;

            //Checking upper right diagonal
            List<Point> upperRightDiagonal = new ArrayList<>();
            int upperRightX = justNextX - 1;
            for (int y = justNextY + 1; y < 8; y++) {
                if (upperRightX < 0)
                    break;
                Piece inLinePiece = playingBoard.getBoardArr()[upperRightX][y].getPieceHolding();
                upperRightDiagonal.add(playingBoard.getBoardArr()[upperRightX][y].getLocation());
                if (inLinePiece == null) {
                    upperRightX--;
                    continue;
                }
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                        && upperRightX == justNextX - 1 && y == justNextY + 1) {
                    foundInDiagonalBlack = true;
                    break;
                }
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                    upperRightX--;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.blackCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                    foundInDiagonalBlack = true;
                    break;
                }
                upperRightX--;
            }
            if (foundInDiagonalBlack)
                checkingPoints.add(upperRightDiagonal);

            foundInDiagonalBlack = false;

            //check if there is an opposite pawn at lower left diagonal
            if (lowerLeftPawn instanceof Pawn && lowerLeftPawn.getColorID() != R.color.blackCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(lowerLeftPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            } else {
                //Lower left diagonal
                List<Point> lowerLeftDiagonal = new ArrayList<>();
                int lowerLeftX = justNextX + 1;
                for (int y = justNextY - 1; y >= 0; y--) {
                    if (lowerLeftX > 7)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[lowerLeftX][y].getPieceHolding();
                    lowerLeftDiagonal.add(playingBoard.getBoardArr()[lowerLeftX][y].getLocation());
                    if (inLinePiece == null) {
                        lowerLeftX++;
                        continue;
                    }
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                            && lowerLeftX == justNextX + 1 && y == justNextY - 1) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                        lowerLeftX++;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.blackCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    lowerLeftX++;
                }
                if (foundInDiagonalBlack)
                    checkingPoints.add(lowerLeftDiagonal);

                foundInDiagonalBlack = false;
            }

            //check if there is an opposite pawn at lower right diagonal
            if (lowerRightPawn instanceof Pawn && lowerRightPawn.getColorID() != R.color.blackCell) {
                List<Point> pawnAttack = new ArrayList<>();
                pawnAttack.add(lowerRightPawn.getPieceLocation());
                checkingPoints.add(pawnAttack);
            }

            //if no opposite pawn then check that diagonal for the attack of the opposite queen or bishop
            else {
                //Lower right diagonal
                List<Point> lowerRightDiagonal = new ArrayList<>();
                int lowerRightX = justNextX + 1;
                for (int y = justNextY + 1; y < 8; y++) {
                    if (lowerRightX > 7)
                        break;
                    Piece inLinePiece = playingBoard.getBoardArr()[lowerRightX][y].getPieceHolding();
                    lowerRightDiagonal.add(playingBoard.getBoardArr()[lowerRightX][y].getLocation());
                    if (inLinePiece == null) {
                        lowerRightX++;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                            && lowerRightX == justNextX + 1 && y == justNextY + 1) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                        lowerRightX++;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.blackCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell)) {
                        foundInDiagonalBlack = true;
                        break;
                    }
                    lowerRightX++;
                }
                if (foundInDiagonalBlack)
                    checkingPoints.add(lowerRightDiagonal);
            }
        }

        //Diagonals checking finished

        //Rows and cols checking
        //Upper front col
        List<Point> upperFrontCol = new ArrayList<>();
        for (int upperFrontX = justNextX - 1; upperFrontX >= 0; upperFrontX--) {
            Piece inLinePiece = playingBoard.getBoardArr()[upperFrontX][justNextY].getPieceHolding();
            upperFrontCol.add(playingBoard.getBoardArr()[upperFrontX][justNextY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && upperFrontX == justNextX - 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(upperFrontCol);

        foundNotDiagonal = false;

        //Lower front col
        List<Point> lowerFrontCol = new ArrayList<>();
        for (int lowerFrontX = justNextX + 1; lowerFrontX < 8; lowerFrontX++) {
            Piece inLinePiece = playingBoard.getBoardArr()[lowerFrontX][justNextY].getPieceHolding();
            lowerFrontCol.add(playingBoard.getBoardArr()[lowerFrontX][justNextY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && lowerFrontX == justNextX + 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(lowerFrontCol);

        foundNotDiagonal = false;

        //Right row
        List<Point> rightRow = new ArrayList<>();
        for (int rightY = justNextY + 1; rightY < 8; rightY++) {
            Piece inLinePiece = playingBoard.getBoardArr()[justNextX][rightY].getPieceHolding();
            rightRow.add(playingBoard.getBoardArr()[justNextX][rightY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && rightY == justNextY + 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(rightRow);

        foundNotDiagonal = false;

        //Left row
        List<Point> leftRow = new ArrayList<>();
        for (int leftY = justNextY - 1; leftY >= 0; leftY--) {
            Piece inLinePiece = playingBoard.getBoardArr()[justNextX][leftY].getPieceHolding();
            leftRow.add(playingBoard.getBoardArr()[justNextX][leftY].getLocation());

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && leftY == justNextY - 1) {
                foundNotDiagonal = true;
                break;
            }
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color)) {
                foundNotDiagonal = true;
                break;
            }
        }

        if (foundNotDiagonal)
            checkingPoints.add(leftRow);

        //Knights check
        Point[] knightsPosition = {new Point(justNextX - 1, justNextY + 2),
                new Point(justNextX - 2, justNextY + 1),
                new Point(justNextX - 2, justNextY - 1),
                new Point(justNextX - 1, justNextY - 2),
                new Point(justNextX + 1, justNextY - 2),
                new Point(justNextX + 2, justNextY - 1),
                new Point(justNextX + 2, justNextY + 1),
                new Point(justNextX + 1, justNextY + 2)};

        for (Point p : knightsPosition) {
            int xCoord = p.getX(), yCoord = p.getY();
            if (xCoord > 7 || xCoord < 0 || yCoord > 7 || yCoord < 0)
                continue;
            Piece locatePiece = playingBoard.getBoardArr()[xCoord][yCoord].getPieceHolding();
            if (locatePiece instanceof Knight && locatePiece.getColorID() != color) {
                List<Point> knightAttack = new ArrayList<>();
                knightAttack.add(locatePiece.getPieceLocation());
                checkingPoints.add(knightAttack);
                break;
            }
        }
        return checkingPoints;
    }

    //Implementation needed
    private boolean isStalemate(List<List<Point>> checkingPoints, int color) {
        int checks = checkingPoints.size();
        if (checks == 0) {
            for (Cell[] cells : playingBoard.getBoardArr()) {
                for (Cell c : cells) {
                    Piece p = c.getPieceHolding();
                    if (p == null)
                        continue;
                    if (p.getColorID() == color)
                        if (hasValidMove(p))
                            return false;
                }
            }
        } else if (checks == 1) {
            King kingInCheck = color == R.color.whiteCell ? whitePlayer.getMyKing() : blackPlayer.getMyKing();
            int x = kingInCheck.getPieceLocation().getX(), y = kingInCheck.getPieceLocation().getY();
            Point[] possibleKingPoints = {new Point(x + 1, y),
                    new Point(x + 1, y + 1),
                    new Point(x, y + 1),
                    new Point(x - 1, y + 1),
                    new Point(x - 1, y),
                    new Point(x - 1, y - 1),
                    new Point(x, y - 1),
                    new Point(x + 1, y - 1),
                    new Point(x, y + 2),
                    new Point(x, y - 2)};
            if (hasValidMove(kingInCheck, Arrays.asList(possibleKingPoints)))
                return false;

            for (Cell[] cells : playingBoard.getBoardArr())
                for (Cell c : cells) {
                    Piece p = c.getPieceHolding();
                    if (p == null)
                        continue;
                    if (p.getColorID() == color)
                        for (List<Point> possiblePoints : checkingPoints)
                            if (hasValidMove(p, possiblePoints))
                                return false;
                }
        } else {
            King kingInCheck = color == R.color.whiteCell ? whitePlayer.getMyKing() : blackPlayer.getMyKing();
            int x = kingInCheck.getPieceLocation().getX(), y = kingInCheck.getPieceLocation().getY();
            Point[] possibleKingPoints = {new Point(x + 1, y),
                    new Point(x + 1, y + 1),
                    new Point(x, y + 1),
                    new Point(x - 1, y + 1),
                    new Point(x - 1, y),
                    new Point(x - 1, y - 1),
                    new Point(x, y - 1),
                    new Point(x + 1, y - 1),
                    new Point(x, y + 2),
                    new Point(x, y - 2)};
            return !hasValidMove(kingInCheck, Arrays.asList(possibleKingPoints));
        }
        return true;
    }

    private boolean hasValidMove(Piece piece) {
        for (Cell[] cells : playingBoard.getBoardArr())
            for (Cell c : cells)
                if (piece.isValidMove(c.getLocation(), playingBoard, true))
                    return true;
        return false;
    }

    private boolean hasValidMove(Piece piece, List<Point> pointsToGo) {
        for (Point point : pointsToGo)
            if (piece.isValidMove(point, playingBoard, true))
                return true;
        return false;
    }

    private Board copyBoard(Board b) {
        Board board = new Board();
        for (int i = 0; i < b.getBoardArr().length; i++) {
            for (int j = 0; j < b.getBoardArr().length; j++) {
                Cell toCopyCell = b.getBoardArr()[i][j];
                Cell copyCell = new Cell(toCopyCell.getContext(),
                        toCopyCell.getPieceHolding(),
                        toCopyCell.getLocation(),
                        toCopyCell.getColorID(),
                        (LinearLayout.LayoutParams) toCopyCell.getLayoutParams());
                board.getBoardArr()[i][j] = copyCell;
            }
        }
        return board;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(playingBoard);
        dest.writeString(turnText.getText().toString());
        dest.writeString(pieceImage.getText().toString());
        dest.writeString(colorView.getText().toString());
        dest.writeValue(whitePlayer);
        dest.writeValue(blackPlayer);
        dest.writeValue(storeClickedCell);
        dest.writeInt(stateOfCells);
        dest.writeValue(isWhiteTurn);
    }
}
