//package dubbo;
//
//import com.tqmall.core.common.entity.Result;
//import com.tqmall.holy.provider.entity.VerifyInfoDTO;
//import com.tqmall.holy.provider.service.RpcVerifyInfoService;
//import com.tqmall.monkey.component.utils.JsonUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * Created by zxg on 16/10/12.
// * 15:11
// * no bug,以后改代码的哥们，祝你好运~！！
// */
//@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//public class HollyDubboTest {
//
//    @Autowired
//    private RpcVerifyInfoService rpcVerifyInfoService;
//
//    @Test
//    public void testVerify(){
//        Long shopId = 675682L;
//        Result<VerifyInfoDTO> result = rpcVerifyInfoService.getVerifyInfoByShopId
//                (shopId);
//
//        System.out.println(JsonUtil.objectToJson(result));
//    }
//}
