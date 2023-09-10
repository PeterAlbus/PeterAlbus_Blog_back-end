package com.peteralbus.controller;

import com.peteralbus.domain.Music;
import com.peteralbus.domain.Result;
import com.peteralbus.service.MusicService;
import com.peteralbus.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/music")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MusicController {
    private final MusicService musicService;

    @RequestMapping("/queryAll")
    Result<List<Music>> queryAllMusic() {
        return ResultUtil.success(musicService.queryAll());
    }

    @RequestMapping("/add")
    Result<?> addMusic(Music music) {
        int result = musicService.add(music);
        if (result > 0) {
            return ResultUtil.success(null);
        } else {
            return ResultUtil.error(500,"添加失败");
        }
    }

    @RequestMapping("/delete")
    Result<?> deleteMusic(Long musicId) {
        int result = musicService.delete(musicId);
        if (result > 0) {
            return ResultUtil.success(null);
        } else {
            return ResultUtil.error(500,"删除失败");
        }
    }
}
