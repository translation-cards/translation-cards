Want to contribute? Great! First, read this page (including the small print at the end).

### Before you contribute
Before we can use your code, you must sign the
[Google Individual Contributor License Agreement](https://cla.developers.google.com/about/google-individual)
(CLA), which you can do online. The CLA is necessary mainly because you own the
copyright to your changes, even after your contribution becomes part of our
codebase, so we need your permission to use and distribute your code. We also
need to be sure of various other things—for instance that you'll tell us if you
know that your code infringes on other people's patents. You don't have to sign
the CLA until after you've submitted your code for review and a member has
approved it, but you must do it before we can put your code into our codebase.
Before you start working on a larger contribution, you should get in touch with
us first through the issue tracker with your idea so that we can help out and
possibly guide you. Coordinating up front makes it much easier to avoid
frustration later on.

### Set up your development environment
1. Install [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html) (7 or 8)
2. Install [Git](https://git-scm.com/downloads)
3. Fork and clone the repo.
  - If you have been added to the repo as a collaborator, simply clone it.
4. Install [Android Studio](https://developer.android.com/studio/index.html)
5. Import the project into Android Studio
  - From Android Studio, select “Import Project (Eclipse, ADT, Gradle, etc.)”
  - Select the folder you cloned the project into, which is “translation-cards” by default
6. Run the app
  - From Android Studio, select Run > Run
  - Select “app”
  - Create New Emulator
  - Choose a device. The Nexus 5 works well.
  - Choose a system image. Marshmallow is recommended.
  - Name the AVD and Finish.
  - Select the newly created emulator and click Ok.
  - If the run process fails, check the 'Messages' section in the lower tray of the IDE (Or by pressing Ctrl-0 or Cmd-0). You may need to download missing packages.
    - Click the 'Gradle' icon in the top right of the window. Click the refresh button (the leftmost icon).
    - A message will appear prompting you to download missing dependencies.
7. Install the [Zenhub plugin](https://www.zenhub.io/) to your browser
8. Navigate to the Boards tab of the Github repo
  - This is the Storyboard for the project. 
  The Storyboard helps track upcoming features that need to be implemented. 
  Read more about storyboards [here](http://whatis.techtarget.com/definition/storyboard).
9. Find the latest work. We use milestones to split apart the cycles of work (also called iterations in Agile)
Choose the latest milestone once in the Boards tab by clicking the Milestones dropdown. The higher the story is on the board the more important it is. The stories also have labels such as ‘UX’, ‘Bug’, or ‘Feature’ which indicate what category of work needs to be done for that story. 


### Pull Requests
* When creating a pull request, be sure to set:
  * The base fork to `translation-cards/translation-cards`
  * The base to `develop`
* Pull requests into the master branch will not be accepted. 
* Please include a descriptive title and a helpful comment.

### Code reviews
All submissions, including submissions by project members, require review. We
use Github pull requests for this purpose.

### Google Drive
All documents, spreadsheets, presentations, and UX work related to this project is saved on the Translation Cards [drive] (https://drive.google.com/open?id=0Bx3lspqM75QyeVRheDJ0cWZlMkU)

### Communication via Slack
For all project updates, questions, concerns and communication we use [slack] (https://translation-cards.slack.com/?redir=%2Fadmin%2Finvites)

### The small print
Contributions made by corporations are covered by a different agreement than
the one above, the
[Software Grant and Corporate Contributor License Agreement](https://cla.developers.google.com/about/google-corporate).
