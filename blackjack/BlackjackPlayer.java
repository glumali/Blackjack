package blackjack;

public class BlackjackPlayer {
    private blackjack.BlackjackHand hand;
    private int funds;

    public BlackjackPlayer(int funds, blackjack.BlackjackHand hand) {
        this.funds = funds;
        this.hand = hand;
    }

    public blackjack.BlackjackHand getHand() {
        return hand;
    }

    public int getFunds() {
        return funds;
    }

    public void setHand(blackjack.BlackjackHand h) {
        hand = h;
    }

    public void setFunds(int f) {
        funds = f;
    }

    public void drawCard(blackjack.BlackjackCard card) {
        hand.addCard(card);
    }
}
