package com.example.android.mainClasses;

import android.content.Context;
import android.os.Parcelable;

import com.example.android.pieces.King;

public abstract class Piece implements Parcelable {

    private final int colorID;
    private Point location;

    protected Piece(int color, Point p) {
        colorID = color;
        location = p;
    }

    public void move(Point newPoint, Board playingBoard, Context context, boolean test) {
        playingBoard.setPieceAtALocation(this.location.getX(), this.location.getY(), null);
        this.setPieceLocation(newPoint);
        playingBoard.setPieceAtALocation(newPoint.getX(), newPoint.getY(), this);
    }

    public boolean isValidMove(Point newPoint, Board board, boolean testing) {
        if (newPoint.getX() < 0 || newPoint.getX() > 7 || newPoint.getY() < 0 || newPoint.getY() > 7)
            return false;
        Piece ifPiece = board.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

        if (ifPiece instanceof King)
            return false;

        boolean res = true;
        if (ifPiece != null) {
            res = ifPiece.getColorID() != this.getColorID();
        }
        return res;
    }

    public Point getPieceLocation() {
        return location;
    }

    void setPieceLocation(Point p) {
        this.location = p;
    }

    public int getColorID() {
        return colorID;
    }
}
