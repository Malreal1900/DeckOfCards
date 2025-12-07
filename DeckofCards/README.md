### Cameron "CJ" Robinson, Brisa Rocha, Malia Curry

### CSCI 3331-001, Juliette Garcia 


# Checkpoint 3

*This is a program that allows the user to play a game of war against the CPU*

*War is 2 player card game, where the deck is split, and
the players flip the top card of the split deck given to
them.
 In case of a tie, a WAR occurs, with each player placing 3
cards face down, then one face up to determine the
winner of the round
The game continues until one player has ALL the cards*

**App.java:**
Base file with no applicable code.

**Card.java:**
Holds ranks and suits of cards.

**Deck.java**
Has a deck list/constructor, methods for shuffling, drawing, finding the size of the deck, and a boolean to see if the deck is empty.

**DeckController.java:**
Connects the actions of altering the deck code-wise to the physical buttons.

**DeckUI.java**
Constructs the UI for running the program.

**DeckView.java**
Methods to interact with and modify the deck.

**Hand.java**
Methods that allow the user to modify their hand.

**Rank.java**
Sets the possible options for a Card's rank.

**SplitDeckDialog.java**
Sets the dialog prompted to the user for certian actions.

**Suit.java**
Enum for the suits.

**WarController.java**'
Controls the game and keeps everything tidy between players.

**WarGame.java**
The game operations for the war game