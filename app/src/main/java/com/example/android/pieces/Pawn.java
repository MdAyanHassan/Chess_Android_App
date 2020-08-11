package com.example.android.pieces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.android.chess.R;
import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Cell;
import com.example.android.mainClasses.Piece;
import com.example.android.mainClasses.Point;

public class Pawn extends Piece {

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Pawn createFromParcel(Parcel source) {
            return new Pawn(source);
        }

        @Override
        public Pawn[] newArray(int size) {
            return new Pawn[size];
        }
    };
    private boolean hasMoved = false;
    private boolean canEnpassant = false;

    public Pawn(int color, Point p) {
        super(color, p);
    }

    private Pawn(Parcel in) {
        super(in.readInt(), (Point) in.readValue(Point.class.getClassLoader()));
        this.hasMoved = (boolean) (in.readValue(Boolean.class.getClassLoader()));
        this.canEnpassant = (boolean) (in.readValue(Boolean.class.getClassLoader()));
    }

    @Override
    public boolean isValidMove(Point newPoint, Board b, boolean test) {
        if (!super.isValidMove(newPoint, b, test))
            return false;

        int baseYDiff = newPoint.getY() - this.getPieceLocation().getY();
        int baseXDiff = newPoint.getX() - this.getPieceLocation().getX();
        int xDiff = Math.abs(newPoint.getX() - this.getPieceLocation().getX());
        int yDiff = Math.abs(newPoint.getY() - this.getPieceLocation().getY());


        //Back movement for white pawn
        if (baseXDiff > 0 && this.getColorID() == R.color.whiteCell)
            return false;

        //Back movement for black pawn
        if (baseXDiff < 0 && this.getColorID() == R.color.blackCell)
            return false;

        //Not more than two units
        if (xDiff > 2)
            return false;

        //Straight movement
        if (yDiff == 0) {

            //First movement then two units okay
            if (!hasMoved && xDiff == 2) {

                //there is a piece then straight movement disallowed
                if (this.getColorID() == R.color.whiteCell) {
                    Piece justNextPiece = b.getBoardArr()[newPoint.getX() + 1][newPoint.getY()].getPieceHolding();
                    Piece nextPiece = b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

                    if (justNextPiece != null || nextPiece != null) {
                        return false;
                    }
                } else {
                    Piece justNextPiece = b.getBoardArr()[newPoint.getX() - 1][newPoint.getY()].getPieceHolding();
                    Piece nextPiece = b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

                    if (justNextPiece != null || nextPiece != null) {
                        return false;
                    }
                }

                //Checking enpassant
                if (newPoint.getY() == 0) {
                    Piece sidePiece = b.getBoardArr()[newPoint.getX()][1].getPieceHolding();
                    if (sidePiece instanceof Pawn && sidePiece.getColorID() != this.getColorID()) {
                        if (!test) {
                            ((Pawn) sidePiece).canEnpassant = true;
                            b.setPieceAtALocation(sidePiece.getPieceLocation().getX(),
                                    sidePiece.getPieceLocation().getY(), sidePiece);
                        }

                    }
                } else if (newPoint.getY() == 7) {
                    Piece sidePiece = b.getBoardArr()[newPoint.getX()][6].getPieceHolding();
                    if (sidePiece instanceof Pawn && sidePiece.getColorID() != this.getColorID()) {
                        if (!test) {
                            ((Pawn) sidePiece).canEnpassant = true;
                            b.setPieceAtALocation(sidePiece.getPieceLocation().getX(),
                                    sidePiece.getPieceLocation().getY(), sidePiece);
                        }
                    }
                } else {
                    Piece leftSidePiece = b.getBoardArr()[newPoint.getX()][newPoint.getY() - 1].getPieceHolding();
                    if (leftSidePiece instanceof Pawn && leftSidePiece.getColorID() != this.getColorID()) {
                        if (!test) {
                            ((Pawn) leftSidePiece).canEnpassant = true;
                            b.setPieceAtALocation(leftSidePiece.getPieceLocation().getX(),
                                    leftSidePiece.getPieceLocation().getY(), leftSidePiece);
                        }
                    }
                    Piece sidePiece = b.getBoardArr()[newPoint.getX()][newPoint.getY() + 1].getPieceHolding();
                    if (sidePiece instanceof Pawn && sidePiece.getColorID() != this.getColorID()) {
                        if (!test) {
                            ((Pawn) sidePiece).canEnpassant = true;
                            b.setPieceAtALocation(sidePiece.getPieceLocation().getX(),
                                    sidePiece.getPieceLocation().getY(), sidePiece);
                        }
                    }
                }
            }

            //not first movement but two units then disallowed
            else if (hasMoved && xDiff == 2) {
                return false;
            }

            //only 1 unit then allowed
            else if (xDiff == 1) {
                //there should not be a piece
                if (b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding() != null) {
                    return false;
                }
            }
        } //straight movement finished

        //checking diagonal movement
        else if (yDiff == 1 && xDiff == 1) {
            Piece toCapturePiece = b.getBoardArr()[newPoint.getX()][newPoint.getY()].getPieceHolding();

            if (toCapturePiece instanceof King)
                return false;
            else if (toCapturePiece == null) {
                if (baseYDiff == -1) {
                    Cell toEnpassantCell = b.getBoardArr()[this.getPieceLocation().getX()][this.getPieceLocation().getY() - 1];
                    Piece toEnpassantPiece = toEnpassantCell.getPieceHolding();
                    if (toEnpassantPiece instanceof Pawn && toEnpassantPiece.getColorID() != this.getColorID() && this.canEnpassant) {
                        if (!test) {
                            this.hasMoved = true;
                            this.canEnpassant = false;
                            b.setPieceAtALocation(this.getPieceLocation().getX(), this.getPieceLocation().getY() - 1, null);
                        }
                        return true;
                    } else
                        return false;

                } else if (baseYDiff == 1) {
                    Cell toEnpassantCell = b.getBoardArr()[this.getPieceLocation().getX()][this.getPieceLocation().getY() + 1];
                    Piece toEnpassantPiece = toEnpassantCell.getPieceHolding();
                    if (toEnpassantPiece instanceof Pawn && toEnpassantPiece.getColorID() != this.getColorID() && this.canEnpassant) {
                        if (!test) {
                            this.hasMoved = true;
                            this.canEnpassant = false;
                            b.setPieceAtALocation(this.getPieceLocation().getX(), this.getPieceLocation().getY() + 1, null);
                        }
                        return true;
                    } else
                        return false;

                }
            } else {
                if (!test) {
                    this.hasMoved = true;
                    this.canEnpassant = false;
                }
                return true;
            }
        }

        //not straight not diagonal 1 unit then disallowed movement
        else {
            return false;
        }

        //if passed all conditions then correct move
        if (!test) {
            this.hasMoved = true;
            this.canEnpassant = false;
        }
        return true;
    }

    @Override
    public void move(Point newPoint, final Board playingBoard, Context context, boolean test) {
        super.move(newPoint, playingBoard, context, test);

        if (!test) {
            if (isPawnPromotion(this.getPieceLocation(), this.getColorID())) {
                final String[] pieces = this.getColorID() == R.color.whiteCell
                        ? new String[]{"\u2655", "\u2656", "\u2657", "\u2658"}
                        : new String[]{"\u265B", "\u265C", "\u265D", "\u265E"};
                AlertDialog.Builder dialogBox = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                dialogBox.setTitle("Choose a piece");
                dialogBox.setItems(pieces, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Piece queen = new Queen(getColorID(), getPieceLocation());
                                playingBoard.setPieceAtALocation(queen.getPieceLocation().getX(), queen.getPieceLocation().getY(), queen);
                                break;
                            case 1:
                                Piece rook = new Rook(getColorID(), getPieceLocation());
                                playingBoard.setPieceAtALocation(rook.getPieceLocation().getX(), rook.getPieceLocation().getY(), rook);
                                break;
                            case 2:
                                Piece bishop = new Bishop(getColorID(), getPieceLocation());
                                playingBoard.setPieceAtALocation(bishop.getPieceLocation().getX(), bishop.getPieceLocation().getY(), bishop);
                                break;
                            case 3:
                                Piece knight = new Knight(getColorID(), getPieceLocation());
                                playingBoard.setPieceAtALocation(knight.getPieceLocation().getX(), knight.getPieceLocation().getY(), knight);
                                break;

                        }
                    }
                });
                dialogBox.show();
            }
        }
    }

    private boolean isPawnPromotion(Point newPoint, int color) {
        int x = newPoint.getX();
        if (color == R.color.whiteCell) {
            return x == 0;
        } else {
            return x == 7;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getColorID() == R.color.blackCell ? "\u265F" : "\u2659";
    }

    @Override
    public int hashCode() {
        return getColorID() == R.color.blackCell
                ? 5 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY()
                : 10 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY();
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
        dest.writeValue(canEnpassant);
    }
}
