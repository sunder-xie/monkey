package com.tqmall.monkey.client.category;

import com.tqmall.monkey.dal.dao.category.AllCategoryViewDao;
import com.tqmall.monkey.domain.commonBean.Page;
import com.tqmall.monkey.domain.entity.category.AllCategoryViewDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 15/6/29.
 */
@Service
public class AllCategoryViewServiceImpl implements AllCategoryViewService{
    @Autowired
    private AllCategoryViewDao allCategoryViewDao;

    @Override
    public Page<AllCategoryViewDO> findAllCategoryPage(Integer cateId, Integer level, String vehicleCode, String partName, int pageIndex, int pageSize) {
        if(pageIndex<1){
            pageIndex = 1;
        }
        if(pageSize<1){
            pageSize = 10;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if(vehicleCode!=null && !"".equals(vehicleCode)){
            params.put("vehicleCode", vehicleCode);
        }
        if(partName!=null && !"".equals(partName)){
            params.put("partName", partName);
        }
        params.put("catId", cateId);

        Page<AllCategoryViewDO> page = null;
        switch(level){
            case 1:
                //page = allCategoryViewDao.getAllCategoryByFirstCatId(params, pageIndex, pageSize);
                page = allCategoryViewDao.selectAllCategorysPageByFirstCatId(params, pageIndex, pageSize);
                break;
            case 2:
                //page = allCategoryViewDao.getAllCategoryBySecondCatId(params, pageIndex, pageSize);
                page = allCategoryViewDao.selectAllCategorysPageBySecondCatId(params, pageIndex, pageSize);
                break;
            case 3:
                //page = allCategoryViewDao.getAllCategoryByThirdCatId(params, pageIndex, pageSize);
                page = allCategoryViewDao.selectAllCategorysPageByThirdCatId(params, pageIndex, pageSize);
                break;
            default:
                //page = allCategoryViewDao.getAllCategoryPage(params, pageIndex, pageSize);
                page = allCategoryViewDao.selectAllCategorysPage(params, pageIndex, pageSize);
                break;
        }

        return page;
    }

    @Override
    public List<AllCategoryViewDO> findAllCategory(Integer cateId, Integer level, String vehicleCode, String partName) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(vehicleCode!=null && !"".equals(vehicleCode)){
            params.put("vehicleCode", vehicleCode);
        }
        if(partName!=null && !"".equals(partName)){
            params.put("partName", partName);
        }
        params.put("catId", cateId);

        List<AllCategoryViewDO> dataList = null;
        switch(level){
            case 1:
                dataList = allCategoryViewDao.getMapper().getAllCategoryByFirstCatId(params);
                break;
            case 2:
                dataList = allCategoryViewDao.getMapper().getAllCategoryBySecondCatId(params);
                break;
            case 3:
                dataList = allCategoryViewDao.getMapper().getAllCategoryByThirdCatId(params);
                break;
            default:
                dataList = allCategoryViewDao.getMapper().getAllCategory(params);
                break;
        }

        return dataList;
    }

    @Override
    public List<AllCategoryViewDO> findAllCategorys(Integer cateId, Integer level, String vehicleCode, String partName) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(vehicleCode!=null && !"".equals(vehicleCode)){
            params.put("vehicleCode", vehicleCode);
        }
        if(partName!=null && !"".equals(partName)){
            params.put("partName", partName);
        }
        params.put("catId", cateId);

        List<AllCategoryViewDO> dataList = null;
        switch(level){
            case 1:
                dataList = allCategoryViewDao.getMapper().selectAllCategorysByFirstCatId(params);
                break;
            case 2:
                dataList = allCategoryViewDao.getMapper().selectAllCategorysBySecondCatId(params);
                break;
            case 3:
                dataList = allCategoryViewDao.getMapper().selectAllCategorysByThirdCatId(params);
                break;
            default:
                dataList = allCategoryViewDao.getMapper().selectAllCategorys(params);
                break;
        }

        return dataList;
    }
}
