import java.util.ArrayList;

/**
 * Created by Cata on 7/7/2016.
 */
public class Player {
    protected ArrayList<Card> cards;
    protected String name;
    protected int total;
    protected int score;
    protected boolean receiving;

    public Player()
    {
        receiving=true;
        name="Nobody";
        cards=new ArrayList<Card>();
    }
    public Player(String Name)
    {
        receiving=true;
        cards=new ArrayList<Card>();
    }
    public String toString() {
        if (cards.size()!=0) {
            String result = String.format("The cards of %s are:/n", name);
            for (Card y : cards)
                result += y;
            result += String.format("with a total score of %d", total);
            return result;
        }
        else return "you are empty-handed";
    }

    public void addCard(Card c){
        cards.add(c);
    }

}
