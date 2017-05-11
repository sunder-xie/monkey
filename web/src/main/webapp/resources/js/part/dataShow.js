var loadingHtml = '<img src="../../../resources/assets/img/ajax-loading.gif" alt="" class="loading">';
var pageSize = 10;
//全局h5 session缓存
var storage = window.sessionStorage;//localStorage sessionStorage;
var storage_key = 'relation_car';
//力洋id列表
var AllliyangList = [];
//已导入的车型map
var relationCarMap = {};

//全局数据
var allDataMap = undefined;
//liyang车型筛选后的数据
var afterLiDataList = undefined;
//力洋车型和分类筛选后的数据
var searchResultDataList = undefined;

var search_brand = $('#search_brand');
var search_factory = $('#search_factory');
var search_series = $('#search_series');
var search_model = $('#search_model');

var liyangIDDiv_body = $('#liyangIDDiv_body');

$(document).ready(function () {
    var loadingEL = $('body');
    App.blockUI(loadingEL);

    //力洋库的车型数据展示
    liyangCar();
    //初始化分页组件
    initPartShowPage();

    App.unblockUI(loadingEL);

    getPartGoodsBaseOeNum()

});


//第一行=============力洋库的车型数据展示
function liyangCar() {
    var loadingEL = $('body');

    $('#search_result_body').addClass('hidden');
    search_factory.addClass("hidden");
    search_series.addClass("hidden");
    search_model.addClass("hidden");
    $('#part_sum_number').text("0");

    $('#liyangExt').addClass("hidden");
    $("#liyangIDDiv").addClass("hidden");
    liyangIDDiv_body.html("");

    App.blockUI(loadingEL);
    $.get("/monkey/part/dataShow/getCar", function (result) {
        if (result.success) {
            var brandMap = result.data;

            var brandList = [];
            $.each(brandMap, function (brandName, factoryMap) {
                var brandKey = "brand-" + brandName;
                relationCarMap[brandKey] = factoryMap;
                brandList.push(brandName);
            });
            var map = {
                list: brandList,
                placeholder: '-请选择品牌-'
            };

            var html = Mustache.render($('#car_select_template').html(), map);
            search_brand.html(html);
            $('#part_sum_number').text("0");

            search_brand.select2({
                placeholder: "-请选择品牌-",
                allowClear: true
            });
            App.unblockUI(loadingEL);

            showStatisticalData(brandMap);
        } else {
            alert_fuc(result.message);
        }

    });

    //品牌变更
    search_brand.change(function () {
        search_factory.addClass("hidden");
        search_series.addClass("hidden");
        search_model.addClass("hidden");
        $('#search_result_body').addClass("hidden");
        $('#part_sum_number').text("0");

        $('#liyangExt').addClass("hidden");
        $("#liyangIDDiv").addClass("hidden");
        liyangIDDiv_body.html("");

        var value = search_brand.val();
        if (value == '-1') {
            return;
        }
        var brandValue = search_brand.val();

        var brandKey = "brand-" + brandValue;
        var factoryMap = relationCarMap[brandKey];
        var factoryList = [];
        $.each(factoryMap, function (factoryName, seriesMap) {
            var factoryKey = "factory-" + brandValue + factoryName;
            relationCarMap[factoryKey] = seriesMap;
            factoryList.push(factoryName);
        });
        //factoryList.sort();
        var map = {
            list: factoryList,
            placeholder: '-请选择厂商-'
        };
        var html = Mustache.render($('#car_select_template').html(), map);
        search_factory.html(html);
        search_factory.removeClass("hidden");
        search_factory.select2({
            placeholder: "-请选择厂商-"
        });

    });

    //厂商变更
    search_factory.change(function () {
        search_series.addClass("hidden");
        search_model.addClass("hidden");
        $('#search_result_body').addClass("hidden");
        $('#part_sum_number').text("0");

        $('#liyangExt').addClass("hidden");
        $("#liyangIDDiv").addClass("hidden");
        liyangIDDiv_body.html("");

        var value = search_factory.val();
        if (value == '-1') {
            return;
        }
        var brandValue = search_brand.val();
        var factoryValue = search_factory.val();
        var factoryKey = "factory-" + brandValue + factoryValue;
        var seriesMap = relationCarMap[factoryKey];
        var seriesList = [];
        $.each(seriesMap, function (seriesName, modelList) {
            var seriesKey = "series-" + brandValue + factoryValue + seriesName;
            relationCarMap[seriesKey] = modelList;
            seriesList.push(seriesName);
        });
        //seriesList.sort();
        var map = {
            list: seriesList,
            placeholder: '-请选择车系-'
        };
        var html = Mustache.render($('#car_select_template').html(), map);
        search_series.html(html);
        search_series.removeClass("hidden");
        search_series.select2({
            placeholder: "-请选择车系-"
        });
    });

    //车系变更
    search_series.change(function () {
        search_model.addClass("hidden");
        $('#search_result_body').addClass("hidden");
        $('#part_sum_number').text("0");

        $('#liyangExt').addClass("hidden");
        $("#liyangIDDiv").addClass("hidden");
        liyangIDDiv_body.html("");
        var value = search_series.val();
        if (value == '-1') {
            return;
        }
        var brandValue = search_brand.val();
        var factoryValue = search_factory.val();
        var seriesValue = search_series.val();
        var seriesKey = "series-" + brandValue + factoryValue + seriesValue;
        var modelMap = relationCarMap[seriesKey];
        var modelList = [];
        $.each(modelMap, function (modelName, modelId) {
            var modelKey = "model-" + brandValue + factoryValue + seriesValue + modelName;
            relationCarMap[modelKey] = modelId;
            modelList.push(modelName);
        });

        var model_map = {
            list: modelList,
            placeholder: '-请选择车型-'
        };
        var html = Mustache.render($('#car_select_template').html(), model_map);
        search_model.html(html);
        search_model.removeClass("hidden");
        search_model.select2({
            placeholder: "-请选择车型-"
        });

    });

    //车型选择
    search_model.change(function () {
        var value = search_model.val();
        if (value == '-1') {
            $("#choose_l_id").val("0");
            return;
        }

        var modelKey = "model-" + search_brand.val() + search_factory.val() + search_series.val() + value;
        var partLId = relationCarMap[modelKey];
        $("#choose_l_id").val(partLId);

        $('#part_sum_number').text("0");

        initSearchData(partLId);

    });

}


