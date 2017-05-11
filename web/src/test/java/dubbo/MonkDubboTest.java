//package dubbo;
//
//import com.google.common.collect.Lists;
//import com.tqmall.core.common.entity.Result;
//import com.tqmall.data.monk.client.bean.MonkClientConstants;
//import com.tqmall.data.monk.client.bean.dto.ChatUserDto;
//import com.tqmall.data.monk.client.bean.dto.TokenDto;
//import com.tqmall.data.monk.client.service.MonkSendMessageService;
//import com.tqmall.data.monk.client.service.MonkService;
//import com.tqmall.monkey.component.utils.JsonUtil;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by zxg on 16/9/8.
// * 11:11
// * no bug,以后改代码的哥们，祝你好运~！！
// */
//
//@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//public class MonkDubboTest {
//    @Autowired
//    private MonkSendMessageService monkSendMessageService;
//    @Autowired
//    private MonkService monkService;
//
//    @org.junit.Test
//    public void testDubbo(){
//
//        ChatUserDto fromUser = new ChatUserDto();
//        fromUser.setSysId(675763);
//        fromUser.setSysName("uc");
//        List<ChatUserDto> toUsers = new ArrayList<>();
//        ChatUserDto toDO = new ChatUserDto();
//        toDO.setSysId(10036);
//        toDO.setSysName("lop");
//        toUsers.add(toDO);
//
//        List<String> messageList = new ArrayList<>();
//        messageList.add("成功发送");
//        messageList.add("什么鬼？？\n\r我不想和你说话");
//        messageList.add("6666");
//
//
//
//        Result result = monkSendMessageService.sendMessageToOther(fromUser,toUsers,messageList);
//        System.out.println(JsonUtil.objectToJson(result));
//    }
//
//    @Test
//    public void testToken(){
//        Result<TokenDto> result = monkService.getToken(MonkClientConstants.SYSNAME_UC);
//        System.out.println(JsonUtil.objectToJson(result));
//    }
//
//}
