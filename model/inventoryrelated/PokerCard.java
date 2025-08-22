package model.inventoryrelated;

public class PokerCard extends Item{

    public static PokerCard oneclub = new PokerCard("oneclub", 5f, 1);
    public static PokerCard twoclub = new PokerCard("twoclub", 5f,2);
    public static PokerCard threeclub = new PokerCard("twoclub", 5f,3 );
    public static PokerCard fourclub = new PokerCard("fourclub", 5f, 4);
    public static PokerCard fiveclub = new PokerCard("fiveclub", 5f, 5);
    public static PokerCard sixclub = new PokerCard("sixclub", 5f, 6);
    public static PokerCard sevenclub = new PokerCard("sevenclub", 5f, 7);
    public static PokerCard eightclub = new PokerCard("eightclub", 5f, 8);
    public static PokerCard nineclub = new PokerCard("nineclub", 5f, 9);
    public static PokerCard tenclub = new PokerCard("tenclub", 4f, 10);
    public static PokerCard jclub = new PokerCard("jclub", 3f, 11);
    public static PokerCard qclub = new PokerCard("qclub", 2f, 12);
    public static PokerCard kclub = new PokerCard("kclub", 1f, 13);
    public static PokerCard aceclub = new PokerCard("aceclub", .8f, 50);

    public static PokerCard onespade = new PokerCard("onespade", 5f ,1);
    public static PokerCard twospade = new PokerCard("twospade", 5f , 2);
    public static PokerCard threespade = new PokerCard("twospade", 5f, 3);
    public static PokerCard fourspade = new PokerCard("fourspade", 5f, 4);
    public static PokerCard fivespade = new PokerCard("fivespade", 5f, 5);
    public static PokerCard sixspade = new PokerCard("sixspade", 5f, 6);
    public static PokerCard sevenspade = new PokerCard("sevenspade", 5f, 7);
    public static PokerCard eightspade = new PokerCard("eightspade", 5f, 8);
    public static PokerCard ninespade = new PokerCard("ninespade", 5f, 9);
    public static PokerCard tenspade = new PokerCard("tenspade", 4f, 10);
    public static PokerCard jspade = new PokerCard("jspade", 3f, 11);
    public static PokerCard qspade = new PokerCard("qspade", 2f, 12);
    public static PokerCard kspade = new PokerCard("kspade", 1f, 13);
    public static PokerCard acespade = new PokerCard("acespade", .8f, 50);

    public static PokerCard oneheart = new PokerCard("oneheart", 5f, 1);
    public static PokerCard twoheart = new PokerCard("twoheart", 5f, 2);
    public static PokerCard threeheart = new PokerCard("twoheart", 5f, 3);
    public static PokerCard fourheart = new PokerCard("fourheart", 5f, 4);
    public static PokerCard fiveheart = new PokerCard("fiveheart", 5f, 5);
    public static PokerCard sixheart = new PokerCard("sixheart", 5f, 6);
    public static PokerCard sevenheart = new PokerCard("sevenheart", 5f, 7);
    public static PokerCard eightheart = new PokerCard("eightheart", 5f, 8);
    public static PokerCard nineheart = new PokerCard("nineheart", 5f, 9);
    public static PokerCard tenheart = new PokerCard("tenheart", 4f, 10);
    public static PokerCard jheart = new PokerCard("jheart", 3f, 11);
    public static PokerCard qheart = new PokerCard("qheart", 2f, 12);
    public static PokerCard kheart = new PokerCard("kheart", 1f, 13);
    public static PokerCard aceheart = new PokerCard("aceheart", .8f, 50);

