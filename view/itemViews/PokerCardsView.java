package view.itemViews;

import model.inventoryrelated.PokerCard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class PokerCardsView extends ItemView{
    /**
     * club seed poker cards sprites
     */
    private static BufferedImage oneclub, twoclub, threeclub, fourclub, fiveclub, sixclub, sevenclub, eightclub, nineclub, tenclub, jclub, qclub, kclub, aceclub;
    /**
     * spade seed poker cards sprites
     */
    private static BufferedImage onespade, twospade, threespade, fourspade, fivespade, sixspade, sevenspade, eightspade, ninespade, tenspade,  jspade, qspade, kspade, acespade;
    /**
     * heart seed poker cards sprites
     */
    private static BufferedImage oneheart, twoheart, threeheart, fourheart, fiveheart, sixheart, sevenheart, eightheart, nineheart, tenheart, jheart, qheart, kheart, aceheart;
    /**
     * diamond seed poker cards sprites
     */
    private static BufferedImage onediamond, twodiamond, threediamond, fourdiamond, fivediamond, sixdiamond, sevendiamond, eightdiamond, ninediamond, tendiamond, jdiamond, qdiamond, kdiamond, acediamond;

    /**
     * the 'joker' card sprite
     */
    private BufferedImage joker;
    /**
     * current sprite of the {@link PokerCard}
     */
    private BufferedImage currentSprite;


    /**
     * view of all of the {@link PokerCard}s. when this object is created, it will load
     * all the poker card sprites via {@link #loadSprites()}.
     */
    public PokerCardsView() {
        loadSprites();
    }


    /**
     * loads all the poker card sprites
     */
    @Override
    public void loadSprites(){
        try {
            oneclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/1club.png"));
            twoclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/2club.png"));
            threeclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/3club.png"));
            fourclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/4club.png"));
            fiveclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/5club.png"));
            sixclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/6club.png"));
            sevenclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/7club.png"));
            eightclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/8club.png"));
            nineclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/9club.png"));
            tenclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/10club.png"));
            jclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/jclub.png"));
            qclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/qclub.png"));
            kclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/kclub.png"));
            aceclub = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/aceclub.png"));

            onespade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/1spade.png"));
            twospade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/2spade.png"));
            threespade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/3spade.png"));
            fourspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/4spade.png"));
            fivespade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/5spade.png"));
            sixspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/6spade.png"));
            sevenspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/7spade.png"));
            eightspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/8spade.png"));
            ninespade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/9spade.png"));
            tenspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/10spade.png"));
            jspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/jspade.png"));
            qspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/qspade.png"));
            kspade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/kspade.png"));
            acespade = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/acespade.png"));

            oneheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/1heart.png"));
            twoheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/2heart.png"));
            threeheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/3heart.png"));
            fourheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/4heart.png"));
            fiveheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/5heart.png"));
            sixheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/6heart.png"));
            sevenheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/7heart.png"));
            eightheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/8heart.png"));
            nineheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/9heart.png"));
            tenheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/10heart.png"));
            jheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/jheart.png"));
            qheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/qheart.png"));
            kheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/kheart.png"));
            aceheart = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/aceheart.png"));

            onediamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/1diamond.png"));
            twodiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/2diamond.png"));
            threediamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/3diamond.png"));
            fourdiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/4diamond.png"));
            fivediamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/5diamond.png"));
            sixdiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/6diamond.png"));
            sevendiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/7diamond.png"));
            eightdiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/8diamond.png"));
            ninediamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/9diamond.png"));
            tendiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/10diamond.png"));
            jdiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/jdiamond.png"));
            qdiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/qdiamond.png"));
            kdiamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/kdiamond.png"));
            acediamond = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/acediamond.png"));

            joker = ImageIO.read(getClass().getClassLoader().getResourceAsStream("items/darkpokercards/JOKER.png"));
//            System.out.println("[PokerCardsView][loadSprites()]loaded poker card sprites");
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**sets the {@link #currentSprite} based on the poker card parameter.
     * example: if the parameter card is a "one of clubs" {@link PokerCard} object, it will set the {@link #currentSprite}
     * to the 'one of clubs' sprite.
     * @param card {@link PokerCard} poker card.
     */
    protected void setCurrentSprite(PokerCard card) {
        String cardName = card.getName();

        switch (cardName){
            case "oneclub": currentSprite = oneclub; break;
            case "twoclub": currentSprite = twoclub; break;
            case "threeclub": currentSprite = threeclub; break;
            case "fourclub": currentSprite = fourclub; break;
            case "fiveclub": currentSprite = fiveclub; break;
            case "sixclub": currentSprite = sixclub; break;
            case "sevenclub": currentSprite = sevenclub; break;
            case "eightclub": currentSprite = eightclub; break;
            case "nineclub": currentSprite = nineclub; break;
            case "tenclub": currentSprite = tenclub; break;
            case "jclub": currentSprite = jclub; break;
            case "qclub": currentSprite = qclub; break;
            case "kclub": currentSprite = kclub; break;
            case "aceclub": currentSprite = aceclub; break;

            case "onespade": currentSprite = onespade; break;
            case "twospade": currentSprite = twospade; break;
            case "threespade": currentSprite = threespade; break;
            case "fourspade": currentSprite = fourspade; break;
            case "fivespade": currentSprite = fivespade; break;
            case "sixspade": currentSprite = sixspade; break;
            case "sevenspade": currentSprite = sevenspade; break;
            case "eightspade": currentSprite = eightspade; break;
            case "ninespade": currentSprite = ninespade; break;
            case "tenspade": currentSprite = tenspade; break;
            case "jspade": currentSprite = jspade; break;
            case "qspade": currentSprite = qspade; break;
            case "kspade": currentSprite = kspade; break;
            case "acespade": currentSprite = acespade; break;

            case "oneheart": currentSprite = oneheart; break;
            case "twoheart": currentSprite = twoheart; break;
            case "threeheart": currentSprite = threeheart; break;
            case "fourheart": currentSprite = fourheart; break;
            case "fiveheart": currentSprite = fiveheart; break;
            case "sixheart": currentSprite = sixheart; break;
            case "sevenheart": currentSprite = sevenheart; break;
            case "eightheart": currentSprite = eightheart; break;
            case "nineheart": currentSprite = nineheart; break;
            case "tenheart": currentSprite = tenheart; break;
            case "jheart": currentSprite = jheart; break;
            case "qheart": currentSprite = qheart; break;
            case "kheart": currentSprite = kheart; break;
            case "aceheart": currentSprite = aceheart; break;

            case "onediamond": currentSprite = onediamond; break;
            case "twodiamond": currentSprite = twodiamond; break;
            case "threediamond": currentSprite = threediamond; break;
            case "fourdiamond": currentSprite = fourdiamond; break;
            case "fivediamond": currentSprite = fivediamond; break;
            case "sixdiamond": currentSprite = sixdiamond; break;
            case "sevendiamond": currentSprite = sevendiamond; break;
            case "eightdiamond": currentSprite = eightdiamond; break;
            case "ninediamond": currentSprite = ninediamond; break;
            case "tendiamond": currentSprite = tendiamond; break;
            case "jdiamond": currentSprite = jdiamond; break;
            case "qdiamond": currentSprite = qdiamond; break;
            case "kdiamond": currentSprite = kdiamond; break;
            case "acediamond": currentSprite = acediamond; break;

            case "joker": currentSprite = joker; break;

            default: currentSprite = oneclub; break;
        }
    }


    /** gets the current poker card sprite
     * @return the {@link #currentSprite}
     */
    public BufferedImage getCurrentSprite(){
        return currentSprite;
    }



}
