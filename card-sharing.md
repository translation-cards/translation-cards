#### Sharing card decks

**Overview**: 

As of Translation Cards 0.2.0, users can now share card decks across Android devices with the Translation Cards app installed. This document summarizes that process.

**Card Sharing Process**

A zipfile (with .txc extension) is created, which includes all audio and card text. This .txc file can be opened by another phone with the Translation Cards app. Those phrases and audio in the .txc file will then be added to a user’s existing Translation Cards deck. Specific steps are as follows:

1. User records audio files corresponding to card phrases 
2. User creates a CSV file, titled card_deck.csv with the card text, and name of associated audio file 
3. User creates a zip file which contains all audio files, and card_deck.csv 
4. User gives zip file a descriptive name, changes extension to .txc 
5. Users shares zipfile (in .txc format) with users, open that file on their phone *adds* those cards and audio to existing card deck 

**Implementation details**  

- audio
  - recorded in an [Android-compatible format](http://developer.android.com/guide/appendix/media-formats.html) 
  - filenames to reflect card titles 
  - append language code to file name 
  - no spaces in filenames 
  - Example: 
      - any-dietary-restrictions-ar.m4a 

- card_deck.csv
  - Pipe-delimited
  - Card text | name of audio file | language name
  - Example:
      - how many in your family|family-ar.m4a|Arabic

- zipfile 
    - contains all audio and card_deck.csv file 
    - renamed to include .txc extension 
    - Example: 
        - food_distribution.txc 
            - card_deck.csv 
            - any-dietary-restrictions-ar.mp4
