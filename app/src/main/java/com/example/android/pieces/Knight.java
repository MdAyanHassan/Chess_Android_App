package com.example.android.pieces;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.android.chess.R;
import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Piece;
import com.example.android.mainClasses.Point;

public class Knight extends Piece {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Knight createFromParcel(Parcel source) {
            return new Knight(source);
        }

        @Override
        public Knight[] newArray(int size) {
            return new Knight[size];
        }
    };
    //The allowed distance is root of 5
    private static final float ALLOWED_DISTANCE = (float) Math.sqrt(5);

    public Knight(int color, Point p) {
        super(color, p);
    }

    private Knight(Parcel in) {
        super(in.readInt(), (Point) (in.readValue(Point.class.getClassLoader())));
    }

    @Override
    public boolean isValidMove(Point newPoint, Board b, boolean test) {
        if (!super.isValidMove(newPoint, b, test))
            return false;

        //Calculating difference in x coords and y coords
        int xDiff = Math.abs(newPoint.getX() - this.getPieceLocation().getX());
        int yDiff = Math.abs(newPoint.getY() - this.getPieceLocation().getY());

        //pythagorean theorem to calculate distance
        float distance = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

        return distance == ALLOWED_DISTANCE;
    }

    @NonNull
    @Override
    public String toString() {
        return getColorID() == R.color.blackCell ? "\u265E" : "\u2658";
    }

    @Override
    public int hashCode() {
        return getColorID() == R.color.blackCell
                ? 4 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY()
                : 8 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY();
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
