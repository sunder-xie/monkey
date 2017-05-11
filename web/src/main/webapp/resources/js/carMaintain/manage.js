var tabId_ = 'tab_plan';
var editPlanFlag = false;
var oldPlanDetail = new Array();
var maintainPlanId_ = 0;
var maintainPlanName = '';
var carModelObject = null;
var carModelText = '';

var carInfoArray = null;
var carInfoYear = null;
var carInfoPower = null;
var fuelTypeCount = 0;

var notRelatedCarInfoArray = null;
var notRelatedYear = null;
var notRelatedPower = null;
var nrfuelTypeCount = 0;


$(document).ready(function(){

    to_init();

});

/* ====================力洋车型库筛选 start========================= */
function clear_select2(id, text){
    $("#s2id_"+id+" .select2-choice .select2-chosen").html(text);
}

function to_init(){
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
                to_get_model_maintain_plan();
            });
        }
    });
}

function get_params(){
    var params = {};
    var vehicleType = $("#vehicleType").val();
    if(vehicleType == -1){
        return null;
    }else{
        params.vehicleType = vehicleType;
    }

    var carBrand = $("#carBrand").val();
    if(carBrand != -1){
        params.carBrand = carBrand;
    }
    var factoryName = $("#factoryName").val();
    if(factoryName != -1){
        params.factoryName = factoryName;
    }
    var carSeries = $("#carSeries").val();
    if(carSeries != -1){
        params.carSeries = carSeries;
    }

    return params;
}

/* 查看车款详细信息 */
function to_show_detail(carId){
    $.ajax({
        url:'/monkey/carInfoAll/toGetCarInfoById',
        type:'POST',
        data:{id : carId},
        success:function(result) {
            var html = Mustache.render($('#car_info_detail_template').html(), result);
            $('#car_info_modal').modal('show');
            $('#car_info_detail').html(html);
        }
    });
}

/* 初始换配置车款相关的组件 */
function clear_car_info(){
    $('#car_info_ft').get(0).options.length = 1;
    $('#not_related_ft').get(0).options.length = 1;

    $('#car_info_power').get(0).options.length = 1;
    $('#car_info_year').get(0).options.length = 1;
    $('#not_related_power').get(0).options.length = 1;
    $('#not_related_year').get(0).options.length = 1;
    $('#car_info_checkbox').attr('checked', true);
    $('#not_related_checkbox').attr('checked', false);
    $('#car_info_table').html('');
    $('#not_related_car_info_table').html('');
}
/* 查询适配车款 */
function to_search_car_info(maintainPlanId, target){
    //maintainPlanId_ = maintainPlanId;
    clear_car_info();

    var params = new Object();
    params.vehicleType = carModelObject.vehicleType;
    params.carBrand = carModelObject.carBrand;
    params.factoryName = carModelObject.factoryName;
    params.carSeries = carModelObject.carSeries;
    params.maintainPlanId = maintainPlanId;

    $.ajax({
        url:'/monkey/carInfoAll/toGetCarInfoForMaintain',
        type:'POST',
        data:params,
        success:function(result) {
            carInfoArray = result.carInfoList;
            notRelatedCarInfoArray = result.notRelatedCarInfoList;
            var op;
            var select;
            var pro;
            if(carInfoArray.length==0){
                $('#car_info_table').html('没有数据！');
            }else{
                $('#car_info_table').html(Mustache.render($('#car_info_template').html(), result));

                carInfoYear = result.carInfoYear;
                select = $('#car_info_year');
                for(pro in carInfoYear){
                    op = $('<option></option>');
                    op.text(pro);
                    op.val(pro);
                    select.append(op);
                }

                carInfoPower = result.carInfoPower;
                select = $('#car_info_power');
                for(pro in carInfoPower){
                    op = $('<option></option>');
                    op.text(pro);
                    op.val(pro);
                    select.append(op);
                }

                select = $('#car_info_ft');
                var ftSet = result.fuelTypeSet;
                fuelTypeCount = ftSet.length;
                for(var i=0; i<fuelTypeCount; i++){
                    op = $('<option></option>');
                    op.text(ftSet[i]);
                    op.val(ftSet[i]);
                    select.append(op);
                }

            }

            if(notRelatedCarInfoArray.length==0){
                $('#not_related_car_info_table').html('没有数据！');
            }else{
                $('#not_related_car_info_table').html(Mustache.render($('#not_related_car_info_template').html(), result));

                notRelatedYear = result.notRelatedYear;
                select = $('#not_related_year');
                for(pro in notRelatedYear){
                    op = $('<option></option>');
                    op.text(pro);
                    op.val(pro);
                    select.append(op);
                }

                notRelatedPower = result.notRelatedPower;
                select = $('#not_related_power');
                for(pro in notRelatedPower){
                    op = $('<option></option>');
                    op.text(pro);
                    op.val(pro);
                    select.append(op);
                }

                select = $('#not_related_ft');
                var nftSet = result.nrFuelTypeSet;
                nrfuelTypeCount = nftSet.length;
                for(var j=0; j<nrfuelTypeCount; j++){
                    op = $('<option></option>');
                    op.text(nftSet[j]);
                    op.val(nftSet[j]);
                    select.append(op);
                }
            }

        }
    });

    //if(target!=undefined){
    //    highlight_tr(target);
    //}
}

