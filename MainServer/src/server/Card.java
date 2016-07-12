package server;

/**
 * Created by Cata on 7/6/2016.
 */
public class Card {
    private int points;
    private int value;
    private String suit;
    public Card()
    {
        value=1;
        suit="uninitialised";
    }
    public Card(String type, int value)
    {
        this.value = value;
        this.suit = type;
        if (value < 12)
            this.points = value;
        else this.points = 10;//pentru ca punctajul J D K e de 10
    }


    public int getPoints(){
        return points;
    }
    public int getValue(){return value;}
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
        if(value == 12)
        {
            result = "Jack of " + suit;
        }
        if(value == 13)
        {
            result = "Queen of " + suit;
        }
        if(value == 14)
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
