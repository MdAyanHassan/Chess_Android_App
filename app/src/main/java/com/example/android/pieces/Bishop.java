package com.example.android.pieces;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.android.chess.R;
import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Piece;
import com.example.android.mainClasses.Point;

public class Bishop extends Piece {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Bishop createFromParcel(Parcel source) {
            return new Bishop(source);
        }

        @Override
        public Bishop[] newArray(int size) {
            return new Bishop[size];
        }
    };

    public Bishop(int color, Point p) {
        super(color, p);
    }

    private Bishop(Parcel in) {
        super(in.readInt(), (Point) (in.readValue(Point.class.getClassLoader())));
    }

    @Override
    public boolean isValidMove(Point newPoint, Board b, boolean testing) {
        if (!super.isValidMove(newPoint, b, testing))
            return false;

        //Piece attackingPiece = b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

        int basicXDiff = newPoint.getX() - this.getPieceLocation().getX();
        int basicYDiff = newPoint.getY() - this.getPieceLocation().getY();

        int absXDiff = Math.abs(basicXDiff);
        int absYDiff = Math.abs(basicYDiff);

        if (absXDiff != absYDiff)
            return false;

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

        //Passed all conditions then good move
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return getColorID() == R.color.blackCell ? "\u265D" : "\u2657";
    }

    @Override
    public int hashCode() {
        return getColorID() == R.color.blackCell
                ? 2 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY()
                : 4 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY();
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
