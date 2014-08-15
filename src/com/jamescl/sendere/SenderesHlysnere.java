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

import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 *
 * @author James Larsson
 */
public class SenderesHlysnere implements Listener{
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void sendChat( AsyncPlayerChatEvent event ) throws IOException{
        if( event.isCancelled() || !Sendere.instance.chat || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        Sendere.instance.publish( "AsyncPlayerChat", event.getMessage(), event.getPlayer().getName() );
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerJoin( PlayerJoinEvent event ) throws IOException{
        
        if( !Sendere.instance.death || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        Sendere.instance.publish( "PlayerJoin", "", event.getPlayer().getName() );
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerLeave( PlayerQuitEvent event ) throws IOException{
        
        if( !Sendere.instance.death || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        Sendere.instance.publish( "PlayerQuit", "", event.getPlayer().getName() );
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerKick( PlayerKickEvent event ) throws IOException{
        if( event.isCancelled() || !Sendere.instance.kick || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        Sendere.instance.publish( "PlayerKick", event.getReason(), event.getPlayer().getName() );
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void playerDeath( PlayerDeathEvent event ) throws IOException{
        if( !Sendere.instance.death || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        Sendere.instance.publish( "PlayerDeath", event.getDeathMessage(), event.getEntity().getName() );
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void weatherChange( WeatherChangeEvent event ) throws IOException{
        if( event.isCancelled() || !Sendere.instance.weather || Sendere.instance.url.equals( "http://www.minwrit.me/hlysnere.php" ) )
            return;
        
        String state;
        if( event.toWeatherState() == true ){
            state = "Rain";
        } else {
            state = "Sun";
        }
        
        Sendere.instance.publish( "WeatherChange", state, event.getWorld().getName() );
    }
}