/*============主页数据===============*/
//获得搜索数据
function getResultData(pageIndex, treeNode) {

    var result_table_page_index = $("#result_table_page_index").val();
    if (Number(result_table_page_index) != Number(pageIndex)) {


        var liyangExt = $('#liyangExt');
        var search_result_body = $("#search_result_body");
        var result_table_tbody = $("#result_table_tbody");
        if (searchResultDataList == undefined || searchResultDataList.length == 0) {
            alert_fuc("此搜索条件下无数据！");
            $('#part_sum_number').text("0");
            result_table_tbody.html("此搜索条件下无数据！");
            search_result_body.addClass("hidden");
            liyangExt.addClass("hidden");

            return false;
        }

        //取当前的第 (n-1)*size - n*size的数据slice
        liyangExt.removeClass("hidden");
        search_result_body.removeClass("hidden");

        var showList = searchResultDataList.slice((pageIndex - 1) * pageSize, pageIndex * pageSize);
        var indexs = 0;
        var map = {
            resultDataList: showList,
            indexesFunc: function () {
                indexs += 1;
                return indexs
            }
        };
        var html = Mustache.render($('#result_data_template').html(), map);
        result_table_tbody.html(html);

        $("#result_table_page_index").val(pageIndex);

        var totalNumber = searchResultDataList.length;
        updateShowPage(totalNumber, Math.ceil(totalNumber / pageSize), pageIndex, "main_pagination");//更新分页组件
        //更改此分类的tree名称,加上此分类下的总数
        if (treeNode != null) {
            if (treeNode.name.indexOf("[") == -1) {
                var new_span = treeNode.name + "[" + totalNumber + "]";
                var span_id = treeNode.tId + "_span";

                $('#' + span_id).text(new_span);
                treeNode.name = new_span;
            }
        }
        //设置总数
        if ($('#part_sum_number').text() == "0") {
            $('#part_sum_number').text(allDataMap['goodsSum']);
        }
        //表格内部的按钮
        main_table_btn();

    }

}

