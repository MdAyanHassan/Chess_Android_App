package com.example.android.mainClasses;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

import com.example.android.chess.AppContextHolder;
import com.example.android.chess.R;
import com.example.android.pieces.Bishop;
import com.example.android.pieces.King;
import com.example.android.pieces.Knight;
import com.example.android.pieces.Pawn;
import com.example.android.pieces.Queen;
import com.example.android.pieces.Rook;

public class Board implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new Board(source);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };
    private final Cell[][] boardArr = new Cell[8][8];

    private final Piece[] blackPiecesInstances = {new Rook(R.color.blackCell, new Point(0, 0))
            , new Knight(R.color.blackCell, new Point(0, 1))
            , new Bishop(R.color.blackCell, new Point(0, 2))
            , new Queen(R.color.blackCell, new Point(0, 3))
            , new King(R.color.blackCell, new Point(0, 4))
            , new Bishop(R.color.blackCell, new Point(0, 5))
            , new Knight(R.color.blackCell, new Point(0, 6))
            , new Rook(R.color.blackCell, new Point(0, 7))};

    private final Piece[] whitePiecesInstance = {new Rook(R.color.whiteCell, new Point(7, 0))
            , new Knight(R.color.whiteCell, new Point(7, 1))
            , new Bishop(R.color.whiteCell, new Point(7, 2))
            , new Queen(R.color.whiteCell, new Point(7, 3))
            , new King(R.color.whiteCell, new Point(7, 4))
            , new Bishop(R.color.whiteCell, new Point(7, 5))
            , new Knight(R.color.whiteCell, new Point(7, 6))
            , new Rook(R.color.whiteCell, new Point(7, 7))};
    private Context context = AppContextHolder.getC();

    public Board() {

    }

    private Board(Parcel in) {
        for (int i = 0; i < 8; i++) {
            Cell[] row = (Cell[]) in.readArray(Cell.class.getClassLoader());
            this.getBoardArr()[i] = row;
        }
    }

    public void makeBoard(LinearLayout[] rowsToFill) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
                , 1f);

        //For loop to make pieces

        for (int j = 0; j < 8; j++) {
            Cell blackPiece = new Cell(context
                    , blackPiecesInstances[j]
                    , new Point(0, j)
                    , j % 2 == 0 ? R.color.whiteCell : R.color.blackCell
                    , params);
            blackPiece.setFreezesText(true);
            blackPiece.setTextSize(25f);
            blackPiece.setText(blackPiece.getPieceHolding().toString());
            boardArr[0][j] = blackPiece;
            rowsToFill[0].addView(blackPiece);

            Cell whitePiece = new Cell(context
                    , whitePiecesInstance[j]
                    , new Point(7, j)
                    , j % 2 == 0 ? R.color.blackCell : R.color.whiteCell
                    , params);
            whitePiece.setFreezesText(true);
            whitePiece.setTextSize(25f);
            whitePiece.setText(whitePiece.getPieceHolding().toString());
            boardArr[7][j] = whitePiece;
            rowsToFill[7].addView(whitePiece);
        }

        //Nested for loop to make empty cells

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                Cell emptyCell = new Cell(context);
                emptyCell.setFreezesText(true);
                emptyCell.setTextSize(25f);
                emptyCell.setText(" ");
                emptyCell.setLayoutParams(params);
                emptyCell.setPieceHolding(null);
                emptyCell.setLocation(new Point(i, j));
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        emptyCell.setColorID(R.color.whiteCell);
                        emptyCell.setBackgroundResource(emptyCell.getColorID());
                    } else {
                        emptyCell.setColorID(R.color.blackCell);
                        emptyCell.setBackgroundResource(emptyCell.getColorID());
                    }
                } else {
                    if (j % 2 == 0) {
                        emptyCell.setColorID(R.color.blackCell);
                        emptyCell.setBackgroundResource(emptyCell.getColorID());
                    } else {
                        emptyCell.setColorID(R.color.whiteCell);
                        emptyCell.setBackgroundResource(emptyCell.getColorID());
                    }
                }
                boardArr[i][j] = emptyCell;
                rowsToFill[i].addView(emptyCell);
            }
        }

        //For loop to make pawns

        for (int j = 0; j < 8; j++) {
            Cell blackPawnCell = new Cell(
                    context
                    , new Pawn(R.color.blackCell, new Point(1, j))
                    , new Point(1, j)
                    , j % 2 == 0 ? R.color.blackCell : R.color.whiteCell
                    , params);
            blackPawnCell.setFreezesText(true);
            blackPawnCell.setText(blackPawnCell.getPieceHolding().toString());
            blackPawnCell.setTextSize(25f);
            boardArr[1][j] = blackPawnCell;
            rowsToFill[1].addView(blackPawnCell);

            Cell whitePawnCell = new Cell(
                    context
                    , new Pawn(R.color.whiteCell, new Point(6, j))
                    , new Point(6, j)
                    , j % 2 == 0 ? R.color.whiteCell : R.color.blackCell
                    , params);
            whitePawnCell.setFreezesText(true);
            whitePawnCell.setText(whitePawnCell.getPieceHolding().toString());
            whitePawnCell.setTextSize(25f);
            boardArr[6][j] = whitePawnCell;
            rowsToFill[6].addView(whitePawnCell);
        }
    }

    public Cell[][] getBoardArr() {
        return boardArr;
    }

    public void setPieceAtALocation(int x, int y, Piece piece) {
        if (piece == null) {
            boardArr[x][y].setPieceHolding(null);
            boardArr[x][y].setText(" ");
        } else {
            boardArr[x][y].setPieceHolding(piece);
            boardArr[x][y].setText(boardArr[x][y].getPieceHolding().toString());
        }

    }

    public Context getContext() {
        return context;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for (Cell[] row : boardArr) {
            dest.writeArray(row);
        }
    }
}
