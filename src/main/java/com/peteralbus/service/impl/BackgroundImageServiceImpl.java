package com.peteralbus.service.impl;

import com.peteralbus.domain.BackgroundImage;
import com.peteralbus.mapper.BackgroundImageMapper;
import com.peteralbus.service.BackgroundImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BackgroundImageServiceImpl implements BackgroundImageService {
    private final BackgroundImageMapper backgroundImageMapper;

    @Override
    public List<BackgroundImage> queryAll() {
        return backgroundImageMapper.selectList(null);
    }

    @Override
    public int add(BackgroundImage backgroundImage) {
        return backgroundImageMapper.insert(backgroundImage);
    }

    @Override
    public int update(BackgroundImage backgroundImage) {
        return backgroundImageMapper.updateById(backgroundImage);
    }

    @Override
    public int delete(Long backgroundId) {
        return backgroundImageMapper.deleteById(backgroundId);
    }

    @Override
    public BackgroundImage queryById(Long backgroundId) {
        return backgroundImageMapper.selectById(backgroundId);
    }
}
