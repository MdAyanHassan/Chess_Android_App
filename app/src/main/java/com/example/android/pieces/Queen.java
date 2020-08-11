package com.example.android.pieces;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.android.chess.R;
import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Piece;
import com.example.android.mainClasses.Point;

public class Queen extends Piece {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Queen createFromParcel(Parcel source) {
            return new Queen(source);
        }

        @Override
        public Queen[] newArray(int size) {
            return new Queen[size];
        }
    };

    public Queen(int color, Point p) {
        super(color, p);
    }

    private Queen(Parcel in) {
        super(in.readInt(), (Point) (in.readValue(Point.class.getClassLoader())));
    }

    @Override
    public boolean isValidMove(Point newPoint, Board b, boolean test) {
        if (!super.isValidMove(newPoint, b, test))
            return false;

        //Piece attackingPiece = b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

        int basicXDiff = newPoint.getX() - this.getPieceLocation().getX();
        int basicYDiff = newPoint.getY() - this.getPieceLocation().getY();

        int absXDiff = Math.abs(basicXDiff);
        int absYDiff = Math.abs(basicYDiff);

        //Trying to move in a row
        if (this.getPieceLocation().getX() == newPoint.getX()) {

            //Going right
            if (basicYDiff > 0) {
                for (int i = this.getPieceLocation().getY() + 1; i < newPoint.getY(); i++) {
                    Piece inLinePiece = b.getBoardArr()[newPoint.getX()][i].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                }
            }

            //Going left
            else {
                for (int i = this.getPieceLocation().getY() - 1; i > newPoint.getY(); i--) {
                    Piece inLinePiece = b.getBoardArr()[newPoint.getX()][i].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                }
            }
        }

        //Trying to move in a column
        else if (this.getPieceLocation().getY() == newPoint.getY()) {

            //Going down
            if (basicXDiff > 0) {
                for (int i = this.getPieceLocation().getX() + 1; i < newPoint.getX(); i++) {
                    Piece inLinePiece = b.getBoardArr()[i][newPoint.getY()].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                }
            }

            //Going up
            else {
                for (int i = this.getPieceLocation().getX() - 1; i > newPoint.getX(); i--) {
                    Piece inLinePiece = b.getBoardArr()[i][newPoint.getY()].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                }
            }
        }

        //Trying to move in a diagonal
        else if (absXDiff == absYDiff) {
            //Bottom right diagonal
            if (basicXDiff > 0 && basicYDiff > 0) {
                int y = this.getPieceLocation().getY() + 1;
                for (int i = this.getPieceLocation().getX() + 1; i < newPoint.getX(); i++) {
                    Piece inLinePiece = b.getBoardArr()[i][y].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                    y++;
                }
            }

            //Bottom left diagonal
            else if (basicXDiff > 0 && basicYDiff < 0) {
                int y = this.getPieceLocation().getY() - 1;
                for (int i = this.getPieceLocation().getX() + 1; i < newPoint.getX(); i++) {
                    Piece inLinePiece = b.getBoardArr()[i][y].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                    y--;
                }
            }

            //Upper right diagonal
            else if (basicXDiff < 0 && basicYDiff > 0) {
                int y = this.getPieceLocation().getY() + 1;
                for (int i = this.getPieceLocation().getX() - 1; i > newPoint.getX(); i--) {
                    Piece inLinePiece = b.getBoardArr()[i][y].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                    y++;
                }
            }

            //Upper left diagonal
            else if (basicXDiff < 0 && basicYDiff < 0) {
                int y = this.getPieceLocation().getY() - 1;
                for (int i = this.getPieceLocation().getX() - 1; i > newPoint.getX(); i--) {
                    Piece inLinePiece = b.getBoardArr()[i][y].getPieceHolding();
                    if (inLinePiece != null)
                        return false;
                    y--;
                }
            }

            //Weird movement disallowed
            else {
                return false;
            }
        }

        //Weird movement disallowed
        else {
            return false;
        }

        //Move allowed
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return getColorID() == R.color.blackCell ? "\u265B" : "\u2655";
    }

    @Override
    public int hashCode() {
        return getColorID() == R.color.blackCell
                ? 6 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY()
                : 12 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getColorID());
        dest.writeValue(getPieceLocation());
    }
}
