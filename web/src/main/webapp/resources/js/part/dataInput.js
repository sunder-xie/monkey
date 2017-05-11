//全局h5 session缓存
var storage = window.sessionStorage;//localStorage;

$(document).ready(function(){
    var loadingEL = $('body');
    App.blockUI(loadingEL);
    //力洋库的车型数据展示
    liyangCar();

    $('#excle_file').change(function(){
        var file = $('#excle_file').get(0).files[0];
        if (file) {
            //判断是否为excle文件（.xlsx/.xls）
            var file_name = file.name;
            var ext = file_name.substring(file_name.lastIndexOf("."), file_name.length).toUpperCase();
            if (ext != ".XLSX" && ext != ".XLS" ){
                alert_fuc("文件需为xlsx或xls结尾的excle文件，请重新选择文件，谢谢~！");
                return false;
            }

            var fileSize = 0;
            if (file.size > 1024 * 1024)
                fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
            else
                fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

            //展示一行数据
            var upload_data = {
                'file_name':file_name,
                'fileSize':fileSize
            };

            var html = Mustache.render($('#upload_body_template').html(),upload_data);
            //console.log(file.name, fileSize, file.type);

            $('#table_result').html(html);

            //查看和导出按钮组
            $('#fail_btn_group').addClass("hidden");
            //上传结果组
            $('#upload_result_panel').addClass("hidden");

            $("#upload_progress").css("width", "0%");//将进度条设置为0
            $('#upload_progress_span').html("0%");

            table_button_click();
        }
        return true
    });


    //下载标准模板
    $('#down_template_btn').unbind("click").click(function(){
        window.location.href = "/monkey/part/dataInput/downLoadOfferGoodsTemplate";
        return false;
    });
    App.unblockUI(loadingEL);
});

//力洋库的车型数据展示
function liyangCar(){
    $('#fileupload').addClass('hidden');
    $("#search_factory").addClass("hidden");
    $("#search_series").addClass("hidden");
    $("#search_model").addClass("hidden");
    $('#fileupload').addClass("hidden");
    $('#table_result').html("");
    //品牌
    var brandMap = JSON.parse(storage.getItem("car_allbrand"));
    if(brandMap == undefined) {
        $.get("/monkey/car/getLiyangCar",{carType:'brand'},function(map){
            map['placeholder'] = '-请选择品牌-';
            var html = Mustache.render($('#car_select_template').html(), map);
            $('#search_brand').html(html);
            $("#search_brand").select2({
                placeholder: "-请选择品牌-",
                allowClear: true
            });
            storage.setItem("car_allbrand",JSON.stringify(map));

        });
    }else{
        var html = Mustache.render($('#car_select_template').html(), brandMap);
        $('#search_brand').html(html);
        $("#search_brand").select2({
            placeholder: "-请选择品牌-"
        });
    }


    //品牌变更
    $("#search_brand").change(function(){
        $("#search_factory").addClass("hidden");
        $("#search_series").addClass("hidden");
        $("#search_model").addClass("hidden");
        $('#fileupload').addClass("hidden");
        $("#excle_file").val("");

        var value = $("#search_brand").val();
        if(value == '-1'){
            return;
        }
        var brandValue = $("#search_brand").val();
        var itemKey = "car_factory_"+brandValue;
        var factoryMap = JSON.parse(storage.getItem(itemKey));

        if(factoryMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {carType: 'factory', carBrand: brandValue}, function (map) {
                map['placeholder'] = '-请选择厂商-';

                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_factory').html(html);
                $("#search_factory").removeClass("hidden");
                $("#search_factory").select2({
                    placeholder: "-请选择厂商-"
                });
                storage.setItem(itemKey,JSON.stringify(map));
            })
        }else{
            var html = Mustache.render($('#car_select_template').html(), factoryMap);
            $('#search_factory').html(html);
            $("#search_factory").removeClass("hidden");
            $("#search_factory").select2({
                placeholder: "-请选择厂商-"
            });
        }
    });

    //厂商变更
    $("#search_factory").change(function(){
        $("#search_series").addClass("hidden")
        $("#search_model").addClass("hidden")
        $('#fileupload').addClass("hidden");
        $("#excle_file").val("");

        var value = $("#search_factory").val();
        if(value == '-1'){
            return;
        }
        var brandValue = $("#search_brand").val();
        var factoryValue = $("#search_factory").val();
        var itemKey = "car_series_"+brandValue+factoryValue;
        var seriesMap = JSON.parse(storage.getItem(itemKey));

        if(seriesMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {
                carType: 'series',
                carBrand: brandValue,
                factoryName: factoryValue
            }, function (map) {
                map['placeholder'] = '-请选择车系-';

                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_series').html(html);
                $("#search_series").removeClass("hidden")
                $("#search_series").select2({
                    placeholder: "-请选择车系-"
                });
                storage.setItem(itemKey,JSON.stringify(map));
            })
        }else{
            var html = Mustache.render($('#car_select_template').html(), seriesMap);
            $('#search_series').html(html);
            $("#search_series").removeClass("hidden")
            $("#search_series").select2({
                placeholder: "-请选择车系-"
            });
        }
    });

    //车系变更
    $("#search_series").change(function(){
        $("#search_model").addClass("hidden")
        $('#fileupload').addClass("hidden");
        $("#excle_file").val("");

        var value = $("#search_series").val();
        if(value == '-1'){
            return;
        }
        var brandValue = $("#search_brand").val();
        var factoryValue = $("#search_factory").val();
        var seriesValue = $("#search_series").val();
        var itemKey = "car_model_"+brandValue+factoryValue+seriesValue;
        var modelMap = JSON.parse(storage.getItem(itemKey));

        if(modelMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {
                    carType: 'model', carBrand: brandValue,
                    factoryName: factoryValue, carSeries: seriesValue
                },
                function (map) {
                    map['placeholder'] = '-请选择车型-';

                    var html = Mustache.render($('#car_select_template').html(), map);
                    $('#search_model').html(html);
                    $("#search_model").removeClass("hidden");
                    $("#search_model").select2({
                        placeholder: "-请选择车型-"
                    });
                    storage.setItem(itemKey, JSON.stringify(map));
                });
        }else{
            var html = Mustache.render($('#car_select_template').html(), modelMap);
            $('#search_model').html(html);
            $("#search_model").removeClass("hidden");
            $("#search_model").select2({
                placeholder: "-请选择车型-"
            });
        }
    });

    //车型选择
    $("#search_model").change(function(){
        var value = $("#search_model").val();
        if(value == '-1'){
            return;
        }
        $('#table_result').html("");
        $("#excle_file").val("");

        $('#fileupload').removeClass("hidden");
        $('#brand').val($("#search_brand").val());
        $('#factory').val($("#search_factory").val());
        $('#series').val($("#search_series").val());
        $('#model').val($("#search_model").val());

        //console.log($("#search_model").val($("#search_brand").val()))
    });

}