/* ====================力洋车型库筛选 end========================= */

/* =================车型保养方案相关操作 start==================== */
/* 初始化Tab内容 */
function init_tab_content(idStr){
    tabId_ = idStr;
    if(maintainPlanId_ == 0){
        $('#'+tabId_).html('请先选择一个保养方案！');
        return;
    }

    if('tab_mileage' == idStr){
        $('#edit_plan_detail_button').addClass('hidden');
        $('#save_plan_detail_button').addClass('hidden');
        $('#copyPlanBt').addClass('hidden');
        $('#addMileBt').removeClass('hidden');
        $('#delSomeMileBt').removeClass('hidden');

        if($('#tab_mileage table').html() == undefined){
            to_search_maintain_mileage();
        }

    }else if('tab_plan' == idStr){
        $('#edit_plan_detail_button').removeClass('hidden');
        $('#save_plan_detail_button').removeClass('hidden');
        $('#copyPlanBt').removeClass('hidden');
        $('#addMileBt').addClass('hidden');
        $('#delSomeMileBt').addClass('hidden');
        //$('.cmm-relation').unbind();

        if($('#tab_plan table').html() == undefined) {
            to_search_maintain_plan_detail();
        }

    }
}

function clear_tab(){
    $('#tab_mileage').empty();
    $('#tab_plan').empty();
}

function selectAllMileage(target){
    if($(target).hasClass('selected-style')){
        $(target).removeClass('selected-style');
        $('#tab_mileage tbody :checked').attr('checked', false);
    }else{
        $(target).addClass('selected-style');
        $('#tab_mileage tbody :checkbox').attr('checked', true);
    }
}

/* 查询保养里程数据列表 */
function to_search_maintain_mileage(){
    $('#tab_plan').empty();
    $('#addMileBt').unbind();
    $('#delSomeMileBt').unbind();
    $('#tab_mileage').html('数据加载中...');

    $.ajax({
        url: '/monkey/carMaintain/toGetMaintainMileage',
        type: 'POST',
        data: {maintainPlanId: maintainPlanId_},
        success: function (result) {
            if(result.length > 0){
                $('#tab_mileage').html(Mustache.render($('#maintain_mileage_template').html()));

                var i = 0;
                var dataMap = {
                    dataList: result,
                    seqFun: function(){
                        return ++i;
                    },
                    itemFun: function(){
                        if(this.itemFlag){
                            return '<span style="color:green;">已配置</span>';
                        }
                        return '<span style="color:red;">未配置</span>';
                    }
                };

                $('#tab_mileage tbody').html(Mustache.render($('#maintain_mileage_data_template').html(), dataMap));
            }else{
                $('#tab_mileage').html('没有数据！');
            }

            $('#addMileBt').click(function(){
                to_add_maintain_mileage();
            });

            $('#delSomeMileBt').click(function(){
                delSomeMile();
            });
        }
    });
}

/* 查询保养方案明细 */
function to_search_maintain_plan_detail(){
    editPlanFlag = false;
    $('#edit_plan_detail_button').unbind().html('编辑方案').removeClass('yellow').addClass('red');
    $('#save_plan_detail_button').unbind();
    $('#copyPlanBt').unbind();
    $('#tab_plan').html('数据加载中...');
    oldPlanDetail.length = 0;

    $.ajax({
        url:'/monkey/carMaintain/toGetMaintainPlanDetail',
        type:'POST',
        data:{maintainPlanId : maintainPlanId_},
        success:function(result){
            if(result.success){
                $('#tab_plan').html(Mustache.render($('#maintain_plan_detail_template').html()));
                var resultArray = result.data;
                var html = '';
                var array;
                var data;
                var len1 = resultArray.length;
                for(var i=0; i<len1; i++){
                    array = resultArray[i];
                    html += '<tr>';
                    var len2 = array.length;
                    for(var j=0; j<len2; j++){
                        if(array[j].flag===undefined){
                            if(i==0){
                                html += '<td style="text-align:center;vertical-align:middle;background:#f5f5f5;font-weight:bold;font-size:16px;">'+array[j]+'</td>';
                            }else{
                                html += '<td style="text-align:center;vertical-align:middle;">'+array[j]+'</td>';
                            }
                        }else{
                            if(array[j].flag===false){
                                array[j].flag = '';
                            }
                            if(array[j].flag===true){
                                array[j].flag = '<i class="fa fa-check" style="color:#b4b4b4;font-size:30px;"></i>';
                                data = new Object();
                                data.modelMileageId = array[j].modelMileageId;
                                data.maintainId = array[j].maintainId;
                                oldPlanDetail.push(data);
                            }
                            html += '<td title="'+array[j].title
                                + '" onclick="edit_maintain_plan(this);" class="cmm-relation" style="text-align:center;vertical-align:middle;"><input name="modelMileageId" type="hidden" value="'
                                + array[j].modelMileageId
                                + '"><input name="maintainId" type="hidden" value="'
                                + array[j].maintainId
                                + '">'
                                + array[j].flag
                                + '</td>';
                        }
                    }
                    html += '</tr>';
                }

                $('#tab_plan tbody').html(html);

                $('#edit_plan_detail_button').click(function(){
                    to_edit_maintain_plan();
                });

                $('#save_plan_detail_button').click(function(){
                    to_save_maintain_plan();
                });

                $('#copyPlanBt').click(function(){
                    to_copy_model_plan();
                });

            }else{
                $('#tab_plan').html(result.message);
            }
        }
    });
}

