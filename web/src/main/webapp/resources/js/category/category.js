var select_condition_text = "";
var modify_cate_bt_text = "请选择要修改的分类";
var modifyFlag_ = false;
var modifyLevel_ = -1;
var modifyCateId_ = -1;
var params_;
var first_cate_code = "";
var second_cate_code = "";
var third_cate_code = "";
var part_code = "";
var default_third_cate_code = "XXX";
var page_ = 1;
var cate_stop_use_msg = "";

var _body = $('body');

$(document).ready(function(){
    button_click();
    to_init();
});

function checkFirstCatCode(value){
    return /^[a-zA-Z\d]$/i.test(value);
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
function button_click(){

    $('.search').bind('keypress',function(event){
        if(event.keyCode == 13) {
            $('#search_button').trigger('click');
        }
    });

    $('#search_button').click(function(){
        to_search_list(1);
    });

    $("#vehicle_code").change(function(){
        to_search_list(1);
    });

    $("#first_cate").change(function(){
        to_init_scSelect(this.value);

        to_search_list(1);

    });


    if($('#modify_cate_button').attr('id')!=undefined){
        modifyFlag_ = true;
        $('#modify_cate_button').click(function(){
            to_modify();
        });
    }

    if(modifyFlag_){
        $('#add_cate_button').click(function(){
            to_add();
        });
    }


    $('#reset_button').click(function(){

        page_ = 1;

        params_ = {};
        params_.pageIndex = 1;
        params_.vehicleCode = '';
        params_.partName = '';
        params_.categoryId = -1;
        params_.level = -1;

        $("#third_cate").get(0).options.length = 1;
        $("#second_cate").get(0).options.length = 1;
        $("#first_cate").val(-1);
        $("#vehicle_code").val('');
        $("#partName").val('');

        cate_stop_use_msg = "";
        select_condition_text = "查询全部数据";
        $("#select_condition").html(select_condition_text);

        if(modifyFlag_){
            modifyLevel_ = -1;
            modifyCateId_ = -1;
            modify_cate_bt_text = "请选择要修改的分类";
            $("#modify_cate_button span").html(modify_cate_bt_text);
        }
    });

}


function to_init(){
    $.ajax({
        url: '/monkey/category/toGetVehicleCode',
        type: 'POST',
        success: function (result) {
            //alert(result.vehicleCodeList.length);
            var vcSelect = $("#vehicle_code");
            vcSelect.get(0).options.length = 1;
            var vcArray = result.vehicleCodeList;
            var op;
            for(var i in vcArray){
                op = $("<option></option>");
                op.val(vcArray[i]);
                op.text(vcArray[i]);
                vcSelect.append(op);
            }

            vcSelect.unbind().change(function(){
                to_init_fcSelect(this.value);

                to_search_list(1);
            });

            //var fcSelect = $("#first_cate");
            //fcSelect.get(0).options.length = 1;
            //var fcArray = result.firstCategoryList;
            //for(var j in fcArray){
            //    op = $("<option></option>");
            //    op.val(fcArray[j].catId);
            //    op.text(fcArray[j].catName);
            //    fcSelect.append(op);
            //}
        }
    });


    App.blockUI(_body);

    //$('#list_tbody').html(loadingHtml);
    params_ = {};
    params_.pageIndex = 1;
    params_.vehicleCode = '';
    params_.partName = '';
    params_.categoryId = -1;
    params_.level = -1;

    $.ajax({
        url:'/monkey/category/toSearchCategory',
        type:'POST',
        data:params_,
        success:function(result){
            var html = '';
            var resultHtml = '';

            var length = result.data.length;

            if(length > 0){
                html = Mustache.render($('#list_template').html(), result);
            }else{
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';

                $('#result_show').html(resultHtml);
            }

            $('#list_tbody').html(html);


            initPage(result.totalPages, 1, result.totalRows, to_search_list);//初始化分页组件

            App.unblockUI(_body);
        }
    });

}

function to_init_fcSelect(vehicleCode){
    var cateSelect = $("#first_cate");
    cateSelect.get(0).options.length = 1;

    $("#second_cate").get(0).options.length = 1;
    $("#third_cate").get(0).options.length = 1;

    if(vehicleCode==''){
        return;
    }

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 1, vehicleCode : vehicleCode},
        success: function (result) {
            var cateArray = result.categoryList;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

            cateSelect.unbind().change(function(){
                to_init_scSelect(this.value);

                to_search_list(1);
            });
        }
    });

}

function to_init_scSelect(pid){
    var cateSelect = $("#second_cate");
    cateSelect.get(0).options.length = 1;

    $("#third_cate").get(0).options.length = 1;

    if(pid == -1){
        return;
    }

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 2, catPid : pid},
        success: function (result) {
            var cateArray = result.categoryList;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

            cateSelect.unbind().change(function(){
                to_init_tcSelect(this.value);

                to_search_list(1);
            });
        }
    });

}

