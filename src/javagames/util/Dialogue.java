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
    public String dialogLevel1[] = new String[23];
    private int dialogIndex = 0;
    
    public Dialogue()
    {
    	dialogLevel1[0] = "Narrator: \"Hello Keith\"\n";
    	dialogLevel1[1] = "Keith: \"My name is not Keith.\"\n";
    	dialogLevel1[2] = "Narrotor: \"Keith use the A and D arrows to move\"\n";
    	dialogLevel1[3] = "Keith: \"I know how to move. This is not new knowledge to me.\"\n";
    	dialogLevel1[4] = "Narrator: \"Use the K key to use your magical knives of magic\nand use the J key to jump.\"\n";
    	dialogLevel1[5] = "Keith: \"Alright, what other moves can I do with my knives?\"\n";
    	dialogLevel1[6] = "Narrator: \"None that is all\"\n";
    	dialogLevel1[7] = "Keith: \"This is rather stran-\"\n";
    	dialogLevel1[8] = "Narrator: \"Keith take this banana\"\n";
    	dialogLevel1[9] = "Keith: \"Why?\"\n";
    	dialogLevel1[10] = "Narrator: \"Keith take the damn banana\"\n";
    	dialogLevel1[11] = "Keith: \"My name is not Keith, what am I going to do with this?\"\n";
    	dialogLevel1[12] = "Narrator: \"When the time comes you will know, now let me give you the backstory about your mighty quest. Once long ago..\"\n";
    	dialogLevel1[13] = "Keith: \"Oh no, no long backstory what do you want me to do?\nAlso why is the text so primitive and sloppily put together?\"\n";
    	dialogLevel1[14] = "Narraotr: \"It is not poilte to interrupt, and becuase \nthere was not enough time to put a proper tutorial level together.\"\n";
    	dialogLevel1[15] = "Keith: \"Huh, that seems pretty lazy..\"\n";
    	dialogLevel1[16] = "Narrator: \"Any way as I was saying, \nand pay attention this is crucial to the story.\"\n";
    	dialogLevel1[17] = "Keith: \"I am not going to read this, just tell me what you want me to do.\"\n";
    	dialogLevel1[18] = "Narrator: \"Well you need to save Sir Prince Rihanna Beyonce-\"\n";
    	dialogLevel1[19] = "Keith: \"Alright save the prince got it.\"\n";
    	dialogLevel1[20] = "Narrator: \"Wait are you sure you do not want to hear the story?\"\n";
    	dialogLevel1[21] = "Keith: \"Nope see ya!\"\n";
    	dialogLevel1[22] = "Narrator: \"I worked really hard on this story, there was a whole lecture on the history of everything and-\"";
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
    
}