/* 车型保养方案数据列表增、删、改*/
function to_add_model_plan(){
    if(carModelObject==null){
        //alert('请先选择车型！');
        return;
    }

    $('#add_data_modal').modal('show');
    $('#add_data_form table').html($('#add_model_plan_template').html());
    $('#add_data_title').html('添加保养方案');
    $('#car_model_span').html(carModelText);
    $('#add_data_ok_button').unbind().click(function(){
        add_model_plan();
    });
}
function add_model_plan(){
    var formData = jq_serializeObject($('#add_data_form'));

    var maintainPlan = formData.maintainPlan.replace(/\s+/g,'');
    var checkFlag = true;
    if(maintainPlan==''){
        checkFlag = false;
    }

    if(!checkFlag){
        return;
    }

    formData.carModel = carModelObject.vehicleType;
    formData.carBrand = carModelObject.carBrand;
    formData.company = carModelObject.factoryName;
    formData.carSeries = carModelObject.carSeries;
    formData.maintainPlan = maintainPlan;

    var jsonStr = JSON.stringify(formData);

    $.ajax({
        url:'/monkey/carMaintain/toAddModelMaintainPlan',
        type:'POST',
        data:{jsonString : jsonStr},
        success:function(result){
            if(result.success){
                to_get_model_maintain_plan(998);
                $('#add_data_modal').modal('hide');
            }else{

            }
        }
    });

}

function to_modify_model_plan(id, name){
    var data = {
        'maintainPlan' : name
    };
    $('#add_data_modal').modal('show');
    $('#add_data_form table').html(Mustache.render($('#modify_model_plan_template').html(),data));
    $('#add_data_title').html('修改方案名称');
    $('#car_model_span').html(carModelText);
    $('#add_data_ok_button').unbind().click(function(){
        var formData = jq_serializeObject($('#add_data_form'));

        var maintainPlan = formData.maintainPlan.replace(/\s+/g,'');
        var checkFlag = true;
        if(maintainPlan==''){
            checkFlag = false;
        }
        if(!checkFlag){
            return;
        }
        formData.maintainPlan = maintainPlan;
        formData.id = id;

        var jsonStr = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/carMaintain/toModifyModelMaintainPlan',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_get_model_maintain_plan(998);
                    $('#add_data_modal').modal('hide');
                }else{

                }
            }
        });

    });
}

function to_delete_model_plan(id, target){
    //highlight_tr(target);

    $('#make_sure_modal').modal('show');
    $('#make_sure_msg').html('您确定要删除吗？');
    $('#make_sure_ok_button').unbind().click(function(){
        var data = {
            'id' : id,
            'isDeleted' : true
        };

        var jsonStr = JSON.stringify(data);

        $.ajax({
            url:'/monkey/carMaintain/toModifyModelMaintainPlan',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_get_model_maintain_plan(998);
                    $('#make_sure_modal').modal('hide');
                }else{

                }
            }
        });

    });
}

/* 查询车型保养方案列表 */
function to_get_model_maintain_plan(target){
    if(target==undefined){
        var params = get_params();
        if(params == null){
            //alert('请选择车型！');
            return;
        }
        carModelObject = params;
        carModelText = carModelObject.carBrand+' | '+carModelObject.factoryName+' | '+carModelObject.vehicleType;
    }

    clear_all();

    $.ajax({
        url:'/monkey/carMaintain/toGetModelMaintainPlan',
        type:'POST',
        data:carModelObject,
        success:function(result){
            var length = result.data.length;
            if(length > 0){
                $('#maintain_plan_table').html(Mustache.render($('#maintain_plan_template').html(), result));
            }else{
                $('#maintain_plan_table').html('没有数据！');
            }
        }
    });

}

/* 查看保养方案明细 */
function show_maintain_plan_detail(maintainPlanId, name, target){
    maintainPlanId_ = maintainPlanId;
    maintainPlanName = name;

    clear_car_info();
    clear_tab();

    $('#tab_mileage_li').removeClass('active');
    $('#tab_mileage').removeClass('active');
    $('#tab_plan_li').addClass('active');
    $('#tab_plan').addClass('active');
    init_tab_content('tab_plan');

    to_search_car_info(maintainPlanId, target);

    highlight_tr(target);

    $('#currentCarModel').html('当前车型：'+carModelText);
    $('#currentMaintainPlan').html('当前方案：'+maintainPlanName);

    $('#refreshBt').unbind().click(function(){
        if('tab_mileage' == tabId_){
            to_search_maintain_mileage();
        }else if('tab_plan' == tabId_){
            to_search_maintain_plan_detail();
        }
    });
}

/* =================车型保养方案相关操作 end==================== */


/* =================保养里程相关操作 start==================== */
function change_add_mileage_type(target){
    var type = target.value;
    if(type==1){
        $('#add_mileage_tbody').html($('#add_mileage_template').html());
    }else if(type==2){
        $('#add_mileage_tbody').html($('#add_mileage_batch_template').html());
    }
}

