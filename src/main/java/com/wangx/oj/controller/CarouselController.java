package com.wangx.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangx.oj.common.Result;
import com.wangx.oj.entity.Carousel;
import com.wangx.oj.mapper.CarouselMapper;
import com.wangx.oj.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carousel")
public class CarouselController {
    @Autowired
    private CarouselService carouselService;

    @RequestMapping(value = "/{page}/{pageSize}", method = RequestMethod.GET)
    public Result getCarouselPagination(@PathVariable Integer page, @PathVariable Integer pageSize) {
        IPage<Carousel> carouselPagination = carouselService.findCarouselPagination(page, pageSize);
        return Result.success(carouselPagination);
    }
}
