package hangman;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;
import java.io.File;
public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException {
        boolean playing;
        int currentGuesses = 0;
        SortedSet<Character> guesses = new TreeSet<>();
        Set<String> wordSet = new HashSet<>();
        StringBuilder word = new StringBuilder();
        EvilHangmanGame game = new EvilHangmanGame();

        String fileName = args[0];
        String length = args[1];
        String g = args[2];

        int totalGuesses = Integer.parseInt(g);
        int wordLength = Integer.parseInt(length);
        File file = new File(fileName);
        Scanner in = new Scanner(System.in);

        game.startGame(file, wordLength);
        playing = true;

        while(playing) {
            int numGuessesLeft = totalGuesses - currentGuesses;
            System.out.printf("You have %d guesses left %n Used letters:", numGuessesLeft);
            guesses = game.getGuessedLetters();
            System.out.println(guesses);
            System.out.printf("Current word: %s", word.toString());
            String input = in.nextLine();
            while (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.print("That's not a letter. Please try again: ");
                input = in.nextLine();
            }
            char letter = input.charAt(0);
            while(true) {
                try {
                    wordSet = game.makeGuess(letter);

                } catch (GuessAlreadyMadeException e) {
                    System.out.println("You already guessed that letter, please try again");
                    continue;
                }
                break;
            }


            currentGuesses ++;
            if (currentGuesses == totalGuesses) {
                playing = false;
            }
        }


    }

}
