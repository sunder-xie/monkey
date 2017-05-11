package biztest;

import com.tqmall.monkey.client.excle.CarInfoExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huangzhangting on 17/3/22.
 */
@ContextConfiguration(locations = "classpath*:conf/application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CarInfoExcelUtilTest {

    @Test
    public void test() throws Exception{
        CarInfoExcelUtil carInfoExcelUtil = new CarInfoExcelUtil();
        carInfoExcelUtil.exportAllData("45", "/Users/huangzhangting/Desktop/");
    }
}