function to_add_maintain_mileage(){
    $('#add_mileage_modal').modal('show');
    $('#add_mileage_tbody').html($('#add_mileage_template').html());
    $('#add_mileage_car').html(carModelText);
    $('#add_mileage_plan').html(maintainPlanName);
    $('#add_mileage_modal select').val(1);
    $('#add_mileage_ok_button').unbind().click(function(){
        add_maintain_mileage();
    });
}
function add_maintain_mileage(){
    var formData = jq_serializeObject($('#add_mileage_modal form'));
    var addType = formData.addType;
    var mileage;
    var checkFlag = true;
    var jsonStr;
    if(addType==1){
        mileage = formData.mileage.replace(/\s+/g,'');
        if(mileage==''){
            checkFlag = false;
        }else{
            checkFlag = /^[1-9]+\d*$/i.test(mileage);
        }
        if(!checkFlag){
            return;
        }
        formData.maintainPlanId = maintainPlanId_;
        formData.mileage = mileage;

        jsonStr = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/carMaintain/toAddMaintainMileage',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_maintain_mileage();
                    $('#add_mileage_modal').modal('hide');
                }else{
                    alert(result.message);
                }
            }
        });

    }else if(addType==2){
        mileage = formData.mileage.replace(/\s+/g,'');
        var addStep = formData.addStep.replace(/\s+/g,'');
        var addNum = formData.addNum.replace(/\s+/g,'');
        if(mileage==''){
            checkFlag = false;
        }else{
            if(!/^[1-9]+\d*$/i.test(mileage)){
                checkFlag = false;
            }
        }
        if(addStep==''){
            checkFlag = false;
        }else{
            if(!/^[1-9]+\d*$/i.test(addStep)){
                checkFlag = false;
            }
        }
        if(addNum==''){
            checkFlag = false;
        }else{
            if(!/^[1-9]+\d*$/i.test(addNum)){
                checkFlag = false;
            }
        }
        if(!checkFlag){
            return;
        }
        formData.maintainPlanId = maintainPlanId_;
        formData.mileage = mileage;
        formData.addStep = addStep;
        formData.addNum = addNum;

        jsonStr = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/carMaintain/toAddMaintainMileageBatch',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_maintain_mileage();
                    $('#add_mileage_modal').modal('hide');
                }else{

                }
            }
        });

    }

}

function to_modify_maintain_mileage(id, mileage){
    var data = new Object();
    data.mileage = mileage;

    $('#add_data_modal').modal('show');
    $('#add_data_form table').html(Mustache.render($('#modify_mileage_template').html(), data));
    $('#add_data_title').html('修改保养里程');
    $('#car_model_span').html(carModelText);
    $('#maintain_plan_span').html(maintainPlanName);
    $('#add_data_ok_button').unbind().click(function(){

        var formData = jq_serializeObject($('#add_data_form'));
        var mileage = formData.mileage.replace(/\s+/g,'');
        var checkFlag = true;
        if(mileage==''){
            checkFlag = false;
        }else{
            checkFlag = /^[1-9]+\d*$/i.test(mileage);
        }
        if(!checkFlag){
            return;
        }
        formData.mileage = mileage;
        formData.id = id;
        formData.maintainPlanId = maintainPlanId_;
        var jsonStr = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/carMaintain/toModifyMaintainMileage',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_maintain_mileage();
                    $('#add_data_modal').modal('hide');
                }else{
                    alert(result.message);
                }
            }
        });

    });

}

function to_delete_maintain_mileage(id, mileage, targetBt){
    $('#make_sure_modal').modal('show');
    $('#make_sure_msg').html('您确定要删除吗？');
    $('#make_sure_ok_button').unbind().click(function(){
        var data = {
            'id' : id,
            'isDeleted' : true,
            'mileage' : mileage,
            'maintainPlanId' : maintainPlanId_
        };

        var jsonStr = JSON.stringify(data);

        $.ajax({
            url:'/monkey/carMaintain/toDeleteMaintainMileage',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    $(targetBt).parent().parent().remove();
                    $('#tab_plan').empty();
                    $('#make_sure_modal').modal('hide');
                }else{

                }
            }
        });

    });
}

function delSomeMile(){
    var checkboxArray = $('#tab_mileage tbody :checked');
    var len = checkboxArray.length;
    if(len == 0){
        alert('请选择要删除的里程');
        return;
    }
    var idArray = [];
    for(var i=0; i<len; i++){
        idArray.push(checkboxArray.eq(i).val())
    }

    $('#make_sure_modal').modal('show');
    $('#make_sure_msg').html('您确定要删除选中的里程吗？');
    $('#make_sure_ok_button').unbind().click(function() {
        $.ajax({
            url: '/monkey/carMaintain/delSomeMile',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(idArray),
            success: function(result){
                if(result.success){
                    to_search_maintain_mileage();
                    $('#make_sure_modal').modal('hide');
                }else{

                }
            }
        });
    });

}

/* =================保养里程相关操作 end==================== */


