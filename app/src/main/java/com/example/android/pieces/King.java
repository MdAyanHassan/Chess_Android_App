package com.example.android.pieces;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.android.chess.R;
import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Cell;
import com.example.android.mainClasses.Piece;
import com.example.android.mainClasses.Point;

public class King extends Piece {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public King createFromParcel(Parcel source) {
            return new King(source);
        }

        @Override
        public King[] newArray(int size) {
            return new King[size];
        }
    };
    private boolean canCastle = true;

    public King(int color, Point p) {
        super(color, p);
    }

    private King(Parcel in) {
        super(in.readInt(), (Point) in.readValue(Point.class.getClassLoader()));
        this.canCastle = (boolean) in.readValue(Boolean.class.getClassLoader());
    }

    @Override
    public boolean isValidMove(Point newPoint, Board b, boolean testing) {
        if (!super.isValidMove(newPoint, b, testing))
            return false;

        //Trying castling kingSide for white piece
        if (this.getColorID() == R.color.whiteCell
                && this.getPieceLocation().equals(new Point(7, 4))
                && newPoint.equals(new Point(7, 6))) {

            Cell justNextCell = b.getBoardArr()[7][5],
                    nextCell = b.getBoardArr()[7][6],
                    rookCell = b.getBoardArr()[7][7];

            Piece justNext = justNextCell.getPieceHolding(),
                    next = nextCell.getPieceHolding(),
                    rook = rookCell.getPieceHolding();
            if (!(canCastle
                    && isValidSquare(this.getPieceLocation(), b, R.color.whiteCell)
                    && justNext == null
                    && next == null
                    && rook instanceof Rook
                    && ((Rook) rook).isHasMoved()
                    && isValidSquare(justNextCell.getLocation(), b, this.getColorID())
                    && isValidSquare(nextCell.getLocation(), b, this.getColorID()))) {
                return false;
            } else {
                if (!testing) {
                    canCastle = false;
                    ((Rook) rook).setHasMoved();
                    rook.move(justNextCell.getLocation(), b, b.getContext(), false);
                }
                return true;
            }
        }

        //Trying castling queenSide for white piece
        else if (this.getColorID() == R.color.whiteCell
                && this.getPieceLocation().equals(new Point(7, 4))
                && newPoint.equals(new Point(7, 2))) {

            Cell justNextCell = b.getBoardArr()[7][3],
                    nextCell = b.getBoardArr()[7][2],
                    rookPathCell = b.getBoardArr()[7][1];

            Piece justNext = justNextCell.getPieceHolding(),
                    next = nextCell.getPieceHolding(),
                    rook = b.getBoardArr()[7][0].getPieceHolding(),
                    rookPathCellPiece = rookPathCell.getPieceHolding();
            if (!(canCastle
                    && isValidSquare(this.getPieceLocation(), b, R.color.whiteCell)
                    && justNext == null
                    && next == null
                    && rook instanceof Rook
                    && ((Rook) rook).isHasMoved()
                    && rookPathCellPiece == null
                    && isValidSquare(justNextCell.getLocation(), b, this.getColorID())
                    && isValidSquare(nextCell.getLocation(), b, this.getColorID()))) {
                return false;
            } else {
                if (!testing) {
                    canCastle = false;
                    ((Rook) rook).setHasMoved();
                    rook.move(justNextCell.getLocation(), b, b.getContext(), false);
                }
                return true;
            }

        }

        //Trying castling kingSide for black piece
        else if (this.getColorID() == R.color.blackCell
                && this.getPieceLocation().equals(new Point(0, 4))
                && newPoint.equals(new Point(0, 6))) {

            Cell justNextCell = b.getBoardArr()[0][5],
                    nextCell = b.getBoardArr()[0][6];

            Piece justNext = justNextCell.getPieceHolding(),
                    next = nextCell.getPieceHolding(),
                    rook = b.getBoardArr()[0][7].getPieceHolding();
            if (!(canCastle
                    && isValidSquare(this.getPieceLocation(), b, R.color.blackCell)
                    && justNext == null
                    && next == null
                    && rook instanceof Rook
                    && ((Rook) rook).isHasMoved()
                    && isValidSquare(justNextCell.getLocation(), b, this.getColorID())
                    && isValidSquare(nextCell.getLocation(), b, this.getColorID()))) {
                return false;
            } else {
                if (!testing) {
                    canCastle = false;
                    ((Rook) rook).setHasMoved();
                    rook.move(justNextCell.getLocation(), b, b.getContext(), false);
                }
                return true;
            }
        }

        //Trying castling queen side for black piece
        else if (this.getColorID() == R.color.blackCell
                && this.getPieceLocation().equals(new Point(0, 4))
                && newPoint.equals(new Point(0, 2))) {

            Cell justNextCell = b.getBoardArr()[0][3],
                    nextCell = b.getBoardArr()[0][2],
                    rookPathCell = b.getBoardArr()[0][1];

            Piece justNext = justNextCell.getPieceHolding(),
                    next = nextCell.getPieceHolding(),
                    rook = b.getBoardArr()[0][0].getPieceHolding(),
                    rookPathCellPiece = rookPathCell.getPieceHolding();
            if (!(canCastle
                    && isValidSquare(this.getPieceLocation(), b, R.color.blackCell)
                    && justNext == null
                    && next == null
                    && rook instanceof Rook
                    && ((Rook) rook).isHasMoved()
                    && rookPathCellPiece == null
                    && isValidSquare(justNextCell.getLocation(), b, this.getColorID())
                    && isValidSquare(nextCell.getLocation(), b, this.getColorID()))) {
                return false;
            } else {
                if (!testing) {
                    canCastle = false;
                    ((Rook) rook).setHasMoved();
                    rook.move(justNextCell.getLocation(), b, b.getContext(), false);
                }
                return true;
            }
        }

        //Not a castling move then a normal move maybe
        else {
            int newX = newPoint.getX(), newY = newPoint.getY();
            int xDiff = Math.abs(newX - this.getPieceLocation().getX()), yDiff = Math.abs(newY - this.getPieceLocation().getY());
            if (xDiff >= 2 || yDiff >= 2) {
                return false;
            }
            if (!(isValidSquare(newPoint, b, this.getColorID()))) {
                return false;
            }
        }

        //Passes all checks then move is good
        if (!testing) {
            canCastle = false;
        }
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return getColorID() == R.color.blackCell ? "\u265A" : "\u2654";
    }

    @Override
    public int hashCode() {
        return getColorID() == R.color.blackCell
                ? 3 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY()
                : 6 * 31 + this.getPieceLocation().getX() + this.getPieceLocation().getY();
    }

    private boolean isValidSquare(Point justNext, Board b, int color) {

        //Getting x and y coordinates
        int justNextX = justNext.getX(), justNextY = justNext.getY();

        //Checking diagonals
        //if color is white then check for upper left pawn or upper right pawn
        if (color == R.color.whiteCell) {
            //Checking upper left pawn && upper right pawn
            Piece upperLeftPawn = null,
                    upperRightPawn = null;

            if (justNextX != 0) {
                if (justNextY == 0) {
                    upperRightPawn = b.getBoardArr()[justNextX - 1][justNextY + 1].getPieceHolding();
                } else if (justNextY == 7) {
                    upperLeftPawn = b.getBoardArr()[justNextX - 1][justNextY - 1].getPieceHolding();
                } else {
                    upperLeftPawn = b.getBoardArr()[justNextX - 1][justNextY - 1].getPieceHolding();
                    upperRightPawn = b.getBoardArr()[justNextX - 1][justNextY + 1].getPieceHolding();
                }
            }
            //Checking lower left diagonal
            int lowerLeftX = justNextX + 1;
            for (int y = justNextY - 1; y >= 0; y--) {
                if (lowerLeftX > 7)
                    break;
                Piece inLinePiece = b.getBoardArr()[lowerLeftX][y].getPieceHolding();
                if (inLinePiece == null) {
                    lowerLeftX++;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                        && lowerLeftX == justNextX + 1 && y == justNextY - 1)
                    return false;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                    lowerLeftX++;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.whiteCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell))
                    return false;
                lowerLeftX++;
            }

            //Checking lower right diagonal
            int lowerRightX = justNextX + 1;
            for (int y = justNextY + 1; y < 8; y++) {
                if (lowerRightX > 7)
                    break;
                Piece inLinePiece = b.getBoardArr()[lowerRightX][y].getPieceHolding();
                if (inLinePiece == null) {
                    lowerRightX++;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                        && lowerRightX == justNextX + 1 && y == justNextY + 1)
                    return false;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                    lowerRightX++;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.whiteCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell))
                    return false;
                lowerRightX++;
            }

            //If there is an opposite pawn attacking then return false and do not check that diagonal
            if (upperLeftPawn instanceof Pawn && upperLeftPawn.getColorID() != R.color.whiteCell)
                return false;

                //else check that diagonal for the attack of opposite bishop or queen
            else {
                //Upper left diagonal
                int upperLeftX = justNextX - 1;
                for (int y = justNextY - 1; y >= 0; y--) {
                    if (upperLeftX < 0)
                        break;
                    Piece inLinePiece = b.getBoardArr()[upperLeftX][y].getPieceHolding();
                    if (inLinePiece == null) {
                        upperLeftX--;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                            && upperLeftX == justNextX - 1 && y == justNextY - 1)
                        return false;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                        upperLeftX--;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.whiteCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell))
                        return false;
                    upperLeftX--;
                }
            }

            //If there is an opposite pawn attacking then return false and do not check that diagonal
            if (upperRightPawn instanceof Pawn && upperRightPawn.getColorID() != R.color.whiteCell)
                return false;

                //else check that diagonal for the attack of opposite bishop or queen
            else {
                //Upper right diagonal
                int upperRightX = justNextX - 1;
                for (int y = justNextY + 1; y < 8; y++) {
                    if (upperRightX < 0)
                        break;
                    Piece inLinePiece = b.getBoardArr()[upperRightX][y].getPieceHolding();
                    if (inLinePiece == null) {
                        upperRightX--;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell
                            && upperRightX == justNextX - 1 && y == justNextY + 1)
                        return false;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell) {
                        upperRightX--;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.whiteCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.blackCell))
                        return false;
                    upperRightX--;
                }
            }
        }

        //else if color is black then check for lower right and lower left opposite pawn attacks
        else {
            //Checking lower left pawn && lower right pawn
            Piece lowerLeftPawn = null,
                    lowerRightPawn = null;

            if (justNextX != 7) {
                if (justNextY == 0)
                    lowerRightPawn = b.getBoardArr()[justNextX + 1][justNextY + 1].getPieceHolding();
                else if (justNextY == 7)
                    lowerLeftPawn = b.getBoardArr()[justNextX + 1][justNextY - 1].getPieceHolding();
                else {
                    lowerLeftPawn = b.getBoardArr()[justNextX + 1][justNextY - 1].getPieceHolding();
                    lowerRightPawn = b.getBoardArr()[justNextX + 1][justNextY + 1].getPieceHolding();
                }
            }

            //Checking upper left diagonal
            int upperLeftX = justNextX - 1;
            for (int y = justNextY - 1; y >= 0; y--) {
                if (upperLeftX < 0)
                    break;
                Piece inLinePiece = b.getBoardArr()[upperLeftX][y].getPieceHolding();
                if (inLinePiece == null) {
                    upperLeftX--;
                    continue;
                }
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                        && upperLeftX == justNextX - 1 && y == justNextY - 1)
                    return false;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                    upperLeftX--;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.blackCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell))
                    return false;
                upperLeftX--;
            }

            //Checking upper right diagonal
            int upperRightX = justNextX - 1;
            for (int y = justNextY + 1; y < 8; y++) {
                if (upperRightX < 0)
                    break;
                Piece inLinePiece = b.getBoardArr()[upperRightX][y].getPieceHolding();
                if (inLinePiece == null) {
                    upperRightX--;
                    continue;
                }
                if (inLinePiece instanceof Rook)
                    break;
                if (inLinePiece instanceof Knight)
                    break;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                        && upperRightX == justNextX - 1 && y == justNextY + 1)
                    return false;
                if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                    upperRightX--;
                    continue;
                }
                if (inLinePiece instanceof Pawn)
                    break;
                if (inLinePiece.getColorID() == R.color.blackCell)
                    break;
                if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell))
                    return false;
                upperRightX--;
            }

            //check if there is an opposite pawn at lower left diagonal
            if (lowerLeftPawn instanceof Pawn && lowerLeftPawn.getColorID() != R.color.blackCell)
                return false;

                //if no opposite pawn then check that diagonal for attack of bishop or queen
            else {
                //Lower left diagonal
                int lowerLeftX = justNextX + 1;
                for (int y = justNextY - 1; y >= 0; y--) {
                    if (lowerLeftX > 7)
                        break;
                    Piece inLinePiece = b.getBoardArr()[lowerLeftX][y].getPieceHolding();
                    if (inLinePiece == null) {
                        lowerLeftX++;
                        continue;
                    }
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                            && lowerLeftX == justNextX + 1 && y == justNextY - 1)
                        return false;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                        lowerLeftX++;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.blackCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell))
                        return false;
                    lowerLeftX++;
                }
            }

            //check if there is an opposite pawn at lower right diagonal
            if (lowerRightPawn instanceof Pawn && lowerRightPawn.getColorID() != R.color.blackCell)
                return false;

                //if no opposite pawn then check that diagonal for the attack of the opposite queen or bishop
            else {
                //Lower right diagonal
                int lowerRightX = justNextX + 1;
                for (int y = justNextY + 1; y < 8; y++) {
                    if (lowerRightX > 7)
                        break;
                    Piece inLinePiece = b.getBoardArr()[lowerRightX][y].getPieceHolding();
                    if (inLinePiece == null) {
                        lowerRightX++;
                        continue;
                    }
                    if (inLinePiece instanceof Knight)
                        break;
                    if (inLinePiece instanceof Rook)
                        break;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.whiteCell
                            && lowerRightX == justNextX + 1 && y == justNextY +
                            1)
                        return false;
                    if (inLinePiece instanceof King && inLinePiece.getColorID() == R.color.blackCell) {
                        lowerRightX++;
                        continue;
                    }
                    if (inLinePiece instanceof Pawn)
                        break;
                    if (inLinePiece.getColorID() == R.color.blackCell)
                        break;
                    if (inLinePiece instanceof Queen || inLinePiece instanceof Bishop && (inLinePiece.getColorID() == R.color.whiteCell))
                        return false;
                    lowerRightX++;
                }
            }
        }

        //Diagonals finished

        //Checking rows and cols
        //Upper front col

        for (int upperFrontX = justNextX - 1; upperFrontX >= 0; upperFrontX--) {
            Piece inLinePiece = b.getBoardArr()[upperFrontX][justNextY].getPieceHolding();

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && upperFrontX == justNextX - 1)
                return false;
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color))
                return false;
        }

        //Lower front col
        for (int lowerFrontX = justNextX + 1; lowerFrontX < 8; lowerFrontX++) {
            Piece inLinePiece = b.getBoardArr()[lowerFrontX][justNextY].getPieceHolding();

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && lowerFrontX == justNextX + 1)
                return false;
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color))
                return false;
        }

        //Right row
        for (int rightY = justNextY + 1; rightY < 8; rightY++) {
            Piece inLinePiece = b.getBoardArr()[justNextX][rightY].getPieceHolding();

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && rightY == justNextY + 1)
                return false;
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color))
                return false;
        }

        //Left row
        for (int leftY = justNextY - 1; leftY >= 0; leftY--) {
            Piece inLinePiece = b.getBoardArr()[justNextX][leftY].getPieceHolding();

            if (inLinePiece == null)
                continue;
            if (inLinePiece instanceof Knight)
                break;
            if (inLinePiece instanceof Bishop)
                break;
            if (inLinePiece instanceof King && inLinePiece.getColorID() != color && leftY == justNextY - 1)
                return false;
            if (inLinePiece instanceof King && inLinePiece.getColorID() == color)
                continue;
            if (inLinePiece instanceof Pawn)
                break;
            if (inLinePiece.getColorID() == color)
                break;
            if (inLinePiece instanceof Rook || inLinePiece instanceof Queen && (inLinePiece.getColorID() != color))
                return false;
        }

        //Knights check
        Point[] knightsPosition = {new Point(justNextX - 1, justNextY + 2),
                new Point(justNextX - 2, justNextY + 1),
                new Point(justNextX - 2, justNextY - 1),
                new Point(justNextX - 1, justNextY - 2),
                new Point(justNextX + 1, justNextY - 2),
                new Point(justNextX + 2, justNextY - 1),
                new Point(justNextX + 2, justNextY + 1),
                new Point(justNextX + 1, justNextY + 2)};

        for (Point p : knightsPosition) {
            int xCoord = p.getX(), yCoord = p.getY();
            if (xCoord > 7 || xCoord < 0 || yCoord > 7 || yCoord < 0)
                continue;
            Piece locatePiece = b.getBoardArr()[xCoord][yCoord].getPieceHolding();
            if (locatePiece instanceof Knight && locatePiece.getColorID() != color)
                return false;
        }

        /*passes all checks then a valid square to go through during castling or
        during any move played by king or even the square on which the king is currently on*/
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getColorID());
        dest.writeValue(getPieceLocation());
        dest.writeValue(canCastle);
    }
}