//生成上传所在的table后的按钮事件
function table_button_click(){
    $('#reset_btn').unbind("click").click(function(){
        //将表单恢复到初始状态。
        $("#fileupload").resetForm();
        $('#table_result').html('');
    });

    $('#submit_btn').unbind("click").click(function(){
        $("#upload_progress").css("width", "0%");
        $('#upload_progress_span').html("0%");

        $('#submit_btn').removeClass("hidden");
        $('#reset_btn').addClass("hidden");
        $('#submit_btn > span').text("loading");
        $('#submit_btn').addClass("disabled");
        $('#submit_btn').parent().attr('cursor','not-allowed');

        //上传完毕后
        $("#fileupload").ajaxForm(
            function(data){
                //console.log(data);
                var failList = data.data;
                if(failList.length == 0){
                    $('#submit_btn').addClass("hidden");
                    $('#reset_btn').addClass("hidden");
                    $("#upload_result_panel").addClass("hidden");

                    //清除show页面的缓存
                    storage.clear();

                    //return true;
                }else{
                    clearInterval(time);
                    $("#upload_progress").css("width", "0%");//将进度条设置为0
                    $('#upload_progress_span').html("0%");
                    alert_fuc("上传失败");
                    $('#submit_btn > span').text("上传");
                    $('#upload_result_panel').removeClass("hidden");
                    $('#submit_btn').removeClass("hidden");
                    $('#reset_btn').removeClass("hidden");
                    $('#submit_btn').removeClass("disabled");
                    $('#submit_btn').parent().removeAttr('cursor');

                    var html = Mustache.render($('#fail_template').html(), {list:failList});
                    $('#faileReason').html(html);
                }

            }
        );

        //定时更新进度条
        var time=window.setInterval(function(){
            $.ajax({
                url:"/monkey/part/dataInput/getProgress?time="+new Date().getTime(),
                dataTypeString:"json",
                success:function(data){
                    if(data.progress == undefined || data.progress == '-1'){
                        // 不会到这里
                        $("#upload_progress").css("width", "0%");//将进度条设置为0
                        $('#upload_progress_span').html("0%");
                        clearInterval(time);
                        alert_fuc("上传失败，请重新上传！");
                        $('#submit_btn > span').text("上传");
                        $('#submit_btn').removeClass("disabled");
                        $('#submit_btn').parent().removeAttr('cursor');

                        $('#reset_btn').removeClass("hidden");
                        $('#submit_btn').removeClass("hidden");

                        //return false;
                    }else if(data.progress =='100%') {
                        alert_fuc("上传成功!");
                        $("#upload_progress").css("width", "100%");//将进度条设置为100
                        $('#upload_progress_span').html("100%");
                        clearInterval(time);
                        $('#submit_btn').addClass("hidden");
                        $('#reset_btn').addClass("hidden");
                        $("#upload_result_panel").addClass("hidden");
                        $('#submit_btn').removeClass("disabled");
                        $('#submit_btn').parent().removeAttr('cursor');

                    }else{
                        var progress = data.progress;
                        $("#upload_progress").css("width", progress);//将进度条设置为0
                        var html = progress;
                        if(progress == "95.0%"){
                            html="车型数据量过多，正在后台处理,请耐心！";
                        }
                        $('#upload_progress_span').html(html);
                    }
                }
            });


        },1500);
    })
}


function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return;
}