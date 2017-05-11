var pageSize = 10;
//全局h5 session缓存
var storage = window.sessionStorage;//localStorage;
var all_brand_key = 'all_brand';

//搜索的全局数据
var brandMap;
var partMap;
var brandPartList ;
var partBrandList ;

$(document).ready(function(){
    var loadingEL = $('body');
    App.blockUI(loadingEL);

    //初始化搜索
    initSearch();
    //初始化分页
    initShowPage();
    //初始化按钮
    initButton();

    getGoodsData(1,null);


    App.unblockUI(loadingEL);
});

function initSearch(){
    //暂时不用缓存
    brandMap = JSON.parse(storage.getItem("commodity_show_brandMap"));
    partMap = JSON.parse(storage.getItem("commodity_show_partMap"));
    brandPartList = JSON.parse(storage.getItem("commodity_show_brandPartList"));
    partBrandList = JSON.parse(storage.getItem("commodity_show_partBrandList"));

    if(brandMap == undefined) {
        $.ajax({
            type: "GET",
            url: "/monkey/commodity/show/brandPartMap",
            async: false,//同步
            success: function (resultMap) {
                brandMap = resultMap.brandNameMap;
                partMap = resultMap.partNameMap;
                brandPartList = resultMap.brandPartMap;
                partBrandList = resultMap.partBrandMap;
                //
                storage.setItem("commodity_show_brandMap", JSON.stringify(brandMap));
                storage.setItem("commodity_show_partMap", JSON.stringify(partMap));
                storage.setItem("commodity_show_brandPartList", JSON.stringify(brandPartList));
                storage.setItem("commodity_show_partBrandList", JSON.stringify(partBrandList));
            }
        });
    }
    //形成brand select2
    var brandhtml = '<option value="-1">-品牌-</option>';
    $.each(brandMap, function (key, value) {
        brandhtml += '<option value="' + key + '">' + value + '</option>';
        $('#search_brand').html(brandhtml);
        $('#search_brand').select2();

        search_change()
    });
    //形成part select2
    var parthtml = '<option value="-1">-标准零件名称-</option>';
    $.each(partMap, function (key, value) {
        parthtml += '<option value="' + key + '">' + value + '</option>';
        $('#search_part').html(parthtml);
        $('#search_part').select2();

        search_change()
    });

    $('#search_brand').select2();
    $('#search_part').select2();

    $('#search_format').bind(function () {
       var val = $(this).val().trim();
        $(this).val(val);
    });
    //绑定回车事件
    $('#search_format').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            $('#sure_search_button').trigger('click');
            return false;
        }
    });
    $('#liyang_car_model_search').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            initLiyangCar();
            return false;
        }
    });




}
//初始化 button
function initButton(){
    $('#sure_search_button').click(function(){
        $("#result_table_page_index").val("0");
        var pageIndex = 1;
        getGoodsData(pageIndex,null);

    });

    $('#add_data_button').click(function(){
        var map = {};
        var html = Mustache.render($('#edit_data_template').html(), map);

        $("#edit_model_body").html(html);
        $("#edit_model_title").html("新增商品");
        initEditModal();
        $('#edit_model').modal('show');
    });

    //导出downLoad
    $('#downLoad_goods_button').click(function () {
        var partValue = $('#search_part').val();
        var brandValue = $('#search_brand').val();
        if("-1" == partValue){
            alert_fuc("没有选择标准零件名称，无法开始导出");
            return false;
        }

        var partText = $("#search_part  option:selected").text();
        var brandText = $("#search_brand  option:selected").text();
        if("-1" == brandValue){
            brandText = "所有商品品牌";
        }
        var confirmString = "选择的标准零件名称："+partText+" <br>";
        confirmString += "选择的品牌:"+brandText+"。<br>" +
            " 确定导出商品excle？";
        confirm_fuc(confirmString,downLoadGoodsExcle);
    });

    $('#downLoad_car_button').click(function () {
        var partValue = $('#search_part').val();
        var brandValue = $('#search_brand').val();
        if("-1" == partValue){
            alert_fuc("没有选择标准零件名称，无法开始导出");
            return false;
        }

        var partText = $("#search_part  option:selected").text();
        var brandText = $("#search_brand  option:selected").text();
        if("-1" == brandValue){
            brandText = "所有商品品牌";
        }
        var confirmString = "选择的标准零件名称："+partText+" <br>";
        confirmString += "选择的品牌:"+brandText+"。<br>" +
            " 确定导出对应的车型TXT？";
        confirm_fuc(confirmString,downLoadCarExcle);
    });
    
    //===============attr属性按钮
    $('#add_attr_button').click(function(){
        var html = Mustache.render($('#attr_add_template').html(), {});
        $('#add_attr_body').append(html);
        return false;
    });


    //保存属性
    //$('#save_attr_btn').click(function(){
    //    confirm_fuc("是否保存目前的属性操作？",save_attr)
    //    return false;
    //});



    //车型添加按钮
    //$('#add_liyang_btn').click(function(){
    //    var liyangId = $('#liyang_car_model_search').val().trim();
    //    if(liyangId != ""){
    //        confirm_fuc("建议:查找下此力洋Id是否存在<br>是否保存"+liyangId+"？",save_liyang);
    //
    //    }
    //    return false;
    //});


    //编辑页面弹出框，隐藏
    //$('#edit_model_sure_button').click(function(){
    //    var id = $('#edit_model_id').val().trim();
    //    if(id == undefined || id == ""){
    //        id = 0;
    //    }
    //
    //    var is_ok = validFunc();
    //    if(!is_ok){
    //        return false;
    //    }
    //    var part_all_name = $('#edit_model_part').val().split("-");
    //    var map = {
    //        'id':id,
    //        'uuId':$('#edit_model_uuId').val(),
    //        'goodsName':$('#edit_model_goodsName').val(),
    //        'goodsCode':$('#edit_model_code').val(),
    //        'goodsFormat':$('#edit_model_format').val(),
    //        'guaranteeTime':$('#edit_model_time').val(),
    //        'saleUnit':$('#edit_model_sale_unit').val(),
    //        'partSumCode':part_all_name[0],
    //        'partName':part_all_name[1],
    //        'partId':$('#edit_model_part_id').val(),
    //        'goodsQualityType':$('#edit_model_quality').val(),
    //        'cateKind':$('#edit_model_cate').val(),
    //        'brandId':$('#edit_model_brand').val(),
    //        'brandName':$('#edit_model_brand').find("option:selected").text(),
    //        'remark':$('#edit_model_remark').val()
    //    };
    //    var oes = "";
    //    var oeArray = $('#edit_model_oe_body').find('.edit_model_oe');
    //    for(var i=0;i<oeArray.length;i++){
    //        var oe = $(oeArray[i]).find('input').val().trim();
    //        if(oe != ""){
    //            oes += " "+oe;
    //        }
    //    }
    //
    //    $.post("/monkey/commodity/show/saveGoods",map,function(result){
    //        var success = result.success;
    //        if(success){
    //            var goodsUuId = result.data;
    //            //存oe码
    //            $.post("/monkey/commodity/show/saveOe",{oes:oes,goodsUuId:goodsUuId},function(oeResult){
    //                if(oeResult.success){
    //                    alert_fuc("成功");
    //                    $('#edit_model').modal('hide');
    //                }else{
    //                    alert_fuc("基础数据 保存成功，oe码保存失败");
    //                    $('#edit_model').modal('hide');
    //                }
    //                $('#sure_search_button').trigger('click');
    //
    //            });
    //
    //        }else{
    //            alert_fuc("失败"+result.message);
    //        }
    //    })
    //})
}
//保存信息判断
function validFunc(){
    if($('#edit_model_goodsName').val().trim() == ""){
        alert_fuc("商品名称不能为空");
        return false;
    }
    if($('#edit_model_code').val().trim() == ""){
        alert_fuc("商品编码不能为空");
        return false;
    }
    if($('#edit_model_format').val().trim() == ""){
        alert_fuc("规格型号不能为空");
        return false;
    }
    if($('#edit_model_brand').val().trim() == "-1"){
        alert_fuc("请选择品牌");
        return false;
    }
    if($('#edit_model_part').val().trim() == ""){
        alert_fuc("标准零件不能为空");
        return false;
    }
    var part_class = $('#edit_model_part').parent().parent();
    if($(part_class).hasClass('has-error') || $(part_class).hasClass('has-warning')){
        alert_fuc("标准零件错误");
        return false;
    }

    return true;
}

