/**
 *
 * Created by LYJ on 20151215.
 */
var select_condition_text = "";
var params_;
var cate_stop_use_msg = "";
var _body = $('body');
var page_ = 1;

$(document).ready(function () {
    params_ = {};
    params_.pageIndex = 1;
    params_.vehicleCode = '';
    params_.levelF = '';
    params_.levelS = '';
    params_.levelT = '';
    params_.partName = '';

    button_click();


    to_init();

    //initPage(1, 1, 0, to_search_list);//初始化分页组件

});

function checkFirstCatCode(value){
    //return /^[a-zA-Z\d]$/i.test(value);//老的1J是1位的切可以使用字母，新的必须是两位数字
    return /^\d{2}$/i.test(value);
}

function checkSecondCatCode(value){
    return /^\d{2}$/i.test(value);
}

function checkThirdCatCode(value){
    return /^\d{3}$/i.test(value);
}

function checkPartCode(value){
    return /^\d{2}$/i.test(value);
}

function replaceCommaCN(value){
    return value.replace(/，+/g,',');
}
function replaceBracketsCN(value){
    return value.replace(/（+/g,'(').replace(/）+/g,')');
}

//按钮点击事件
function button_click() {
    $('.search').bind('keypress', function (event) {
        if (event.keyCode == 13) {
            $('#search_button').trigger('click');
        }
    });

    $('#search_button').click(function () {
        to_search_list(1);
    });

    $('#reset_button').click(function () {
        page_ = 1;

        params_ = {};
        params_.pageIndex = 1;
        params_.vehicleCode = '';
        params_.levelF = '';
        params_.levelS = '';
        params_.levelT = '';
        params_.partName = '';

        $("#third_cate").get(0).options.length = 1;
        $("#second_cate").get(0).options.length = 1;
        $("#first_cate").get(0).options.length = 1;
        $("#vehicle_code").val('');
        $("#partName").val('');
    });

    //modify 相关的js
    $('#add_cate_button').click(function(){
        to_add();
    });
}

function getDataMap(dataObj) {
    var index = 0;
    var dataMap = {
        data: dataObj,
        indexRenderer: function () {
            index++;
            return index;
        },
        catKindName: function () {
            var result = "";
            if(this.catKind == "0"){
                result = "易损件";
            }else if(this.catKind == "1") {
                result = "全车件";
            }
            return result;
        }
    };
    return dataMap;
}

function to_init() {
    $.ajax({
        url: '/monkey/categoryXPart/getCatXPartExportExcelVersion',
        type: 'POST',
        success: function (result) {
            $("#export_version").text("V"+result.data);
        }
    });

    $.ajax({
        url: '/monkey/categoryXPart/getVehicleCode',
        type: 'POST',
        success: function (result) {
            var vcSelect = $("#vehicle_code");
            var vcArray = result.vehicleCodeList;
            var op;
            for (var i in vcArray) {
                op = $("<option></option>");
                op.val(vcArray[i]);
                op.text(vcArray[i]);
                vcSelect.append(op);
            }
        }
    });

    $("#vehicle_code").change(function () {
        var vehicleCode = $("#vehicle_code").val();
        var cateSelect = $("#first_cate");
        cateSelect.get(0).options.length = 1;
        $("#second_cate").get(0).options.length = 1;
        $("#third_cate").get(0).options.length = 1;
        if (vehicleCode == '') {
            return;
        }
        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 1, vehicleCode: vehicleCode},
            success: function (result) {
                var dataArray = result.categoryAttrList;
                var op;
                for (var i in dataArray) {
                    op = $("<option></option>");
                    op.val(dataArray[i].catId);
                    op.text(dataArray[i].catName);
                    cateSelect.append(op);
                }
            }
        });
    });

    $("#first_cate").change(function () {
        var firstCatId = $("#first_cate").val();
        var cateSelect = $("#second_cate");
        cateSelect.get(0).options.length = 1;
        $("#third_cate").get(0).options.length = 1;
        if (firstCatId == '-1') {
            return;
        }
        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 2, parentId: firstCatId},
            success: function (result) {
                var dataArray = result.categoryAttrList;
                var op;
                for (var i in dataArray) {
                    op = $("<option></option>");
                    op.val(dataArray[i].catId);
                    op.text(dataArray[i].catName);
                    cateSelect.append(op);
                }
            }
        });
    });

    $("#second_cate").change(function () {
        var secondCatId = $("#second_cate").val();
        var cateSelect = $("#third_cate");
        cateSelect.get(0).options.length = 1;
        if (secondCatId == '-1') {
            return;
        }
        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 3, parentId: secondCatId},
            success: function (result) {
                var dataArray = result.categoryAttrList;
                var op;
                for (var i in dataArray) {
                    op = $("<option></option>");
                    op.val(dataArray[i].catId);
                    op.text(dataArray[i].catName);
                    cateSelect.append(op);
                }
            }
        });
    });

    App.blockUI(_body);

    params_ = {};
    params_.pageIndex = 1;
    params_.vehicleCode = '';
    params_.levelF = '';
    params_.levelS = '';
    params_.levelT = '';
    params_.partName = '';

    $.ajax({
        url: '/monkey/categoryXPart/searchCategoryXPart',
        type: 'POST',
        data: params_,
        dataType: 'json',
        success: function (result) {
            var html = '';
            var resultHtml = '';
            var length = result.data.length;
            if (length > 0) {
                html = Mustache.render($('#list_template').html(), getDataMap(result.data));
            } else {
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';
                $('#result_show').html(resultHtml);
            }
            $('#list_tbody').html(html);
            initPage(result.totalPages, 1, result.totalRows, to_search_list);//初始化分页组件
            App.unblockUI(_body);
        }
    });
}

