var loadingHtml = '<img src="../../../resources/assets/img/ajax-loading.gif" alt="" class="loading">';
var pageSize = 5;

$(document).ready(function(){
    var loadingEL = $('body');
    App.blockUI(loadingEL);

    //初始化分页组件
    initPage(0, 1, 0, getFailData);
    //获得未处理的失败数据
    $.get("/monkey/offerGoods/dataInput/sumFailData",function(sum){
       if(Number(sum)>0){
           $('#confirm_model_content').html('您还有'+sum+'条未处理的失败数据，是否现在处理？');
           $('#fail_sum_number').text(sum);

           $('#confirm_model').modal('show');
       }
    });
    //处理未处理的失败数据
    $('#confirm_model_sure_button').click(function(){
        $('#fail_btn_group').removeClass("hidden");
        $('#upload_result_panel').removeClass("hidden");
        $('#fail_data_table').addClass("hidden");


        $('#confirm_model').modal('hide');
    });

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

            $('#table_result').html(html)

            //查看和导出按钮组
            $('#fail_btn_group').addClass("hidden");
            //上传结果组
            $('#upload_result_panel').addClass("hidden");
            $('#fail_data_table').addClass("hidden");

            $("#upload_progress").css("width", "0%");//将进度条设置为0
            $('#upload_progress_span').html("0%");

            table_button_click();
        }
        return true
    });



    //下载标准模板
    $('#down_template_btn').unbind("click").click(function(event){
        window.location.href = "/monkey/offerGoods/dataInput/downLoadOfferGoodsTemplate";
        event.stopPropagation();
        //event.preventDefault();
        return false;
    });

    //查看失败记录
    $('#data_see_btn').click(function(){
        $('#fail_data_table').removeClass("hidden");

        var pageIndex = 1;
        getFailData(pageIndex,pageSize);


    });
    //导出失败记录
    $('#data_out_excle_btn').click(function(){
        $.get("/monkey/offerGoods/dataInput/sumFailData",function(sum){
            if(Number(sum)>0){
                window.location.href = "/monkey/offerGoods/dataInput/failDataToExportExcel";
                $('#data_see_btn').addClass("hidden");
            }else{
                alert_fuc("失败数据已全部导出或后台正在导出下载，请耐心~！若时间过长，请刷新后重试")
            }
        });

    });

    App.unblockUI(loadingEL);
});

//生成上传所在的table后的按钮事件
function table_button_click(){
    $('#reset_btn').unbind("click").click(function(){
        //将表单恢复到初始状态。
        $("#fileupload").resetForm();
        $('#table_result').html('');
    });

    $('#submit_btn').click(function(){
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
                //上传成功后执行的方法
                uploadLoadSuccess(data.sum_number,data.success_number,data.fail_number);
                $('#submit_btn').addClass("hidden");
            }
        );

        //定时更新进度条
        var time=window.setInterval(function(){
            $.ajax({
                url:"/monkey/offerGoods/dataInput/getProgress?time="+new Date().getTime(),
                dataTypeString:"json",
                success:function(data){
                    if(data.progress == undefined || data.progress == '-1'){
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
                        $("#upload_progress").css("width", "100%");//将进度条设置为0
                        $('#upload_progress_span').html("100%");
                        clearInterval(time);
                        alert_fuc("上传成功");
                        $('#submit_btn').removeClass("disabled");
                        $('#submit_btn').parent().removeAttr('cursor');


                        //return true;
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


        },1000);
    })
}


//获得失败记录的变
function getFailData(pageIndex,size){

    //$('#fail_data_table_tbody').html(loadingHtml);
    if(size == undefined){
        size = pageSize
    }

    var this_page_index = $("#this_page_index").val();
    if(Number(this_page_index) != Number(pageIndex)) {
        var loadingEL = $('#fail_data_table');
        App.blockUI(loadingEL);

        $.get("/monkey/offerGoods/dataInput/searchFailData", {index: pageIndex, pageSize: size},
            function (result_map) {
                //console.log(result_map);
                App.unblockUI(loadingEL);


                var html = Mustache.render($('#fail_data_template').html(), result_map);
                $('#fail_data_table_tbody').html(html);

                $("#this_page_index").val(pageIndex);

                updatePage(result_map.totalNumber, result_map.totalPages, pageIndex);//更新分页组件
                //生成的table总的查看原因按钮
                $(".fail_data_see_failreason").unbind("click").click(function(){
                    $(this).parent().siblings().toggleClass("hidden");
                });
            }
        );
    }
}

//上传成功后
function uploadLoadSuccess(sum_number,success_number,fail_number){
    $('#sum_number').text(sum_number);
    $('#success_number').text(success_number);
    $('#fail_number').text(fail_number);

    $.get("/monkey/offerGoods/dataInput/sumFailData",function(sum){

        $('#upload_result_panel').removeClass("hidden");
        $('#data_see_btn').removeClass("hidden");
        if(Number(sum)>0){
            $('#fail_sum_number').text(sum);
            if(sum > 0) {
                exitFailData()
            }
        }
    });

}

//存在失败记录后
function exitFailData(){
    //查看和导出按钮出现
    $('#fail_btn_group').removeClass("hidden");
    //边框发光
    $('#fail_sum_number').parent().pulsate({
        color: "#d84a38",
        reach: 50,
        repeat: 10,
        speed: 1000,
        glow: true

    });
    $('#data_out_excle_btn').parent().pulsate({
        color: "#4cae4c",
        reach: 50,
        repeat: 10,
        speed: 1000,
        glow: true
    });

}

function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return;
}