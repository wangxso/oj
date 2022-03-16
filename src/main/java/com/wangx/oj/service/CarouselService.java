package com.wangx.oj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.entity.Carousel;

public interface CarouselService {
    IPage<Carousel> findCarouselPagination(int page, int pageSize);
}