//=====结束初始化button===

//导出商品excle
function downLoadGoodsExcle(){

    var partValue = $('#search_part').val();
    var brandValue = $('#search_brand').val();
    var partText = $("#search_part  option:selected").text();
    var brandText = $("#search_brand  option:selected").text();
    if("-1" == brandValue){
        brandText = "所有商品品牌";
    }

    var url = "/monkey/commodity/show/exportGoodsExcelForDianShang?";
    url += "partId="+partValue;
    url += "&&partText="+partText;
    url += "&&brandId="+brandValue;
    url += "&&brandText="+brandText;


    window.location.href = url;

    showProgress();

    return false;
}

// 保存商品属性
function save_attr(){
    var goodsUuId = $('#liyang_car_model_uuId').val();
    var attr_keys = $('.attr_key');

    // 删除attr
    var delete_attr_array = $('.attr_show.active');
    var delete_value = "";
    for(var i = 0;i<delete_attr_array.length;i++) {
        var value = $(delete_attr_array[i]).text().trim();
        delete_value += value+" ";
    }
    $.post("/monkey/commodity/show/deleteAttr",{delete_value:delete_value.trim(),goodsUuId:goodsUuId},function(data){
        if(data.success){
            for(var i = 0;i<attr_keys.length;i++){
                var key = $(attr_keys[i]).val().trim();
                if("" == key){
                    continue;
                }
                var value = $(attr_keys[i]).siblings('input').val().trim();

                //保存
                $.post("/monkey/commodity/show/saveAttr",{attrKey:key,attrValue:value,goodsUuId:goodsUuId})
            }
            alert_fuc("保存成功");
        }else{
            alert_fuc("保存失败"+data.message);
        }
    });


    $('#attr_model').modal("hide");
    return false;
}