/* 编辑保养方案按钮 */
function to_edit_maintain_plan(){
    editPlanFlag = !editPlanFlag;
    if(editPlanFlag){
        $('#edit_plan_detail_button').html('退出编辑').removeClass('red').addClass('yellow');
        $('.cmm-relation').mouseover(function(){
            on_mouse_over(this);
        });
        $('.cmm-relation').mouseout(function(){
            on_mouse_out(this);
        });
    }else{
        $('#edit_plan_detail_button').html('编辑方案').removeClass('yellow').addClass('red');
        $('.cmm-relation').unbind();
    }
}

/* 编辑保养方案 */
function edit_maintain_plan(target){
    if(!editPlanFlag){
        return;
    }
    //var inputs = $(target).children('input');
    //var carMileageId = $(target).children('input[name="carMileageId"]').val();
    //var carMileageId = inputs.get(0).value;
    //var maintainId = inputs.get(1).value;
    //var relation = $(target).attr('title');

    var imgs = $(target).children('i');
    if(imgs.length==0){

        $(target).append('<i class="fa fa-check" style="color:#b4b4b4;font-size:30px;"></i>');

        /*
        $('#make_sure_modal').modal('show');
        $('#make_sure_msg').html('您确定要<span style="color:red;font-weight:bold;">添加记录（'+relation+'）</span>吗？');
        $('#make_sure_ok_button').unbind().click(function(){

            var data = {
                'carMileageId' : carMileageId,
                'maintainId' : maintainId
            };

            var jsonStr = JSON.stringify(data);

            $.ajax({
                url:'/monkey/carMaintain/toAddCarMileageMaintainRelation',
                type:'POST',
                data:{jsonString : jsonStr},
                success:function(result){
                    if(result.success){
                        $(target).append('<img src="/resources/assets/img/check_26.png" style="">');
                        $('#make_sure_modal').modal('hide');
                    }else{

                    }
                }
            });

        });
        */
    }else{

        $(imgs.get(0)).remove();

        /*
        $('#make_sure_modal').modal('show');
        $('#make_sure_msg').html('您确定要<span style="color:red;font-weight:bold;">删除记录（'+relation+'）</span>吗？');
        $('#make_sure_ok_button').unbind().click(function(){

            var data = {
                'carMileageId' : carMileageId,
                'maintainId' : maintainId
            };

            var jsonStr = JSON.stringify(data);

            $.ajax({
                url:'/monkey/carMaintain/toDeleteCarMileageMaintainRelation',
                type:'POST',
                data:{jsonString : jsonStr},
                success:function(result){
                    if(result.success){
                        $(imgs.get(0)).remove();
                        $('#make_sure_modal').modal('hide');
                    }else{

                    }
                }
            });

        });
        */
    }
}

/* 保存保养方案 */
function to_save_maintain_plan(){
    var relatedTd = $('.cmm-relation').has('i');
    var data;
    var addArray = new Array();
    var deleteArray = new Array();
    var inputs;
    var modelMileageId;
    var maintainId;
    var flag;
    var i,j;

    //找出需要添加的数据
    for(i=0; i<relatedTd.length; i++){
        flag = false;
        inputs = $(relatedTd[i]).children('input');
        modelMileageId = inputs.get(0).value;
        maintainId = inputs.get(1).value;
        for(j=0; j<oldPlanDetail.length; j++){
            if(oldPlanDetail[j].modelMileageId==modelMileageId && oldPlanDetail[j].maintainId==maintainId){
                flag = true;
                break;
            }
        }
        if(!flag){
            data = new Object();
            data.modelMileageId = modelMileageId;
            data.maintainId = maintainId;
            addArray.push(data);
        }
    }
    if(addArray.length==0 && relatedTd.length==oldPlanDetail.length){
        alert('数据没有改动！');
        return;
    }

    //找出需要删除的数据
    for(i=0; i<oldPlanDetail.length; i++){
        flag = false;
        for(j=0; j<relatedTd.length; j++){
            inputs = $(relatedTd[j]).children('input');
            modelMileageId = inputs.get(0).value;
            maintainId = inputs.get(1).value;
            if(oldPlanDetail[i].modelMileageId==modelMileageId && oldPlanDetail[i].maintainId==maintainId){
                flag = true;
                break;
            }
        }
        if(!flag){
            deleteArray.push(oldPlanDetail[i]);
        }
    }

    if(addArray.length==0 && deleteArray.length==0){
        alert('数据没有改动！');
        return;
    }

    var msg = '<span style="color:red;font-weight:bold;">删除记录数：'+deleteArray.length+'条<br>添加记录数：'+addArray.length+'条</span><br>您确定要保存吗？'
    $('#make_sure_modal').modal('show');
    $('#make_sure_msg').html(msg);
    $('#make_sure_ok_button').unbind().click(function() {
        var ok_bt = this;
        ok_bt.disabled = "disabled";

        data = {
            'addArray' : addArray,
            'deleteArray' : deleteArray
        };

        var jsonStr = JSON.stringify(data);

        $.ajax({
            url:'/monkey/carMaintain/toEditMaintainPlan',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_maintain_plan_detail();
                    $('#tab_mileage').empty();
                    ok_bt.disabled = "";
                    $('#make_sure_modal').modal('hide');
                }else{
                    ok_bt.disabled = "";
                }
            }
        });

    });

}

function on_mouse_over(target){
    $(target).css('background', 'orange');
}

