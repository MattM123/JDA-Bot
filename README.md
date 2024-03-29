# Overview
* User application information serialised into multiple class files
* Role based execution control using Discord's built-in user roles
* Keeps track of users and allows them to assign their own permissions without having to rely on staff
* Leverages [mattmalecs' Pterodactyl Wrapper](https://github.com/mattmalec/Pterodactyl4J "Ptero link"), the [BTE Build Team API](https://github.com/BuildTheEarth/build-team-api "BTEAPI link"), and [JDA](https://github.com/DV8FromTheWorld/JDA "JDA link"), a java wrapper for the Discord API 

#### Overview of the more complex features ####
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
	* If a users application list is empty, a message is returned in the channel the command was executed instead of an application
	* Does not return pending applications
	
* Postgres database
	* Based on reactions in the proper submissions channel, will alter database values
	* Retrieves database records every 60 seconds and places them in auto-paginagting embed message
	* Backlog channel provides uptime even when a database connection cannot be established
		* On message react, if database connection cannot be established, the user ID of the message will be sent in the backlog channel
		* Once connection is regained, `=merge` will iterate through each backlog message. For each user ID held in the database, it will increment the corresponding count value and delete the backlog message once complete.

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
| `=add <discordID>` | Manually increments a users database record. | true|
| `=remove <discordID>` | Manually decrements a users database record. | true|
| `=merge` | Merges the backlog into the database after a connection outage. | true|

[Discord server](discord.gg/a3GEGEf "DC link")

Note: Given the niche use case of this bot, it was not designed to be used with other discord servers or even other BTE discord servers. If you have any questions about it you can reach me in the above discord server.