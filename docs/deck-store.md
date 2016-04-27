# **Deck Store**

### Version 1
The simplest form of a deck store could be created as a static hosted webpage as seen on [Jeff's GitHub page](http://jwishnie.github.io/txc/). In the app, a user could click a menu option to download more decks. This would take them to our "Deck Store" webpage. Here the user can click on which deck they want to download.

### Version 2
A second version to this idea is allowing deck owners to update their own decks on the store at will. To do this we would need to set up a Deck Store repository. The deck owner would own a branch in this repository, and that branch would be connected with their deck in the Deck Store. To update their deck, the deck owner would push their changes to their branch. This would trigger a build to happen in our SnapCI pipeline. The build will:

 1. Bundle the audio and deck specification file into a zipped TXC
 2. Publish the TXC to a unique artifact repository (Amazon S3 bucket) that hosts their TXC file
 3. The static Deck Store webpage would point to their hosted deck, and users will be able to update whenever they please.

 This is beneficial to us, because we will not have to manually version every deck that is hosted in our deck store. When the SnapCI build for an owners deck is successful it will publish the deck changes for us. This however will not notify users that decks have been updated. One possible solution to this is putting timestamps next to the decks in the deck store.

### Future versions
 Future versions can include the following modifications:
 - Create a new artifact repository for the deck based on its hash (if a repository does not already exist)
 - Verify the deck specification file is properly constructed, and let the deck owner know what to fix if it is not.
 - Verify that all audio files listed in the deck spec are included in the repository
 - The deck store will not be updated if all verification checks pass.

 These future changes are more geared toward giving deck owners a faster feedback loop with regards to the validity of the syntax and content of their decks.
