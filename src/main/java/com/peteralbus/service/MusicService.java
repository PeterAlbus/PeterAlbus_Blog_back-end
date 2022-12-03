package com.peteralbus.service;

import com.peteralbus.domain.Music;

import java.util.List;

/**
 * The interface Music service.
 * Created on 2022/6/27.
 *
 * @author PeterAlbus
 */
public interface MusicService
{
    /**
     * Query all list.
     *
     * @return the list
     */
    List<Music> queryAll();

    /**
     * Add int.
     *
     * @param music the music
     * @return the int
     */
    int add(Music music);

    /**
     * Delete int.
     *
     * @param musicId the music id
     * @return the int
     */
    int delete(Long musicId);
}
