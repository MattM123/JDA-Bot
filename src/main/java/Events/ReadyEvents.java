package Events;

import com.marcuzzo.JDABot.Bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyEvents extends ListenerAdapter {
	
	private Guild guild = Bot.jda.getGuildById(735990134583066679L);
	
	public void onReadyEvent(ReadyEvent event) {
		Role improve = guild.getRoleById(1006335736695500801L);
		
		//Checks if a rejected user can re-apply, removes role if the time requirement has been met
		if (Events.RoleEvents.usersDenied.values().contains(System.currentTimeMillis() + "")) {
			if (guild.getMemberById(Events.RoleEvents.usersDenied.getKey("" + System.currentTimeMillis())).getRoles().contains(improve))
				guild.removeRoleFromMember(Events.RoleEvents.usersDenied.getKey("" + System.currentTimeMillis()), improve).queue();
		}
	}
}
