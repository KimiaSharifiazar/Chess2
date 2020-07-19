package Finall_Project_2.Server.Game;

import Finall_Project_2.Server.User.User;

import java.io.Serializable;

//chon badan qarare ina ro dar yek file record haye bazimo zakhire konam pas serializable ro piade kardim
public class GameRecord implements Serializable {
    public final Game game;
    public final User winner;

    public GameRecord(Game game, User winner) {
        this.game = game;
        this.winner = winner;
    }
}