//保存力洋Id
function save_liyang(){
    var liyangId = $('#liyang_car_model_search').val().trim();
    var goodsUuId = $('#liyang_car_model_uuId').val();

    $.post("/monkey/commodity/show/saveLiyang",{liyangId:liyangId,goodsUuId:goodsUuId},function(data){
        if(data.success) {
            initLiyangCar();

            alert_fuc("保存成功");
        }else{
            alert_fuc("保存失败，原因："+data.message);
        }
    })
}

//删除力洋Id

function delete_liyang(){
    var liyangId = $('#liyang_car_model_del_liyangId').val();
    var goodsUuId = $('#liyang_car_model_uuId').val();

    $.post("/monkey/commodity/show/deleteLiyang",{liyangId:liyangId,goodsUuId:goodsUuId},function(data){
        if(data.success) {
            initLiyangCar();

            alert_fuc("删除成功");
        }else{
            alert_fuc("删除失败，原因："+data.message);
        }
    })
}

//导出车型excle
function downLoadCarExcle(){

    var partValue = $('#search_part').val();
    var brandValue = $('#search_brand').val();
    var partText = $("#search_part  option:selected").text();
    var brandText = $("#search_brand  option:selected").text();
    if("-1" == brandValue){
        brandText = "所有商品品牌";
    }

    var url = "/monkey/commodity/show/exportCarExcelForDianShang?";
    url += "partId="+partValue;
    url += "&&partText="+partText;
    url += "&&brandId="+brandValue;
    url += "&&brandText="+brandText;

    window.location.href = url;

    showProgress();

    return false;
}


