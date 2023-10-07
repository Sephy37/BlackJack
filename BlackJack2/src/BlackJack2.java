import java.util.*;
import java.util.Random;
import java.util.ArrayList;
import java.lang.NumberFormatException;
import java.util.InputMismatchException;
public class BlackJack2 {
	private static final Scanner scanner = new Scanner(System.in);
	private static GameStatus gameStatus;
	private static final int BEST_OF_THREE_GAMES = 3;
	private static final int BEST_OF_FIVE_GAMES = 5;
	private static final int BEST_OF_TEN_GAMES = 10;
	private static Random rand = new Random();
	private static ArrayList<Integer>randomNumberList = new ArrayList<Integer>();
	private static ArrayList<Integer>computerCards = new ArrayList<Integer>();
	private static ArrayList<Integer>playerCards = new ArrayList<Integer>();
	private static int turns;
	private static int level;
	private static int playerChoice;
	private static int computerChoice;
	private static int playAgain;
	private static String anotherCard = "";
	private static String answer = "";
	private static boolean gameOver = false;
	private static int playerScore;
	private static int computerScore;
	private static int playerTotal;
	private static int computerTotal;
	public static void main(String[] args) {
		welcomeMessage();
		playSeriesOfGames();

	}
	
	private static void welcomeMessage() {
	   System.out.println("********************");
	   System.out.println("*     Welcome to   *");
	   System.out.println("*     BlackJack    *");
	   System.out.println("********************");
	}
	
	private static void playSeriesOfGames() {
		gameStatus = GameStatus.GAME_ON;
		while(gameStatus != GameStatus.GAME_OVER) {
			setUpGame();
			while(turns > 0) {
				startGame();
			}
			System.out.println("The game series is done. Would you like to play another series?");
			answer = scanner.nextLine();
			if(answer.equalsIgnoreCase("no")) {
				gameStatus = GameStatus.GAME_OVER;
			}
		}
	}
	
	private static void setUpGame() {
		gameStatus = GameStatus.GAME_ON;
		playerScore = 0;
		computerScore = 0;
		turns = getNumberOfGames();
	}
	
	private static int getNumberOfGames() {
		System.out.println("Press 1 for three games, 2 for five games or 3 for ten games: ");
		level = checkInput();
		level = setNumberOfGames();
		return level;
	}
	
	private static int setNumberOfGames() {
		switch(level) {
			case 1: 
				level = BEST_OF_THREE_GAMES;
				break;
				
			case 2:
				level = BEST_OF_FIVE_GAMES;
				break;
				
			case 3:
				level = BEST_OF_TEN_GAMES;
				break;
				
			case 0:
				gameStatus = GameStatus.GAME_OVER;
				
			default:
				checkNumberOfGames();
		}
		return level;
	}
	
	private static int checkNumberOfGames() {
		if(level < 0 || level > 3) {
			System.err.println("PLEASE ONLY CHOSE OPTIONS 1,2,3 OR 0 TO EXIT!");
			level = getNumberOfGames();
			return level;
		}
		return level;
	}
	
	/*private static void startGame() {
		gameOver = false;
		computerCards.clear();
		playerCards.clear();
		answer = getAnswer();
		if(answer.equalsIgnoreCase("no")) {
			gameStatus = GameStatus.GAME_OVER;
		}else if(answer.equalsIgnoreCase("yes")) {
			playGame();
		}else {
			System.out.println("INVALID ENTRY!");
		}
	}*/
	
	private static void startGame() {
		gameOver = false;
		computerCards.clear();
		playerCards.clear();
		playGame();
		turns--;
		
	}
	
	private static String getAnswer() {
		System.out.println("Do you want to play a game of BlackJack? Type yes to play or no to exit: ");
		answer = scanner.nextLine();
		return answer;
	}
	
	private static void playGame() {
		anotherCard = "";
		getRandomCards();
		while(!gameOver) {
			calculateTotals();
			displayCurrentCards();
			decidePlayerMove();
			decideComputerMove();
			checkEndConditions();
			
		}
		getFinalTally();
	}
	
	private static void calculateTotals() {
		playerTotal = sumLists(playerCards);
		computerTotal = sumLists(computerCards);
	}
	
	private static void displayCurrentCards() {
		System.out.println("Your cards are " + playerCards + " and players current score is " + playerTotal);
		System.out.println("Computers first card is: " + computerCards.get(0));
	}
	
	private static void decidePlayerMove() {
		while(playerTotal != 0 && playerTotal <= 21) {
			System.out.println("Do you want another card? ");
			anotherCard = scanner.nextLine();
			if(anotherCard.equalsIgnoreCase("yes")) {
				playerChoice = dealCards().get(rand.nextInt(randomNumberList.size()));
				playerCards.add(playerChoice);
				playerTotal = sumLists(playerCards);
				displayCurrentCards();
			}
			if(anotherCard.equalsIgnoreCase("no") || playerTotal > 21 ) {
				gameOver = true;
				break;
			}
		}
	}
	