//设置要被搜索的数据源
function initSearchData(partLId) {
    var search_brand = $("#search_brand").val();
    var search_factory = $("#search_factory").val();
    var search_series = $("#search_series").val();
    var search_model = $("#search_model").val();
    var all_car = search_brand + search_factory + search_series + search_model;

    //从数据库中取
    var loadingEL = $('body');
    App.blockUI(loadingEL);

    var search_params = {};
    search_params['partLId'] = partLId;
    search_params['brandName'] = search_brand;
    $.get("/monkey/part/dataShow/getDataFromCar", search_params, function (resultVO) {
        if (!resultVO.success) {
            alert_fuc(resultVO.message);
            $('#search_result_body').addClass("hidden");
            $('#liyangExt').addClass("hidden");
            return false;
        }
        allDataMap = resultVO.data;

        searchResultDataList = allDataMap['partGoodsShowBOList'];
        afterLiDataList = searchResultDataList;
        InitLiyangCate(allDataMap, all_car);

        App.unblockUI(loadingEL);
    })

}

//设置liyang、分类、以及当前分页
function InitLiyangCate(allDataMap, all_car) {
    //================liyang
    var liyangMap = allDataMap['liyangMap'];
    //获得该力洋车型下的所有排量、上市年款、变速箱形式、力洋Id、销售名称ex
    liYangExt(liyangMap, all_car);

    //cat
    var categoryMap = allDataMap['categoryMap'];
    //初始化tree
    initCateTree(categoryMap);

    $("#result_table_page_index").val("0");
    var pageIndex = 1;
    getResultData(pageIndex);
}

/*第二行===start=======获得该力洋车型下的所有排量、上市年款、变速箱形式、力洋Id、销售名称ex*/
function liYangExt(liyangMap, all_car) {
    $("#liyangIDDiv").addClass("hidden");
    liyangIDDiv_body.html("");

    var key = 'liyang_' + all_car;
    var allList = JSON.parse(storage.getItem(key + "_allList"));
    var displayYearSet = JSON.parse(storage.getItem(key + "_displayYearSet"));
    var transSet = JSON.parse(storage.getItem(key + "_transSet"));
    AllliyangList = JSON.parse(storage.getItem(key + "_liyang"));

    if (allList == undefined || allList == null) {
        var map = liyangMap;
        allList = map.allList;
        displayYearSet = map.displayYearSet;
        transSet = map.transSet;
        AllliyangList = map.AllliyangList;

        storage.setItem(key + "_allList", JSON.stringify(allList));
        storage.setItem(key + "_displayYearSet", JSON.stringify(displayYearSet));
        storage.setItem(key + "_transSet", JSON.stringify(transSet));
        storage.setItem(key + "_liyang", JSON.stringify(AllliyangList));
    }
    //赋值
    inintExtSelect2("search_display_year", "-请选择排量-上市年款-", displayYearSet);
    inintExtSelect2("search_trans", "-请选择变速器-", transSet);
    //按钮的点击事件
    //查看力洋Id数据
    $('#liyangId_button').unbind("click").click(function () {
        $('#liyangIDDiv').toggleClass("hidden");

        changeLiYangData();
    });
    //撤销所有力洋Id
    $('#liyangIDDiv_resert_button').unbind("click").click(function () {
        $('#liyangIDDiv_body > .row> .span').removeClass('active');
    });

    //更改
    $('#search_display_year').unbind("change").change(function () {
        var display_year_value = $("#search_display_year").val();

        var trans_value = $("#search_trans").val();
        changeExt(display_year_value, trans_value, allList, "search_display_year");
        changeLiYangData();

    });
    $('#search_trans').unbind("change").change(function () {
        var display_year_value = $("#search_display_year").val();

        var trans_value = $("#search_trans").val();
        changeExt(display_year_value, trans_value, allList, "search_trans");
        changeLiYangData();

    });


    //确定筛选
    $('#sure_search_button').unbind("click").click(function () {
        $('#liyangIDDiv').addClass("hidden");
        $('#part_sum_number').text("0");

        $("#result_table_page_index").val("0");
        $('#search_sum_code').val('');
        changeCatAndSearchDataByLi();
    });

    //是否删除
    $('#delete_all_button').unbind("click").click(function () {
        var brand = search_brand.val();
        var factory = search_factory.val();
        var series = search_series.val();
        var model = search_model.val();
        if ("-1" == brand || "-1" == factory || "-1" == series || "-1" == model) {
            alert_fuc("请选择全车型");
            return false;
        }

        var confirmString = "车型：" + brand + "-" + factory + "-" + series + "-" + model + "<br>";
        confirmString += "确定删除该车型下的所有数据？";
        confirm_fuc(confirmString, deleteAll);
    });

}

