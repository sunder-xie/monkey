$(function(){
    var loadingEL = $('body');
    App.blockUI(loadingEL);

    //初始化数据
    ready_data_func();
    //初始化按钮的点击时间
    ready_button_func();

    App.unblockUI(loadingEL);

});

//=========================一级调用的function========================
//初始化数据
function ready_data_func(){
    //总进度条
    $("#all_progress").knob({
        'stopper': true,
        'readOnly' : 'readonly',
        // UI
        'displayInput':true,
        'thickness' :  0.35,
        'inline' : true,
        'step' : 1
    });
    $("#all_progress").parent().css("display","block");
    $("#all_progress").parent().css("margin","0 auto");



    // 获得分类的总数
    getCateNum();
    //获得car总数
    getCarNum();
    //获得pool未导入的数据
    //getPoolNum();


}

//初始化按钮
function ready_button_func(){
    //=======总的====
    $('#data_out_excle_btn').click(function(){
        console.log("开始导出数据至电商");
        //判断是否存在reord的记录
        var loadingEL = $("#body");
        App.blockUI(loadingEL);
        $.get("/monkey/offerGoods/dataOutput/hasRecord",function(result){
            if(result){
                window.location.href = "/monkey/offerGoods/dataOutput/exportExcelForDianShang";
                App.unblockUI(loadingEL);
            }else{
                alert_fuc("数据均已导出，数据库无数据");
                App.unblockUI(loadingEL);
            }

        });

    });

    //=============car=====
    $('#start_car_btn').click(function(){
       console.log("开始车型匹配");
        $('#start_car_btn > span').text("loading");
        $('#start_car_btn').addClass("disabled");


        $.get("/monkey/offerGoods/dataOutput/startMatchCar",function(resultVO){
            $('#start_car_btn > span').text("开始车型匹配");
            $('#start_car_btn').removeClass("disabled");

            if(resultVO.success){
                alert_fuc("车型匹配成功");
                getCarNum();
                //getPoolNum();
            }else{
                alert_fuc("车型匹配失败，请重新登录重试");
            }

        });

    });
    //$('#downLoad_car_btn').click(function(){
    //    console.log("开始车型错误下载");
    //
    //    mainProgress();
    //
    //});

    //=============goods cate=====
    $('#start_cate_btn').click(function(){
        console.log("开始分类匹配");
        $('#start_cate_btn > span').text("loading");
        $('#start_cate_btn').addClass("disabled");


        $.get("/monkey/offerGoods/dataOutput/startMatchGoodsCate",function(resultVO){
            $('#start_cate_btn > span').text("开始分类匹配");
            $('#start_cate_btn').removeClass("disabled");

            var status = resultVO.success;
            if(status){
                alert_fuc("分类匹配成功");
                getCateNum();
                //getPoolNum();
            }else{
                alert_fuc("分类匹配失败，请重新登录重试");
            }

        });

    });
    $('#downLoad_cate_btn').click(function(){
        console.log("开始分类错误下载");
        window.location.href = "/monkey/offerGoods/dataOutput/failCateToExportExcel";

        $('#downLoad_cate_btn').addClass('hidden');
        var cate_progress = Number($('#cate_progress').width());
        cate_progress += 50;
        $('#cate_progress').css("width", cate_progress+"%");
        $('#cate_progress_span').text(cate_progress+"%");

        //mainProgress()
    });


}

//========================二级调用的function===============

//获得分类的总数
function getCateNum(){
    $.get("/monkey/offerGoods/dataOutput/get_cate_number",function(result_map){
        var cate_new_sum = result_map.cate_new_sum;
        var cate_not_excle_sum = result_map.cate_not_excle_sum;

        var cate_progress = 0;

        $('#cate_new_sum_span').text(cate_new_sum);
        $('#cate_not_excle_sum_span').text(cate_not_excle_sum);

        //按钮的操作,存在新的分类数，则出现开始匹配按钮；存在未导出的分类，则出现导出按钮
        if(Number(cate_new_sum) > 0){
            $('#start_cate_btn').removeClass('hidden');
        }else {
            $('#start_cate_btn').addClass('hidden');
            cate_progress += 50;

        }

        if(Number(cate_not_excle_sum) > 0){
            $('#downLoad_cate_btn').removeClass('hidden');
        }else{
            $('#downLoad_cate_btn').addClass('hidden');
            cate_progress += 50
        }


        $('#cate_progress').css("width", cate_progress+"%");
        $('#cate_progress_span').text(cate_progress+"%");


        //总进度条
        mainProgress();
    });
}

//获得车型的总数
function getCarNum(){
    $.get("/monkey/offerGoods/dataOutput/get_car_number",function(result_map){
        var car_new_sum = result_map.car_new_sum;
        var car_not_excle_sum = result_map.car_not_excle_sum;
        var car_progress = 0;

        $('#car_new_sum_span').text(car_new_sum);
        $('#car_not_excle_sum_span').text(car_not_excle_sum);

        //按钮的操作,存在新的分类数，则出现开始匹配按钮；存在未导出的分类，则出现导出按钮
        if(Number(car_new_sum) > 0){
            $('#start_car_btn').removeClass('hidden');
        }else{
            $('#start_car_btn').addClass('hidden');
            car_progress = 100;

        }

        //if(Number(car_not_excle_sum) > 0){
        //    $('#downLoad_car_btn').removeClass('hidden');
        //}else{
        //    $('#downLoad_car_btn').addClass('hidden');
        //    car_progress += 50;
        //
        //}

        $('#car_progress').css("width", car_progress+"%");
        $('#car_progress_span').text(car_progress+"%");

        //总进度条
        mainProgress();

    });
}

//查看全局的进度条进度。参数:增加的数量,减少的数量
function mainProgress(){
    var all_progress = 0;
    if($('#start_car_btn').hasClass('hidden')){
        all_progress += 34;
    }
    //if($('#downLoad_car_btn').hasClass('hidden')){
    //    all_progress += 16;
    //}
    if($('#start_cate_btn').hasClass('hidden')){
        all_progress += 33;
    }
    if($('#downLoad_cate_btn').hasClass('hidden')){
        all_progress += 33;
    }
    //if($('#start_goods_in_btn').hasClass('hidden')){
    //    all_progress += 18;
    //}
    //if($('#start_carRelation_in_btn').hasClass('hidden')){
    //    all_progress += 18;
    //}

    $('#all_progress').val(all_progress).trigger('change');

    if (all_progress == 100) {
        //如果record中无数据
        $('#data_out_excle_btn').removeClass('hidden');

        $('#data_out_excle_btn').parent().pulsate({
            color: "#d9534f", //脉冲的颜色
            reach: 8,   //脉冲出去的px长度
            repeat: 20, //重复的次数
            speed: 1000 //单次脉冲的时间：ms

        });

    }else{
        $('#data_out_excle_btn').addClass('hidden');
    }
}


function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return;
}