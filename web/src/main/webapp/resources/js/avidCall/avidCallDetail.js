/**
 * Created by huangzhangting on 16/10/28.
 */

var body = $('body');

$(document).ready(function(){

    $('#carBrand').change(function(){
        initCarSeries(this.value);
    });
    $('#carSeries').change(function(){
        initCarModel(this.value);
    });
    $('#carModel').change(function(){
        initCarName(this.value);
    });
    $('#vinInput').blur(function(){
        searchVin(this);
    }).keyup(function(event){
        var val = this.value;
        if(!/^[0-9a-zA-Z]+$/g.test(val)){
            val = val.replace(/[^0-9a-zA-Z]/g, '').substr(0, 17);
            $(this).val(val);
        }
        $('#vinLength').text(val.length);
    });

    /* 查询车品牌 */
    initCarBrand();

    /* 查询配件品质 */
    initGoodsQuality();

    /* 添加联想输入事件 */
    autocompleteFun('.goods-name');

    $('.del-goods-btn').click(function(){
        deleteGoods(this);
    });
    $('.add-goods-btn').click(function(){
        addGoods();
    });

    $('.turn-wish-btn').click(function(){
        turnWishList();
    });
    $('.cancel-btn').click(function(){
        shopCancel();
    });
    $('.save-btn').click(function(){
        tempSave();
    });
    $('.return-btn').click(function(){
        goToListPage();
    });
});

function goToListPage(){
    window.location.href = '/monkey/avidCall/index?searchParamJson='+$('#searchParamJson').val();
}

var qualityHtml = '<option value="0">--请选择--</option>';
/* 配件品质下拉框初始化 */
function initGoodsQuality(){
    Ajax.get({
        url: '/monkey/common/getGoodsQuality',
        success: function(result){
            if(result.success){
                qualityHtml = template('qualityTemplate', result);
                initQualityList('select.goods-quality');
                initQualityList('select.back-quality');
            }
        }
    });
}
function initQualityList(obj){
    var goodsQualityList = $(obj);
    var len = goodsQualityList.length;
    for(var i=0; i<len; i++){
        var gq = goodsQualityList.eq(i);
        var id = gq.data('id');
        initHtmlByObj(gq, qualityHtml, id);
    }
}

function getSubCarList(pId, successFun){
    if(pId < 0){
        return;
    }
    Ajax.get({
        url: '/monkey/car/getCarByPid',
        data: {pid: pId},
        success: successFun
    });
}

/* 品牌下拉框初始化 */
function initCarBrand(){
    var carBrandId = $('#carBrand').data('id');
    var carSeriesId = $('#carSeries').data('id');
    var carModelId = $('#carModel').data('id');
    var carId = $('#carName').data('id');
    //console.log(carBrandId+' '+carSeriesId+' '+carModelId+' '+carId);

    var successFun = function(result){
        if(result.success){
            var html = template('carBrandTemplate', result);
            initSelectHtml('carBrand', html, carBrandId);
        }
    };
    getSubCarList(0, successFun);

    if(carBrandId>0){
        initCarSeries(carBrandId, carSeriesId);
    }
    if(carSeriesId>0){
        initCarModel(carSeriesId, carModelId);
    }
    if(carModelId>0){
        initCarName(carModelId, carId);
    }
}

/* 车系下拉框初始化 */
function initCarSeries(pId, val){
    initSelect('carSeries');
    initSelect('carModel');
    initSelect('carName');

    getSubCarList(pId, function(result){
        if(result.success){
            var html = template('carSeriesTemplate', result);
            initSelectHtml('carSeries', html, val);
        }
    });
}

/* 车型下拉框初始化 */
function initCarModel(pId, val){
    initSelect('carModel');
    initSelect('carName');

    getSubCarList(pId, function(result){
        if(result.success){
            var html = template('subCarTemplate', result);
            initSelectHtml('carModel', html, val);
        }
    });
}

/* 排量年款下拉框初始化 */
var _carList;
function initCarName(carModelId, val){
    initSelect('carName');
    if(carModelId<1){
        return;
    }
    Ajax.get({
        url: '/monkey/maintain/cars',
        data: {modelId: carModelId},
        success: function(result){
            if(result.success){
                var html = template('subCarTemplate', result);
                initSelectHtml('carName', html, val);
                _carList = result.data;
            }
        }
    });
}