//更改下拉div中力洋ID和力洋Name的值
function changeLiYangData() {
    if (AllliyangList.length > 0) {
        liyangIDDiv_body.html(loadingHtml);
        var params = {};
        var listArray = [];

        var list = [];
        var size = AllliyangList.length;
        for (var i = 0; i < size; i++) {
            list.push(AllliyangList[i]);
            if (i % 5 == 4 && i != size) {
                var map = {'list': list};
                map['list'] = list;
                listArray.push(map);
                list = new Array();
            } else if (i == (size - 1)) {
                var map = {};
                map['list'] = list;
                listArray.push(map);
            }
        }
        AllliyangList = [];
        params['listArray'] = listArray;

        var html = Mustache.render($('#liyang_select_body').html(), params);
        liyangIDDiv_body.html(html);

        $('#liyangIDDiv_body >.row> .span').unbind('click').click(function () {
            $(this).toggleClass('active');
        })
    }
}

//根据值更改其他几项的值
function changeExt(display_year_value, trans_value, allList, changeName) {
    var displayYearList = [];
    var transList = [];

    var displayYearList_new = [];
    var transList_new = [];


    var liyangList = [];

    for (var i = 0; i < allList.length; i++) {
        var value = allList[i];
        var array = value.split("-");
        var display_year_v = array[2] + "-" + array[3];
        var trans_v = array[4];

        if (changeName == "search_display_year") {
            if (display_year_value != '-1') {
                if (value.indexOf('-' + display_year_value + '-') == -1) {
                    continue;
                } else {
                    transList_new.push(trans_v);
                }
            } else {
                transList_new.push(trans_v);
            }

            if (trans_value != '-1') {
                if (value.indexOf('-' + trans_value) == -1) {
                    continue;
                }
            }
        }

        if (changeName == "search_trans") {
            if (trans_value != '-1') {
                if (value.indexOf('-' + trans_value) == -1) {
                    continue;
                } else {
                    displayYearList_new.push(display_year_v);
                }
            } else {
                displayYearList_new.push(display_year_v);
            }

            if (display_year_value != '-1') {
                if (value.indexOf('-' + display_year_value + '-') == -1) {
                    continue;
                }
            }

        }

        displayYearList.push(display_year_v);
        transList.push(trans_v);

        var map = {};
        map['id'] = array[0];
        map['name'] = array[1];
        liyangList.push(map);

    }
    //更改值
    AllliyangList = liyangList;
    if (AllliyangList.length == 0) {
        alert_fuc("此筛选项下无对应的力洋Id数据，请重新选择");
    }

    if (display_year_value == '-1') {
        inintExtSelect2("search_display_year", "-请选择排量-上市年款-", displayYearList);
    } else if (changeName == "search_trans") {
        inintExtSelect2("search_display_year", "-请选择排量-上市年款-", displayYearList_new, display_year_value);

    }

    if (trans_value == "-1") {
        inintExtSelect2("search_trans", "-请选择变速器-", transList);
    } else if (changeName == "search_display_year") {
        inintExtSelect2("search_trans", "-请选择变速器-", transList_new, trans_value);

    }

}

//初始化select2
function inintExtSelect2(id, placeholder, setList, selectValue) {
    setList.sort();
    $.unique(setList);
    setList.sort();
    $.unique(setList);
    setList.sort();

    var showMap = {};
    showMap['placeholder'] = placeholder;
    showMap['list'] = setList;
    var html = Mustache.render($('#car_select_template').html(), showMap);
    $('#' + id).html(html);

    if (selectValue != undefined && selectValue != null && selectValue != "-1") {
        $('#' + id).val(selectValue);
    }

    $("#" + id).select2({
        placeholder: placeholder
    });


}

/*第二行===end=======获得该力洋车型下的所有排量、上市年款、变速箱形式、力洋Id、销售名称ex*/