function showProgress(){
    //定时更新进度条
    $("#upload_progress").css("width", "0%");//将进度条设置为0
    $('#upload_progress_span').html("0%");
    $('#progress_model').modal('show');
    progressTime =window.setInterval(function(){
        $.ajax({
            url:"/monkey/commodity/show/getProgress?time="+new Date().getTime(),
            dataTypeString:"json",
            success:function(data){
                if(data.progress == undefined || data.progress == '-1'){
                    $("#upload_progress").css("width", "0%");//将进度条设置为0
                    $('#upload_progress_span').html("0%");
                    clearInterval(progressTime);

                    $('#progress_model').modal('hide');
                    alert_fuc("下载失败，请刷新后重新下载！");

                    //return false;
                }else if(data.progress =='100%') {
                    $("#upload_progress").css("width", "100%");
                    $('#upload_progress_span').html("100%");
                    clearInterval(progressTime);
                    $('#progress_model').modal('hide');
                    alert_fuc("下载成功~！");

                }else{
                    var progress = data.progress;
                    $("#upload_progress").css("width", progress);//将进度条设置为0

                    var html = progress;
                    if(progress == "98.0%"){
                        html="数据量过多，正在后台处理";
                    }
                    $('#upload_progress_span').html(html);
                }
            }
        });

    },1000);

    return false;
}

//=========================end download================

function search_change(){
    $('#search_brand').unbind('change').change(function(){
        var brandValue = $(this).val();
        var partValue = $('#search_part').val();

        if(brandValue != '-1'){
            var map = {
                list:brandPartList[brandValue],
                placeholder:'-标准零件名称-'
            };
            var html = Mustache.render($('#search_select_template').html(), map);
            $('#search_part').html(html);
            if(partValue != undefined){
                $('#search_part').val(partValue);
            }
            $('#search_part').select2();

        }else{
            var parthtml = '<option value="-1">-标准零件名称-</option>';
            $.each(partMap,function(key,value){
                parthtml += '<option value="'+key+'">'+value+'</option>';
                $('#search_part').html(parthtml);
                if(partValue != undefined){
                    $('#search_part').val(partValue);
                }
                $('#search_part').select2();
            });
        }
    });

    $('#search_part').unbind('change').change(function(){
        var partValue = $(this).val();
        var brandValue = $('#search_brand').val();

        if(partValue == '-1') {
            var brandhtml = '<option value="-1">-品牌-</option>';
            $.each(brandMap, function (key, value) {
                brandhtml += '<option value="' + key + '">' + value + '</option>';
                $('#search_brand').html(brandhtml);
                if (brandValue != undefined) {
                    $('#search_brand').val(brandValue);
                }
                $('#search_brand').select2();
            });
        }else{
            var map = {
                list:partBrandList[partValue],
                placeholder:'-品牌-'
            };
            var html = Mustache.render($('#search_select_template').html(), map);
            $('#search_brand').html(html);
            if(brandValue != undefined){
                $('#search_brand').val(brandValue);
            }
            $('#search_brand').select2();
        }
    });

}

