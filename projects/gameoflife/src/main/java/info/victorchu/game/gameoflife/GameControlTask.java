package info.victorchu.game.gameoflife;

public class GameControlTask implements Runnable {

    private final CeilsMatrix ceilsMatrix;
    private final int mills;
    public GameControlTask(CeilsMatrix ceilsMatrix,int mills) {
        this.ceilsMatrix = ceilsMatrix;
        this.mills = mills;
    }

    @Override
    public void run() {
        ConsoleLayOutTool.clearScreen();
        long loop = 0;
        while (true){
            loop++;
            ceilsMatrix.updateCeilsStatues();
            ConsoleLayOutTool.printCeils(ceilsMatrix.getCurrentMatrix(),ceilsMatrix.getRows(),ceilsMatrix.getCols(),loop);
            try {
                Thread.sleep(mills);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}