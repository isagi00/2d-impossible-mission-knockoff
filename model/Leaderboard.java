package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;


//the leaderboard always loads the leaderboard.txt file as an array of size 10.
//the saves the players score (if high enough) when the player extracts.
public class Leaderboard {

    public static class LeaderbordEntry{           // this is to store name - score pairs
        String name;
        int score;
        LeaderbordEntry(String name, int score){
            this.name = name;
            this.score = score;
        }
        @Override
        public String toString(){
            return name + " " + score;
        }
    }
    private static Leaderboard instance;        //singleton pattern for the leaderboard.

    private List<LeaderbordEntry> leaderbordEntries;

    private final String LEADERBOARD_FILENAME = "leaderboard.txt";


    private Leaderboard() {
        this.leaderbordEntries = new ArrayList<>(10);
        loadLeaderBoard();      //load the leaderboard .txt file contents

    }

    private void loadLeaderBoard() {

        InputStream in = Leaderboard.class.getClassLoader().getResourceAsStream(LEADERBOARD_FILENAME);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            for (int i = 0; i < 10; i++) {      //for 10 lines
                String line = bufferedReader.readLine();        //read each line
                String[] parts = line.split(" ", 2);
                try {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    leaderbordEntries.add(new LeaderbordEntry(name, score));
                }
                catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (IOException e){
            System.out.println("[Leaderboard][loadLeaderboard()] failed to load leaderboard values: ");
            e.printStackTrace();
        }
    }

    public void addScore(String name, int score){
        //only add the score if it is higher than the last one
        if (score > leaderbordEntries.get(leaderbordEntries.size() -1).score) {
            leaderbordEntries.set(leaderbordEntries.size() - 1, new LeaderbordEntry(name, score)); //replace the lowest score
            Collections.sort(leaderbordEntries, (aGuy, bGuy) -> Integer.compare(bGuy.score, aGuy.score));  //sort in reverse order (descending)

            saveLeaderboard();
        }
    }

    private void saveLeaderboard() {    //saves all the leaderboard scores
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(LEADERBOARD_FILENAME))){
            for (LeaderbordEntry entry : leaderbordEntries) {
                printWriter.println(entry);       //overwrite he leaderboard .txt file with the current ones
            }
            System.out.println("[Leaderboard][saveLeaderboard()] Saved leaderboard entry" + leaderbordEntries);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<LeaderbordEntry> getLeaderbordEntries() {
        return leaderbordEntries;
    }

    public List<String> getLeaderbordEntryNames() {
        List<String> names = new ArrayList<>();
        for (LeaderbordEntry entry : leaderbordEntries) {
            names.add(entry.name);
        }
        return names;
    }

    public List<Integer> getLeaderbordEntryScores() {
        List<Integer> scores = new ArrayList<>();
        for (LeaderbordEntry entry : leaderbordEntries) {
            scores.add(entry.score);
        }
        return scores;
    }

    public static synchronized Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

    @Override
    public String toString() {
        for (LeaderbordEntry entry : leaderbordEntries) {
            System.out.println(entry);
        }
        return "";
    }

    public void refresh() {
        loadLeaderBoard();
    }







}