//获得goods主体数据
function getGoodsData(pageIndex,size){
    if(size == undefined){
        size = pageSize;
    }
    if(pageIndex == undefined){
        pageIndex = 1;
    }

    var result_table_page_index = $("#result_table_page_index").val();
    if(Number(result_table_page_index) != Number(pageIndex)) {
        var loadingEL = $('body');
        App.blockUI(loadingEL);

        var params = {};
        var brandId = $('#search_brand').val();
        var partId = $('#search_part').val();
        var format = $('#search_format').val().trim();
        if(brandId != "-1"){
            params['brandId'] = brandId;
        }
        if(partId != "-1"){
            params['partId'] = partId;
        }
        params['format'] = format;
        params['index'] = pageIndex;
        params['pageSize'] = size;

        $.get("/monkey/commodity/show/getGoodsPage",params,function(resultVO){
            App.unblockUI(loadingEL);

            if(resultVO.success) {
                var result_map = resultVO.data;
                var totleNumber = result_map.totalNumber;

                if (Number(totleNumber) == 0) {
                    alert_fuc("此搜索条件下无数据！");
                    $('#result_table_tbody').html("无数据");
                    return false;
                }

                var index = 0;
                var map = {
                    goodsList:result_map.goodsList,
                    indexs:function(){
                        index ++;
                        return index;
                    },
                    qualityName:function(){
                        var quality = Number(this.goodsQualityType);
                        if(0 == quality){
                            return "品牌件";
                        }else  if(1 == quality){
                            return "正厂原厂";
                        }else  if(2 == quality){
                            return "正厂配套";
                        }else  if(3 == quality){
                            return "正厂下线";
                        }else  if(4 == quality){
                            return "全新拆车";
                        }else  if(5 == quality){
                            return "旧件拆车";
                        }else  if(6 == quality){
                            return "副厂";
                        }else  if(9 == quality){
                            return "高仿";
                        }else {
                            return "不识别";
                        }
                    },
                    cateKindName:function(){
                        var cateKind = Number(this.cateKind);
                        if(cateKind == 0){
                            return "易损件";
                        }else{
                            return "全车件";
                        }
                    },
                    saleStatusName:function(){
                        var saleStatus = Number(this.saleStatus);
                        if(saleStatus == 0){
                            return '<span class="label label-success">预售</span>';
                        }else if(saleStatus == 1){
                            return '<span class="label label-danger">在售</span>';
                        }else{
                            return '<span class="label label-default">停售</span>';

                        }
                    },
                    remarkRender:function(){
                        var remark = this.remark;
                        var html = '<div class="remark_body hidden"><span>'+remark+'</span></div>';
                        if("" != remark){
                            html = '<span class="remark_btn btn red">more</span>'+html;
                        }
                        return html;
                    }
                }
                var html = Mustache.render($('#result_data_template').html(), map);
                $('#result_table_tbody').html(html);

                $("#result_table_page_index").val(pageIndex);

                updateShowPage(totleNumber, result_map.totalPages, pageIndex,"main_pagination");//更新分页组件

                main_table_btn();
            }else{
                alert_fuc(resultVO.message);
                $('#search_result_body').addClass("hidden");
                $('#liyangExt').addClass("hidden");

                return;
            }

        });
    }
}

//主页面的表格按钮点击事件
function main_table_btn(){
    $('.see_liyang_car_btn').unbind("click").click(function(){
        var uuId = $(this).siblings(".uuId").val();
        $('#liyang_car_model_uuId').val(uuId);

        $('#liyang_car_model_search').val('');

        //获得数据
        initLiyangCar();

    });

    $('.see_attr_btn').unbind("click").click(function(){
        var uuId = $(this).siblings(".uuId").val();
        $('#liyang_car_model_uuId').val(uuId);

        getAttrData(uuId);
    });
    //$('.see_oe_btn').unbind("click").click(function(){
    //    var goodsCode = $(this).siblings(".goodsCode").val();
    //    $('#liyang_car_model_uuId').val(goodsCode);
    //
    //    getOEData(goodsCode);
    //});
    //查看备注
    $(".remark_btn").unbind("click").click(function(){
        $(this).siblings("div").toggleClass("hidden");
    });
    //编辑

    $('.edit_btn').unbind("click").click(function(){
        //基本属性
        var parents = $(this).parent().parent();
        var goodsName =parents.find(".goodsName").text().trim();
        var map = {
            "edit_model_id":parents.find(".id").val().trim(),
            "edit_model_uuId":$(this).siblings(".uuId").val(),
            "edit_model_goodsName":goodsName,
            "edit_model_code":parents.find(".goodsCode").text().trim(),
            "edit_model_format":parents.find(".goodsFormat").text().trim(),
            "edit_model_time":parents.find(".guaranteeTime").val().trim(),
            "edit_model_sale_unit":parents.find(".saleUnit").text().trim(),
            "edit_model_part":parents.find(".partSumCode").val().trim()+"-"+parents.find(".partName").text().trim(),
            "edit_model_part_id":parents.find(".partId").val().trim(),
            "edit_model_remark":parents.find(".remark").val().trim()
        };
        var html = Mustache.render($('#edit_data_template').html(), map);

        $("#edit_model_body").html(html);
        $("#edit_model_title").html("编辑商品-"+goodsName);

        initEditModal(parents.find(".goodsQualityType").val().trim(),parents.find(".cateKind").val().trim(),parents.find(".brandId").val().trim());
        $('#edit_model').modal('show');
        //oe
        $.get("/monkey/commodity/show/searchOe", {uuId:$(this).siblings(".uuId").val()},function(result) {
            var success = result.success;
            if (success) {
                var list = result.data;
                for(var i =0;i<list.length;i++){
                    var html = Mustache.render($('#model_oe_template').html(),{"oe_number":list[i].oeNumber});
                    $('#edit_model_oe_body').append(html);
                }
            }
        });

    });
}

