package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame{
    private Set<String> words;
    private SortedSet<Character> guessedLetters;
    private HashMap<String, Set<String>> map;
    private HashMap<String, Integer> countMap;
    private StringBuilder baseWord;
    private int wordLength;
    public EvilHangmanGame() {
        words = new HashSet<>();
        wordLength = 0;
        map = new HashMap<>();
        baseWord = new StringBuilder();
        guessedLetters = new TreeSet<>();
        countMap = new HashMap<>();
    }
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        boolean isEqual=false;
        words.clear();
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
        setBaseWord();
    }

    public void setBaseWord() {
        baseWord.setLength(0);
        baseWord.append("_".repeat(Math.max(0, wordLength)));
    }

    public StringBuilder getBaseWord() {
        return baseWord;
    }


    public int getWordLength() {
        return wordLength;
    }

    private String getSubsetKey(char guess, String word, List<Integer> charIndex) {
        StringBuilder pattern = baseWord;
        for (int i = 0; i < charIndex.size(); i++) {
            pattern.setCharAt(charIndex.get(i), guess);
        }

        String returnPattern = pattern.toString();
        pattern.setLength(0);
        setBaseWord();
        return returnPattern;
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        boolean isBlank = false;
        String pattern = null;
        if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }
        guessedLetters.add(guess);
        for(String word:words) {
            List<Integer> charList = new ArrayList<Integer>();
            int charIndex= word.indexOf(guess);
            if (charIndex==-1) {
                pattern = baseWord.toString();
                isBlank = true;
            }
            while (charIndex >= 0) {
                charList.add(charIndex);
                charIndex = word.indexOf(guess, charIndex+1);
            }

            if (!isBlank) {
                pattern = getSubsetKey(guess, word, charList);
            }
            isBlank = false;
            if(map.containsKey(pattern)) {
                Set<String> helper = map.get(pattern);
                helper.add(word);
                map.put(pattern, helper); //Todo: figure out how to append a string to a set that is already in the map
                int num = countMap.get(pattern);
                num++;
                countMap.put(pattern, num);
            }
            else{
                Set<String> helperSet = new HashSet<>();
                helperSet.add(word);
                map.put(pattern, helperSet);
                countMap.put(pattern, 1);
            }
        }//ToDO: after all the subsets have been added, check to see which is the largest and have that be the new set
        String mapKey = null;
        List<String> keyList = new ArrayList<>();
        int largestSet = 0;
        int maxValueInMap=(Collections.max(countMap.values()));
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            if(entry.getValue().size() == maxValueInMap) {
                keyList.add(entry.getKey());
            }
        }
        if(keyList.size() > 1){
            mapKey = tiebreakers(keyList);
        }
        else {
            mapKey = keyList.get(0);
        }

        Set <String> returnSet = new HashSet<>();
        returnSet = map.get(mapKey);
        words = returnSet;
        map.clear();
        countMap.clear();
        return returnSet;
    }

    private String tiebreakers(List<String> keyList) {
        List<String> returnWord = new ArrayList<>();
        Map<String, Integer> numChar = new HashMap<>();
        Map<String, Integer> rightMost = new HashMap<>();
        int counter=0;
        for (String key: keyList) {
            if(key.equals(baseWord.toString())) {
                return key;
            }
        }
        for (String key: keyList){
            for(int i = 0; i < key.length(); i++) {
                if(key.charAt(i) != '_') {
                    counter++;
                }
            }
            numChar.put(key, counter);
            counter = 0;
        }
        int lowest = 10;
        for (Map.Entry<String, Integer> entry : numChar.entrySet()) {
            if(entry.getValue() < lowest){
                lowest = entry.getValue();
            }
        }
        for (Map.Entry<String, Integer> entry : numChar.entrySet()) {
            if(entry.getValue()==lowest){
                returnWord.add(entry.getKey());
            }
        }
        if(returnWord.size()==1) {
            return returnWord.get(0);
        }
        else {
            int iTotal = 0;
            for(String w: returnWord) {
                for(int i = 0; i < w.length(); i++) {
                    if(w.charAt(i) != '_') {
                        iTotal = iTotal + i;
                    }
                }
                rightMost.put(w, iTotal);
                iTotal = 0;
            }
            returnWord.clear();
            String returnW = null;
            int max = Collections.max(rightMost.values());
            for(Map.Entry<String, Integer> entry: rightMost.entrySet()){
                if (entry.getValue() == max){
                    returnW = entry.getKey();
                }
            }
            return returnW;
        }
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
