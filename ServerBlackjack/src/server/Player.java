package server;

import java.util.ArrayList;


public class Player

{
    private ArrayList<Card> notAces; //the cards of each player
    private ArrayList<Card> aces;
    private ArrayList<Card> cards;
    private int total;

    public Player()
    {
        cards = new ArrayList<Card>();
        aces = new ArrayList<Card>();
        notAces = new ArrayList<Card>();
        total = 0;
    }

    public ArrayList<Card> getCards()
    {
        return cards;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal()
    {
        total = 0;
        for(Card c : notAces)
        {
            if(c.getValue() >= 10)
                total += 10;
            else
                total += c.getValue();
        }
        for(Card a : aces)
        {
            if(total <= 10)
                total += 11;
            else
                total += 1;
        }
    }

    public void addCard(Card card)
    {
        if(card.getValue() == 1)
        {
            aces.add(card);
        }
        else
        {
            notAces.add(card);
        }
        cards.add(card);
        setTotal();

    }
}