//因力洋车型数据改变，分类和筛选数据也会因此改变
function changeCatAndSearchDataByLi() {
    var loadingEL = $('body');
    App.blockUI(loadingEL);

    var liyangSpan = $('#liyangIDDiv_body > .row > .active ');
    var liyangSpanSize = liyangSpan.length;
    var categoryMap = {};
    if (liyangSpanSize == 0) {
        //searchResultDataList = allDataMap['partGoodsShowBOList'];
        //categoryMap = allDataMap['categoryMap'];
        $('#liyangIDDiv_body > .row > span').addClass("active");
        liyangSpan = $('#liyangIDDiv_body > .row > .active ');
        liyangSpanSize = liyangSpan.length;
    }

    searchResultDataList = [];
    //筛选的liyangId
    var liyangIdList = [];
    for (var i = 0; i < liyangSpanSize; i++) {
        var liyangId = (liyangSpan.children('.span_id')).eq(i).text();
        liyangIdList.push(liyangId);
    }

    var liyang_size = liyangIdList.length;
    $.each(allDataMap['partGoodsShowBOList'], function (i, showBO) {
        var showLiyangList = (showBO['liyangList']);
        var show_size = showLiyangList.length;

        var mergeList = showLiyangList.concat(liyangIdList);
        mergeList.sort();
        $.unique(mergeList);
        if (mergeList.length < (liyang_size + show_size)) {
            //有重复
            searchResultDataList.push(showBO);
            //生成新的cate对象
            //{catName=变速器系统, catCode=5}
            var catString = "{catName=NAME, catCode=CODE}";
            var firstCat = catString.replace("NAME", showBO['firstCateName']).replace("CODE", showBO['firstCateCode']);
            var secondCat = catString.replace("NAME", showBO['secondCateName']).replace("CODE", showBO['secondCateCode']);
            var thirdCat = catString.replace("NAME", showBO['thirdCateName']).replace("CODE", showBO['thirdCateCode']);
            //var secondCat = {'catName': showBO['secondCateName'], 'catCode': showBO['secondCateCode']};
            //var thirdCat = {'catName': showBO['thirdCateName'], 'catCode': showBO['thirdCateCode']};
            var partCat = {
                'catName': showBO['partName'],
                'catCode': showBO['partCode'],
                'vehicleCode': showBO['vehicleCode']
            };

            var secondCatMap = categoryMap[firstCat];
            if (undefined == secondCatMap) {
                secondCatMap = {};
            }
            var thirdCatMap = secondCatMap[secondCat];
            if (undefined == thirdCatMap) {
                thirdCatMap = {};
            }
            var partSet = thirdCatMap[thirdCat];
            if (null == partSet) partSet = [];
            if ($.inArray(partCat, partSet) == -1) partSet.push(partCat);
            thirdCatMap[thirdCat] = partSet;
            secondCatMap[secondCat] = thirdCatMap;
            categoryMap[firstCat] = secondCatMap;
        }
    });


    App.unblockUI(loadingEL);
    //liyang筛选后的结果
    afterLiDataList = searchResultDataList;
    //初始化tree
    initCateTree(categoryMap);

    $("#result_table_page_index").val("0");
    var pageIndex = 1;
    getResultData(pageIndex);
}


/*================start tree============================*/

//初始化tree
function initCateTree(categoryMap) {
    var cateTreeList = initZtreeParentCate(categoryMap, 1, []);


    $.fn.zTree.init($("#cate_select_tree"), getTreeSetting(), cateTreeList);
    //搜索分类
    searchCate();

}

//将map四级转化为ztree的四级
function initZtreeParentCate(dataMap, catLevel, parentList) {
    if (catLevel < 4) {
        $.each(dataMap, function (catBO, nextMap) {
            // {catName=变速器系统, catCode=5}
            var thisCate = [];
            var catArray = catBO.replace("{catName=", "").replace(" catCode=", "").replace("}", "").split(",");
            thisCate['name'] = catArray[0];
            thisCate['code'] = catArray[1];


            thisCate['cateLevel'] = catLevel;
            thisCate['isParent'] = true;
            var thisChildList = [];
            thisChildList = initZtreeParentCate(nextMap, catLevel + 1, thisChildList);
            thisCate['children'] = thisChildList;
            parentList.push(thisCate);
        });
        parentList = parentList.sort(function (a, b) {
            return Number(a['code']) - Number(b['code'])
        })
    } else {
        //part..set
        $.each(dataMap, function (i, partBO) {
            var thisCate = [];
            thisCate['name'] = partBO.vehicleCode + partBO['catName'];
            thisCate['code'] = partBO['catCode'];
            thisCate['cateLevel'] = catLevel;
            thisCate['isParent'] = false;
            parentList.push(thisCate);
        });
    }
    return parentList;
}
//tree点击事件
function nodeOnClick(event, treeId, treeNode, clickFlag) {
    if (clickFlag != 1) {
        return
    }
    var level = treeNode.cateLevel;
    var search_sum_code = "";
    if (level == 1) {
        search_sum_code = treeNode.code;
    }
    if (level == 2) {
        search_sum_code = treeNode.getParentNode().code + treeNode.code;
    }
    if (level == 3) {
        search_sum_code = (treeNode.getParentNode().getParentNode().code + treeNode.getParentNode().code + treeNode.code);
    }
    if (level == 4) {
        search_sum_code = ( treeNode.code);
    }
    //原先searchData基础上筛选出符合这个code 的数据
    var search_size = search_sum_code.length;
    searchResultDataList = [];
    $.each(afterLiDataList, function (i, showBO) {
        var sum_code = showBO['partCode'];
        if (sum_code.substr(0, search_size) == search_sum_code) {
            searchResultDataList.push(showBO);
        }
    });

    $("#result_table_page_index").val("0");
    var pageIndex = 1;
    getResultData(pageIndex, treeNode);


}
//展开子集
function nodeOnExpand(event, treeId, treeNode) {
    if (treeNode.isOpened != undefined) {
        return
    }
    treeNode.isOpened = true;
}