	private static void decideComputerMove() {
		if(computerTotal < 17) {
			computerChoice = dealCards().get(rand.nextInt(randomNumberList.size()));
			computerCards.add(computerChoice);
			computerCards = calculateScore(computerCards);
			computerTotal = sumLists(computerCards);
		}
	}
	
	private static void checkEndConditions() {
		if(playerTotal == 0 || playerTotal > 21 || anotherCard.equalsIgnoreCase("no") && computerTotal > 17 ) {
			gameOver = true;
		}
	}
	
	private static void getFinalTally() {
		System.out.println("Players final hand is " + playerCards + " and players final score is " + playerTotal);
		System.out.println("Computers final hand is " + computerCards + " and computers final score is " + computerTotal);
		compareScores(playerTotal,computerTotal);
	}
	
	private static void printScore() {
		System.out.println("Current player score is : " + playerScore);
		System.out.println("Cureent computer score is: " + computerScore);
		
	}
	
	private static void checkIfRoundEnded(int score) {
		if(score >= turns){
			askToPlayAgain();
			playAgain();
		}
	}
	
	/*private static boolean checkIfRoundEnded(int score) {
		if(score >= turns) {
			askToPlayAgain();
			playAgain();
			return true;
		}
		return false;
	}*/
	
	private static void compareScores(int playerTotal,int computerTotal) {
		if(playerTotal > 21 && computerTotal > 21){
            System.out.println("YOU BOTH WENT OVER! GAME OVER");
        }else if(playerTotal == computerTotal){
            System.out.println("DRAW!");
        }else if(playerTotal > 21){
            System.out.println("PLAYER WENT OVER! COMPUTER WINS! GAME OVER! ");
            computerScore+=1;
            printScore();
            checkIfRoundEnded(playerScore);
        }else if(computerTotal > 21){
            System.out.println("COMPUTER WENT OVER! PLAYER WINS! GAME OVER! ");
            playerScore+=1;
            printScore();
            checkIfRoundEnded(computerScore);
        }else if(playerTotal > computerTotal){
            System.out.println("PLAYER WINS!");
            playerScore+=1;
            printScore();
            checkIfRoundEnded(playerScore);
        }else{
            System.out.println("COMPUTER WINS!");
            computerScore+=1;
            printScore();
            checkIfRoundEnded(computerScore);
        }
	}
	
	/*May not be implemented, for testing purposes*/
    private static void checkWhoWon(){
        
    }
    
    private static int sumLists(ArrayList<Integer>randomNumbers) {
    	int sum = 0;
    	for(int i: randomNumbers) {
    		sum+=i;
    	}
    	return sum;
    }
    
    private static ArrayList<Integer>dealCards(){
    	randomNumberList.clear();
    	for(int i = 0; i<4; i++) {
    		randomNumberList.add(11);
    		for(int j = 2; j<10; j++) {
    			randomNumberList.add(j);
    		}
    		for(int j = 0; j<4; j++) {
    			randomNumberList.add(10);
    		}
    	}
    	return randomNumberList;
    }
    
    private static void getRandomCards() {
    	for(int i = 0; i < 2; i++) {
    		computerChoice = dealCards().get(rand.nextInt(randomNumberList.size()));
    		playerChoice = dealCards().get(rand.nextInt(randomNumberList.size()));
    		computerCards.add(computerChoice);
    		playerCards.add(playerChoice);
    	}
    }
    
    private static ArrayList<Integer>calculateScore(ArrayList<Integer>randomNumbers){
    	if(sumLists(randomNumbers) == 21 && randomNumbers.size()== 2) {
    		return randomNumbers;
    	}
    	if(randomNumbers.contains(11) && sumLists(randomNumbers)> 21) {
    		randomNumbers.remove(Integer.valueOf(11));
    		randomNumbers.add(1);
    	}
    	return randomNumbers;
    }
    
    private static void askToPlayAgain() {
    	System.out.println("Do you want to play again? press 1 to play again or 2 to exit: ");
    	playAgain = checkInput();
    }
    
    private static void playAgain() {
    	while(playAgain != 1 && playAgain !=2) {
    		playAgain = checkInput();
    	}
    	if(playAgain == 2) {
    		gameStatus = GameStatus.GAME_OVER;
    		gameOver = true;
    		exit();
    	}else if(playAgain == 1) {
    		gameStatus = GameStatus.GAME_ON;
    		gameOver = false;
    		//playerScore = 0;
    		//computerScore = 0;
    		setUpGame();
    	}
    }
    
    private static void exit() {
    	System.out.println("Thanks for playing!");
    }
    
    private static int checkInput() {
    	boolean isValid = true;
        while(isValid) {
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); 
                return input;
            } catch(InputMismatchException e) {
                scanner.nextLine(); 
                System.err.println("INVALID INPUT!");
            }
        }
        return scanner.nextInt();
    }
    
	
	

}