function to_init_tcSelect(pid){
    var cateSelect = $("#third_cate");
    cateSelect.get(0).options.length = 1;
    if(pid == -1){
        return;
    }

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 3, catPid : pid},
        success: function (result) {
            var cateArray = result.categoryList;
            var op;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

            cateSelect.unbind().change(function(){
                to_search_list(1);
            });
        }
    });

}


//查询数据
function get_params(pageIndex){
    var params = {};
    params.pageIndex = pageIndex;
    params.vehicleCode = $("#vehicle_code").val();
    params.partName = $("#partName").val().replace(/\s+/g,'');

    select_condition_text = "";
    if(params.vehicleCode!=""){
        select_condition_text += params.vehicleCode+" ";
    }

    var thirdCateId = $("#third_cate").val();
    if(thirdCateId==-1){
        var secondCateId = $("#second_cate").val();
        if(secondCateId==-1){
            var firstCateId = $("#first_cate").val();
            if(firstCateId==-1){
                params.categoryId = -1;
                params.level = -1;
                cate_stop_use_msg = "";
                if(select_condition_text=="" && params.partName==""){
                    select_condition_text = "查询全部数据";
                }
                if(modifyFlag_){
                    modify_cate_bt_text = "请选择要修改的分类";
                    modifyLevel_ = -1;
                    modifyCateId_ = -1;
                }
            }else{
                params.categoryId = firstCateId;
                params.level = 1;
                cate_stop_use_msg = $("#first_cate :selected").text();
                select_condition_text += cate_stop_use_msg + " ";
                if(modifyFlag_) {
                    modify_cate_bt_text = "修改 [ 一级分类 ]：" + cate_stop_use_msg;
                    modifyLevel_ = 1;
                    modifyCateId_ = firstCateId;
                }
            }
        }else{
            params.categoryId = secondCateId;
            params.level = 2;
            cate_stop_use_msg = $("#second_cate :selected").text();
            select_condition_text += $("#first_cate :selected").text()+" "+cate_stop_use_msg+" ";
            if(modifyFlag_) {
                modify_cate_bt_text = "修改 [ 二级分类 ]：" + cate_stop_use_msg;
                modifyLevel_ = 2;
                modifyCateId_ = secondCateId;
            }
        }
    }else{
        params.categoryId = thirdCateId;
        params.level = 3;
        cate_stop_use_msg = $("#third_cate :selected").text();
        select_condition_text += $("#first_cate :selected").text()+" "+$("#second_cate :selected").text()+" "+cate_stop_use_msg+" ";
        if(modifyFlag_) {
            modify_cate_bt_text = "修改 [ 三级分类 ]：" + cate_stop_use_msg;
            modifyLevel_ = 3;
            modifyCateId_ = thirdCateId;
        }
    }

    if(params.partName!=""){
        select_condition_text += params.partName;
    }

    return params;
}

function to_search_list(pageIndex){
    page_ = pageIndex;
    params_ = get_params(pageIndex);

    if(modifyFlag_){
        $("#modify_cate_button span").html(modify_cate_bt_text);
    }

    App.blockUI(_body);

    $.ajax({
        url:'/monkey/category/toSearchCategory',
        type:'POST',
        data:params_,
        success:function(result){
            var html = '';
            var resultHtml = '';

            var length = result.data.length;

            if(length > 0){
                html = Mustache.render($('#list_template').html(), result);
                $('#result_show').html('');
            }else{
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';

                $('#result_show').html(resultHtml);
            }

            $('#list_tbody').html(html);


            updatePage(result.totalRows, result.totalPages, pageIndex);//更新分页组件

            $("#select_condition").html(select_condition_text);

            App.unblockUI(_body);
        }
    });


}


//导出数据Excel
function toExportExcel(type){

    var paramStr = "level=-1";

    if(type != 'all'){
        paramStr = "vehicleCode="+params_.vehicleCode+"&partName="+params_.partName
            +"&categoryId="+params_.categoryId+"&level="+params_.level;
    }

    window.location.href = "/monkey/category/toExportExcel?"+paramStr;

    //var url = "/monkey/category/toExportExcel?"+paramStr;
    //
    //var xmlhttp;
    //if(window.XMLHttpRequest){
    //    xmlhttp = new XMLHttpRequest();
    //}else{
    //    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    //}
    //
    //xmlhttp.onreadystatechange = function(){
    //    if(this.readyState==4){
    //        if(this.status==200){
    //            //alert('下载完成');
    //            alert(this.responseText);
    //        }
    //    }
    //
    //}
    //
    //xmlhttp.open("GET", url, true);
    //xmlhttp.send(null);

}

