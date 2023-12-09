package com.peteralbus.service;

import com.peteralbus.domain.BackgroundImage;

import java.util.List;

/**
 * The interface Background image service.
 */
public interface BackgroundImageService {
    /**
     * Query all list.
     *
     * @return the list
     */
    List<BackgroundImage> queryAll();

    /**
     * Add int.
     *
     * @param backgroundImage the background image
     * @return the int
     */
    int add(BackgroundImage backgroundImage);

    /**
     * Update int.
     *
     * @param backgroundImage the background image
     * @return the int
     */
    int update(BackgroundImage backgroundImage);

    /**
     * Delete int.
     *
     * @param backgroundId the background id
     * @return the int
     */
    int delete(Long backgroundId);

    /**
     * Query by id background image.
     *
     * @param backgroundId the background id
     * @return the background image
     */
    BackgroundImage queryById(Long backgroundId);
}
