package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.data.Deck;

import java.io.Serializable;

/**
 * Created by njimenez on 4/12/16.
 */
public class NewDeckContext implements Serializable{
    private Deck deck;

    public NewDeckContext(){
        this.deck = new Deck();
    }

    public NewDeckContext(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck(){
        return deck;
    }
}
