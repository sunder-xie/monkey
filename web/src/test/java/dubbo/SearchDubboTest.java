package dubbo;

import com.tqmall.monkey.component.utils.JsonUtil;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.common.result.Result;
import com.tqmall.search.dubbo.client.cloudepc.param.EpcAvidCallRequest;
import com.tqmall.search.dubbo.client.cloudepc.result.EpcAvidCallDTO;
import com.tqmall.search.dubbo.client.cloudepc.service.CloudEpcService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zxg on 16/10/31.
 * 15:34
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchDubboTest {
    @Autowired
    private CloudEpcService cloudEpcService;

    @Test
    private void testPage() {
        EpcAvidCallRequest request = new EpcAvidCallRequest();
        PageableRequest pageableRequest = new PageableRequest();
        Result<Page<EpcAvidCallDTO>> result = cloudEpcService.queryEpcAvidCalls(request, pageableRequest);

        System.out.println(JsonUtil.objectToJson(result));

    }


}