//搜索分类
function searchCate() {
    //绑定回车事件
    $('#cate_search_keyword').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            $('#search_cate_btn').trigger('click');
        }
    });

    $('#search_cate_btn').unbind('click').click(function () {
        var cateKey = $('#cate_search_keyword').val().trim().toLowerCase();
        var treeObj = $.fn.zTree.getZTreeObj("cate_select_tree");
        var nodes = treeObj.getNodes();

        //一级
        var hideNodes = [];
        var showNodes = [];
        for (var i = 0; i < nodes.length; i++) {
            var name = nodes[i].name.toLowerCase();
            if (name.search(cateKey) >= 0) {
                showNodes.push(nodes[i])
            } else {
                hideNodes.push(nodes[i])
            }
        }
        treeObj.hideNodes(hideNodes);
        treeObj.showNodes(showNodes)

    });

}

function getTreeSetting() {
    return {
        view: {
            showLine: false,//去除连接线
            expandSpeed: "fast" //设置 zTree节点展开、折叠时的动画速度或取消动画(三种默认定义："slow", "normal", "fast")或 表示动画时长的毫秒数值(如：1000)
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            onClick: nodeOnClick,
            onExpand: nodeOnExpand
        }
    };
}

/*================end tree============================*/

//删除
function deleteAll() {
    var params = {};
    params['searchId'] = $("#choose_l_id").val();
    params['searchBrand'] = search_brand.val();
    params['searchFactory'] = search_factory.val();
    params['searchSeries'] = search_series.val();
    params['searchModel'] = search_model.val();

    $.get("/monkey/part/dataShow/deleteAll", params, function (resultVO) {
        if (resultVO.success) {
            alert_fuc("删除成功");
            //清除show页面的缓存
            storage.clear();

            $('#part_sum_number').text("0");
            $('#search_firstCate').val('');
            $('#search_secondCate').val('');
            $('#search_thirdCate').val('');


            $("#result_table_page_index").val("0");
            searchResultDataList = undefined;
            var pageIndex = 1;
            getResultData(pageIndex);
        }
    });
}

/*============分页===============*/
//初始化分页组件
function initPartShowPage() {
    var string = "{current_page}/{max_page}";

    $('#main_pagination').jqPagination({
        max_page: 1,
        page_string: string,
        paged: function (page) {
            getResultData(page);
            return false;
        }
    });

    $('#liyang_car_model_pagination').jqPagination({
        max_page: 1,
        page_string: string,
        paged: function (page) {
            getLiyangData(page);
            return false;
        }
    });
}

//更新分页组件
function updateShowPage(rows, maxPage, page, paginationID) {
    var str = "{current_page}/{max_page}";
    if (rows != null && rows !== undefined) {
        str = str + "(共" + rows + "条)";
    }
    //alert(str)
    var jqPage = $('#' + paginationID).data('jqPagination');
    jqPage.options.page_string = str;

    if (maxPage < page) {
        if (page != 1) {
            page = page - 1;
        }
        if ((page - getCurrentPage(paginationID)) != 0) {
            jqPage.setPage(page, true);//第二个参数必须是true，否则会去调用回调函数
        }
        if (maxPage == 0) {
            maxPage = 1;
        }
        jqPage.setMaxPage(maxPage, true);//必须有一个得更新，否则更新不了 page_string
    } else {
        if ((page - getCurrentPage(paginationID)) != 0) {
            jqPage.setPage(page, true);//第二个参数必须是true，否则会去调用回调函数
        }
        jqPage.setMaxPage(maxPage, true);//必须有一个得更新，否则更新不了 page_string
    }
}

