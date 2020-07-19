package Finall_Project_2.Server.Game;

enum Color{ White , Black }

public abstract class Piece {

    private final Color color;
    protected int score;

    protected Piece(Color color) {
        this.color = color;
    }

    public Color getColor(){return color;}

    public void move(){}
}
