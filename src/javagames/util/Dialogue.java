package javagames.util;

/**
 * This class stores the dialogue for the game
 *
 * @author Kristopher
 */
public class Dialogue {
  public String dialogLevel1[]  = new String[24];
  public String dialogLevel2[]  = new String[4];
  public String dialogLevel3a[] = new String[4];
  public String dialogLevel3b[] = new String[8];
  private int   dialogIndex     = 0;

  public Dialogue() {
    dialogLevel1[0] = "Narrator: \"Hello, Keith.\"\n";
    dialogLevel1[1] = "Keith: \"My name is not Keith. It's Annabe-\"\n";
    dialogLevel1[2] = "Narrotor: \"Keith, use the A and D keys to move.\"\n";
    dialogLevel1[3] =
        "Keith: \"I know how to move. This isn't new knowledge to me.\"\n";
    dialogLevel1[4] =
        "Narrator: \"Use the K key to use your magical knives of magicness\n and use the J key to jump.\"\n";
    dialogLevel1[5] =
        "Keith: \"Alright, and what other moves can I do with my knives?\"\n";
    dialogLevel1[6] = "Narrator: \"None. That is all.\"\n";
    dialogLevel1[7] = "Keith: \"This is rather stran-\"\n";
    dialogLevel1[8] = "Narrator: \"Keith, take this banana\"\n";
    dialogLevel1[9] = "Keith: \"Why?\"\n";
    dialogLevel1[10] = "Narrator: \"Keith, take the damn banana!\"\n";
    dialogLevel1[11] =
        "Keith: \"Again, my name is not Keith! Now, what am I going to do with this?\"\n";
    dialogLevel1[12] =
        "Narrator: \"When the time comes, you will know. Now let me give you the backstory about your mighty quest. Once long ago...\"\n";
    dialogLevel1[13] =
        "Keith: \"Oh no... No long backstory. What do you want me to do?";
    dialogLevel1[14] =
        "Also why is the text so primitive and sloppily put together?\"\n";
    dialogLevel1[15] =
        "Narrator: \"It's not polite to interrupt. There wasn't enough time to put a proper tutorial level together.\"\n";
    dialogLevel1[16] = "Keith: \"Huh, that seems pretty lazy...\"\n";
    dialogLevel1[17] =
        "Narrator: \"Anyway... Pay attention now, this is crucial to the story.\"\n";
    dialogLevel1[18] =
        "Keith: \"How about no? I'm not going to read this. Just tell me what you want done.\"\n";
    dialogLevel1[19] =
        "Narrator: \"...Well, you need to save Sir Prince Rihanna Beyonce-\"\n";
    dialogLevel1[20] = "Keith: \"Alright, save the prince. Got it.\"\n";
    dialogLevel1[21] =
        "Narrator: \"Wait! Are you sure you don't want to hear the story?\"\n";
    dialogLevel1[22] = "Keith: \"Yep! See ya!\"\n";
    dialogLevel1[23] =
        "Narrator: \"But I worked really hard on this story! there's a whole lecture on the history of everything and-\"";

    dialogLevel2[0] =
        "Narrator: \"Alright Keith, the castle is straight to the right!\"";
    dialogLevel2[1] =
        "Keith: \"OH REALLY!? I thought it was in the background.\n";
    dialogLevel2[2] =
        "           I can only move left and right, this world is surprisingly 2 dimensional.\"";
    dialogLevel2[3] =
        "Narrator: \"You know what? Fine. I wont say anything else throughout this level.\"";

    dialogLevel3a[0] =
        "Narrator: \"You're in the castle Keith, the prince is here somewhere.\"";
    dialogLevel3a[1] = "Keith: \"I don't need you to explain everything.\"";
    dialogLevel3a[2] = "Narrator: \"I am just trying to-\"";
    dialogLevel3a[3] = "Keith: \"I don't care! Leave me alone.\"";

    dialogLevel3b[0] = "Keith: \"Hello?\nIs anyone here?\"";
    dialogLevel3b[1] =
        "Narrator: \"YOUR PRINCE IS IN ANOTHER CASTLE! Keith, you must go-\"";
    dialogLevel3b[2] =
        "Keith: \"ARE YOU KIDDING ME?! HOW LONG DID YOU KNOW THIS VOICE?!\"";
    dialogLevel3b[3] =
        "Narrator: \"Since level 2. I was going to tell you, but decided not too. On the bright side you can now do something rather similar";
    dialogLevel3b[4] = "           in the next castle!\"";
    dialogLevel3b[5] =
        "Keith: \"You know what, this is stupid.\n I'm DONE. Going home now.\"";
    dialogLevel3b[6] = "Narrator: \"But Keith, you must go-\"";
    dialogLevel3b[7] =
        "Keith: \"For the LAST. TIME. It's ANNA!\n The prince is useless and so are you, disembodied voice!\"";
  }

  public String LevelOneDialogue() {
    if (dialogIndex < dialogLevel1.length) {
      dialogIndex++;
      return dialogLevel1[dialogIndex - 1];
    }
    else {
      dialogIndex = 0;
      return null;
    }
  }

  public String LevelTwoDialogue() {
    if (dialogIndex < dialogLevel2.length) {
      dialogIndex++;
      return dialogLevel2[dialogIndex - 1];
    }
    else {
      dialogIndex = 0;
      return null;
    }
  }

  public String LevelThreeADialogue() {
    if (dialogIndex < dialogLevel3a.length) {
      dialogIndex++;
      return dialogLevel3a[dialogIndex - 1];
    }
    else {
      dialogIndex = 0;
      return null;
    }
  }

  public String LevelThreeBDialogue() {
    if (dialogIndex < dialogLevel3b.length) {
      dialogIndex++;
      return dialogLevel3b[dialogIndex - 1];
    }
    else {
      dialogIndex = 0;
      return null;
    }
  }

}
