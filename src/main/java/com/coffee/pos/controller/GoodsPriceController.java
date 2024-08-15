package com.coffee.pos.controller;

import com.coffee.pos.dto.CommonListResponse;
import com.coffee.pos.dto.CommonObjectResponse;
import com.coffee.pos.dto.goods_price.CreateGoodsPriceDTO;
import com.coffee.pos.enums.CommonStatus;
import com.coffee.pos.model.GoodsPrice;
import com.coffee.pos.service.GoodsPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;


@RestController
@RequestMapping("api/v1/good_price")
public class GoodsPriceController {
    @Autowired GoodsPriceService goodsPriceService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonObjectResponse> getGoodsPriceById(@PathVariable String id) {
        GoodsPrice goodsPrice = goodsPriceService.queryById(id);
        if (goodsPrice != null) {
            CommonObjectResponse response =
                    new CommonObjectResponse("Success", CommonStatus.SUCCESS, goodsPrice);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CommonObjectResponse> save(
            @RequestBody CreateGoodsPriceDTO createGoodsPriceDTO) {
        GoodsPrice goodsPrice = goodsPriceService.createGoodsPrice(createGoodsPriceDTO);
        if (goodsPrice != null) {
            return new ResponseEntity<>(
                    new CommonObjectResponse("Success", CommonStatus.SUCCESS, goodsPrice),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    //[Feature] Add goods_price get all and patch #1
    //新增取得goods_price list，需要有分頁、排序和以goods_id 搜尋，patch 更新只能夠更新price，並且需要更新update_at
    @GetMapping
    public ResponseEntity<CommonListResponse<GoodsPrice>> getGoodsPrices(
            @RequestParam(required = false) String goods_id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction =
                sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page-1,size,Sort.by(direction,sort));
        Page<GoodsPrice> goodsPriceList;
        if(goods_id != null && !goods_id.isEmpty()) {
            goodsPriceList = goodsPriceService.findByGoodsById(goods_id,pageable);
        } else {
            goodsPriceList = goodsPriceService.getAll(pageable);
        }
        CommonListResponse<GoodsPrice> response =
                new CommonListResponse<>("Success", CommonStatus.SUCCESS, goodsPriceList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CommonObjectResponse> updatePrice(
            @PathVariable String id, @RequestParam int price, @RequestBody CreateGoodsPriceDTO createGoodsPriceDTO) {
        GoodsPrice existingGoodsPrice = goodsPriceService.queryById(id);
        if (existingGoodsPrice != null) {
            existingGoodsPrice.setPrice(price);
            existingGoodsPrice.setUpdateAt(LocalDateTime.now());
            goodsPriceService.update(existingGoodsPrice);
            return new ResponseEntity<>(
                    new CommonObjectResponse("Success", CommonStatus.SUCCESS, existingGoodsPrice),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