//TODO 获得oe码
function getOEData(uuId){
    $.get("/monkey/commodity/show/searchOe", {uuId:uuId},function(result){
        var success = result.success;
        if(success){
            var list = result.data;
            var length = list.length;

            var showList = new Array();
            for(var i=0;i<length; i=i+4){
                var map = {};
                map['one'] = list[i].oeNumber;
                map['two'] = list[i].oeNumber;
                map['three'] = list[i].oeNumber;
                map['four'] = list[i].oeNumber;
                map['firstValue'] = list[i].attrValue;

                if(i+1 < length) {
                    map['secondKey'] = list[i + 1].attrName+":";
                    map['secondValue'] = list[i + 1].attrValue;
                }else{
                    map['secondKey'] = "";
                    map['secondValue'] = "";
                }
                showList.push(map);
            }

            var html = Mustache.render($('#attr_show_template').html(), {list:showList});
            $('#attr_model_body').html(html);

            $('#attr_model').modal("show");
        }else{
            alert_fuc("该商品无OE码信息");
            $('#attr_model').modal("hide");
        }
    });
}

//属性数据
function getAttrData(uuId){
    $.get("/monkey/commodity/show/searchAttr", {uuId:uuId},function(result){
        var success = result.success;
        if(success){
            var list = result.data;
            var length = list.length;

            var allList = new Array();
            var colList = new Array();
            var colSize = 4;
            for(var i=0;i<length;i++){
                var map = {};
                map['firstKey'] = list[i].attrName;
                map['firstValue'] = list[i].attrValue;

                colList.push(map);
                if(colList.length == colSize || i == length-1){
                    allList.push({colList:colList});
                    colList = new Array();
                    continue;
                }
            }

            var html = Mustache.render($('#attr_show_template').html(), {list:allList});
            $('#attr_model_body').html(html);
            $('#add_attr_body').html('');

            $('#attr_model').modal("show");
        }else{
            var html = Mustache.render($('#attr_add_template').html(), {});
            $('#attr_model_body').html('');
            $('#add_attr_body').html(html);
            $('#attr_model').modal("show");
        }
        $('.attr_show').bind('click').click(function(){
           $(this).toggleClass("active");
        });
    });
}

