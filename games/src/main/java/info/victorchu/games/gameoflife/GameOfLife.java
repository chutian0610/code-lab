package info.victorchu.games.gameoflife;

import java.util.concurrent.Executors;

public class GameOfLife {
    public static void main(String[] args) {
        CeilsMatrix ceilsMatrix = new CeilsMatrix(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Double.valueOf(args[2]));
        Executors.newSingleThreadExecutor().submit(new GameControlTask(ceilsMatrix,Integer.valueOf(args[3])));
    }
}