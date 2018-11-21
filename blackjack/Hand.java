package blackjack;

import java.util.ArrayList;

public class Hand<T extends blackjack.Card> {
    protected ArrayList<T> inHand = new ArrayList<T>();

    public int getScore() {
        int score = 0;
        for (T card : inHand) score += card.value();
        return score;
    }

    public void addCard(T card) {
        inHand.add(card);
    }

    public void discardCards() {
        inHand.clear();
    }
}
