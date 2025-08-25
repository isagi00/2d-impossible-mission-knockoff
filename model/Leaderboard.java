package model;

import model.entities.Player;

import javax.lang.model.type.ArrayType;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//the leaderboard always loads the leaderboard.txt file as an array of size 10.
//the saves the players score (if high enough) when the player extracts.
public class Leaderboard {

    private List<Integer> leaderboard;
    private final String LEADERBOARD_FILENAME = "leaderboard.txt";


    public Leaderboard() {
        this.leaderboard = new ArrayList<>(10);
        loadLeaderBoard();      //load the leaderboard .txt file contents

    }

    private void loadLeaderBoard() {
        File file = new File(LEADERBOARD_FILENAME);
        InputStream in = getClass().getClassLoader().getResourceAsStream(LEADERBOARD_FILENAME);
        leaderboard.clear();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            for (int i = 0; i < 10; i++) {      //for 10 lines
                String line = bufferedReader.readLine();        //read each line
                leaderboard.add(Integer.parseInt(line.trim()));     //add to the score to the leaderboard list
            }
            System.out.println("[Leaderboard][loadLeaderboard()] loaded leaderboard values: " + leaderboard);
        }
        catch (IOException e){
            System.out.println("[Leaderboard][loadLeaderboard()] failed to load leaderboard values: ");
            e.printStackTrace();
        }
    }

    public void addScore(int score){
        //only add the score if it is higher than the last one
        if (score > leaderboard.get(leaderboard.size() -1)) {
            leaderboard.set(leaderboard.size() - 1, score); //replace the lowest score
            Collections.sort(leaderboard, Collections.reverseOrder());  //sort in reverse order (descending)
            saveLeaderboard();
        }
    }

    private void saveLeaderboard() {    //saves all the leaderboard scores
        try(PrintWriter printWriter = new PrintWriter(new FileWriter(LEADERBOARD_FILENAME))){
            for (int score : leaderboard) {
                printWriter.println(score);       //overwrite he leaderboard .txt file with the current ones
            }
            System.out.println("[Leaderboard][saveLeaderboard()] Saved leaderboard values" + leaderboard);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Integer> getLeaderboard() {
        return leaderboard;
    }








}
