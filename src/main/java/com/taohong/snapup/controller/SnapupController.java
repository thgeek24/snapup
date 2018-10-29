package com.taohong.snapup.controller;

import com.taohong.snapup.domain.OrderInfo;
import com.taohong.snapup.domain.SnapupOrder;
import com.taohong.snapup.domain.SnapupUser;
import com.taohong.snapup.redis.RedisService;
import com.taohong.snapup.result.CodeMsg;
import com.taohong.snapup.result.Result;
import com.taohong.snapup.service.GoodsService;
import com.taohong.snapup.service.OrderService;
import com.taohong.snapup.service.SnapupService;
import com.taohong.snapup.service.SnapupUserService;
import com.taohong.snapup.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author taohong on 09/10/2018
 */
@Controller
@RequestMapping("/snapup")
public class SnapupController {

    @Autowired
    SnapupUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SnapupService snapupService;

    /**
     * 351 QPS at HEAP:="-Xms2048m -Xmx2048m -Xss256k" (order created & no exception)
     * 3000 * 10
     */
    @RequestMapping(value = "/do_snapup", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> snapup(Model model, SnapupUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // Check stock
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.SNAPUP_OVER);
        }

        // Check if have snapped up an item
        SnapupOrder order = orderService.getSnapupOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SNAPUP);
        }

        // Reduce stock, make order, write snap-up order
        OrderInfo orderInfo = snapupService.snapup(user, goods);
        return Result.success(orderInfo);
    }
}