function on_mouse_out(target){
    $(target).css('background', '');
}

function highlight_tr(target){
    var tr = $(target).parent().parent();
    var trArray = tr.siblings();
    for(var i=0; i<trArray.length; i++){
        $(trArray[i]).css('background', '');
    }
    tr.css('background', 'rgba(223, 240, 216, 1)');
}

/* 初始化所有组件 */
function clear_all(){
    //tabId_ = 'tab_plan';
    maintainPlanId_ = 0;
    maintainPlanName = '';
    editPlanFlag = false;
    oldPlanDetail.length = 0;

    $('#edit_plan_detail_button').unbind();
    $('#save_plan_detail_button').unbind();
    $('#copyPlanBt').unbind();
    $('#addMileBt').unbind();
    $('#delSomeMileBt').unbind();
    $('#refreshBt').unbind();
    $('#'+tabId_).html('请选择一个保养方案！');
    $('#currentCarModel').html('');
    $('#currentMaintainPlan').html('');

    //初始化车款配置组件
    clear_car_info();
}


/* 保存车款方案配置 */
function to_save_car_maintain_plan(){
    if(maintainPlanId_==0){
        alert('请先选择一个方案！');
        return;
    }

    var deleteBox = $('#car_info_table input:not(:checked)');
    var addBox = $('#not_related_car_info_table input:checked');

    var deleteArray = new Array();
    var addArray = new Array();
    var data;
    for(var i=0; i<deleteBox.length; i++){
        data = new Object();
        data.maintainPlanId = maintainPlanId_;
        data.l_id = deleteBox[i].value;

        deleteArray.push(data);
    }
    for(var j=0; j<addBox.length; j++){
        data = new Object();
        data.maintainPlanId = maintainPlanId_;
        data.l_id = addBox[j].value;

        addArray.push(data);
    }

    if(addArray.length==0 && deleteArray.length==0){
        alert('数据没有改动！');
        return;
    }

    var msg = '<span style="color:red;font-weight:bold;">删除记录数：'+deleteArray.length+'条<br>添加记录数：'+addArray.length+'条</span><br>您确定要保存吗？'
    $('#make_sure_modal').modal('show');
    $('#make_sure_msg').html(msg);
    $('#make_sure_ok_button').unbind().click(function() {
        var ok_bt = this;
        ok_bt.disabled = "disabled";

        data = {
            'addArray' : addArray,
            'deleteArray' : deleteArray
        };

        var jsonStr = JSON.stringify(data);

        $.ajax({
            url:'/monkey/carMaintain/toModifyCarMaintainPlan',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_car_info(maintainPlanId_);
                    ok_bt.disabled = "";
                    $('#make_sure_modal').modal('hide');
                }else{
                    ok_bt.disabled = "";
                }
            }
        });

    });
}


/* 当前方案车款筛选条件 */

/*
 *   过滤力洋车型
 *   ft：燃料类型   cy：年款   cp：排量
 * */
function grep_car_info_list(carInfoList, ft, cy, cp){
    if(ft==-1){
        if(cy==-1){
            if(cp==-1){
                return carInfoList;
            }else{
                return $.grep(carInfoList,
                    function(el, idx){
                        return el.displacement==cp;
                    });
            }
        }else{
            if(cp==-1){
                return $.grep(carInfoList,
                    function(el, idx){
                        return el.modelYear==cy;
                    });
            }else{
                return $.grep(carInfoList,
                    function(el, idx){
                        return (el.modelYear==cy && el.displacement==cp);
                    });
            }
        }
    }else{
        if(cy==-1){
            if(cp==-1){
                return $.grep(carInfoList,
                    function(el, idx){
                        return el.fuelType==ft;
                    });
            }else{
                return $.grep(carInfoList,
                    function(el, idx){
                        return (el.fuelType==ft && el.displacement==cp);
                    });
            }
        }else{
            if(cp==-1){
                return $.grep(carInfoList,
                    function(el, idx){
                        return (el.fuelType==ft && el.modelYear==cy);
                    });
            }else{
                return $.grep(carInfoList,
                    function(el, idx){
                        return (el.fuelType==ft && el.modelYear==cy && el.displacement==cp);
                    });
            }
        }
    }
}

function change_car_info_ft(target){
    if(fuelTypeCount <= 1){
        return;
    }

    var cy = $('#car_info_year').val();
    var cp = $('#car_info_power').val();
    var carInfoList = grep_car_info_list(jqCopyArray(carInfoArray), target.value, cy, cp);

    //alert(carInfoList.length);
    $('#car_info_table').html(Mustache.render($('#car_info_template').html(), {carInfoList: carInfoList}));
}

