package server;

public class Card {

    private int value;
    private String suit;

    public Card()
    {
        this.value= 0;
        this.suit = "";
    }

    public Card(int value, String suit)
    {
        this.value = value;
        this.suit = suit;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public String getSuit()
    {
        return suit;
    }

    public void setSuit(String suit)
    {
        this.suit = suit;
    }

    public String toString()
    {
        String result = "";
        if(value == 1)
        {
            result = "Ace of " + suit;
        }
        if(value == 11)
        {
            result = "Jack of " + suit;
        }
        if(value == 12)
        {
            result = "Queen of " + suit;
        }
        if(value == 13)
        {
            result = "King of " + suit;
        }
        if(value <= 10 && value != 1)
        {
            result = value + " of " + suit;
        }
        return result;
    }
}

