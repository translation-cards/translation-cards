# The TXC Format

TXCs have two parts: a specification file and the audio files themselves. Those files are then packaged as a ZIP file (all contents should be in the top-level directory) and given the file extension ".txc".

## Specification File

The specification file should be called "card_deck.json", with a "deck" object at the top level. The card_deck.json file should be UTF-8 encoded.

### deck Objects

A deck object has:
- a "source_language" field, specifying the ISO code of the source language for the deck
- a "deck_label" field, specifying the name of the deck
- a "author" field, specifying the name of the organization or person who produced it
- optionally, a "source_language_name" field, specifying the particular language a deck author would like to be displayed when a two letter iso code maps to multiple language names.
- optionally, an "id" field, specifying a string that uniquely identifies the deck
- optionally, a "timestamp" field, specifying the time the deck was created. This is in [Unix Time](https://en.wikipedia.org/wiki/Unix_time), then converted to milliseconds.
- optionally, a "locked" field, set to either "true" or "false", depending on whether the deck should be locked (defaults to false)
- optionally, a "license-url" field, pointing to a specific license that the deck is distributed under
- optionally, a "readme" field, describing the contents of the deck
- a "languages" field with an array of objects, each containing information about the cards for one target language, as described below

### language Objects

Each language object has:
- an "iso_code" field, specifying the ISO code of the translated language
- a "cards" field with an array of objects, each containing information about one card, as described below
- optionally, a "dest_language_name" field, specifying the particular language a deck author would like to be displayed when a two letter iso code maps to multiple language names.

### translation Objects

Each translation object has:
- a "card_label" field, specifying the phrase of the translation
- a "dest_audio" field, specifying the name of the audio file
- optionally, a "dest_txt" field, specifying the translated card_label phrase

## Example card_deck.json File Contents

[Humanitarian Responder - Transit Centre](https://github.com/translation-cards/default-deck/blob/master/txc/card_deck.json)

## Prior Formats

Previously, information about decks was specified in pipe-delimited text files, with a line for each card (and, optionally, all the meta information in a line at the top beginning with "META:").

