package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    private Set<String> words;
    private HashMap<String, Set<String>> map;
    private StringBuilder baseWord;
    private int wordLength;
    public EvilHangmanGame() {
        words = new HashSet<>();
        wordLength = 0;
        map = new HashMap<>();
        baseWord = new StringBuilder();
    }
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        boolean isEqual=false;
        if(dictionary.length() == 0) {
            throw new EmptyDictionaryException();
        }
        Scanner scanner = new Scanner(dictionary);
        while(scanner.hasNext()){
            String str = scanner.next();
            if(str.length() == wordLength) {
                words.add(str);
            }
        }
        if (words.size() == 0) {
            throw new EmptyDictionaryException();
        }
        this.wordLength = wordLength;
    }

    public void setBaseWord() {
        baseWord.append("_".repeat(Math.max(0, wordLength)));
    }


    public int getWordLength() {
        return wordLength;
    }

    private String getSubsetKey(char guess, String word, int charIndex) {
        StringBuilder pattern = new StringBuilder();

//        for (int i = 0; i < wordLength; i++) {
//            if(i == charIndex) {
//                //pattern.append(guess);
//
//            }
//            else {
//                pattern.append('-');
//            }
//        }
        return pattern.toString();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        for(String word:words) {
            int charIndex= word.indexOf(guess);
            if (charIndex==-1) {
                continue;
            }

            String pattern = getSubsetKey(guess, word, charIndex);
            if(map.containsKey(pattern)) {
                Set<String> helper = map.get(pattern);
                helper.add(word);
                map.put(pattern, helper); //Todo: figure out how to append a string to a set that is already in the map
            }
            else{
                Set<String> helperSet = new HashSet<>();
                helperSet.add(word);
                map.put(pattern, helperSet);
            }
        }//ToDO: after all the subsets have been added, check to see which is the largest and have that be the new set
        //ToDO: then have the current pattern of the largest subset be the new pattern that we work off. might have to implement that lower too
        //ToDO: have a starter pattern and each turn it is added to with guessed letters or not. turn one it is blank then it gets letters. tostring to return
        return null;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