    public static PokerCard onediamond = new PokerCard("onediamond", 5f, 1);
    public static PokerCard twodiamond = new PokerCard("twodiamond", 5f, 2);
    public static PokerCard threediamond = new PokerCard("twodiamond", 5f, 3);
    public static PokerCard fourdiamond = new PokerCard("fourdiamond", 5f, 4);
    public static PokerCard fivediamond = new PokerCard("fivediamond", 5f, 5);
    public static PokerCard sixdiamond = new PokerCard("sixdiamond", 5f ,6);
    public static PokerCard sevendiamond = new PokerCard("sevendiamond", 5f, 7);
    public static PokerCard eightdiamond = new PokerCard("eightdiamond", 5f ,8);
    public static PokerCard ninediamond = new PokerCard("ninediamond", 5f,9);
    public static PokerCard tendiamond = new PokerCard("tendiamond", 4f, 10);
    public static PokerCard jdiamond = new PokerCard("jdiamond", 3f, 11);
    public static PokerCard qdiamond = new PokerCard("qdiamond", 2f, 12);
    public static PokerCard kdiamond = new PokerCard("kdiamond", 1f, 13);
    public static PokerCard acediamond = new PokerCard("acediamond", .8f, 50);

    public static PokerCard joker = new PokerCard("joker", .5f, 100);

    private static PokerCard[] pokerCards = {
            oneclub, twoclub, threeclub, fourclub, fiveclub, sixclub, sevenclub, eightclub, nineclub, tenclub, jclub, qclub, kclub, aceclub,
            onespade, twospade, threespade, fourspade, fivespade, sixspade, sevenspade, eightspade, ninespade, tenspade, jspade, qspade, kspade, acespade,
            oneheart, twoheart, threeheart, fourheart, fiveheart, sixheart, sevenheart, eightheart, nineheart, tenheart, jheart, qheart, kheart, aceheart,
            onediamond, twodiamond, threediamond, fourdiamond, fivediamond, sixdiamond, sevendiamond, eightdiamond, ninediamond, tendiamond, jdiamond, qdiamond, kdiamond, acediamond,
            joker
    };

    private final String name;
    private final float dropRate;
    private final int value;


    private PokerCard(String name, float droprate, int value){
        this.name = name;
        this.dropRate = droprate;
        this.value = value;
    }


    //default randomizer
    public static PokerCard getRandomPokerCard(){
        //calculate the total of all drop rates
        //lets say 3 cards with .1, .2, .3, -> total = .6
        float total = 0;
        for (PokerCard card : pokerCards){
            total += card.dropRate;
        }

        //pick a random number along the drop rate
        //picks a number between 0 - .6 , so if it is 0.0 - 0.1 -> the card 1 gets picked (16% chance) ...
        float random = (float) Math.random() * total;
        float count = 0;
        for (PokerCard card : pokerCards){
            count += card.dropRate;
            if(random  <= count){
                return card;
            }
        }
        return null;
    }


    //boosted rates to get an ace
    public static PokerCard getBoostedAcePokerCard(){
        if (Math.random() < 0.3){       //30% to get an ace
            PokerCard[] aces = {aceclub, acespade, acediamond, aceheart};
            return aces[ (int) Math.random() * aces.length];
        }
        else{   //otherwise return a random card with the same rates
            return getRandomPokerCard();
        }
    }


    //boosted rates to get a face card
    public static PokerCard getBoostedFacePokerCard(){
        if (Math.random() < 0.3){
            PokerCard[] faceCards = {jclub, qclub, kclub,
                                    jspade, qspade, kspade,
                                    jdiamond, qdiamond, kdiamond,
                                    jheart, qheart, kheart,};
            return faceCards[ (int) Math.random() * faceCards.length];
        }
        else{
            return getRandomPokerCard();
        }
    }

    //boosted rates to get a 10 card
    public static PokerCard getBoosted10Card(){
        if (Math.random() < 0.3){
            PokerCard[] tencards = {tenclub, tenspade, tendiamond, tenheart};
            return tencards[ (int) Math.random() * tencards.length];
        }
        else{
            return getRandomPokerCard();
        }
    }




    public String getName() {
        return name;
    }


    public int getValue() {
        return value;
    }





}
