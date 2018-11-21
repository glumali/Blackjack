package blackjack;

public abstract class Card {
    private boolean available = true;

    protected int faceValue;
    protected blackjack.Suit suit;

    public Card(int val, blackjack.Suit s) {
        faceValue = val;
        suit = s;
    }

    public abstract int value();

    public blackjack.Suit suit() {
        return suit;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markUnavailable() {
        available = false;
    }

    public void markAvailable() {
        available = true;
    }

    public String toString() {
        String card;
        if (faceValue == 1) card = "Ace";
        else if (faceValue == 11) card = "Jack";
        else if (faceValue == 12) card = "Queen";
        else if (faceValue == 13) card = "King";
        else card = "" + faceValue;

        return (card + " of " + suit);
    }
}
