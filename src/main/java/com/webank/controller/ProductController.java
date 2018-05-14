package com.webank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理模块
 */
@Controller
@RequestMapping(value = "product")
public class ProductController {
    @RequestMapping(value = "getProductInfo", method = RequestMethod.POST)
    @ResponseBody
    public void getProductInfo(Integer productId) {

    }

    @RequestMapping(value = "getProductDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public void getProductDetailInfo() {

    }
}
