var loadingHtml = '<img src="../../../resources/assets/img/ajax-loading.gif" alt="" class="loading">';
var pageSize = 5;
var storage = window.sessionStorage;//localStorage;

//定义全局的进度条对象
var progressTime;
$(document).ready(function(){
    $('#fail_col').addClass("hidden");
    $('#update_col').addClass("hidden");

    var loadingEL = $('body');
    App.blockUI(loadingEL);

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
        window.location.href = "/monkey/commodity/input/downLoadTemplate";
        event.stopPropagation();
        return false;
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
            function(resultMap){
                $('#successNum').text(resultMap.successNum);
                $('#failNum').text(resultMap.failNum);
                $('#upGoodsNum').text(resultMap.upGoodsNum);

                $('#fail_col').removeClass("hidden");

                var html = Mustache.render($('#fail_template').html(), resultMap);
                $('#fail_body').html(html);
                //$('#submit_btn').addClass("hidden");
                //清除show页面的缓存
                storage.removeItem("commodity_show_brandMap");
                storage.removeItem("commodity_show_partMap");
                storage.removeItem("commodity_show_brandPartList");
                storage.removeItem("commodity_show_partBrandList");
                //更新数据
                var upGoodsNum = resultMap.upGoodsNum;
                if(Number(upGoodsNum) > 0){
                    var upList = resultMap.upGoodsList;

                    var params = {};
                    var listArray = new Array();

                    var list = new Array();
                    var size = upList.length;
                    for(var i=0;i<size;i++){
                        list.push(upList[i]);
                        if(i%5 == 4 && i != size){
                            var map = {};
                            map['list'] = list;
                            listArray.push(map);
                            list = new Array();
                        }else if(i == (size-1)){
                            var map = {};
                            map['list'] = list;
                            listArray.push(map);
                        }
                    }
                    params['listArray'] = listArray;

                    var html = Mustache.render($('#update_template').html(), params);
                    $('#need_up_body').html(html);


                    confirm_fuc("现有"+upGoodsNum+"条商品编码数据跟数据库冲突，是否更新入库",updateNeedGoods,notUpdateNeedGoods);
                }
            }
        );

        //定时更新进度条
        progressTime =window.setInterval(function(){
            $.ajax({
                url:"/monkey/commodity/input/getProgress?time="+new Date().getTime(),
                dataTypeString:"json",
                success:function(data){
                    if(data.progress == undefined || data.progress == '-1'){
                        $("#upload_progress").css("width", "0%");//将进度条设置为0
                        $('#upload_progress_span').html("0%");
                        clearInterval(progressTime);
                        alert_fuc("上传失败，请重新上传！");
                        $('#submit_btn > span').text("上传");
                        $('#submit_btn').removeClass("disabled");
                        $('#submit_btn').parent().removeAttr('cursor');

                        $('#reset_btn').removeClass("hidden");
                        $('#submit_btn').removeClass("hidden");

                        //return false;
                    }else if(data.progress =='100%') {
                        $("#upload_progress").css("width", "100%");
                        $('#upload_progress_span').html("100%");
                        clearInterval(progressTime);
                        alert_fuc("成功");
                        $('#submit_btn').removeClass("disabled");
                        $('#submit_btn').parent().removeAttr('cursor');
                        $('#submit_btn > span').text("上传");

                    }else if(data.progress =='-2') {
                        //等待更新
                        $("#upload_progress").css("width", "80%");
                        $('#upload_progress_span').html("80%");

                    }else{
                        var progress = data.progress;
                        $("#upload_progress").css("width", progress);//将进度条设置为0

                        var html = progress;
                        if(progress == "98.0%"){
                            html="车型数据量过多，正在后台处理,请耐心！";
                        }
                        $('#upload_progress_span').html(html);
                    }
                }
            });


        },2000);
    })
}



function updateNeedGoods(){
    $('#update_header').html("已更新的数据");

    $.get("/monkey/commodity/input/repalceHave",function(result){
       if(result) {
           alert_fuc("更新成功");
           $('#update_col').removeClass("hidden");

       }else{
           alert_fuc("更新失败，联系开发或者重新登录试试");
       }
    });
}

function notUpdateNeedGoods(){
    $("#upload_progress").css("width", "100%");
    $('#upload_progress_span').html("100%");
    clearInterval(progressTime);
    alert_fuc("上传成功");
    $('#submit_btn').removeClass("disabled");
    $('#submit_btn').parent().removeAttr('cursor');

    $('#update_header').html("未更新的数据");
    $('#update_col').removeClass("hidden");

}




//=================辅助工具类

function confirm_fuc(text,successFuction,resetFuction){
    $('#confirm_model_content').html(text);
    $('#confirm_model').modal('show');

    $('#confirm_model_sure_button').unbind('click').click(function(){
        successFuction();
        $('#confirm_model').modal('hide');
    });
    $('#confirm_model_reset_button').unbind('click').click(function(){
        resetFuction();
        $('#confirm_model').modal('hide');
    });

    return;
}

function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return;
}