/* 初始化下拉框 */
function initSelect(idStr){
    var select = setSelect2(idStr);
    select.get(0).options.length = 1;
}
function setSelect2(idStr, val){
    return initSelectHtml(idStr, undefined, val);
}
function initSelectHtml(idStr, html, val){
    return initHtmlByObj('#'+idStr, html, val);
}

function initHtmlByObj(obj, html, val){
    var jqObj = $(obj);
    if(html!==undefined && html!==null){
        jqObj.html(html);
    }

    if(val===undefined || val===null || val==''){
        val = 0;
    }
    jqObj.select2('val', val);

    return jqObj;
}

/** vin码查询 */
function handleVin(vin){
    return vin.replace(/\s/g, '');
}
function searchVin(el){
    var vin = handleVin(el.value);
    //alert(vin);
    $(el).val(vin);
    if(vin.length != 17){
        LayerUtil.tips('请输入17位VIN码', el);
        return;
    }

    Ajax.get({
        url: '/monkey/car/getCarByVin',
        data: {vin: vin},
        success: function(result){
            //console.log(result);
            if(result.success){
                var vinCarList = result.data;
                if(vinCarList.length > 1){
                    $('#confirmModal').modal('show');
                    var html = template('vinCarTemplate', result);
                    $('#confirmModalContent').html(html);
                    $('.vin-car').click(function(){
                        var carId = $(this).data('id');
                        selectVinCar(carId, vinCarList);
                    });
                }else{
                    setVinCar(vinCarList[0]);
                }
            }else{
                LayerUtil.tips(result.message, el);
            }
        }
    });
}
function setVinCar(vinCar){
    setSelect2('carBrand', vinCar.brandId);
    initCarSeries(vinCar.brandId, vinCar.seriesId);
    initCarModel(vinCar.seriesId, vinCar.modelId);
    initCarName(vinCar.modelId, vinCar.id);
}
function selectVinCar(carId, vinCarList){
    var vinCar;
    var len = vinCarList.length;
    for(var i=0; i<len; i++){
        vinCar = vinCarList[i];
        if(carId==vinCar.id){
            break;
        }
    }
    if(vinCar!==undefined){
        $('#confirmModal').modal('hide');
        setVinCar(vinCar);
    }
}

/** 配件名称联想输入 */
function autocompleteFun(el){
    $(el).autocomplete({
        source: function( queryObj, callbackFun ) {
            var searchName = $.trim(queryObj.term);
            Ajax.get({
                url: '/monkey/common/cate/suggest',
                data: {keyword: searchName},
                success: function( result ) {
                    if(result.success){
                        callbackFun($.map(result.data, function(item) {
                            return {
                                label: item,
                                value: item
                            }
                        }));
                    }
                }
            });

        },
        minLength: 1,
        autoFocus: true,
        delay: 500,
        select: function( event, ui ) {// ui.item为选中的项
            //alert(JSON.stringify(ui.item));
        }
    });
}

/** 删除配件 */
function deleteGoods(el){
    var goodsList = $('.goods-row');
    if(goodsList.length<2){
        LayerUtil.tips('至少有一个商品', el);
        return;
    }
    $(el).parent().parent().remove();
}
/** 添加配件 */
function addGoods(){
    var html = template('goodsTemplate', {qualityHtml: qualityHtml});
    var jqObj = $(html);
    $('.goods-info-hr').before(jqObj);

    /* 名称联想输入 */
    var goodsNameInput = jqObj.find('.goods-name');
    autocompleteFun(goodsNameInput);

    /* 删除按钮添加事件 */
    var delGoodsBtn = jqObj.find('.del-goods-btn');
    delGoodsBtn.click(function(){
        deleteGoods(this);
    });

    /* 下拉框美化 */
    jqObj.find('.select2me').select2();
}


/* 组装创建需求单，请求参数 */
function checkId(id, msg){
    if(id===undefined || id===null || id<1){
        throw msg;
    }
}
//验证是否手机号
function isMobile (val) {
    return /^1[34578]\d{9}$/.test(val);
}
function repSpace(val){
    return val.replace(/\s/g, '');
}

