# The TXC Format

TXCs have two parts: a specification file and the audio files themselves. Those files are then packaged as a ZIP file (all contents should be in the top-level directory) and given the file extension ".txc".

## Specification File

The specification file should be called "spec.xml", and the top-level element should be a "deck" element.

### deck Element

A deck element has:
- a "source-language" attribute specifying the name of the source language for the deck
- exactly one "meta" element
- any number of "language" elements

### meta Element

A meta element has:
- a "label" element, containing the name of the deck
- a "publisher" element, containing the name of the organization or person who produced it
- optionally, an "id" element, containing a string that uniquely identifies the deck
- optionally, a "timestamp" element, containing the timestamp for the time the deck was created
- optionally, a "locked" element, containing "true" or "false", depending on whether the deck should be locked (defaults to false)

### language Elements

Each language element has:
- an attribute "name" with the name of the language
- any number of "translation" elements.

### translation Elements

Each translation element has:
- a "label" element, containing the label
- a "filename" element, containing the name of the audio file
- optionally, a "translation-text" element, containing text in the target language to display along with the audio

## Prior Formats

Previously, information about decks was specified in pipe-delimited text files, with a line for each card (and, optionally, all the meta information in a line at the top beginning with "META:").

