package com.example.android.mainClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

import com.example.android.chess.AppContextHolder;

public class Cell extends androidx.appcompat.widget.AppCompatButton implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Cell createFromParcel(Parcel source) {
            return new Cell(source);
        }

        @Override
        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
    private static final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
            , 1f);
    private int colorID;
    private Point location;
    private Piece pieceHolding;

    public Cell(Context context) {
        super(context);
    }

    public Cell(Context context, Piece p, Point l, int color, LinearLayout.LayoutParams params) {
        super(context);
        this.pieceHolding = p;
        this.location = l;
        this.colorID = color;
        this.setBackgroundResource(colorID);
        this.setLayoutParams(params);
    }

    private Cell(Parcel in) {
        super(AppContextHolder.getC());
        this.colorID = in.readInt();
        this.location = (Point) (in.readValue(Point.class.getClassLoader()));
        this.pieceHolding = (Piece) (in.readValue(Piece.class.getClassLoader()));
        this.setLayoutParams(params);
        this.setBackgroundResource(colorID);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public Piece getPieceHolding() {
        return pieceHolding;
    }

    public void setPieceHolding(Piece p) {
        this.pieceHolding = p;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int id) {
        this.colorID = id;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point p) {
        this.location = p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(colorID);
        dest.writeValue(location);
        dest.writeValue(pieceHolding);
    }
}
