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
        StringBuilder usedLetters = new StringBuilder();
        String word="";
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
            System.out.printf("You have %d guesses left %n Used leters %s%n Word: %s%n Enter guess: %n", numGuessesLeft,usedLetters, word);
            String input = in.nextLine();
            while (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.print("That's not a letter. Please try again: ");
                input = in.nextLine();
            }
            while(word.contains(input)) {
                System.out.print("The letter has already been guessed");
                input = in.nextLine();
            }
            word = word + input;

            currentGuesses ++;
            if (currentGuesses == totalGuesses) {
                playing = false;
            }
        }


    }

}
