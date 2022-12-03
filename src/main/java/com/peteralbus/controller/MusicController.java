package com.peteralbus.controller;

import com.peteralbus.domain.Music;
import com.peteralbus.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * The type Music controller.
 * @author PeterAlbus
 * Created on 2022/6/27.
 */
@RestController
@RequestMapping("/music")
@CrossOrigin
public class MusicController
{
    private MusicService musicService;

    @Autowired
    public void setMusicService(MusicService musicService)
    {
        this.musicService = musicService;
    }

    @RequestMapping("/queryAll")
    List<Music> queryAllMusic()
    {
        return musicService.queryAll();
    }

    @RequestMapping("/add")
    String addMusic(Music music)
    {
        int result= musicService.add(music);
        if(result>0)
        {
            return "success";
        }
        else
        {
            return "fail";
        }
    }

    @RequestMapping("/delete")
    String deleteMusic(Long musicId)
    {
        int result= musicService.delete(musicId);
        if(result>0)
        {
            return "success";
        }
        else
        {
            return "fail";
        }
    }
}
