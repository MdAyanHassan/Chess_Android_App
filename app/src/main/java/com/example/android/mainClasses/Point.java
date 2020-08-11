package com.example.android.mainClasses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Point implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Point createFromParcel(Parcel source) {
            return new Point(source);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
    private int x;
    private int y;

    private Point(Parcel in) {
        this.x = in.readInt();
        this.y = in.readInt();
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Point))
            return false;
        Point other = (Point) obj;

        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
    }
}
