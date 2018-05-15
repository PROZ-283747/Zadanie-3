/*
 *	@autor Adela Jaworowska / adela.jaworowska@gmail.com
 */

package app;

public class GameModel {

	int boardSize;
	int moveCount;

	String board[][];

	public GameModel(int size) {
		moveCount = 0;
		boardSize = size;
		board = new String[3][3];
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j) {
				board[i][j] = "";
			}
		}
	}

	public String checkGame() {
		System.out.println("CHECK_GAME");
		// checking columns
		String tempSign = "";

		for (int i = 0; i < boardSize; ++i) {
			for (int j = 0; j < boardSize; ++j) {
				if (board[i][j].equals(""))
					break;
				else
					tempSign = board[i][0];
				if (!board[i][j].equals(tempSign))
					break;
				if (j == boardSize - 1) {
					System.out.println("Kolumny  j: " + j);
					return tempSign;
				}
			}
		}
		// check rows
		for (int j = 0; j < boardSize; ++j) {
			for (int i = 0; i < boardSize; ++i) {
				if (board[i][j].equals(""))
					break;
				else {
					tempSign = board[0][j];
					if (!board[i][j].equals(tempSign))
						break;
					if (i == boardSize - 1) {
						System.out.println("Rzêdy i: " + i);
						return tempSign;
					}
				}
			}
		}
		// check antidiagonal
		for (int i = 0, j = boardSize - 1; i < boardSize && j >= 0; ++i, --j) {
			if (board[i][j].equals(""))
				break;
			tempSign = board[0][boardSize - 1];
			if (!board[i][j].equals(tempSign))
				break;
			if (i == boardSize - 1) {
				System.out.println("Antydiagonal  i: " + i);
				return tempSign;
			}
		}

		// check diagonal
		for (int i = 0; i < boardSize; ++i) {
			if (board[i][i].equals(""))
				break;

			tempSign = board[0][0];
			if (!board[i][i].equals(tempSign))
				break;
			if (i == boardSize - 1) {
				System.out.println("Diagonal i: " + i);
				return tempSign;
			}
		}

		if (moveCount == 9) {
			System.out.println("MoveCount: " + moveCount);
			return "draw";
		}

		return "continue";
	}
	
	public String[][] getBoard() {
		return board;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void incrementMoveCount() {
		this.moveCount++;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}

	public void setFieldOnBoard(String sign, int x, int y) {
		this.board[x][y] = sign;
	}

	public void resetGameModel() {
		for (int i = 0; i < boardSize; ++i) {
			for (int j = 0; j < boardSize; ++j) {
				board[i][j] = "";
			}
		}
		moveCount = 0;
	}

	public void printBoard() {
		System.out.print("*******");
		for (int i = 0; i < boardSize; ++i) {
			System.out.print("\n");
			for (int j = 0; j < boardSize; ++j) {
				System.out.print(board[i][j] + " ");
			}
		}
		System.out.println("*******");
	}
}