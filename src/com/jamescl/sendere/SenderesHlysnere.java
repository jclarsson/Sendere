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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author James Larsson
 */
public class SenderesHlysnere implements Listener{
    
    public void publish( String a, String b, String c ) throws MalformedURLException, IOException{
        String urlUse = Sendere.instance.url + "?a=" + a + "&b=" + b.replace( " ", "<[;:]>" ) + "&c=" + c;
 
        URL obj = new URL( urlUse );
	HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

	con.setRequestMethod( "GET" );

	con.setRequestProperty( "User-Agent", "Mozilla/5.0" );
 
	int responseCode = con.getResponseCode();
 
	BufferedReader in = new BufferedReader(
            new InputStreamReader( con.getInputStream() )
        );
    }
    
    @EventHandler
    public void sendChat( AsyncPlayerChatEvent event ) throws IOException{
        if( event.isCancelled() || !Sendere.instance.chat || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        this.publish( "AsyncPlayerChat", event.getMessage(), event.getPlayer().getName() );
    }
    
    @EventHandler
    public void playerJoin( PlayerJoinEvent event ) throws IOException{
        if( !Sendere.instance.death || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        this.publish( "PlayerJoin", "", event.getPlayer().getName() );
    }
    
    @EventHandler
    public void playerLeave( PlayerQuitEvent event ) throws IOException{
        if( !Sendere.instance.death || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        this.publish( "PlayerQuit", "", event.getPlayer().getName() );
    }
    
    @EventHandler
    public void playerKick( PlayerKickEvent event ) throws IOException{
        if( event.isCancelled() || !Sendere.instance.kick || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        this.publish( "PlayerKick", event.getReason(), event.getPlayer().getName() );
    }
    
    @EventHandler
    public void playerDeath( PlayerDeathEvent event ) throws IOException{
        if( !Sendere.instance.death || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        this.publish( "PlayerDeath", event.getDeathMessage(), event.getEntity().getName() );
    }
    
    @EventHandler
    public void weatherChange( WeatherChangeEvent event ) throws IOException{
        if( event.isCancelled() || !Sendere.instance.weather || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        String state;
        if( event.toWeatherState() == true ){
            state = "Rain";
        } else {
            state = "Sun";
        }
        
        this.publish( "WeatherChange", state, event.getWorld().getName() );
    }
}
