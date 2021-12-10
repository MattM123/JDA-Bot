# Overview
* User application information serialised into multiple class files
* Role based execution control using Discord's built-in user roles
* Keeps track of users and allows them to assign their own permissions without having to rely on staff
* Leverages [mattmalecs' Pterodactyl Wrapper](https://github.com/mattmalec/Pterodactyl4J "Ptero link"), the [BTE Build Team API](https://github.com/BuildTheEarth/build-team-api "BTEAPI link"), and [JDA](https://github.com/DV8FromTheWorld/JDA "JDA link"), a java wrapper for the Discord API 

#### Overview of the more complex commands ####
* `=link <mcusername>`
	* Validates `<mcusername>` argument by referencing user application submitted to BuildTheEarth.net
	* If a users application cannot be referenced due to error or non-existance and the user is on the team, the user is considered merged and is added to the team and permissions are assigned
	* If a users application can be referenced and the user is not on the team, the user is added to the team and permissions are assigned
	* All other cases result in failure to add user to the team and assign permissions
	* Does not reference pending applications

* `=getapp -<discordUserID> -<n>`
	* Retrieves `n` application submitted by a given `discordUserID`
	* `n` > 0
	* If a user is identified to have been merged into the team, a message is returned in the channel the command was executed instead of an application
	* If a users application list is empty, a message is returned in the channel the command was executed instad of an application
	* Does not return pending applications
	

# Commands
| Command       | Description   | Staff command |
|:--------------|:-------------:|:-------------:|
| `=ping`         | Pings Discord API| false|
| `=role`       | Provides list of self-assignable roles and command usage| false|
| `=measure`      | Shows GEP tutorial on measuring vertical distances| false|
| `=server`		| Shows server status and resource usage| false|
| `=map`			| Shows map of states included in BTE Midwest| false|
| `=applicant`	| Gives user permissions on the server for the applicant building area| false|
| `=link <mcusername>`| If user has been accepted to the team, assigns builder rank with full build permissions on Minecraft server based on state discord role of user and gives Builder discord role, | false|
| `=getapp -<discordUserID> -<n>`    | Retrieves a specific application for a given `DiscordUserID`.| true|
| `=/<command>`  | Sends console command to the Midwest server| true|

# Using the bot code
This bot was not made with the intent for other build teams to use but if you can read my source code and use Pterodactyl 
it is doable. If you self host or use a hosting service with Pterodactyl you will be able to host it alongside your Minecraft server.
If there's enough interest in using it then I may make notes on what needs to be changed within the code and other steps to use it yourself.

For any questions regarding the bot's code, you can reach me in our [Discord server](discord.gg/a3GEGEf "DC link")