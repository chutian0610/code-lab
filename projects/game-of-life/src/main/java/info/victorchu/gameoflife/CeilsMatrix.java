package info.victorchu.gameoflife;

import java.util.Random;

public class CeilsMatrix {
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private final int rows;
    private final int cols;

    /**
     * 返回当前细胞状态快照
     * @return
     */
    public boolean[][] getCurrentMatrix() {
        boolean[][] snapshot = new boolean[rows][cols];
        for(int i = 0;i < currentMatrix.length;i++){
            for(int j = 0;j < currentMatrix[i].length;j++){
                snapshot[i][j] = currentMatrix[i][j];
            }
        }
        return snapshot;
    }

    private boolean[][] currentMatrix;

    /**
     * 细胞容器初始化方法
     * @param rows
     * @param cols
     */
    public CeilsMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        currentMatrix = new boolean[rows][cols];
    }

    /**
     * 此函数初始化容器，同时按照density[0,1.0] 的比例,设置细胞的生命状态
     * @param rows
     * @param cols
     * @param density
     */
    public CeilsMatrix(int rows, int cols,double density){
        this.rows = rows;
        this.cols = cols;
        currentMatrix = new boolean[rows][cols];
        // 随机
        Random RANDOM = new Random();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                currentMatrix[i][j] = (RANDOM.nextDouble() >= density);
            }
        }
    }

    /**
     * 更新所有细胞状态
     */
    public void updateCeilsStatues(){
        for (int i=0; i< rows;i++){
            for (int j=0;j<cols;j++){
                updateCeilStatus(i,j);
            }
        }
    }

    /**
     * 更新某个细胞的状态
     * @param i 行
     * @param j 列
     */
    private void updateCeilStatus(int i,int j){
        boolean[] neighbourStatus = getNeighbours(currentMatrix,i,j,rows,cols);
        int aliveCount = 0;
        for (boolean alive : neighbourStatus) {
            if (alive) {
                ++aliveCount;
            }
        }
        if (!currentMatrix[i][j]) {
            // current is dead
            if (aliveCount == 3) {
                currentMatrix[i][j] = true;
            } else {
                // do nothing
            }
        } else {
            // current is alive
            if (aliveCount < 2) {
                // 人口稀少
                currentMatrix[i][j] = false;
            } else if (aliveCount == 2 || aliveCount == 3) {
                // do nothing
            } else {
                // 过度拥挤
                currentMatrix[i][j] = false;
            }
        }
    }

    /**
     * 此处求索引映射值,通过取余处理了边界溢出
     * @param n 索引
     * @param border 边界
     * @return
     */
    private static int normalize(int n, int border) {
        int normalized = n % border;
        return (normalized >= 0) ? normalized : border + normalized;
    }

    /**
     * 获取某个细胞的邻居状态
     * @param ceilMap 细胞矩阵
     * @param row 细胞所在横座标
     * @param col 细胞所在纵座标
     * @param ROW 矩阵边界[行]
     * @param COL 矩阵边界[列]
     * @return
     */
    private static boolean[] getNeighbours(boolean[][] ceilMap, int row,
                                           int col,int ROW,int COL) {
        return new boolean[] {
                ceilMap[normalize(row - 1, ROW)][normalize(col - 1, COL)],
                ceilMap[normalize(row - 1, ROW)][normalize(col, COL)],
                ceilMap[normalize(row - 1, ROW)][normalize(col + 1, COL)],
                ceilMap[normalize(row, ROW)][normalize(col - 1, COL)],
                ceilMap[normalize(row, ROW)][normalize(col + 1, COL)],
                ceilMap[normalize(row + 1, ROW)][normalize(col - 1, COL)],
                ceilMap[normalize(row + 1, ROW)][normalize(col, COL)],
                ceilMap[normalize(row + 1, ROW)][normalize(col + 1, COL)]
        };
    }
}
