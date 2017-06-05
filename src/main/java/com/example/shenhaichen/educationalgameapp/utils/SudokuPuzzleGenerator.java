package com.example.shenhaichen.educationalgameapp.utils;

import java.util.Random;

/**
 *  using the method of random to generate sudoku puzzle
 * Created by shenhaichen on 12/05/2017.
 */
public class SudokuPuzzleGenerator {
    private Random random = new Random();

    /**
     * use this value for make sure the matrix could draw successfully
     */
    private static final int MAX_CALL_RANDOM_ARRAY_TIMES = 220;

    // recording how many time call the method of bulidRandomArray()
    private int currentTimes = 0;
    private static int SQUARE_NUM = 9;
    private static SudokuPuzzleGenerator instance = null;
    // use for 9 small squares
    private int square_sqrt;

    private SudokuPuzzleGenerator() {
        square_sqrt = (int) Math.sqrt(SQUARE_NUM);
    }

    public static SudokuPuzzleGenerator getInstance() {
        if (instance == null) {
            instance = new SudokuPuzzleGenerator();
        }

        return instance;
    }

    /**
     * after get the whole array, then creating random space of it
     * this space will allow player to fill up
     * @param wholeArray
     * @param level
     * @return
     */
    public int[][] createSpaceMatrix(int[][] wholeArray, GameLevel level) {
        int[][] gameArray = new int[SQUARE_NUM][SQUARE_NUM];
        for (int i = 0; i < wholeArray.length; i++) {
            int[] row = wholeArray[i];
            System.arraycopy(row, 0, gameArray[i], 0, SQUARE_NUM);
        }
        // depends on the game level to create the space, more difficult more space
        int digNum = getDigNum(level);
        for (int i = 0; i < digNum; i++) {
            int rowRan = (int) (Math.random() * SQUARE_NUM);
            int colRan = (int) (Math.random() * SQUARE_NUM);
            if (gameArray[rowRan][colRan] == 0) {
                i--;
            } else {
                gameArray[rowRan][colRan] = 0;
            }
        }
        return gameArray;
    }

    /**
     * base on the level to create how many space will leave
     *
     * @param level
     * @return the number of space
     */
    private int getDigNum(GameLevel level) {
        switch (level) {
            case BASE:
                return random.nextInt(2) + 18;
            case PRIMARY:
                return random.nextInt(2) + 22;
            case INTERMEDIATE:
                return random.nextInt(3) + 25;
            case ADVANCED:
                return random.nextInt(4) + 29;
            case EVIL:
                return random.nextInt(5) + 35;
            default:
                return 8;
        }
    }

    /**
     *  generate the puzzle matrix, the Two-dimensional array contains all number
     * @return the final array
     */
    public int[][] generatePuzzleMatrix() {
        int[][] randomMatrix = new int[SQUARE_NUM][SQUARE_NUM];
        for (int row = 0; row < SQUARE_NUM; row++) {
            if (row == 0) {
                currentTimes = 0;
                randomMatrix[row] = buildRandomArray();
            } else {
                int[] tempRandomArray = buildRandomArray();
                for (int col = 0; col < SQUARE_NUM; col++) {
                    if (currentTimes < MAX_CALL_RANDOM_ARRAY_TIMES) {
                        if (!isCandidateNmbFound(randomMatrix, tempRandomArray,
                                row, col)) {
                            // reset this row to zero, then create new number of row
                            resetValuesInRowToZero(randomMatrix, row);
                            row -= 1;
                            col = SQUARE_NUM - 1;
                            tempRandomArray = buildRandomArray();
                        }
                    } else {
                       // to avoid out of bounds
                        row = -1;
                        col = SQUARE_NUM - 1;
                        resetValuesToZeros(randomMatrix);
                        currentTimes = 0;
                    }
                }
            }
        }
        return randomMatrix;
    }

    /**
     * return an array that contains random number from 1 to 9
     */
    private int[] buildRandomArray() {
        currentTimes++;
        int[] array = new int[SQUARE_NUM];

        for (int i = 0; i < SQUARE_NUM; i++) {
            array[i] = i + 1;
        }
        int randomInt = 0;

        // randomly create a number from 1 to 8, to instead of first position of this
        // array. after testing, which need 20 times could create a array that contain number
        // from 1 to 9
        for (int i = 0; i < 20; i++) {
            randomInt = random.nextInt(SQUARE_NUM - 1) + 1;
            int temp = array[0];
            array[0] = array[randomInt];
            array[randomInt] = temp;
        }

        return array;
    }
    /**
     * in order to check there is conflict or not，have to make sure, which square we check, Block 1
     * start from point[0][0], Block 2 start from point[3]][0],Block 9 start from point[6][6]
     */
    private boolean noConflictInBlock(int[][] candidateMatrix, int row, int col) {

        int baseRow = row / square_sqrt * square_sqrt;
        int baseCol = col / square_sqrt * square_sqrt;

        for (int rowNum = 0; rowNum < SQUARE_NUM - 1; rowNum++) {
            if (candidateMatrix[baseRow + rowNum / square_sqrt][baseCol + rowNum % square_sqrt] == 0) {
                continue;
            }
            for (int colNum = rowNum + 1; colNum < SQUARE_NUM; colNum++) {
                if (candidateMatrix[baseRow + rowNum / square_sqrt][baseCol + rowNum % square_sqrt] == candidateMatrix[baseRow
                        + colNum / 3][baseCol + colNum % square_sqrt]) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     *
     * because the values are store in a array, which will show as a row,
     * the value of next row must be 0
     * So when check the conflict of column, just check the value is repeat will previous column
     *
     */
    private boolean noConflictInColumn(int[][] candidateMatrix, int row, int col) {

        int currentValue = candidateMatrix[row][col];
        for (int rowNum = 0; rowNum < row; rowNum++) {
            if (currentValue == candidateMatrix[rowNum][col]) {
                return false;
            }
        }
        return true;
    }

    /**
     *  similar with previous function
     */
    private boolean noConflictInRow(int[][] candidateMatrix, int row, int col) {

        int currentValue = candidateMatrix[row][col];

        for (int colNum = 0; colNum < col; colNum++) {
            if (currentValue == candidateMatrix[row][colNum]) {
                return false;
            }
        }

        return true;
    }

    private boolean noConflict(int[][] candidateMatrix, int row, int col) {
        return noConflictInRow(candidateMatrix, row, col)
                && noConflictInColumn(candidateMatrix, row, col)
                && noConflictInBlock(candidateMatrix, row, col);
    }

    private boolean isCandidateNmbFound(int[][] randomMatrix,
                                        int[] randomArray, int row, int col) {
        for (int i = 0; i < randomArray.length; i++) {
            // put number into the Two-dimensional array, then check column and row is conflict or not.
            randomMatrix[row][col] = randomArray[i];
            if (noConflict(randomMatrix, row, col)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  if value conflict
     * @param matrix
     * @param row
     */
    private void resetValuesInRowToZero(int[][] matrix, int row) {
        for (int j = 0; j < SQUARE_NUM; j++) {
            matrix[row][j] = 0;
        }
    }

    private void resetValuesToZeros(int[][] matrix) {
        for (int row = 0; row < SQUARE_NUM; row++) {
            for (int col = 0; col < SQUARE_NUM; col++) {
                matrix[row][col] = 0;
            }
        }
    }

    /**
     * @return the currentTimes
     */
    public int getCurrentTimes() {
        return currentTimes;
    }

    /**
     * @param currentTimes the currentTimes to set
     */
    public void setCurrentTimes(int currentTimes) {
        this.currentTimes = currentTimes;
    }
}
