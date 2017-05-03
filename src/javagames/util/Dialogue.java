/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javagames.util;

/**
 *
 * @author Kristopher
 */
public class Dialogue
{
    public String dialogLevel1[] = new String[24];
    public String dialogLevel2[] = new String[4];
    public String dialogLevel3a[] = new String[4];
    public String dialogLevel3b[] = new String[8];
    private int dialogIndex = 0;
    
    public Dialogue()
    {
    	dialogLevel1[0] = "Narrator: \"Hello Keith\"\n";
    	dialogLevel1[1] = "Keith: \"My name is not Keith. It's Annabe-\"\n";
    	dialogLevel1[2] = "Narrotor: \"Keith use the A and D arrows to move.\"\n";
    	dialogLevel1[3] = "Keith: \"I know how to move. This is not new knowledge to me.\"\n";
    	dialogLevel1[4] = "Narrator: \"Use the K key to use your magical knives of magic\n and use the J key to jump.\"\n";
    	dialogLevel1[5] = "Keith: \"Alright, what other moves can I do with my knives?\"\n";
    	dialogLevel1[6] = "Narrator: \"None that is all\"\n";
    	dialogLevel1[7] = "Keith: \"This is rather stran-\"\n";
    	dialogLevel1[8] = "Narrator: \"Keith take this banana\"\n";
    	dialogLevel1[9] = "Keith: \"Why?\"\n";
    	dialogLevel1[10] = "Narrator: \"Keith take the damn banana\"\n";
    	dialogLevel1[11] = "Keith: \"My name is not Keith, what am I going to do with this?\"\n";
    	dialogLevel1[12] = "Narrator: \"When the time comes you will know, now let me give you the backstory about your mighty quest. Once long ago..\"\n";
    	dialogLevel1[13] = "Keith: \"Oh no, no long backstory what do you want me to do?";
    	dialogLevel1[14] = "Also why is the text so primitive and sloppily put together?\"\n";
    	dialogLevel1[15] = "Narrator: \"It is not poilte to interrupt, and becuase there was not enough time to put a proper tutorial level together.\"\n";
    	dialogLevel1[16] = "Keith: \"Huh, that seems pretty lazy..\"\n";
    	dialogLevel1[17] = "Narrator: \"Any way as I was saying, \nand pay attention this is crucial to the story.\"\n";
    	dialogLevel1[18] = "Keith: \"I am not going to read this, just tell me what you want me to do.\"\n";
    	dialogLevel1[19] = "Narrator: \"Well you need to save Sir Prince Rihanna Beyonce-\"\n";
    	dialogLevel1[20] = "Keith: \"Alright save the prince got it.\"\n";
    	dialogLevel1[21] = "Narrator: \"Wait are you sure you do not want to hear the story?\"\n";
    	dialogLevel1[22] = "Keith: \"Nope see ya!\"\n";
    	dialogLevel1[23] = "Narrator: \"I worked really hard on this story, there was a whole lecture on the history of everything and-\"";
    	
    	dialogLevel2[0] = "Narrator: \"Alright Keith the castle is straight to the right!\"";
    	dialogLevel2[1] = "Keith: \"No really? I thought it was in the background.\n";
    	dialogLevel2[2] = "           I can only move left and right, this world is surprisingly 2 dimensional.\"";
    	dialogLevel2[3] = "Narrator: \"Fine, you know what, I wont say anything else throughout this level.\"";
    	
    	dialogLevel3a[0] = "Narrator: \"You are in the castle Keith, the prince is here somewhere.\"";
    	dialogLevel3a[1] = "Keith: \"I don't need you to explain everything.\"";
    	dialogLevel3a[2] = "Narrator: \"I am just trying to-\"";
    	dialogLevel3a[3] = "Keith: \"I don't care leave me alone.\"";

    	dialogLevel3b[0] = "Keith: \"Hello?\nIs anyone here?\"";
    	dialogLevel3b[1] = "Narrator: \"YOUR PRINCE IS IN ANOTHER CASTLE! Keith, you must go-\"";
    	dialogLevel3b[2] = "Keith: \"ARE YOU KIDDING ME?! HOW LONG DID YOU KNOW THIS VOICE?!\"";
    	dialogLevel3b[3] = "Narrator: \"Since level 2 I was going to tell you, but decided not too. On the bright side you can now do something rather similar";
    	dialogLevel3b[4] = "           in the next castle!\"";
    	dialogLevel3b[5] = "Keith: \"You know what, this is ridiculous.\n I am done I am going home.\"";
    	dialogLevel3b[6] = "Narrator: \"But Keith, you must go-\"";
    	dialogLevel3b[7] = "Keith: \"My name is not Keith, it's Anna!\n The prince is useless and so are you disembodied voice!\"";
    }
    
    public String LevelOneDialogue()
    {
    	if(dialogIndex < dialogLevel1.length)
    	{
    		dialogIndex++;
    		return dialogLevel1[dialogIndex-1];
    	}
    	else
    	{
    		dialogIndex = 0;
    		return null;
    	}
    }
    
    public String LevelTwoDialogue()
    {
    	if(dialogIndex < dialogLevel2.length)
    	{
    		dialogIndex++;
    		return dialogLevel2[dialogIndex-1];
    	}
    	else
    	{
    		dialogIndex = 0;
    		return null;
    	}
    }
    
    public String LevelThreeADialogue()
    {
    	if(dialogIndex < dialogLevel3a.length)
    	{
    		dialogIndex++;
    		return dialogLevel3a[dialogIndex-1];
    	}
    	else
    	{
    		dialogIndex = 0;
    		return null;
    	}
    }
    
    public String LevelThreeBDialogue()
    {
    	if(dialogIndex < dialogLevel3b.length)
    	{
    		dialogIndex++;
    		return dialogLevel3b[dialogIndex-1];
    	}
    	else
    	{
    		dialogIndex = 0;
    		return null;
    	}
    }
    
}
