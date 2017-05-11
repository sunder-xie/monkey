package dubbo;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.epc.client.bean.enums.SourceEnums;
import com.tqmall.data.epc.client.bean.param.order.ConfirmSettleParam;
import com.tqmall.data.epc.client.bean.param.order.ConfirmShippingParam;
import com.tqmall.data.epc.client.bean.param.order.OrderGoodsParam;
import com.tqmall.data.epc.client.server.order.OrderService;
import com.tqmall.monkey.component.utils.JsonUtil;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 16/8/30.
 * 19:18
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderDubboTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testConfirmSettle(){

        ConfirmSettleParam param = new ConfirmSettleParam();
        param.setOrderSn("Q16083081280002");
        param.setOperatorSource(SourceEnums.ERP.getCode());
        param.setOperator("中西543");
        param.setHasSettleAmount(new BigDecimal(10));
        Result result = orderService.confirmSettle(param);

        System.out.println(JsonUtil.objectToJson(result));
    }

    @Test
    public void testConfirmShipping(){
        ConfirmShippingParam shippingParam = new ConfirmShippingParam();

        shippingParam.setOrderSn("Q16083176880009");
        shippingParam.setOperator("中西232");
        shippingParam.setOperatorSource(SourceEnums.STORES.getCode());
        shippingParam.setSellerId(50439);
        shippingParam.setSellerOrderNote("这是我的上家备注");
        shippingParam.setShippingCompany("恒通物流公司");
        shippingParam.setShippingNo("H123131");
        shippingParam.setShippingId(1);

        Result result = orderService.confirmShipping(shippingParam);
        System.out.println(JsonUtil.objectToJson(result));
    }

    @Test
    public void testModifyOrderPrice(){
        String orderSn = "Q16083063120001";
        Integer sellerId = 12;
        List< OrderGoodsParam > paramList = new ArrayList<>();
        String operator = "中西";

        OrderGoodsParam goodsParam  =new OrderGoodsParam();
        goodsParam.setGoodsId(1);
        goodsParam.setSoldPrice(new BigDecimal(100.1));
        paramList.add(goodsParam);
        OrderGoodsParam goodsParam1  =new OrderGoodsParam();
        goodsParam1.setGoodsId(2);
        goodsParam1.setSoldPrice(new BigDecimal(1500.1));
        paramList.add(goodsParam1);

        Result result = orderService.modifyOrderPrice(orderSn,sellerId,paramList,operator);
        System.out.println(JsonUtil.objectToJson(result));
    }
}
