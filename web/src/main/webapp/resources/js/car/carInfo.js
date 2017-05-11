var search_url_;
var select_condition_text = "";
var search_alert_msg = '[ 请选择车型 ]';//'[ 请选择车型 ] 或 [ 填写车型 ]';

$(document).ready(function(){
    button_click();
    to_init();
});

//按钮点击事件
function button_click(){
    $('#search_button').click(function(){
        to_search_list(1);
    });

    $('.search').bind('keypress',function(event){
        if(event.keyCode == 13){
            $('#search_button').trigger('click');
        }
    });


    $('#reset_button').click(function(){
        $("#carBrand").val(-1);
        $("#factoryName").get(0).options.length = 1;
        $("#carSeries").get(0).options.length = 1;
        $("#vehicleType").get(0).options.length = 1;
        //$("#carModel").val('');

        select_condition_text = '';
        $("#select_condition").html(select_condition_text);

        clear_select2("carBrand", "[ 请选择品牌 ]");
        clear_select2("factoryName", "[ 请选择厂家 ]");
        clear_select2("carSeries", "[ 请选择车系 ]");
        clear_select2("vehicleType", "[ 请选择车型 ]");
    });

    $("#modify_version_button").click(function () {
        MONKEY.confirmFun({
            width: 400,
            content: '确定更新导出excel时数据的版本号吗？',
            okFun: function(){
                $.ajax({
                    url: '/monkey/carInfoAll/refreshVehicleTypeExportExcelVersion',
                    type: 'POST',
                    data: {},
                    success: function (result) {
                        if (result.success) {
                            $("#export_version").text("V"+result.data);
                        }
                        MONKEY.alertFunc({content: result.message});
                    },
                    error: function (result) {
                        MONKEY.alertFunc({content: "系统错误!"});
                    }
                });
            }
        });

    });

    $("#re_create_excel_button").click(function () {
        MONKEY.confirmFun({
            width: 400,
            content: '确定根据当前版本号重新生成全量数据的excel吗(生成大概需要3、4分钟)？',
            okFun: function(){
                $.ajax({
                    url: '/monkey/carInfoAll/createAllDataExcel',
                    type: 'GET',
                    data: {},
                    success: function (result) {
                        MONKEY.alertFunc({content: result.message});
                    },
                    error: function (result) {
                        MONKEY.alertFunc({content: "系统错误!"});
                    }
                });
            }
        });
    });
}

function clear_select2(id, text){
    $("#s2id_"+id+" .select2-choice .select2-chosen").html(text);
}


function to_init(){

    //获取版本号
    $.ajax({
        url: '/monkey/carInfoAll/getVehicleTypeExportExcelVersion',
        type: 'GET',
        success: function (result) {
            $("#export_version").text("V"+result.data);
        }
    });

    $.ajax({
        url: '/monkey/carInfoAll/toGetCarBrands',
        type: 'POST',
        success: function (result) {
            var select = $("#carBrand");
            select.get(0).options.length = 1;
            var array = result.data;
            var op;
            for(var i in array){
                op = $("<option></option>");
                op.val(array[i]);
                op.text(array[i]);
                select.append(op);
            }

            select.unbind().change(function(){
                to_init_factory_select(this.value);
            });
        }
    });


    initPage(0, 1, 0, to_search_list);//初始化分页组件
}

function to_init_factory_select(carBrand){
    var select = $("#factoryName");
    select.get(0).options.length = 1;

    $("#carSeries").get(0).options.length = 1;
    $("#vehicleType").get(0).options.length = 1;
    clear_select2("factoryName", "[ 请选择厂家 ]");
    clear_select2("carSeries", "[ 请选择车系 ]");
    clear_select2("vehicleType", "[ 请选择车型 ]");

    if(carBrand == -1){
        return;
    }

    $.ajax({
        url: '/monkey/carInfoAll/toGetFactoryNames',
        type: 'POST',
        data: {carBrand : carBrand},
        success: function (result) {
            var array = result.data;
            var op;
            for(var i in array){
                op = $("<option></option>");
                op.val(array[i]);
                op.text(array[i]);
                select.append(op);
            }

            select.unbind().change(function(){
                to_init_series_select(carBrand, this.value);
            });
        }
    });
}


