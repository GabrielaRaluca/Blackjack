package server;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {

    public enum Suit
    {
        DIAMONDS, HEARTS, SPADES, CLUBS
    }

    private ArrayList<Card> deck;


    public Deck()
    {
        deck = new ArrayList<Card>();
        for(int i = 1; i <= 13; i++)
        {
            for(Suit s : Suit.values())
            {
                deck.add(new Card(i, s.toString()));
            }
        }
        Collections.shuffle(deck);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card drawCard()
    {
        Card result = deck.get(0);
        deck.remove(0);
        return result;
    }

}
