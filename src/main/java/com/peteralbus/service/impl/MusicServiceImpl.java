package com.peteralbus.service.impl;

import com.peteralbus.domain.Music;
import com.peteralbus.mapper.MusicMapper;
import com.peteralbus.service.MusicService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Music service.
 * @author PeterAlbus
 * Created on 2022/6/27.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MusicServiceImpl implements MusicService
{
    private final MusicMapper musicMapper;

    @Override
    public List<Music> queryAll()
    {
        return musicMapper.selectList(null);
    }

    @Override
    public int add(Music music)
    {
        return musicMapper.insert(music);
    }

    @Override
    public int delete(Long musicId)
    {
        return musicMapper.deleteById(musicId);
    }
}