function change_car_info_year(target){
    if(jqPropCount(carInfoYear)<=1){
        return;
    }

    var cy = target.value;
    var select = $('#car_info_power');
    var cp = select.val();
    select.get(0).options.length = 1;
    var op;
    if(cy==-1){
        //重置排量选择框
        for(var pro in carInfoPower){
            op = $('<option></option>');
            op.text(pro);
            op.val(pro);
            if(cp==pro){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }else{
        //重置排量选择框
        var array = carInfoYear[cy];
        for(var i=0; i<array.length; i++){
            op = $('<option></option>');
            op.text(array[i]);
            op.val(array[i]);
            if(cp==array[i]){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }

    var ft = $('#car_info_ft').val();

    var carInfoList = grep_car_info_list(jqCopyArray(carInfoArray), ft, cy, cp);

    $('#car_info_table').html(Mustache.render($('#car_info_template').html(), {carInfoList: carInfoList}));
    $('#car_info_checkbox').attr('checked', true);
}

function change_car_info_power(target){
    if(jqPropCount(carInfoPower)<=1){
        return;
    }

    var cp = target.value;
    var select = $('#car_info_year');
    var cy = select.val();
    select.get(0).options.length = 1;
    var op;
    if(cp==-1){
        //重置年款选择框
        for(var pro in carInfoYear){
            op = $('<option></option>');
            op.text(pro);
            op.val(pro);
            if(cy==pro){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }else{
        //重置年款选择框
        var array = carInfoPower[cp];
        for(var i=0; i<array.length; i++){
            op = $('<option></option>');
            op.text(array[i]);
            op.val(array[i]);
            if(cy==array[i]){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }

    var ft = $('#car_info_ft').val();

    var carInfoList = grep_car_info_list(jqCopyArray(carInfoArray), ft, cy, cp);

    $('#car_info_table').html(Mustache.render($('#car_info_template').html(), {carInfoList: carInfoList}));
    $('#car_info_checkbox').attr('checked', true);
}

/* 没有配置方案车款筛选条件 */
function change_not_related_ft(target){
    if(nrfuelTypeCount <= 1){
        return;
    }

    var cy = $('#not_related_year').val();
    var cp = $('#not_related_power').val();

    var carInfoList = grep_car_info_list(jqCopyArray(notRelatedCarInfoArray), target.value, cy, cp);

    //alert(carInfoList.length);
    $('#not_related_car_info_table').html(Mustache.render($('#not_related_car_info_template').html(), {notRelatedCarInfoList: carInfoList}));
}

function change_not_related_year(target){
    if(jqPropCount(notRelatedYear)<=1){
        return;
    }

    var cy = target.value;
    var select = $('#not_related_power');
    var cp = select.val();
    select.get(0).options.length = 1;
    var op;
    if(cy==-1){
        //重置排量选择框
        for(var pro in notRelatedPower){
            op = $('<option></option>');
            op.text(pro);
            op.val(pro);
            if(cp==pro){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }else{
        //重置排量选择框
        var array = notRelatedYear[cy];
        for(var i=0; i<array.length; i++){
            op = $('<option></option>');
            op.text(array[i]);
            op.val(array[i]);
            if(cp==array[i]){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }

    var ft = $('#not_related_ft').val();

    var carInfoList = grep_car_info_list(jqCopyArray(notRelatedCarInfoArray), ft, cy, cp);

    $('#not_related_car_info_table').html(Mustache.render($('#not_related_car_info_template').html(),
        {notRelatedCarInfoList: carInfoList}));
    $('#not_related_checkbox').attr('checked', false);
}

function change_not_related_power(target){
    if(jqPropCount(notRelatedPower)<=1){
        return;
    }

    var cp = target.value;
    var select = $('#not_related_year');
    var cy = select.val();
    select.get(0).options.length = 1;
    var op;
    if(cp==-1){
        //重置年款选择框
        for(var pro in notRelatedYear){
            op = $('<option></option>');
            op.text(pro);
            op.val(pro);
            if(cy==pro){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }else{
        //重置年款选择框
        var array = notRelatedPower[cp];
        for(var i=0; i<array.length; i++){
            op = $('<option></option>');
            op.text(array[i]);
            op.val(array[i]);
            if(cy==array[i]){
                op.attr('selected', true);
            }
            select.append(op);
        }
    }

    var ft = $('#not_related_ft').val();

    var carInfoList = grep_car_info_list(jqCopyArray(notRelatedCarInfoArray), ft, cy, cp);

    $('#not_related_car_info_table').html(Mustache.render($('#not_related_car_info_template').html(),
        {notRelatedCarInfoList: carInfoList}));
    $('#not_related_checkbox').attr('checked', false);
}


/* 全选框 */
function car_info_all_check(target, idStr){
    $('#'+idStr+' :checkbox').attr('checked', target.checked);

    //var val = target.checked;
    //var checkboxs = $('#'+idStr+' :checkbox');
    //var addBox = $('#not_related_car_info_table ');
    //for(var i=0; i<checkboxs.length; i++){
    //    $(checkboxs[i]).attr('checked', val);
    //}
}


/* 拷贝保养方案 */
function to_copy_model_plan(planId, planName){
    if(editPlanFlag){
        alert('请先保存方案');
        return;
    }

    /*$.ajax({
        url:'/monkey/carMaintain/toGetMaintainPlanDetail',
        type:'POST',
        data:{maintainPlanId : planId},
        success:function(result){
            if(result.success){
                $('#copy_plan_content').html($('#maintain_plan_detail_template').html());
                var resultArray = result.data;
                var html = '';
                var array;
                var data;
                for(var i=0; i<resultArray.length; i++){
                    array = resultArray[i];
                    html += '<tr>';
                    for(var j=0; j<array.length; j++){
                        if(array[j].flag===undefined){
                            if(i==0){
                                html += '<td style="text-align:center;vertical-align:middle;background:#f5f5f5;font-weight:bold;font-size:16px;">'+array[j]+'</td>';
                            }else{
                                html += '<td style="text-align:center;vertical-align:middle;">'+array[j]+'</td>';
                            }
                        }else{
                            if(array[j].flag===false){
                                array[j].flag = '';
                            }
                            if(array[j].flag===true){
                                array[j].flag = '<i class="fa fa-check" style="color:#b4b4b4;font-size:30px;"></i>';
                                data = new Object();
                                data.modelMileageId = array[j].modelMileageId;
                                data.maintainId = array[j].maintainId;
                                oldPlanDetail.push(data);
                            }
                            html += '<td title="'+array[j].title
                                + '" onclick="edit_maintain_plan(this);" class="cmm-relation" style="text-align:center;vertical-align:middle;"><input name="modelMileageId" type="hidden" value="'
                                + array[j].modelMileageId
                                + '"><input name="maintainId" type="hidden" value="'
                                + array[j].maintainId
                                + '">'
                                + array[j].flag
                                + '</td>';
                        }
                    }
                    html += '</tr>';
                }

                $('#copy_plan_content tbody').html(html);
                $('#copy_plan_content').append($('#copy_plan_template').html());
                $('#copy_plan_modal').modal('show');
            }else{

            }
        }
    });*/

    $('#copy_plan_content').html($('#copy_plan_template').html());
    $('#carBrand_cp').select2();
    $('#factoryName_cp').select2();
    $('#carSeries_cp').select2();
    $('#vehicleType_cp').select2();
    $('#copy_plan_modal').modal('show');
    initBrandSelect();
    $('#copy_plan_ok_button').unbind().click(function(){
        jqDisable(this);
        copyPlan(this);
    });
}

function initBrandSelect(){
    $.ajax({
        url: '/monkey/carInfoAll/toGetCarBrands',
        type: 'POST',
        success: function (result) {
            var select = $("#carBrand_cp");
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
                initCompanySelect(this.value);
            });
        }
    });

}

function initCompanySelect(carBrand){
    var select = $("#factoryName_cp");
    select.get(0).options.length = 1;

    $("#carSeries_cp").get(0).options.length = 1;
    $("#vehicleType_cp").get(0).options.length = 1;
    clear_select2("factoryName_cp", "[ 请选择厂家 ]");
    clear_select2("carSeries_cp", "[ 请选择车系 ]");
    clear_select2("vehicleType_cp", "[ 请选择车型 ]");

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
                initSeriesSelect(carBrand, this.value);
            });
        }
    });
}

function initSeriesSelect(carBrand, factoryName){
    var select = $("#carSeries_cp");
    select.get(0).options.length = 1;

    $("#vehicleType_cp").get(0).options.length = 1;
    clear_select2("carSeries_cp", "[ 请选择车系 ]");
    clear_select2("vehicleType_cp", "[ 请选择车型 ]");

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
                initVehicleTypeSelect(carBrand, factoryName, this.value);
            });
        }
    });
}

function initVehicleTypeSelect(carBrand, factoryName, carSeries){
    var select = $("#vehicleType_cp");
    select.get(0).options.length = 1;

    clear_select2("vehicleType_cp", "[ 请选择车型 ]");

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

        }
    });
}

