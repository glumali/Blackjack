package blackjack;

public class BlackjackCard extends blackjack.Card {
    public BlackjackCard(int val, blackjack.Suit s) {
        super(val, s);
    }

    // face cards are worth 10
    // valuation of aces are handled below
    public int value() {
        if (isAce()) return 1;
        else if (faceValue >= 11 && faceValue <= 13) return 10;
        else return faceValue;
    }

    public int minValue() {
        if (isAce()) return 1;
        else return value();
    }

    public int maxValue() {
        if (isAce()) return 11;
        else return value();
    }

    public boolean isAce() {
        return faceValue == 1;
    }

    public boolean isFaceCard() {
        return faceValue >= 11 && faceValue <= 13;
    }
}
