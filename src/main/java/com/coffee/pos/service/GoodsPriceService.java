package com.coffee.pos.service;

import com.coffee.pos.dto.goods_price.CreateGoodsPriceDTO;
import com.coffee.pos.model.Goods;
import com.coffee.pos.model.GoodsPrice;
import com.coffee.pos.repository.GoodsPriceRepository;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GoodsPriceService {
    private static final Logger logger = LoggerFactory.getLogger(GoodsPriceService.class);

    @Autowired GoodsPriceRepository goodsPriceRepository;

    @Autowired GoodsService goodsService;

    // Create, Read, Update
    public GoodsPrice createGoodsPrice(CreateGoodsPriceDTO createGoodsPriceDTO) {
        GoodsPrice goodsPrice = mapToEntity(createGoodsPriceDTO);
        if (goodsPrice != null) {
            return goodsPriceRepository.save(goodsPrice);
        } else {
            return null;
        }
    }

    public GoodsPrice queryById(String id) {
        logger.info("[GoodsPrice][queryById] User use {} to search from id.", id);
        return goodsPriceRepository.findById(id).orElse(null);
    }

    private GoodsPrice mapToEntity(CreateGoodsPriceDTO createGoodsPriceDTO) {
        Goods goods = goodsService.findById(createGoodsPriceDTO.getGoods_id().toString());
        if (goods != null) {
            return GoodsPrice.builder()
                    .goods(goods)
                    .price(createGoodsPriceDTO.getPrice())
                    .updateAt(LocalDateTime.now())
                    .createAt(LocalDateTime.now())
                    .build();
        } else {
            return null;
        }
    }

    public Page<GoodsPrice> findByGoodsById(String id, Pageable pageable) {
        return goodsPriceRepository.findByGoodsIdContaining(id, pageable);
    }
    public Page<GoodsPrice>getAll(Pageable pageable) {
        return goodsPriceRepository.findAll(pageable);
    }
    public void update(GoodsPrice existingGoodsPrice) {
        goodsPriceRepository.save(existingGoodsPrice);
    }
}
