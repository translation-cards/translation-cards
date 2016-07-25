# Development Guide

We encourage Agile practices on this project. We track our work by creating User Stories (also called ‘issues’ in GitHub) and placing them on the ZenHub Storyboard in the appropriate place. It may seem like extra work to update the Storyboard, but it’s a great way to share information and coordinate with the team in a collaborative atmosphere. Below is a walk-through of our process, starting from analysis of work all the way through quality assurance.

You can participate in any part of the process. Jump right into wherever you would like, just be aware of where you are and who you will be passing your work to next.

## User Stories
Typically, User Stories start out as simple ideas such as “The deck creation process should have a summary screen”. During Analysis, we turn these ideas into specific requirements which let us know when a feature is completed. When writing a story it’s important to consider this feature’s interactions with other features, previous and upcoming screens, and older versions of the application. There can be a lot to think about!

The story cards resulting from analysis are used by developers to create the features, and by quality assurance to validate the feature. Having a well thought out story can make life a lot easier for people down the line.

## Business Analyst/XD
1. Move the Issue card on the ZenHub board.
  1. Take a card you’d like to work on from the ‘Backlog’ pipeline and move it to the ‘In Analysis’ pipeline.
1. Update the Issue.
  1. Add a description of the feature and why it is needed.
  1. Add specific Acceptance Criteria using the form:
    1. Given [context], When [action], Then [expected outcome] for example:
      ```
      Given I am creating a deck
    
      When I click next after adding a title
    
      Then I am directed to enter a deck source language
      ```
    1. If you require more context on a card, don’t hesitate to ask an organizer
1. Move the Issue (again!)
  1. Take the completed card and put it in the Ready for Dev pipeline

## Developer
1. Find a pair
  1. If you can, work with another developer. You will likely write better code and finish faster. It’s also a great way to make friends!
2. Choose a story
  1. Stories are ranked in order of priority. Try to pick a story at or near the top of the Ready for Dev pipeline.
  1. Make a comment on the story card to indicate who is working on it.
  1. Move the card from ‘Ready for Dev’ to ‘In Dev’
3. Create a branch
  1. In your forked project, create a branch for the feature. Name it descriptively, for example deck-creation-summary-screen
4. Complete the story
  1. We try to practice Test Driven Development. Before implementing any code, first go to a class’ respective test class (or create one if it doesn’t yet exist) and write a test that will fail until we implement the first simplest step to developing the feature. When we see the test fail, it’s time to write code to make it pass. Once we have a passing test always make sure to refactor the code you have written. Red - Green - Refactor.
  1. The tests also serve as a form of documentation. If you’re not sure what a class is supposed to do, read the titles of its tests to get a gist of its behavior.
  1. Run all the tests before you push, whether you added new ones or not. If they don’t pass, try to make them pass without modifying existing tests. They serve as regression indicators, and changing them to suit your new feature should be done sparingly.
5. Take Breaks
  1. You’d be surprised how much progress you can make by taking it easy for five minutes and coming back to your code with a fresh mind. Still can’t solve it? This is a collaborative atmosphere, feel free to ask someone on the slack channel, or ask an organizer.
6. Pull request
  1. When you’re done with the feature and all the tests are passing, push your code to your forked project.
  2. View your new branch on the Github site, and create a New Pull Request.
    1. Set the base fork to translation-cards/translation-cards
    1. Set the base to develop 
      * Pull requests into the master branch will not be accepted. 
  3. Before making a pull request, make sure you grab the latest changes from the upstream repo. Take a look at this document out to see how you can do this: [Syncing a Fork](https://help.github.com/articles/syncing-a-fork/)
  4. Title your Pull Request and add a description
  5. Click “Create Pull Request” button
7. Move the Issue Card
  1. Take the card from the ‘In Dev’ pipeline and place it in the ‘Ready for QA’ pipeline

## Quality Analyst
1. Choose a story
  1. Find a higher priority issue card in the ‘Ready for QA’ column and move it to ‘In QA’
2. Pull the code
  
  You can do this one of two ways:
  1. Have the developers who worked on the story load the application from their computer onto a physical android device for you to test.
  
  OR, using Git

  1. Find the pull request for that story. Let’s assume the pull request is “Deck Creation Summary Screen #151”
  2. Navigate to your project directory and use the following git commands to get the pull request’s code onto your machine
    1. git fetch origin pull/151/head:deck-creation-summary-screen
    2. git checkout deck-creation-summary-screen
3. Run the application
  1. Use your emulator or an Android device
4. Check the Acceptance Criteria
  1. Make sure the requirements listed in the card are satisfied
5. Check for edge cases
  1. Check interactions with other screens/features, and for specific cases tha wree not touched on in the card
6. Send it back!
  1. If something is wrong, get in touch with the people who worked on it, and share your findings with them.
  2. If necessary, move the issue card back to In Dev.
7. Merge the Pull Request
  1. Once everything looks good, and you’re sure the pull request is trying to merge into translation-cards/develop-hackathon, merge the pull request.
  2. DO NOT merge pull requests into translation-cards/master
