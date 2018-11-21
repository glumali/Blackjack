package blackjack;

import java.util.ArrayList;

public class BlackjackHand extends blackjack.Hand<blackjack.BlackjackCard> {
    // aces can be either 1 or 11
    private ArrayList<Integer> possibleScores() {
        ArrayList<Integer> scores = new ArrayList<>();
        for (blackjack.BlackjackCard card : inHand) {
            if (card.isAce()) {
                if (scores.size() == 0) {
                    scores.add(card.maxValue());
                    scores.add(card.minValue());
                } else {
                    int oldSize = scores.size();
                    // make new entries, adding 11 to all existing values
                    for (int i = 0; i < oldSize; i++)
                        scores.add(scores.get(i) + card.maxValue());

                    // add 1 to all old values
                    for (int i = 0; i < oldSize; i++)
                        scores.set(i, scores.get(i) + card.minValue());
                }
            }
            // not an ace
            else {
                if (scores.size() == 0) {
                    scores.add(card.value());
                } else {
                    for (int i = 0; i < scores.size(); i++) {
                        scores.set(i, scores.get(i) + card.value());
                    }
                }
            }
        }
        return scores;
    }

    public int getScore() {
        ArrayList<Integer> scores = possibleScores();
        int maxGood = Integer.MIN_VALUE;
        int minBust = Integer.MAX_VALUE;

        for (int score : scores) {
            if (score > 21 && score < minBust) minBust = score;
            else if (score <= 21 && score > maxGood) maxGood = score;
        }
        // never found a good hand
        if (maxGood == Integer.MIN_VALUE) return minBust;
        else return maxGood;
    }

    public boolean isBust() {
        return getScore() > 21;
    }

    public boolean is21() {
        return getScore() == 21;
    }

    // only happens with Ace and a 10-point card
    public boolean isBlackjack() {
        if (inHand.size() != 2 || !is21()) return false;

        // if size 2 and score is 21, must be an ace
        // only not a blackjack if a 10 is found
        for (blackjack.BlackjackCard card : inHand)
            if (card.faceValue == 10) return false;

        return true;
    }

    // used for dealer logic
    public boolean isSoft17() {
        // soft 17 if score is 17 and hand includes an ace
        if (getScore() != 17) return false;

        for (blackjack.BlackjackCard card : inHand)
            if (card.faceValue == 1) return true;

        // no ace found
        return false;
    }
}