//分页---获得当前页
function getCurrentPage(paginationID) {
    return $('#' + paginationID).jqPagination('option', 'current_page');
}


//表格内部的按钮
function main_table_btn() {
    //查看力洋车型
    $('.see_liyang_car_btn').unbind("click").click(function () {
        var goodsId = $(this).siblings(".goodsId").val();
        var picId = $(this).siblings(".picId").val();

        $('#liyang_car_model_goodsId').val(goodsId);
        $('#liyang_car_model_picId').val(picId);

        //设置model的头
        $('#liyang_car_model_title').text(search_brand.val() + "-" + search_factory.val() + "-" + search_series.val() + "-" + search_model.val());
        //获得数据
        var pageIndex = 1;
        var pageSize = 5;
        $('#liyang_car_model_pageSize').val(pageSize);

        $("#liyang_car_model_page_index").val("0");
        getLiyangData(pageIndex, pageSize);

        //弹出层出现
        $("#liyang_car_model").modal("show");
    });
}

/*======================弹出层 table---车型数据=======================*/
function getLiyangData(pageIndex, size) {

    //$('#liyang_car_model_table_tbody').html(loadingHtml);
    if (size == undefined) {
        size = $('#liyang_car_model_pageSize').val();
    }

    var result_table_page_index = $("#liyang_car_model_page_index").val();
    if (Number(result_table_page_index) != Number(pageIndex)) {
        var loadingEL = $('#liyang_car_model');
        App.blockUI(loadingEL);

        var params = {};
        params['goodsId'] = $('#liyang_car_model_goodsId').val();
        params['picId'] = $('#liyang_car_model_picId').val();
        params['partLId'] = $("#choose_l_id").val();
        params['brandName'] = search_brand.val();

        params['index'] = pageIndex;
        params['pageSize'] = size;
        $.get("/monkey/part/dataShow/searchLiyangCar", params,
            function (resultVO) {
                console.log(resultVO);

                App.unblockUI(loadingEL);
                if (resultVO.success) {
                    var result_map = resultVO.data;

                    if (result_map.resultDataList.length == 0) {
                        alert_fuc("此配件无对应的力洋车型！");
                        $('#liyang_car_model_table_tbody').html("此配件无对应的力洋车型!");
                        return;
                    }

                    var html = Mustache.render($('#liyang_car_data_template').html(), result_map);
                    $('#liyang_car_model_table_tbody').html(html);

                    $("#liyang_car_model_page_index").val(pageIndex);

                    updateShowPage(result_map.totalNumber, result_map.totalPages, pageIndex, "liyang_car_model_pagination");//更新分页组件

                } else {
                    alert_fuc(resultVO.message);
                    return false;
                }
            });
    }
}

function showStatisticalData(brandMap) {
    var brandCount = 0;
    var factoryCount = 0;
    var seriesCount = 0;
    var modelCount = 0;
    $.each(brandMap, function (brandName, factoryMap) {
        brandCount++;

        $.each(factoryMap, function (factoryName, seriesMap) {
            factoryCount++;

            $.each(seriesMap, function (seriesName, modelMap) {
                seriesCount++;

                $.each(modelMap, function () {
                    modelCount++;
                });
            });
        });
    });
    $("#statisticalData_brand").text(brandCount);
    $("#statisticalData_factory").text(factoryCount);
    $("#statisticalData_series").text(seriesCount);
    $("#statisticalData_model").text(modelCount);
}

function getPartGoodsBaseOeNum() {
    $.get("/monkey/part/dataShow/getPartGoodsBaseOeNum", {}, function (result) {
        if (result.success) {
            $("#statisticalData_oe").html(result.data);
        }
    });
}

//=============help=======
function alert_fuc(text) {
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return false;
}


function confirm_fuc(text, successFuction) {
    $('#confirm_model_content').html(text);
    $('#confirm_model').modal('show');

    $('#confirm_model_sure_button').unbind('click').click(function () {
        successFuction();
        $('#confirm_model').modal('hide');
    });

    return false;
}
