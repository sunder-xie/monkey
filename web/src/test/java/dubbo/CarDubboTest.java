package dubbo;

import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.tqmall.athena.client.car.CarCategoryService;
import com.tqmall.athena.domain.result.carcategory.CarCategoryDTO;
import com.tqmall.athena.domain.result.carcategory.CarListDTO;
import com.tqmall.athena.domain.result.carcategory.CarListSuit4GoodsDTO;
import com.tqmall.athena.domain.result.carcategory.HotCarBrandDTO;
import com.tqmall.core.common.entity.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertTrue;

/**
 * Created by zxg on 15/9/22.
 * 15:16
 */
@ContextConfiguration(locations = "classpath*:test-dubbo-consume.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class CarDubboTest  {

    @Autowired
    private CarCategoryService carCategoryService;
//
//    @Autowired
//    private MaintainService maintain;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        System.out.println(new Date());
//        RpcContext rpcContext = RpcContext.getContext();
//        Result<List<CarCategoryDTO>> allCat = new Result<>();
//        Result<List<CarCategoryDTO>> firstCat = new Result<>();
//
//        allCat = carCategoryService.getAllCarCategory();
//        Future<Result> allCatFuture = rpcContext.getFuture();
//        firstCat = carCategoryService.getCarCategoryByPid(1);
//        Future<Result> firstCatFuture = rpcContext.getFuture();
//
//        allCat = allCatFuture.get();
//        firstCat = firstCatFuture.get();
//        try {
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//
//        Result<List<ItemDTO>>  result = maintain.getItems();
//        System.out.println(JSON.toJSONString(result));

    }

    @Test
    public void testNotify(){
        Result<CarCategoryDTO> result = carCategoryService.getCarCategoryByPrimaryId(1);
        System.out.println(JSON.toJSONString(result));



    }

    @Test
    public void testGetPrimaryCarCategory(){
        Integer id = 86;
        Result<CarCategoryDTO> result = carCategoryService.getCarCategoryByPrimaryId(id);

        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void testGetAllCarCategory(){
        Result<List<CarCategoryDTO>> result = carCategoryService.getAllCarCategory();
        List<CarCategoryDTO> list = result.getData();
        System.out.println("=====================testGetAllCarCategory:"+list.size());
        for(CarCategoryDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" pid:"+carCategoryDTO.getPid());
        }
        System.out.println("=====================end:");
        System.out.println(JSON.toJSONString(result));
        assertTrue(list.size()>0);
    }

    @Test
    public void testGetCarCategoryByPid(){
        Integer pid = 70158;
        Result<List<CarCategoryDTO>> result = carCategoryService.getCarCategoryByPid(pid);
        List<CarCategoryDTO> list = result.getData();
        System.out.println("=====================testGetCarCategoryByPid:"+list.size());
        for(CarCategoryDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" pid:"+carCategoryDTO.getPid()+" level:");
        }
        System.out.println(JSON.toJSONString(list));

        System.out.println("=====================end:");

//        assertTrue(list.size()>0);
    }

    @Test
    public void testGetCarParentsByCarId(){
        Integer carId = 48761;
        Result<List<CarCategoryDTO>> result = carCategoryService.getCarParentsByCarId(carId);
        List<CarCategoryDTO> list = result.getData();
        System.out.println("=====================testGetCarParentsByCarId:");
        for(CarCategoryDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" pid:"+carCategoryDTO.getPid());
        }
        System.out.println("=====================end:");

        assertTrue(list.size()>0);
    }

    @Test
    public void testQueryCarCatInfoByLId(){
        String liyangId = "ACC0133A0001";
        Result<CarCategoryDTO> result = carCategoryService.queryCarCatInfoByLId(liyangId);
        CarCategoryDTO carCategoryDTO = result.getData();
        System.out.println("=====================testQueryCarCatInfoByLId:");
        System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getName()+" :level"+ carCategoryDTO.getLevel()+" pid:"+carCategoryDTO.getPid());

        assertTrue(carCategoryDTO != null);
    }


    @Test
    public void testCategoryCarInfo(){
//        Integer pid = 87;//马自达
//        Integer pid = 9;//奥迪 level 1
//        Integer pid = 188;//奥迪 进口80 level 2
        Integer pid = 76237;//奥迪 进口 A8L level 3

        Result<List<CarListDTO>> result = carCategoryService.categoryCarInfo(pid);
        List<CarListDTO> list = result.getData();
        System.out.println("=====================testCategoryCarInfo:" + list.size());
        for(CarListDTO carCategoryDTO:list){
            System.out.println(carCategoryDTO.getId()+" "+carCategoryDTO.getCarName()+" pid:"+carCategoryDTO.getPid());
        }
        System.out.println("=====================end:");
        assertTrue(list.size()>0);
    }

    @Test
    public void testGetCartListByGoodsId(){
        Integer carId = 9289;
        Result<List<CarListSuit4GoodsDTO>> result = carCategoryService.getCartListByGoodsId(carId);
        List<CarListSuit4GoodsDTO> list = result.getData();
        System.out.println("=====================testGetCarParentsByCarId:"+list.size());

        System.out.println(JSON.toJSONString(list));
        assertTrue(list.size() > 0);
    }


    @Test
    public void testHotCar(){
        Integer cityId = 52;
        Result<List<HotCarBrandDTO>> result = carCategoryService.getHotCarBrandList(cityId);

        System.out.println(JSON.toJSONString(result));
    }



}
