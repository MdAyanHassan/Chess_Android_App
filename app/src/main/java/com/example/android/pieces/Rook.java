package com.example.android.pieces;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.android.chess.R;
import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Piece;
import com.example.android.mainClasses.Point;

public class Rook extends Piece {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Rook createFromParcel(Parcel source) {
            return new Rook(source);
        }

        @Override
        public Rook[] newArray(int size) {
            return new Rook[size];
        }
    };
    private boolean hasMoved = false;

    public Rook(int color, Point p) {
        super(color, p);
    }

    private Rook(Parcel in) {
        super(in.readInt(), (Point) in.readValue(Point.class.getClassLoader()));
        this.hasMoved = (boolean) (in.readValue(Boolean.class.getClassLoader()));
    }

    @Override
    public boolean isValidMove(Point newPoint, Board b, boolean test) {
        if (!super.isValidMove(newPoint, b, test))
            return false;

        //Piece attackingPiece = b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

        //Trying to move in a row
        if (this.getPieceLocation().getX() == newPoint.getX()) {
            int basicYDiff = newPoint.getY() - this.getPieceLocation().getY();

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
            int basicXDiff = newPoint.getX() - this.getPieceLocation().getX();

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

        //Disallowed move
        else {
            return false;
        }

        if (!test) {
            this.hasMoved = true;
        }
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return getColorID() == R.color.blackCell ? "\u265C" : "\u2656";
    }

    @Override
    public int hashCode() {
        return getColorID() == R.color.blackCell
                ? 7 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY()
                : 14 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY();
    }

    boolean isHasMoved() {
        return !hasMoved;
    }

    void setHasMoved() {
        this.hasMoved = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getColorID());
        dest.writeValue(getPieceLocation());
        dest.writeValue(hasMoved);
    }
}
