package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**model.
 *  represents the 'leaderboard' object. it contains an inner class, a 'LeaderboardEntry' class.
 * the leaderboard is used to track the top 10 leading scores with name, and points.
 * it follows the singleton pattern, so only a single instance of the leaderboard is created.
 * get the leaderboard instance via getInstance().
 *
 * the leaderboard loads the leaderboard.txt file, converts them into a List<LeaderBoardEntry> of size 10.
 * each leaderboard entry has a name, and a score.
 * it the saves the players score (if high enough) when the player extracts.
 */
public class Leaderboard {


    /**
     * represents a leaderboard entry for when a player successfully extracts.
     * it contains 2 fields initialized in the constructor, a 'String name ' and a 'int score'.
     */
    public static class LeaderboardEntry {           // this is to store name - score pairs
        /**
         * represents the name of the player
         */
        String name;
        /**
         * represents the total points scored by the player
         */
        int score;

        /**
         * @param name name of the player
         * @param score total points scored by the player
         */
        LeaderboardEntry(String name, int score){
            this.name = name;
            this.score = score;
        }

        /**
         * @return returns [name] + " " + [score]
         */
        @Override
        public String toString(){
            return name + " " + score;
        }
    }

    /**
     * represents the one and only leaderboard instance
     */
    private static Leaderboard instance;        //singleton pattern for the leaderboard.

    /**
     * list of leaderboard entries
     */
    private final List<LeaderboardEntry> leaderbordEntries;

    /**
     * name of the leaderboard .txt file used to store the leaderboard scores
     */
    private final String LEADERBOARD_FILENAME = "leaderboard.txt";


    /**
     * constructor, initializes the list leaderboardEntries(size 10)
     * calls loadLeaderBoard method when it is first created
     */
    private Leaderboard() {
        this.leaderbordEntries = new ArrayList<>(10);
        loadLeaderBoard();      //load the leaderboard .txt file contents
    }

    /**
     * populates the leaderboardEntries with LeaderboardEntry.
     * a leaderboard entry contains name of the player and the pts scored
     */
    private void loadLeaderBoard() {
        InputStream in = Leaderboard.class.getClassLoader().getResourceAsStream(LEADERBOARD_FILENAME);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            for (int i = 0; i < 10; i++) {      //for 10 lines
                String line = bufferedReader.readLine();        //read each line
                String[] parts = line.split(" ", 2);
                try {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    leaderbordEntries.add(new LeaderboardEntry(name, score));
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

    /**
     * adds the player score into leaderboardEntries ONLY if it is higher than the last one.
     * sorts right after in descending order.
     * calls saveLeaderboard() to save it into the leaderboard.txt file.
     * @param name name of the player
     * @param score total pts of the player
     */
    public void addScore(String name, int score){
        //only add the score if it is higher than the last one
        if (score > leaderbordEntries.get(leaderbordEntries.size() -1).score) {
            leaderbordEntries.set(leaderbordEntries.size() - 1, new LeaderboardEntry(name, score)); //replace the lowest score
            Collections.sort(leaderbordEntries, (aGuy, bGuy) -> Integer.compare(bGuy.score, aGuy.score));  //sort in reverse order (descending)

            saveLeaderboard();
        }
    }

    /**
     * saves the player score into the leaderboard .txt file
     * it is called automatically when a score gets added via addScore()
     */
    private void saveLeaderboard() {    //saves all the leaderboard scores
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(LEADERBOARD_FILENAME))){
            for (LeaderboardEntry entry : leaderbordEntries) {
                printWriter.println(entry);       //overwrite he leaderboard .txt file with the current ones
            }
            System.out.println("[Leaderboard][saveLeaderboard()] Saved leaderboard entry" + leaderbordEntries);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * @return returns leaderboardEntries list
     */
    public List<LeaderboardEntry> getLeaderbordEntries() {
        return leaderbordEntries;
    }

    /**
     * @return returns a list the player names
     */
    public List<String> getLeaderbordEntryNames() {
        List<String> names = new ArrayList<>();
        for (LeaderboardEntry entry : leaderbordEntries) {
            names.add(entry.name);
        }
        return names;
    }

    /**
     * @return returns a list of the leaderboard scores
     */
    public List<Integer> getLeaderbordEntryScores() {
        List<Integer> scores = new ArrayList<>();
        for (LeaderboardEntry entry : leaderbordEntries) {
            scores.add(entry.score);
        }
        return scores;
    }

    /**
     * @return returns the LeaderBoard instance
     */
    public static synchronized Leaderboard getInstance() {
        if (instance == null) {
            instance = new Leaderboard();
        }
        return instance;
    }

}
