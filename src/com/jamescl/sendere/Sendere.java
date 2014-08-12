/*
 * The MIT License
 *
 * Copyright 2014 James Larsson.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jamescl.sendere;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author James Larsson
 */
public class Sendere extends JavaPlugin{
    public static Sendere instance;
    public final static Logger log = Logger.getLogger( "Minecraft" );
    public final String Version    = "1.1.0";
    public String url              = "http://www.minwrit.me/hlysnere.php";
    public Boolean logResponse     = false;
    public Boolean chat            = true;
    public Boolean join            = true;
    public Boolean quit            = true;
    public Boolean kick            = true;
    public Boolean death           = true;
    public Boolean weather         = true;
    public Boolean playerList      = true;
    public Boolean pingYes         = true;
    
    @Override
    public void onEnable(){
        Sendere.instance = this;
        
        this.saveDefaultConfig();
        this.url = this.getConfig().getString("URL" );
        this.logResponse = this.getConfig().getBoolean( "Log-Response" );
        this.chat = this.getConfig().getBoolean( "Chat" );
        this.join = this.getConfig().getBoolean( "Join" );
        this.quit = this.getConfig().getBoolean( "Quit" );
        this.kick = this.getConfig().getBoolean( "Kick" );
        this.death = this.getConfig().getBoolean( "Death" );
        this.weather = this.getConfig().getBoolean( "Weather" );
        this.playerList = this.getConfig().getBoolean( "Playerlist" );
        this.pingYes = this.getConfig().getBoolean( "Ping" );
        
        this.getServer().getPluginManager().registerEvents( new SenderesHlysnere(), this );
        
        try{
            this.publish( "ServerStart", "", "" );
        } catch( IOException ex ){
            Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
        }
        
        String toReport;
        toReport = "";
        
        if( this.chat )
            toReport = toReport + "chatting";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.join )
            toReport = toReport + "joining";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.quit )
            toReport = toReport + "quitting";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.kick )
            toReport = toReport + "kicking";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.death )
            toReport = toReport + "death";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.weather )
            toReport = toReport + "weather changing";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.playerList )
            toReport = toReport + "playerlist";
        
        if( toReport != "" )
            toReport = toReport + ", ";
        
        if( this.pingYes )
            toReport = toReport + "ping";
        
        if( this.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            toReport = "";
        
        if( toReport.equals( "" ) )
            toReport = "naught";
        
        for( Player player: Bukkit.getServer().getOnlinePlayers() ){
            try{
                this.publish( "PlayerJoin", "", player.getName() );
            } catch( IOException ex ){
                Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        
        if( this.pingYes ){
            Long ping = this.getConfig().getLong( "Interval" ) * 20;
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    try{
                        Sendere.instance.publish( "Ping", "", "" );
                    } catch( IOException ex ){
                        Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
                    }
                }
            }, 0L, ping);
        }
        this.log( "Sendere has been enabled. Reporting " + toReport + " to " + this.url, Level.INFO ); 
    }
    
    @Override
    public void onDisable(){
        for( Player player: Bukkit.getServer().getOnlinePlayers() ){
            try {
                this.publish( "PlayerQuit", "", player.getName() );
            } catch( IOException ex ){
                Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        try{
            this.publish( "ServerStop", "", "" );
        } catch ( IOException ex ){
            Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
        }
        this.log( "Sendere has been disabled.", Level.INFO );
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if( cmd.getName().equalsIgnoreCase("sendere") ){
            if( args[0].equals( "reload" ) || args[0].equals( "rl" ) ){
                if ( !(sender instanceof Player) ) {
                    this.log( "Reloading configuration...", Level.INFO );
                    this.reloadConfig();
                    this.url = this.getConfig().getString("URL" );
                    this.logResponse = this.getConfig().getBoolean( "Log-Response" );
                    this.chat = this.getConfig().getBoolean( "Chat" );
                    this.join = this.getConfig().getBoolean( "Join" );
                    this.quit = this.getConfig().getBoolean( "Quit" );
                    this.kick = this.getConfig().getBoolean( "Kick" );
                    this.death = this.getConfig().getBoolean( "Death" );
                    this.weather = this.getConfig().getBoolean( "Weather" );
                    this.playerList = this.getConfig().getBoolean( "Playerlist" );
                    this.log( "Configuration reloaded.", Level.INFO );
                } else {
                    if ( sender.hasPermission("sendere.reload") ) {
                        sender.sendMessage("[Sendere] Reloading configuration...");
                        this.reloadConfig();
                        this.url = this.getConfig().getString("URL" );
                        this.logResponse = this.getConfig().getBoolean( "Log-Response" );
                        this.chat = this.getConfig().getBoolean( "Chat" );
                        this.join = this.getConfig().getBoolean( "Join" );
                        this.quit = this.getConfig().getBoolean( "Quit" );
                        this.kick = this.getConfig().getBoolean( "Kick" );
                        this.death = this.getConfig().getBoolean( "Death" );
                        this.weather = this.getConfig().getBoolean( "Weather" );
                        sender.sendMessage("[Sendere] Configuration reloaded.");
                    }
                }
            } else {
                sender.sendMessage( "[Sendere] Version " + this.Version );
            }
        }
                
        if( args[0].equals( "adminmsg" ) ){
            if( args.length < 1 ){
                try{
                    if ( !( sender instanceof Player ) ) {
                        for( Player player: Bukkit.getServer().getOnlinePlayers() ){
                            player.sendMessage( "[Admin Message: Console] " + args[0] );
                        }
                        this.publish( "AdminMessage", args[0], "Console" );
                    } else {
                        if ( sender.hasPermission("sendere.adminmsg") ) {
                            for( Player player: Bukkit.getServer().getOnlinePlayers() ){
                                player.sendMessage( "[Admin Message: " + sender.getName() + "] " + args[0] );
                            }
                            this.publish( "AdminMessage", args[0], sender.getName() );
                        }
                    }	
                } catch ( IOException ex ){
                    Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
                }
            } else {
                sender.sendMessage("Not enough arguments!");
                return false;
            }
                
            return true;
        }
          
        return false; 
    
    }
    
    @SuppressWarnings("LoggerStringConcat")
    public void log( String s, Level l ){
        Sendere.log.log( l, "[Sendere] " + s );
    }
    
    @SuppressWarnings("LoggerStringConcat")
    public void publish( String a, String b, String c ) throws MalformedURLException, IOException{
        String urlUse = url + "?a=" + a + "&b=" + b.replace( " ", "<[;:]>" ) + "&c=" + c;
 
        URL obj = new URL( urlUse );
        HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

        con.setRequestMethod( "GET" );

        con.setRequestProperty( "User-Agent", "Mozilla/5.0" );
 
        int responseCode = con.getResponseCode();
 
	BufferedReader in = new BufferedReader(
            new InputStreamReader( con.getInputStream() )
        );
        String inputLine;
        StringBuffer response = new StringBuffer();
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        if( this.logResponse )
            Sendere.log.log( Level.INFO, "[Sendere] The listener said:\n" + response );
        
        if( responseCode != 200 )
            Sendere.log.log( Level.WARNING, "[Sendere] The listener did not have a HTTP Status code 200. Instead, it was " + responseCode + ". Wikipedia has a good article about what each HTTP Status code means." );
    }
    
}
