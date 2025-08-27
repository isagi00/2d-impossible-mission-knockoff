package view;

import controller.*;
import model.Leaderboard;
import model.entities.Dog;
import model.entities.Drone;
import model.entities.Player;
import model.interactableObjects.InteractableObject;
import view.gamePanelViews.LeaderboardView;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AudioManager implements Observer {
    private static AudioManager instance;       //single instance of the audio manager (singleton ptrn)
    private Map<String, List<Clip>> soundEffects;     //is gonna have like : name - list of sound effect (all sound effects get loaded here)
    private int CopiesPerSound = 5;                                               //allows multiple clips to play simultaneously (max 3 clips under the same name)


    private AudioManager() {        //load all sound fx when creating this audio manager instance
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }


    @Override
    public void update(Observable o, Object arg) {      //whenever model changes something, audio plays
        if (o instanceof TitleScreenController) {
            switch((String)(arg)){      //cast that to into a string :)
                case "menu up":
                    playSound("menu up");
                    System.out.println("[AudioManager] played menu up sound effect");
                    break;
                case "menu down":
                     playSound("menu down");
                    System.out.println("[AudioManager] played menu down sound effect");
                    break;
                case "menu enter":
                    playSound("menu enter");
                    System.out.println("[AudioManager] played menu enbter sound effect");
                    break;
            }
        }
        else if (o instanceof LeaderboardViewController){
            switch((String)(arg)){
                case "menu enter":
                    playSound("menu enter");
                    System.out.println("[AudioManager] played menu enter sound effect");
                    break;
            }
        }

        else if (o instanceof WhatsYourNameController){
            switch((String)(arg)){
                case "enter pressed":
                    playSound("menu enter");
                    System.out.println("[AudioManager] played name submitted sound effect");
                    break;
                case "key typed":
                    playSound("entered char");
                    System.out.println("[AudioManager] played key typed sound effect");
                    break;
                case "backspace pressed":
                    playSound("deleted char");
                    System.out.println("[AudioManager] played backspace pressed sound effect");
                    break;
            }
        }

        else if (o instanceof PauseMenuController){
            switch((String)(arg)){
                case "menu up":
                    playSound("menu up");
                    System.out.println("[AudioManager] played menu up sound effect");
                    break;
                case "menu down":
                    playSound("menu down");
                    System.out.println("[AudioManager] played menu down sound effect");
                    break;
                case "menu enter":
                    playSound("menu enter");
                    System.out.println("[AudioManager] played menu enter sound effect");
                    break;
            }
        }

        else if(o instanceof ResultScreenController){
            switch((String)(arg)){
                case "enter pressed":
                    playSound("menu enter");
                    System.out.println("[AudioManager] played menu enter sound effect");
                    break;
            }
        }

        else if (o instanceof GameKeyListener){
            switch((String)(arg)){
                case "esc pressed":
                    playSound("deleted char");
                    System.out.println("[AudioManager] played esc pressed sound effect");
                    break;
            }
        }
        else if (o instanceof Player){
            switch((String)(arg)){
                case "player jumped":
                    playSound("player jump");
                    System.out.println("[AudioManager] played player jump sound effect");
                    break;
                case "player died":
                    playSound("player death");
                    System.out.println("[AudioManager] played player death sound effect");
                    break;
                case "player moved":
                    playSound("player step");
                    System.out.println("[AudioManager] played player step sound effect");
                    break;
                case "player extracted":
                    playSound("player extracted");
                    System.out.println("[AudioManager] played player extracted sound effect");
                    break;
            }
        }
        else if (o instanceof InteractableObject){
            switch((String)(arg)){
                case "rare card found":
                    playSound("rare card found");
                    System.out.println("[AudioManager] played rare card found sound effect");
                    break;
                case "common card found":
                    playSound("common card found");
                    System.out.println("[AudioManager] played common card found sound effect");
                    break;
            }
        }
        else if (o instanceof Dog){
            switch((String)(arg)){
                case "dog moving":
                    playSound("dog step");
                    System.out.println("[AudioManager] played dog moving sound effect");
                    break;
            }
        }
        else if(o instanceof Drone){
            switch((String)(arg)){
                case "drone moving":
                    playSound("drone step");
                    System.out.println("[AudioManager] played drone moving sound effect");
                    break;
            }
        }

    }


    private void loadSoundEffects() {   //load all sound once at startup
        //title screen  / pause menu / result screen / leaderboard navigation sounds
        loadSound("menu up", "sounds/menuup.wav");
        loadSound("menu down", "sounds/menudown.wav");
        loadSound("menu enter", "sounds/menuenter.wav");
        //enter name sounds (used in what's your name controller)
        loadSound("entered char", "sounds/enteredchar.wav");
        loadSound("deleted char", "sounds/deletedchar.wav");
        //player sounds
        loadSound("player jump", "sounds/playerjump.wav");
        loadSound("player death", "sounds/playerdeath.wav");
        loadSound("player step", "sounds/playerstep.wav");
        loadSound("player extracted", "sounds/playerextracted.wav");
        //rare card and common card found
        loadSound("rare card found", "sounds/rarecardcollected.wav");
        loadSound("common card found", "sounds/commoncardcollected.wav");
        //dog sounds
        loadSound("dog step", "sounds/dogstep.wav");
        //drone sounds
        loadSound("drone step", "sounds/dronestep.wav");
    }


    //loads a 5 same sound clips given the sound effect filepath
    private void loadSound(String soundName, String filePath){
        try{
            URL soundurl = getClass().getClassLoader().getResource(filePath);  //get url to the audio sample
            if (soundurl == null) {
                throw new IllegalArgumentException("[AudioManager] couldnt find: " + filePath );

            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundurl);   //this creates audio stream from the url does the heavy lifting hell yea
            ais.mark(Integer.MAX_VALUE);        // mark the start of the audio...
            List<Clip> audioClips = new ArrayList<>(CopiesPerSound);         //MAX 5 INSTANCES OF THE SAME AUDIO CLIP

            for (int i = 0; i < 5; i ++){       //pre-load multiple instances of the same clip under the same name, so that if player spams a key audio still gets reproduced
                ais.reset();            //... and reset back to it
                Clip audioClip = AudioSystem.getClip();  //creates the audio 'clip' and opens it
                audioClip.open(ais);

                audioClips.add(audioClip);  //add to list
            }
            soundEffects.put(soundName, audioClips);  //put the name - list of sound clips in the map

            System.out.println("[AudioManager] loaded sound,  " + soundName + " from " + filePath);
            ais.close();
        }
        catch (Exception e){
            System.out.println("[AudioManager] uanble to load sound,  " + soundName +" from " + filePath);
            e.printStackTrace();
        }
    }

    private void playSound(String soundName) {

        new Thread(() -> {
            //without creating a new thread, the audio would freeze the game thread, so that even in the title screen
            //the game would freeze for around 200ms when navigating the menu.
            //by creating new little threads when the audio plays, there is no game thread freezing anymoreì
            List<Clip> audioClips = soundEffects.get(soundName);

            Clip availableClip = null ;         //start with no availableClip clip
            for (Clip audioClip : audioClips){
                if (!audioClip.isRunning()){    //if a clip instance is not currently running then set it as the availableClip clip
                    availableClip = audioClip;
//                    System.out.println("[AudioManager] played " + soundName + " sound effect from clip: " + availableClip);   //ok sure that there are 5 different clips of the same audio clip
                    break;  //end the loop when found the availableClip clip
                }
            }

            //if all the different instatnce of the same sound are occupied then just dont play the sound
            if (availableClip == null){
                return;
            }

            availableClip.stop();
            availableClip.setFramePosition(0);
            availableClip.start();
        }).start();
    }



}
