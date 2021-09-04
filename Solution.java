package bataille2;

import java.util.*;
import java.io.*;
import java.math.*;
import static java.util.AbstractMap.SimpleEntry;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of cards for player 1
        Queue<Card> player1Cards = new LinkedList<Card>();
        for (int i = 0; i < n; i++) {
            player1Cards.add(new Card(in.next())); // the n cards of player 1
        }
        int m = in.nextInt(); // the number of cards for player 2
        Queue<Card> player2Cards = new LinkedList<Card>();
        for (int i = 0; i < m; i++) {
            player2Cards.add(new Card(in.next())); // the m cards of player 2
        }

        GameResult gameResult = play(player1Cards, player2Cards);
        System.out.println(gameResult.winner == -1 ? "PAT": gameResult);
    }

	private static GameResult play(Queue<Card> player1Cards, Queue<Card> player2Cards) {
        GameResult gameResult = new GameResult();
        gameResult.rounds = 0;
		Queue<Card> player1DiscardedCards = new LinkedList<>();
        Queue<Card> player2DiscardedCards = new LinkedList<>();
        boolean exAequo = false;

        while(!player1Cards.isEmpty() && !player2Cards.isEmpty()) {
            gameResult.rounds++;
            Card player1Card = player1Cards.poll();
            Card player2Card = player2Cards.poll();

            Optional<Integer> roundWinner = getRoundWinner(player1Card, player2Card);
            if(roundWinner.isPresent()) {
                Queue<Card> winnerCards = (roundWinner.get() == 1) ? player1Cards : player2Cards;
                winnerCards.add(player1Card);
                winnerCards.addAll(player1DiscardedCards);
                player1DiscardedCards.clear();
                winnerCards.add(player2Card);
                winnerCards.addAll(player2DiscardedCards);
                player2DiscardedCards.clear();
            }
            else {
                player1DiscardedCards.add(player1Card);
                player2DiscardedCards.add(player2Card);
                if(player1Cards.size() < 3 || player2Cards.size() < 3) {
                    exAequo = true;
                    break;
                }
                for(int i = 0; i < 3; i++) {
                    player1DiscardedCards.add(player1Cards.poll());
                    player2DiscardedCards.add(player2Cards.poll());
                }
            }
        }

        if(player1DiscardedCards.size() > 0 || player2DiscardedCards.size() > 0) {
            exAequo = true;
        }

        if(exAequo) {
            gameResult.winner = -1;
        }
        else {
            gameResult.winner = player1Cards.isEmpty() ? 2 : 1;
        }
        return gameResult;
	}

    private static Optional<Integer> getRoundWinner(Card player1Card, Card player2Card) {
        int roundResult = player1Card.compareTo(player2Card);
        if(roundResult > 0) {
            return Optional.of(1);
        }
        else if(roundResult < 0) {
            return Optional.of(2);
        }
        else {
            return Optional.empty();
        }
    }
}

class GameResult {
    int winner;
    int rounds;

    @Override
    public String toString() {
        return winner + " " + rounds;
    }
}

class Card implements Comparable<Card> {
    private static Map<String, Integer> cardStrengths = Map.ofEntries(
        new SimpleEntry<String, Integer>("A",  13),
        new SimpleEntry<String, Integer>("K",  12),
        new SimpleEntry<String, Integer>("Q",  11),
        new SimpleEntry<String, Integer>("J",  10),
        new SimpleEntry<String, Integer>("10", 9),
        new SimpleEntry<String, Integer>("9",  8),
        new SimpleEntry<String, Integer>("8",  7),        
        new SimpleEntry<String, Integer>("7",  6),
        new SimpleEntry<String, Integer>("6",  5),
        new SimpleEntry<String, Integer>("5",  4),
        new SimpleEntry<String, Integer>("4",  3),
        new SimpleEntry<String, Integer>("3",  2),
        new SimpleEntry<String, Integer>("2",  1)
    );
    
    String value;
    String color;

    Card(String cardAsString) {
        this.value = cardAsString.substring(0, cardAsString.length() - 1);
        this.color = cardAsString.substring(cardAsString.length() - 1);
    }

	@Override
	public int compareTo(Card other) {
		return Integer.compare(
            cardStrengths.get(this.value),
            cardStrengths.get(other.value)
        );
	}
}