//查询数据
function get_params(pageIndex) {
    params_ = {};
    params_.pageIndex = pageIndex;
    params_.vehicleCode = $("#vehicle_code").val();
    params_.levelF = $("#first_cate :selected").val();
    params_.levelS = $("#second_cate :selected").val();
    params_.levelT = $("#third_cate :selected").val();
    params_.partName = $("#partName").val().replace(/\s+/g, '');

    var flag = 1;
    select_condition_text = "";
    if (params_.vehicleCode != "") {
        flag = 0;
        select_condition_text += params_.vehicleCode + " ";
    }
    if (params_.levelF != '') {
        flag = 0;
        select_condition_text += $("#first_cate :selected").text() + " ";
    }
    if (params_.levelS != '') {
        flag = 0;
        select_condition_text += $("#second_cate :selected").text() + " ";
    }
    if (params_.levelT != '') {
        flag = 0;
        select_condition_text += $("#third_cate :selected").text() + " ";
    }
    if (params_.partName != '') {
        flag = 0;
        select_condition_text += params_.partName + " ";
    }
    if (flag == 1) {
        select_condition_text = "查询全部数据";
    }
    return params_;
}

function to_search_list(pageIndex) {
    page_ = pageIndex;
    params_ = get_params(pageIndex);
    App.blockUI(_body);
    $.ajax({
        url: '/monkey/categoryXPart/searchCategoryXPart',
        type: 'POST',
        data: params_,
        dataType: 'json',
        success: function (result) {
            var html = '';
            var resultHtml = '';
            var length = result.data.length;
            if (length > 0) {
                html = Mustache.render($('#list_template').html(), getDataMap(result.data));
            } else {
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';
                $('#result_show').html(resultHtml);
            }
            $('#list_tbody').html(html);
            //initPage(result.totalPages, 1, result.totalRows, to_search_list);//初始化分页组件
            updatePage(result.totalRows, result.totalPages, pageIndex);//更新分页组件
            $("#select_condition").html(select_condition_text);
            App.unblockUI(_body);
        }
    });
}

//导出数据Excel
function toExportExcel(type) {
    var paramStr = "";
    if (type == 'all') {
        paramStr = "vehicleCode=&levelF=&levelS=&levelT=&partName";
    } else {
        paramStr = "vehicleCode=" + params_.vehicleCode + "&levelF=" + params_.levelF + "&levelS=" + params_.levelS + "&levelT=" + params_.levelT + "&partName=" + params_.partName;
    }
    window.location.href = "/monkey/categoryXPart/toExportExcel?" + paramStr;
}