/*======================弹出层 table---车型数据=======================*/
function getLiyangData(pageIndex,size){

    if(size == undefined){
        size = $('#liyang_car_model_pageSize').val();
    }

    var result_table_page_index = $("#liyang_car_model_page_index").val();
    if(Number(result_table_page_index) != Number(pageIndex)){
        var loadingEL = $('#liyang_car_model');
        App.blockUI(loadingEL);

        var params = {};
        params['uuId'] = $('#liyang_car_model_uuId').val();
        params['searchLiyang'] = $('#liyang_car_model_search').val().trim();
        params['index'] = pageIndex;
        params['pageSize'] = size;
        $.get("/monkey/commodity/show/searchLiyangCar", params,
            function (resultVO) {
                console.log(resultVO);

                App.unblockUI(loadingEL);
                if(resultVO.success) {
                    var result_map = resultVO.data;

                    if (result_map.resultDataList.length == 0) {
                        $('#liyang_car_model_table_tbody').html("此配件无对应的力洋车型!");
                        $("#liyang_car_model_page_index").val('1');
                        updateShowPage(result_map.totalNumber, result_map.totalPages, pageIndex,"liyang_car_model_pagination");//更新分页组件
                        $("#liyang_car_model").modal("show");
                        return;
                    }

                    var html = Mustache.render($('#liyang_car_data_template').html(), result_map);
                    $('#liyang_car_model_table_tbody').html(html);

                    $("#liyang_car_model_page_index").val(pageIndex);

                    updateShowPage(result_map.totalNumber, result_map.totalPages, pageIndex,"liyang_car_model_pagination");//更新分页组件

                    $('.del_liyang_car_btn').unbind('click').click(function(){
                        var del_liyangId = $(this).parent().parent().find('.leyelId').html().trim();
                        $('#liyang_car_model_del_liyangId').val(del_liyangId);
                        confirm_fuc("是否确认删除："+del_liyangId+" 与该商品的对应关系？",delete_liyang);
                        return false;
                    });
                    //弹出层出现
                    $("#liyang_car_model").modal("show");
                }else{
                    alert_fuc(resultVO.message);
                    return;
                }
            });
    }
}
//=========自定义初始化
function initLiyangCar(){
    $("#liyang_car_model_page_index").val("0");
    var pageIndex = 1;
    var pageSize = 8;
    $('#liyang_car_model_pageSize').val(pageSize);

    getLiyangData(pageIndex, pageSize);
}
//=====================弹出层编辑页面事件=====================
function initEditModal(quality_value,cate_value,brand_value){
    //$('#add_atrr_button').bind('click').click(function(){
    //    var html = Mustache.render($('#model_attr_template').html(),{});
    //    $('#edit_model_attr_body').prepend(html);
    //
    //    return false;
    //});
    $('#edit_model_format,#edit_model_code').keyup(function(){
        //中文都为空格
        var value = $(this).val().replace(/[\u4E00-\u9FA5]/g, '').replace(" ","");
        $(this).val(value);
    });

    $('#add_oe_button').bind('click').click(function(){
        var html = Mustache.render($('#model_oe_template').html(),{});
        $('#edit_model_oe_body').prepend(html);

        $('.edit_model_oe input').bind('keyup').keyup(function(){
            //中文都为空格
            var value = $(this).val().replace(/[\u4E00-\u9FA5]/g, '').replace(" ","");
            $(this).val(value);
        });

        return false;
    });

    if(quality_value == undefined){
        quality_value = 0;
    }
    if(cate_value == undefined){
        cate_value = 1;
    }
    $('#edit_model_quality').val(quality_value).select2();
    $('#edit_model_cate').val(cate_value).select2();

    //获得品牌数据
    var brand_list = JSON.parse(storage.getItem(all_brand_key));
    if(brand_list == undefined) {
        $.get("/monkey/commodity/show/getGoodsBrand", function (result) {

            var success = result.success;
            if(success) {
                var list = result.data;
                var map = {
                    'placeholder':'请选择品牌',
                    'list':list,
                    'brandName':function(){
                        var nameCh = this.nameCh;
                        var nameEn = this.nameEn;
                        if(nameCh == ""){
                            return nameEn;
                        }
                        if(nameEn == ""){
                            return nameCh;
                        }
                        return nameCh+"/"+nameEn;
                    }
                };
                var html = Mustache.render($('#all_brand_select_template').html(), map);
                $('#edit_model_brand').html(html);


                if(brand_value != undefined){
                    $('#edit_model_brand').val(brand_value);
                }
                $('#edit_model_brand').select2();

                storage.setItem(all_brand_key, JSON.stringify(list));
            }
        });
    }else{
        var map = {
            'placeholder':'请选择品牌',
            'list':brand_list,
            'brandName':function(){
                var nameCh = this.nameCh;
                var nameEn = this.nameEn;
                if(nameCh == ""){
                    return nameEn;
                }
                if(nameEn == ""){
                    return nameCh;
                }
                return nameCh+"/"+nameEn;
            }
        };
        var html = Mustache.render($('#all_brand_select_template').html(), map);
        $('#edit_model_brand').html(html);

        if(brand_value != undefined){
            $('#edit_model_brand').val(brand_value);
        }
        $('#edit_model_brand').select2();
    }

    //标准零件筛选事件
    $("#edit_model_part").autocomplete({
        source: function( request, response ) {
            var searchPart = $("#edit_model_part").val().trim();
            if(searchPart.indexOf("-") > -1){
                searchPart = searchPart.split("-")[0];
            }
            var key = "getPartByLike"+searchPart;
            var list = JSON.parse(storage.getItem(key));
            if ( list != undefined ) {
                autoSource(response,list);
                return;
            }
            $.ajax({
                url: "/monkey/goods/SearchPartByKey",
                dataType: "json",
                data:{
                    key: searchPart
                },
                success: function( data ) {
                    var success = data.success;
                    if(success) {
                        var list = data.data;
                        storage.setItem(key, JSON.stringify(list));
                        autoSource(response, list);
                    }
                }
            });
        },
        minLength: 1,
        autoFill: true,
        select: function( event, ui ) {
            var item = ui.item;
            var value = item.value;
            $("#edit_model_part").val(value);
            $("#edit_model_part_id").val(item.id);

            var parent = $("#edit_model_part").parent().parent();

            parent.removeClass("has-error");
            parent.removeClass("has-warning");
            parent.addClass("has-success");
            var map = {
                tips:"fa fa-check",
                content:"成功"
            };
            var html = Mustache.render($('#tips_template').html(), map);
            parent.find("i").replaceWith(html);
        }
    });
}

