package Finall_Project_2.Server.Game;

public class Board{
    private final Cell[][] board;


    public Board() {
        this.board = new Cell[8][8];
        for (int i=0 ; i<8 ; i++){
            board[i]=new Cell[8];
        }
    }

    public Cell[][] getBoard() {
        return board;
    }

}


class Cell{

    Color color;
    Piece piece;

    public Cell(Color color , Piece piece){
        this.color=color;
        this.piece=piece;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
