package hangman;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;
import java.io.File;
public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException {
        IEvilHangmanGame game;
        boolean playing = false;
        int currentGuesses = 0;
        int currWord = 0;
        SortedSet<Character> guesses = new TreeSet<>();
        Set<String> wordSet = new HashSet<>();
        StringBuilder word = new StringBuilder();
        game = new EvilHangmanGame();

        String fileName = args[0];
        String length = args[1];
        String g = args[2];

        int totalGuesses = Integer.parseInt(g);
        int wordLength = Integer.parseInt(length);
        File file = new File(fileName);
        Scanner in = new Scanner(System.in);

        try {
            game.startGame(file, wordLength);
            playing = true;
        }catch (EmptyDictionaryException e) {
            System.exit(0);
        }

        while(playing) {
            int numGuessesLeft = totalGuesses - currentGuesses;
            System.out.printf("You have %d guesses left %n Used letters:", numGuessesLeft);
            guesses = game.getGuessedLetters();
            System.out.println(guesses);
            System.out.printf("Current word: %s%n", word.toString());
            String input = in.nextLine();
            while (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.print("That's not a letter. Please try again: ");
                input = in.nextLine();
            }
            char letter = input.charAt(0);
            letter = Character.toLowerCase(letter);
            while(true) {
                try {//TODO: update input here
                    wordSet = game.makeGuess(letter);

                } catch (GuessAlreadyMadeException e) {
                    System.out.println("You already guessed that letter, please try again");
                    continue;
                }
                break;
            }
            boolean containsLetter = true;
            Iterator<String> wordIterator = wordSet.iterator();
            String findChar = wordIterator.next();
            containsLetter = findChar.indexOf(letter) != -1;
            List<Integer> charList = new ArrayList<>();

            if(containsLetter) {
                System.out.printf("There was an %c", letter);
                if (currWord == wordLength){
                    System.out.printf("You win! The word was %s", findChar);
                }
                else{
                    int letterIndex = findChar.indexOf(letter);
                    while(letterIndex >= 0) {
                        charList.add(letterIndex);
                        letterIndex = findChar.indexOf(letter, letterIndex+1);
                    }
                    for (int i = 0; i < charList.size(); i++) {
                        word.setCharAt(charList.get(i),letter);
                    }
                    currWord++;
                }
            }
            else {
                System.out.printf("Sorry, there are no %c's%n", letter);
                currentGuesses ++;
            }

            if (currentGuesses == totalGuesses) {
                playing = false;
            }
        }

        Iterator<String> end = wordSet.iterator();
        String finalWord = end.next();
        System.out.printf("Sorry but you lost, just like get better at this game, ya know? The word was %s", finalWord);

    }

    boolean isLetter(String input) {

    }

}