//autoComplete 显示
function autoSource(response,list){
    var parent = $("#edit_model_part").parent().parent();
    if(list.length == 0){
        parent.removeClass("has-success");
        parent.removeClass("has-warning");
        parent.addClass("has-error");
        var map = {
            tips:"fa fa-times",
            content:"不存在数据"
        };
        var html = Mustache.render($('#tips_template').html(), map);
        parent.find("i").replaceWith(html);
    }else{
        parent.removeClass("has-error");
        parent.removeClass("has-success");
        parent.addClass("has-warning");
        var map = {
            tips:"fa fa-spinner",
            content:"正在搜索"
        };
        var html = Mustache.render($('#tips_template').html(), map);
        parent.find("i").replaceWith(html);
    }
    response(
        $.map( list, function( value ) {
            return {
                value: value.sumCode + "-" + value.partName,
                id: value.id
            }
        })
    );
}
//=====================分页========================================
function initShowPage(){
    var string = "{current_page}/{max_page}";

    $('#main_pagination').jqPagination({
        max_page : 1,
        page_string : string,
        paged: function(page){
            getGoodsData(page);
            return false;
        }
    });

    $('#liyang_car_model_pagination').jqPagination({
        max_page : 1,
        page_string : string,
        paged: function(page){
            getLiyangData(page);
            return false;
        }
    });
}
//更新分页组件
function updateShowPage(rows, maxPage, page,paginationID){
    var str = "{current_page}/{max_page}";
    if(rows != null && rows !== undefined){
        str = str + "(共" + rows + "条)";
    }
    //alert(str)
    var jqPage = $('#'+paginationID).data('jqPagination');
    jqPage.options.page_string = str;

    if(maxPage<page){
        if(page!=1){
            page=page-1;
        }
        if((page-getCurrentPage(paginationID)) != 0){
            jqPage.setPage(page, true);//第二个参数必须是true，否则会去调用回调函数
        }
        if(maxPage==0){
            maxPage =1;
        }
        jqPage.setMaxPage(maxPage, true);//必须有一个得更新，否则更新不了 page_string
    }else{
        if((page-getCurrentPage(paginationID)) != 0){
            jqPage.setPage(page, true);//第二个参数必须是true，否则会去调用回调函数
        }
        jqPage.setMaxPage(maxPage, true);//必须有一个得更新，否则更新不了 page_string
    }
}

//分页---获得当前页
function getCurrentPage(paginationID){
    return $('#'+paginationID).jqPagination('option', 'current_page');
}



//======================辅助的alert
function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return;
}


function confirm_fuc(text,successFuction){
    $('#confirm_model_content').html(text);
    $('#confirm_model').modal('show');

    $('#confirm_model_sure_button').unbind('click').click(function(){
        successFuction();
        $('#confirm_model').modal('hide');
    });

    return;
}