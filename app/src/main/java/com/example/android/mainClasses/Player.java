package com.example.android.mainClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.pieces.King;

public class Player implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
    private King myKing;
    private Piece currentPiece;

    private Player(Parcel in) {
        this.myKing = (King) in.readValue(King.class.getClassLoader());
        this.currentPiece = (Piece) in.readValue(Piece.class.getClassLoader());
    }

    public Player(King myKing) {
        this.myKing = myKing;
    }

    Piece getCurrentPiece() {
        return currentPiece;
    }

    void setCurrentPiece(Piece currentPiece) {
        this.currentPiece = currentPiece;
    }

    void setKing(Piece piece) {
        this.myKing = (King) piece;
    }

    King getMyKing() {
        return myKing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(myKing);
        dest.writeValue(currentPiece);
    }
}
