package server;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Cata on 7/6/2016.
 */

public class Deck {
    public enum Suit
    {
        DIAMONDS, HEARTS, SPADES, CLUBS
    }
    private ArrayList<Card> cards;



    public Deck()
    {
        cards=new ArrayList<Card>();
        int j=0;
        for (int i=2;i<=14;i++) {
            for(Suit s : Suit.values())
            {
                cards.add(new Card(s.toString(),i));
            }
        }
        shuffle();
    }
    public String toString(){
        return String.format("in pachet sunt %s de carti", cards.size());
    }
    public void shuffle(){
        Collections.shuffle(cards);
    }
    public Card drawCard()
    {
        Card tmp=cards.get(0);
        cards.remove(0);
        return tmp;
    }



    //not needed for arraylists
    private void shuffleArray(Card[] array)
    {
        int index;
        Card temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