function copyPlan(targetBt){
    var vehicleType = $('#vehicleType_cp').select2('val');
    if(vehicleType == -1){
        alert('请选择目标车型');
        jqEnable(targetBt);
        return;
    }

    var carBrand = $('#carBrand_cp').select2('val');
    var factoryName = $('#factoryName_cp').select2('val');
    var carSeries = $('#carSeries_cp').select2('val');

    if(carBrand==carModelObject.carBrand && factoryName==carModelObject.factoryName && carSeries==carModelObject.carSeries && vehicleType==carModelObject.vehicleType){
        alert('目标车型与拷贝车型一致，请重新选择');
        jqEnable(targetBt);
        return;
    }

    //alert(carBrand+' '+factoryName+' '+carSeries+' '+vehicleType+' '+maintainPlanId_);
    var dataMap = {
        carBrand: carBrand,
        company: factoryName,
        carSeries: carSeries,
        carModel: vehicleType,
        id: maintainPlanId_
    };

    $('#makeSureModal').modal('show');
    var msg = '您确定要将【'+carModelObject.carBrand+' '+carModelObject.vehicleType
        +'】的保养方案【'+maintainPlanName+'】<br>拷贝给【'+carBrand+' '+vehicleType+'】吗？';
    $('#makeSureMsg').html(msg);
    $('#makeSureCancelBt').unbind().click(function() {
        $('#makeSureModal').modal('hide');
        jqEnable(targetBt);
    });
    $('#makeSureOkBt').unbind().click(function() {
        $('#makeSureModal').modal('hide');
        $.ajax({
            url: '/monkey/carMaintain/copyModelMaintainPlan',
            type: 'POST',
            data: dataMap,
            success: function (result) {
                if (result.success) {
                    alert('拷贝成功');
                    $('#copy_plan_modal').modal('hide');
                } else {
                    alert('拷贝失败');
                }

                jqEnable(targetBt);
            }
        });
    });
}

