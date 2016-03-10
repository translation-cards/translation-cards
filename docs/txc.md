# The TXC Format

TXCs have two parts: a specification file and the audio files themselves. Those files are then packaged as a ZIP file (all contents should be in the top-level directory) and given the file extension ".txc".

## Specification File

The specification file should be called "spec.json", with a "deck" object at the top level. The spec.json file should be UTF-8.

### deck Objects

A deck object has:
- a "source_language" field specifying the name of the source language for the deck
- a "label" field, specifying the name of the deck
- a "publisher" field, specifying the name of the organization or person who produced it
- optionally, an "id" field, specifying a string that uniquely identifies the deck
- optionally, a "timestamp" field, specifying the timestamp for the time the deck was created
- optionally, a "locked" field, set to either "true" or "false", depending on whether the deck should be locked (defaults to false)
- a "languages" field with an array of objects, each containing information about the cards for one target language, as described below

### language Objects

Each language object has:
- a "name" field with the name of the language
- a "translations" field with an array of objects, each containing information about one card, as described below

### translation Objects

Each translation object has:
- a "label" field, specifying the label
- a "filename" field, specifying the name of the audio file
- optionally, a "translation_text" field, containing text in the target language to display along with the audio

## Example spec.json File Contents

{
  "source_language": "English",
  "label": "Greeting Deck",
  "publisher": "Fred",
  "languages": [
    {
      "name": "Spanish",
      "translations": [
        {
          "label": "Hello",
          "filename": "es_hello.mp3",
          "translation_text": "Hola."
        }
      ]
    },
    {
      "name": "Robot",
      "translations": [
        {
          "label": "Hello",
          "filename": "ro_hello.mp3",
          "translation_text": "Beep boop beep."
        },
      ]
    }
  ]
}

## Prior Formats

Previously, information about decks was specified in pipe-delimited text files, with a line for each card (and, optionally, all the meta information in a line at the top beginning with "META:").

