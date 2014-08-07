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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author James Larsson
 */
public class Sendere extends JavaPlugin{
    public static Sendere instance;
    public final static Logger log = Logger.getLogger( "Minecraft" );
    public String url              = "http://www.minwrit.me/hlysnere.php";
    public Boolean chat            = true;
    public Boolean join            = true;
    public Boolean quit            = true;
    public Boolean kick            = true;
    public Boolean death           = true;
    public Boolean weather         = true;
    
    @Override
    public void onEnable(){
        Sendere.instance = this;
        
        this.saveDefaultConfig();
        this.url = this.getConfig().getString("url" );
        this.chat = this.getConfig().getBoolean( "Chat" );
        this.join = this.getConfig().getBoolean( "Join" );
        this.quit = this.getConfig().getBoolean( "Quit" );
        this.kick = this.getConfig().getBoolean( "Kick" );
        this.death = this.getConfig().getBoolean( "Death" );
        this.weather = this.getConfig().getBoolean( "Weather" );
        
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
        
        if( this.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            toReport = "";
        
        if( toReport.equals( "" ) )
            toReport = "naught";
        
        for( Player player: Bukkit.getServer().getOnlinePlayers() ) {
            try{
                this.publish( "PlayerJoin", player.getName(), "" );
            } catch( IOException ex ){
                Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        
        this.log( "Sendere has been enabled. Reporting " + toReport + " to " + this.url, Level.INFO ); 
    }
    
    @Override
    public void onDisable(){
        for( Player player: Bukkit.getServer().getOnlinePlayers() ){
            try {
                this.publish( "PlayerQuit", player.getName(), "" );
            } catch( IOException ex ){
                Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        try{
            this.publish( "ServerStop", "", "" );
        } catch (IOException ex){
            Logger.getLogger( Sendere.class.getName() ).log( Level.SEVERE, null, ex );
        }
        this.log( "Sendere has been disabled.", Level.INFO );
    }
    
    public void log( String s, Level l ){
        Sendere.log.log( l, "[Sendere] " + s );
    }
    
    public void publish( String a, String b, String c ) throws MalformedURLException, IOException{
        String urlUse = url + "?a=" + a + "&b=" + b + "&c=" + c.replace( " ", "<[;:]>" );
 
        URL obj = new URL( urlUse );
	HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

	con.setRequestMethod( "GET" );

	con.setRequestProperty( "User-Agent", "Mozilla/5.0" );
 
	int responseCode = con.getResponseCode();
 
	BufferedReader in = new BufferedReader(
            new InputStreamReader( con.getInputStream() )
        );
    }
    
}
