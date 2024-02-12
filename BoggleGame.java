import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Random;

public class BoggleGame implements BoggleGameInterface{

    public int count;
    public StringBuilder currentSolution;

    @Override
    public char[][] generateBoggleBoard(int size) {
        if(size <= 0) {
            return null;
        }
        int stringLength = size * size;
        if(stringLength <= 0) {
            return null;
        }
        String s = generateRandomString(stringLength);
        char[][] board = new char[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                board[i][j] = s.charAt(i * size + j);
            }
        }
        return board;
    }

    @Override
    public int countWords(char[][] boggleBoard, DictInterface dictionary) {
        // TODO Implement this method
        for(int i = 0; i < boggleBoard.length; i++) {
            for(int j = 0; j < boggleBoard.length; j++) {
                //set all letters back to uppercase
                resetBoard(boggleBoard);
                    
                StringBuilder sb = new StringBuilder();
                solve(boggleBoard, dictionary, i, j, 0, sb);
                continue;
            }
        }

        return this.count;
    }

    /* private int countWords_helper(char[][] boggleBoard, DictInterface dictionary, int x, int y, StringBuilder sb){
        boggleBoard[x][y] = Character.toLowerCase(boggleBoard[x][y]);
        sb.append(boggleBoard[x][y]);
        int res = dictionary.searchPrefix(sb);

        System.out.println("String so far: " + sb.toString());

        //stop recursing if it is not possible that sb could be a word
        if(res == 0) return 0;

        System.out.println("res: " + res);

        //if sb is a word increment count and continue
        if(sb.length() >= 3 && (res == 2 || res == 3)){
            this.count++;
        }

        //recurse for each next possible square in the boggle board
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                System.out.println("here");
                if(indexIsValid(x, y, i, boggleBoard)){
                    System.out.println("here1");
                    if((res == 3 || res == 1) && !Character.isLowerCase(boggleBoard[x + i][y + j])){
                        System.out.println("here2");
                        countWords_helper(boggleBoard, dictionary, x + i, y + j, sb);
                        resetBoard(boggleBoard);
                    }
                }
            } 
        }

        return 0;
    } */

    private void solve(char[][] boggleBoard, DictInterface dictionary, int row, int col, /* for debugging */ int depth, StringBuilder sb) {
		for (int direction = 0; direction < 8; direction++) {
			if (indexIsValid(row, col, direction, boggleBoard)) {
                System.out.println("index is valid");
				sb.append(nextChar(row, col, direction, boggleBoard));
                System.out.println("String so far: " + sb.toString());
				// mark the letter as used
				Tile nextTiles = nextTiles(row, col, direction);
				boggleBoard[nextTiles.row][nextTiles.col] = Character.toLowerCase(boggleBoard[nextTiles.row][nextTiles.col]);
                int res = dictionary.searchPrefix(sb);
                System.out.println(res);
                if (res == 0) { //word not possible
                    if(sb.length() >= 3) {
                        return;
                    }
                    solve(boggleBoard, dictionary, nextTiles.row, nextTiles.col, depth + 1, sb);
                }

				if (res == 1) { // prefix but not word
					solve(boggleBoard, dictionary, nextTiles.row, nextTiles.col, depth + 1, sb);
                    System.out.println("prefix but not word");
				}
				if (res == 2) { // word but not prefix
                    System.out.println("word but not prefix");
					if (sb.length() >= 3) {
                        this.count++;
                        System.out.println("count increases");
						System.out.println(sb.toString());
					}
				}

				if (res == 3) { // word and prefix
                    System.out.println("word and prefix");
					// TODO: Write the code for the word and prefix case
					if (sb.length() >= 3) {
                        this.count++;
                        System.out.println("count increases");
						System.out.println(sb.toString());
					}
					solve(boggleBoard, dictionary, nextTiles.row, nextTiles.col, depth + 1, sb);
				}
				sb.deleteCharAt(sb.length() - 1);
				boggleBoard[nextTiles.row][nextTiles.col] = Character.toUpperCase(boggleBoard[nextTiles.row][nextTiles.col]);
			}
		}
	}

    private boolean indexIsValid(int row, int col, int direction, char[][] boggleBoard) {
		Tile tiles = nextTiles(row, col, direction);
		if (tiles.row < 0 || tiles.row >= boggleBoard.length) {
			return false;
		}
		if (tiles.col < 0 || tiles.col >= boggleBoard.length) {
			return false;
		}
		if (Character.isLowerCase(boggleBoard[tiles.row][tiles.col])) {
			return false;
		}
		return true;
	}

    private char nextChar(int row, int col, int direction, char[][] boggleBoard) {
		Tile tiles = nextTiles(row, col, direction);
		return boggleBoard[tiles.row][tiles.col];
	}

	private Tile nextTiles(int row, int col, int direction) {
		Tile result = null;
		switch (direction) {
			case 0: // up left
				result = new Tile(row - 1, col - 1);
				break;
			case 1: // up
				result = new Tile(row - 1, col);
				break;
			case 2: // up right
				result = new Tile(row - 1, col + 1);
				break;
			case 3: // right
				result = new Tile(row, col + 1);
				break;
			case 4: // bottom right
				result = new Tile(row + 1, col + 1);
				break;
			case 5: // bottom
				result = new Tile(row + 1, col);
				break;
			case 6: // bottom left
				result = new Tile(row + 1, col - 1);
				break;
			case 7: // left
				result = new Tile(row, col - 1);
				break;
		}
		return result;
	}

    private void resetBoard(char[][] boggleBoard) {
        for(int i = 0; i < boggleBoard.length; i++) {
            for(int j = 0; j < boggleBoard.length; j++) {
                boggleBoard[i][j] = Character.toUpperCase(boggleBoard[i][j]);
            }
        }
        return;
    }

    @Override
    public int countWordsOfCertainLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
        int count = 0;
        for(int i = 0; i < boggleBoard.length; i++) {
            for(int j = 0; j < boggleBoard.length; j++) {
                resetBoard(boggleBoard);
                StringBuilder currentSolution = new StringBuilder();
                solveForWordLength(boggleBoard, dictionary, i, j, 0, currentSolution, wordLength);
            }
        }
        return count;
    }

    private void solveForWordLength(char[][] boggleBoard, DictInterface dictionary, int row, int col, int depth, StringBuilder sb, int wordLength) {
        for (int direction = 0; direction < 8; direction++) {
			if (indexIsValid(row, col, direction, boggleBoard)) {
				sb.append(nextChar(row, col, direction, boggleBoard));
				// mark the letter as used
				Tile nextTiles = nextTiles(row, col, direction);
                boggleBoard[nextTiles.row][nextTiles.col] = Character.toLowerCase(boggleBoard[nextTiles.row][nextTiles.col]);		
                int res = dictionary.searchPrefix(sb);

				if (res == 1) { // prefix but not word
					solveForWordLength(boggleBoard, dictionary, nextTiles.row, nextTiles.col, depth + 1, sb, wordLength);
				}
				if (res == 2) { // word but not prefix
					if (sb.length() == wordLength) {
                        count++;
						System.out.println(sb.toString());
					}
				}

				if (res == 3) { // word and prefix
					if (sb.length() == wordLength) {
                        count++;
						System.out.println(sb.toString());
					}
					solveForWordLength(boggleBoard, dictionary, row, col, depth, sb, wordLength);
				}
				sb.deleteCharAt(sb.length() - 1);
				boggleBoard[nextTiles.row][nextTiles.col] = Character.toLowerCase(boggleBoard[nextTiles.row][nextTiles.col]);
			}
		} 
    }

    @Override
    public boolean isWordInDictionary(DictInterface dictionary, String word) {
        return false;
    }

    @Override
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
        // TODO Implement this method
        return false;
    }

    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
        // TODO Implement this method
        return null;
    }

    @Override
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
        // TODO Implement this method
        return null;
    }

    @Override
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
        // TODO Implement this method
        return false;
    }

    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary, int length) {
        // TODO Implement this method
        return null;
    }

    private String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();

        // System.out.println(generatedString);
        return generatedString;
    }
    
}
