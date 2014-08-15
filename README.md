Sendere
=======

Did you ever want to have the game send actions to a custom URL for processing into a website? Sendere lets you do that. Simply install Sendere, and set up a listener. 

EDIT 8/14: I just sent version 1.1.0 in for approval. This document has been edited to cover that version, and may not apply to earlier versions.
## What can Sendere send?
As of version 1.1.0, Sendere can send:
* Weather changes
* Players chatting
* Players going 6 feet under/pushing up daisies/resting in peace/any other way to say "Die" without confusion with changing things' color
* Players joining
* Players quitting
* Players being kicked
* Player list
* Pings
=== How do I set up a listener? ===
A listener is at a URL, and **must** support GET.
Sendere sends to listeners in this format: *http:/ /myamazingwebsite.com/myevenmoreamazinglistener.php***?a=Action&b=Info&c=Name**

a: The actual action itself: AsyncPlayerChat, PlayerJoin, PlayerQuit, PlayerKick, PlayerDeath, WeatherChange, Ping.

b: What happened in this action. For AsyncPlayerChat, it would be the content of the message. For PlayerKick, it would be the reason. For WeatherChange, it would be the new state, which would be either "Sun" or "Rain". For Ping, it would be the list of players, if enabled.
**//All spaces in b are replaced by "<[;:]>" so that it doesn't cut off the content of the message.//**

c: The name of the player or world. For AsyncPlayerChat, PlayerJoin, PlayerQuit, PlayerKick, and PlayerDeath, it would be the username of the player. For WeatherChange, it would be the world.

Here is an example of a PHP listener that simply outputs everything to a log file:
```php
<?php
if( @$_GET[ "a" ] ){ //Determines if it's a server sending data, or just a web crawler.
        if( @$_GET[ "b" ] )
                $b = str_replace( "<[;:]>", " ", @$_GET[ "b" ] ); //Turns "<[;:]>" back into spaces.
        
        file_put_contents( "serverActions.txt", "\n" . "[a]" . $_GET[ "a" ] . ":::[b]" . @$b . ":::[c]" . @$_GET[ "c" ], FILE_APPEND ); //Outputs everything to a log file.
}
?>
```

## Why do I need this?
This helps to bring the experience of the game to your server's website.
With knowledge of Internet scripting, you could use it to:
* Show online players
* Display current server weather
* Show what everyone on the server is talking about
* Tally peoples' deaths
* Show if the server is running or closed
* Inflict punishment on your Forum for being kicked from the game
* Send e-mail to server staff when a player is kicked
* And so much more!

Some selected items from that list have had [tutorials](http://dev.bukkit.org/bukkit-plugins/sendere/pages/tutorials/) made!

=== Other Builds ===
You can find Beta and Nightly builds [here](http://www.jamescl.com/Sendere/). These may be extremely buggy, and HAVE NOT been approved by BukkitDev Staff. Use them at your own risk.
=== Example Config ===
```yaml
#Sets the URL to send the information to. For help with how to set up a listener, and an example one, go to http://dev.bukkit.org/bukkit-plugins/sendere/.
URL: "http://www.minwrit.me/hlysnere.php"

#Do you want to log whenever a response is given from the listener?
Log-Response: false
    
#Send players' actions
Chat: true
Join: true
Quit: true
Kick: true
Death: true
    
#Send weather changes
Weather: true

#Send ping to server
Ping: true
#Pinging interval, in seconds
Interval: 30
#Send playerlist with each Ping
Playerlist: true
```
