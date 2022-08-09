package Events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyEvents extends ListenerAdapter {
	
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	
	public void onReadyEvent(ReadyEvent event) {
		Role improve = guild.getRoleById(1006335736695500801L);
		File file = new File("/JDABot/src/main/java/commands/RejectedUsers.txt");
		StringBuilder newContents = new StringBuilder();
	/*	
		//Checks if a rejected user can re-apply, removes role if the time requirement has been met
		try {
			Scanner scan = new Scanner(file);
			String line;
			while (scan.hasNextLine()) {
				line = scan.nextLine();
				
				if (line.contains(System.currentTimeMillis() + "")) {
					guild.removeRoleFromMember(line.split(":")[0], improve).queue();
					continue;
				}
				newContents.append(line);
			}
			
			//Delete File Content
			PrintWriter pw = new PrintWriter(file);
			pw.close();
	
			//Write new contents to file
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			writer.append(newContents.toString());
			writer.close();		
			
			scan.close();	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
}
