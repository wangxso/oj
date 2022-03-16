package com.wangx.oj.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangx.oj.entity.Carousel;
import com.wangx.oj.mapper.CarouselMapper;
import com.wangx.oj.service.CarouselService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CarouselServiceImpl implements CarouselService {
    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public IPage<Carousel> findCarouselPagination(int page, int pageSize) {
        Page<Carousel> carouselPage = new Page<>(page, pageSize);
        IPage<Carousel> carouselIPage = carouselMapper.selectPage(carouselPage, null);
        return carouselIPage;
    }
}