function createWishListParam(){
    var vin = handleVin($('#vinInput').val());
    if(''==vin){
        throw '请填写车架号/VIN码';
    }
    var brandId = $('#carBrand').val();
    checkId(brandId, '请选择车品牌');

    var seriesId = $('#carSeries').val();
    checkId(seriesId, '请选择车系');

    var modelId = $('#carModel').val();
    checkId(modelId, '请选择车型');

    var carId = $('#carName').val();
    checkId(carId, '请选择排量年款');

    var len = _carList.length;
    var car;
    for(var i=0; i<len; i++){
        car = _carList[i];
        if(carId==car.id){
            break;
        }
    }
    if(car===undefined){
        throw '获取排量年款错误，请联系开发人员';
    }

    var shopTel = repSpace($('#shopTel').val());
    if(!isMobile(shopTel)){
        throw '请填写正确的联系电话';
    }
    var fillInPerson = $('#fillInPerson').val().trim();
    if(''==fillInPerson){
        throw '请填写填单人';
    }
    var fillInPersonTel = repSpace($('#fillInPersonTel').val());
    if(!isMobile(fillInPersonTel)){
        throw '请填写正确的填单人电话';
    }

    return {
        vin: vin,
        isDeckVehicle: 0,
        isModifiedVehicle: $('#isModifyCar').is(':checked')?1:0,
        isReceiptPrice: $('#invoiceType').val(),
        brand: brandId,
        series: seriesId,
        model: modelId,
        engine: car.powerId,
        year: car.pid,
        tqCarModel: carId,
        //companyName: $('#shopName').val(),
        telephone: shopTel,
        wishListMemo: $('#orderRemark').val().trim(),
        wishListMaker: fillInPerson,
        wishListMakerTel: fillInPersonTel,
        goodsList: getGoodsParam(),
        token: $('#token').val(),
        avidCallId: $('#avidCallId').val()
    };
}

/** 组装商品信息 */
function getGoodsParam(){
    var goodsRowList = $('.goods-row');
    var len = goodsRowList.length;
    if(len==0){
        throw '至少有一个商品';
    }
    var goodsParam = [];
    for(var i=0; i<len; i++){
        var goodsRow = goodsRowList.eq(i);

        var goodsName = goodsRow.find('input[name="goodsName"]').val().trim();
        var goodsOe = goodsRow.find('input[name="goodsOe"]').val().trim();
        var goodsNumber = parseInt(goodsRow.find('input[name="goodsNumber"]').val());
        var goodsUnit = goodsRow.find('input[name="goodsUnit"]').val().trim();
        var goodsRemark = goodsRow.find('input[name="goodsRemark"]').val().trim();

        if(''==goodsName){
            throw '请填写商品名称';
        }
        if(isNaN(goodsNumber) || goodsNumber<1){
            throw '请填写正确的商品数量';
        }

        var selectList = goodsRow.find('select');
        var goodsQuality = selectList.eq(0).val();
        var backQuality = selectList.eq(1).val();
        checkId(goodsQuality, '请选择首选品质');
        if(goodsQuality===backQuality){
            throw '备选品质不能和首选品质一样';
        }
        if(backQuality > 0){
            goodsQuality = goodsQuality+','+backQuality;
        }

        var goods = {
            goodsName: goodsName,
            qualityTypeStr: goodsQuality,
            goodsOe: goodsOe,
            goodsNumber: goodsNumber,
            goodsMeasureUnit: goodsUnit,
            goodsMemo: goodsRemark
        };
        goodsParam.push(goods);
    }

    return goodsParam;
}

/** 转需求单 */
function turnWishList(){
    App.blockUI(body);

    var reqParam;
    try{
        reqParam = createWishListParam();
    }catch(message){
        LayerUtil.msg(message);

        App.unblockUI(body);
        return;
    }
    //console.log(reqParam);
    Ajax.postJson({
        url: '/monkey/avidCall/turnWish',
        data: JSON.stringify(reqParam),
        success: function(result){
            //console.log(result);
            if(result.success){
                LayerUtil.msgFun('转需求单成功', goToListPage);
            }else{
                LayerUtil.msg(result.message);
            }

            App.unblockUI(body);
        }
    });
}

/** 门店取消 */
function shopCancel(){
    var modal = $('#confirmModal');
    modal.modal('show');
    $('#confirmModalContent').html(template('cancelTemplate'));
    $('#confirmModalOkBtn').removeClass('hidden').unbind().click(function(){
        var input = $(':radio:checked');
        if(input.length==0){
            LayerUtil.msg('请选择原因');
            return;
        }
        var reason = '其他';
        if(input.hasClass('other-reason-radio')){
            reason = $('.other-reason').val().trim();
            if(''==reason){
                LayerUtil.msg('请填写其他原因');
                return;
            }
        }else{
            reason = input.next().text();
        }

        layer.confirm('确认要取消吗？', {icon: 3, title: '提示'}, function(index){
            layer.close(index);

            App.blockUI(modal);

            Ajax.post({
                url: '/monkey/avidCall/cancelAvidCall',
                data: {
                    id: $('#avidCallId').val(),
                    reason: reason
                },
                success: function(result){
                    if(result.success){
                        LayerUtil.msgFun('取消成功', goToListPage);
                    }else{
                        LayerUtil.msg(result.message);
                    }

                    App.unblockUI(modal);
                }
            });
        });

    });

}

