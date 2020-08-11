package com.example.android.chess;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.mainClasses.Board;
import com.example.android.mainClasses.Cell;
import com.example.android.mainClasses.HandleClick;
import com.example.android.mainClasses.Player;
import com.example.android.pieces.King;

public class GameActivity extends AppCompatActivity {

    private LinearLayout[] rows = new LinearLayout[8];
    private Board playingBoard;
    private HandleClick cellClickListener;
    private Player whitePlayer, blackPlayer;
    private TextView turnText, pieceSelected, colorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        AppContextHolder.setC(this);

        assignRows();
        turnText = findViewById(R.id.turn_text);
        pieceSelected = findViewById(R.id.piece_selected);
        colorView = findViewById(R.id.color_view);

        if (savedInstanceState != null) {
            this.playingBoard = savedInstanceState.getParcelable("chessBoard");
            this.cellClickListener = savedInstanceState.getParcelable("click");
            this.whitePlayer = savedInstanceState.getParcelable("whitePlayer");
            this.blackPlayer = savedInstanceState.getParcelable("blackPlayer");

            cellClickListener.setColorView(colorView);
            cellClickListener.setPieceImage(pieceSelected);
            cellClickListener.setTurnText(turnText);

            turnText.setText(savedInstanceState.getString("whoseTurn"));
            pieceSelected.setText(savedInstanceState.getString("whichPiece"));
            String whoseColor = savedInstanceState.getString("whichColor");
            if (whoseColor != null) {
                colorView.setText(whoseColor);
                colorView.setBackgroundResource(whoseColor.equals(" ") ? R.color.blackCell : R.color.whiteCell);
            }

            regainBoardState();
        } else {

            //Making board
            playingBoard = new Board();
            playingBoard.makeBoard(rows);

            //Making players
            whitePlayer = new Player((King) playingBoard.getBoardArr()[7][4].getPieceHolding());
            blackPlayer = new Player((King) playingBoard.getBoardArr()[0][4].getPieceHolding());

            //initialising cell click listener
            cellClickListener = new HandleClick(playingBoard, whitePlayer, blackPlayer, colorView, turnText, pieceSelected);

        }
        attachListener();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("chessBoard", playingBoard);
        outState.putParcelable("click", cellClickListener);
        outState.putParcelable("whitePlayer", whitePlayer);
        outState.putParcelable("blackPlayer", blackPlayer);
        outState.putString("whoseTurn", turnText.getText().toString());
        outState.putString("whichPiece", pieceSelected.getText().toString());
        outState.putString("whichColor", colorView.getText().toString());
    }

    private void assignRows() {
        for (int i = 0; i < 8; i++) {
            String stringID = "row" + i;
            int resID = getResources().getIdentifier(stringID, "id", getPackageName());
            rows[i] = findViewById(resID);
        }
    }

    private void attachListener() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                playingBoard.getBoardArr()[i][j].setOnClickListener(cellClickListener);
            }
        }
    }

    private void regainBoardState() {
        Cell[][] boardArr = playingBoard.getBoardArr();

        int k = 0;
        for (Cell[] row : boardArr) {
            for (Cell c : row) {
                if (c.getParent() != null)
                    ((ViewGroup) c.getParent()).removeView(c);
                rows[k].addView(c);
            }
            k++;
        }
    }

}
