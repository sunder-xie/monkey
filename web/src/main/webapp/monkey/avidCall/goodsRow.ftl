<div class="row row-fix goods-row">
    <div class="col-md-2 col-sm-4 goods_div">
        <input type="text" class="form-control goods-name" value="${goods.goodsName}">
    </div>
    <div class="col-md-2 col-sm-4 goods_div">
        <input type="text" class="form-control" value="${goods.goodsOe}">
    </div>
    <div class="col-md-1 col-sm-2 goods_div">
        <input type="number" class="form-control" value="${goods.goodsNumber}">
    </div>
    <div class="col-md-1 col-sm-2 goods_div">
        <input type="text" class="form-control" value="${goods.goodsUnit}">
    </div>
    <div class="col-md-2 col-sm-4 goods_div">
        <select class="form-control select2me goods-quality" data-id="${goods.goodsQualityId}">
            <option value="-1">--请选择--</option>
        </select>
    </div>
    <div class="col-md-2 col-sm-4 goods_div">
        <select class="form-control select2me back-quality" data-id="${goods.backQualityId}">
            <option value="-1">--请选择--</option>
        </select>
    </div>
    <div class="col-md-2 col-sm-4 goods_div" >
        <input type="text" class="form-control" value="${goods.goodsRemark}">
    </div>
    <div class="del_goods_div">
        <i title="删除商品" class="fa fa-times-circle red del-goods-btn" ></i>
    </div>
</div>
