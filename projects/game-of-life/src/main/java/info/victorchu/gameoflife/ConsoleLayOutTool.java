package info.victorchu.gameoflife;

public class ConsoleLayOutTool {
    private static final char ESC = '\033';
    private static final String ANSI_CLS = "\033[2J";
    private static final char alive = '*';
    private static final char dead = ' ';

    // clean screen (only linux and mac terminal support)
    public static void clearScreen() {
        System.out.print(ANSI_CLS);
    }

    public static void printCeils(boolean[][] ceil,int row,int col,long loop) {
        int count =0;
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                print(i, j, (ceil[i][j]) ? alive : dead);
                if(ceil[i][j]){
                    count++;
                }
            }
        }
        System.out.print(String.format("\n总容量%d,第%d天,存活%d个细胞\n", row*col,loop, count));
        System.out.print("注:'*'代表细胞存活");
    }

    private static void print(int row, int col, char c) {
        System.out.print(String.format("%c[%d;%df%c", ESC, row, col, c));
    }
}