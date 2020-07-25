package com.marcuzzo.JDABot;

import javax.security.auth.login.LoginException;
import com.apollographql.apollo.api.Response;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	public void onGuildMessageRecieved(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);
		
		if (event.getMessage().getContentRaw().equalsIgnoreCase("!test")) {
			event.getChannel().sendMessage("Tested!").queue();
			
		}
	}
}