function to_init_series_select(carBrand, factoryName){
    var select = $("#carSeries");
    select.get(0).options.length = 1;

    $("#vehicleType").get(0).options.length = 1;
    clear_select2("carSeries", "[ 请选择车系 ]");
    clear_select2("vehicleType", "[ 请选择车型 ]");

    if(factoryName == -1){
        return;
    }

    $.ajax({
        url: '/monkey/carInfoAll/toGetCarSeries',
        type: 'POST',
        data: {carBrand : carBrand, factoryName : factoryName},
        success: function (result) {
            var array = result.data;
            var op;
            for(var i in array){
                op = $("<option></option>");
                op.val(array[i]);
                op.text(array[i]);
                select.append(op);
            }

            select.unbind().change(function(){
                to_init_vehicle_type_select(carBrand, factoryName, this.value);
            });
        }
    });
}


function to_init_vehicle_type_select(carBrand, factoryName, carSeries){
    var select = $("#vehicleType");
    select.get(0).options.length = 1;

    clear_select2("vehicleType", "[ 请选择车型 ]");

    if(carSeries == -1){
        return;
    }

    $.ajax({
        url: '/monkey/carInfoAll/toGetVehicleTypes',
        type: 'POST',
        data: {carBrand : carBrand, factoryName : factoryName, carSeries : carSeries},
        success: function (result) {
            var array = result.data;
            var op;
            for(var i in array){
                op = $("<option></option>");
                op.val(array[i]);
                op.text(array[i]);
                select.append(op);
            }

            select.unbind().change(function(){
                to_search_list(1);
            });
        }
    });
}

function get_params(pageIndex){
    var params = {};
    var vehicleType = $("#vehicleType").val();
    if(vehicleType == -1){
        //var carModel = $("#carModel").val().trim();
        //if(carModel == ''){
        //    return null;
        //}
        //params.vehicleType = carModel;
        //search_url_ = 'toSearchCarInfosByVehicleType';

        return null;
    }else{
        params.vehicleType = vehicleType;
        search_url_ = 'toSearchCarInfos';
        //$("#carModel").val("");
    }

    params.pageIndex = pageIndex;
    select_condition_text = '';

    var carBrand = $("#carBrand").val();
    if(carBrand != -1){
        params.carBrand = carBrand;
        select_condition_text += carBrand + ' | ';
    }
    var factoryName = $("#factoryName").val();
    if(factoryName != -1){
        params.factoryName = factoryName;
        select_condition_text += factoryName + ' | ';
    }
    var carSeries = $("#carSeries").val();
    if(carSeries != -1){
        params.carSeries = carSeries;
        select_condition_text += carSeries + ' | ';
    }
    select_condition_text += params.vehicleType;

    return params;
}

function to_search_list(pageIndex){
    var params = get_params(pageIndex);
    if(params == null){
        MONKEY.alertFunc({content: search_alert_msg});
        return;
    }

    $('#list_tbody').html(loadingHtml);

    $.ajax({
        url:'/monkey/carInfoAll/'+search_url_,
        type:'POST',
        data:params,
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
        }
    });

}

function to_show_detail(carId){
    $.ajax({
        url:'/monkey/carInfoAll/toGetCarInfoById',
        type:'POST',
        data:{id : carId},
        success:function(result) {
            var html = Mustache.render($('#car_info_template').html(), result);
            //$('#car_info_modal').modal('show');
            //$('#car_info_detail').html(html);

            MONKEY.alertFunc({width:800, content: html});
        }
    });
}



function get_params_for_excel(){
    var paramStr = "";

    var vehicleType = $("#vehicleType").val();
    if(vehicleType == -1){
        //var carModel = $("#carModel").val().trim();
        //if(carModel == ''){
        //    return null;
        //}
        //paramStr = "type=1&vehicleType="+carModel;

        return null;
    }else{
        paramStr = "type=0&vehicleType="+vehicleType;
        //$("#carModel").val("");
    }
    var carBrand = $("#carBrand").val();
    if(carBrand != -1){
        paramStr += "&carBrand="+carBrand;
    }
    var factoryName = $("#factoryName").val();
    if(factoryName != -1){
        paramStr += "&factoryName="+factoryName;
    }
    var carSeries = $("#carSeries").val();
    if(carSeries != -1){
        paramStr += "&carSeries="+carSeries;
    }

    return paramStr;
}

function toExportExcel(){
    var paramStr = get_params_for_excel();
    if(paramStr == null){
        MONKEY.alertFunc({content: search_alert_msg});
        return;
    }
    window.location.href = "/monkey/carInfoAll/toExportExcel?"+paramStr;
}

function toExportExcelAll() {
    $.ajax({
        url: '/monkey/carInfoAll/queryExportLyAllDataFlag',
        type: 'GET',
        data: {},
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                window.location.href = "/monkey/carInfoAll/downloadLiYangDataExcel";
            } else {
                MONKEY.alertFunc({content: result.message});
            }
        }
    });
}