/** 临时保存 */
function handleId(id){
    if(id===undefined || id===null || id<0){
        return 0;
    }
    return id;
}
function getSaveParam(){
    var vin = handleVin($('#vinInput').val());

    var brandId = $('#carBrand').val();
    checkId(brandId, '请选择车品牌');

    var seriesId = $('#carSeries').val();
    checkId(seriesId, '请选择车系');

    var modelId = handleId($('#carModel').val());
    var carId = handleId($('#carName').val());

    var shopTel = repSpace($('#shopTel').val());
    var fillInPerson = $('#fillInPerson').val().trim();
    var fillInPersonTel = repSpace($('#fillInPersonTel').val());

    return {
        id: $('#avidCallId').val(),
        carVin: vin,
        isModifyCar: $('#isModifyCar').is(':checked')?1:0,
        invoiceType: $('#invoiceType').val(),
        carBrandId: brandId,
        carSeriesId: seriesId,
        carModelId: modelId,
        carId: carId,
        shopTel: shopTel,
        fillInPerson: fillInPerson,
        fillInPersonTel: fillInPersonTel,
        orderRemark: $('#orderRemark').val().trim(),
        goodsParamList: getSaveGoodsParam()
    };
}
/* 组装商品信息 */
function getSaveGoodsParam(){
    var goodsRowList = $('.goods-row');
    var len = goodsRowList.length;
    if(len==0){
        return null;
    }
    var goodsParam = [];
    for(var i=0; i<len; i++){
        var goodsRow = goodsRowList.eq(i);
        var goods = getGoods(goodsRow);
        if(goods!=null){
            goodsParam.push(goods);
        }
    }

    return goodsParam;
}
/* 组装商品信息 */
function getGoods(goodsRow){
    var flag = false;
    var goodsName = goodsRow.find('input[name="goodsName"]').val().trim();
    var goodsOe = goodsRow.find('input[name="goodsOe"]').val().trim();
    var goodsNumber = parseInt(goodsRow.find('input[name="goodsNumber"]').val());
    var goodsUnit = goodsRow.find('input[name="goodsUnit"]').val().trim();
    var goodsRemark = goodsRow.find('input[name="goodsRemark"]').val().trim();

    var goods = {};
    if(''!=goodsName || ''!=goodsOe || ''!=goodsUnit || ''!=goodsRemark){
        flag = true;
        goods['goodsName'] = goodsName;
        goods['goodsOe'] = goodsOe;
        goods['goodsUnit'] = goodsUnit;
        goods['goodsRemark'] = goodsRemark;
    }

    var selectList = goodsRow.find('select');
    var goodsQuality = selectList.eq(0).val();
    var backQuality = selectList.eq(1).val();
    if(goodsQuality>0 || backQuality>0){
        flag = true;
        goods['goodsQualityId'] = goodsQuality;
        goods['backQualityId'] = backQuality;
    }

    if(flag){
        if(isNaN(goodsNumber) || goodsNumber<1){
            throw '请填写正确的商品数量';
        }
        goods['goodsNumber'] = goodsNumber;

        var goodsId = goodsRow.find('.hiddenGoodsId').val();
        //console.log('商品id：'+goodsId);
        if(goodsId!==undefined && goodsId!=''){
            goods['id'] = goodsId;
        }

        return goods;
    }

    return null;
}

/** 临时保存 */
function tempSave(){

    App.blockUI(body);

    var reqParam;
    try{
        reqParam = getSaveParam();
    }catch(message){
        LayerUtil.msg(message);

        App.unblockUI(body);
        return;
    }
    //console.log(reqParam);
    Ajax.postJson({
        url: '/monkey/avidCall/modifyAvidCall',
        data: JSON.stringify(reqParam),
        success: function(result){
            //console.log(result);
            if(result.success){
                LayerUtil.msgFun('保存成功', goToListPage);
            }else{
                LayerUtil.msg(result.message);
            }

            App.unblockUI(body);
        }
    });
}
