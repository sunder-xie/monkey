
$(document).ready(function(){

    $('#excle_file').change(function(){
        var file = $('#excle_file').get(0).files[0];
        if (file) {
            //判断是否为excle文件（.xlsx/.xls）
            var fileName = file.name;
            var ext = fileName.substring(fileName.lastIndexOf('.')).toUpperCase();
            if (ext != '.XLSX' && ext != '.XLS' ){
                $('#excle_file').val('');
                alert('文件需为xlsx或xls结尾的excle文件，请重新选择文件，谢谢~！');
                return false;
            }

            var fileSize = 0;
            if (file.size > 1024 * 1024)
                fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
            else
                fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

            var uploadData = {
                'fileName':fileName,
                'fileSize':fileSize
            };

            var html = Mustache.render($('#upload_body_template').html(), uploadData);

            $('#table_result').html(html);

            file_table_button();
        }

    });

    //表单异步提交  这只是jquery.form.js插件的一种用法，还有其他用法
    $('#fileupload').ajaxForm();
    $('#fileupload').submit(function() {
        // 异步提交表单
        $(this).ajaxSubmit();
        // return false 阻止表单submit提交
        return false;
    });

});

//文件列表按钮添加事件
function file_table_button(){
    $('#reset_btn').unbind().click(function(){
        $('#fileupload').resetForm();
        $('#table_result').empty();
    });

    $('#submit_btn').unbind().click(function(){
        $('#submit_btn').addClass('hidden');
        $('#reset_btn').addClass('hidden');
        $('#loading_btn').removeClass('hidden');
        $('#upload_progress').css('width', '0%');
        $('#upload_progress_span').html('0%');

        //定时更新进度条
        var time = window.setInterval(function(){
            $.ajax({
                url:'/monkey/carInfoAll/toGetProgress?time='+(new Date()).getTime(),
                dataTypeString:'json',
                success:function(data){
                    if(data.progress == undefined || data.progress == '-1'){
                        clearInterval(time);
                        $('#upload_progress').css('width', '0%');
                        $('#upload_progress_span').html('0%');
                        alert('上传失败，请重新上传！');
                        $('#loading_btn').addClass('hidden');
                        $('#submit_btn').removeClass('hidden');
                        $('#reset_btn').removeClass('hidden');
                    }else if(data.progress == '100%') {
                        clearInterval(time);
                        $('#upload_progress').css('width', '100%');
                        $('#upload_progress_span').html('100%');
                        $('#loading_btn').html('<i class="fa fa-check-square-o"></i>上传成功 ');
                        $('#excle_file').val('');
                    }else{
                        $('#upload_progress').css('width', data.progress);
                        $('#upload_progress_span').html(data.progress);
                    }
                }
            });

        },1000);
    });

}


//获得失败记录的变
function getFailData(pageIndex,size){
    $('#fail_data_table_tbody').html(loadingHtml);
    if(size == undefined){
        size = pageSize
    }

    var this_page_index = $('#this_page_index').val();
    if(this_page_index != pageIndex) {
        $.get('/monkey/offerGoods/dataInput/searchFailData', {index: pageIndex, pageSize: size},
            function (result_map) {
                //console.log(result_map);

                var html = Mustache.render($('#fail_data_template').html(), result_map);
                $('#fail_data_table_tbody').html(html);

                $('#this_page_index').val(pageIndex);

                updatePage(result_map.totalNumber, result_map.totalPages, pageIndex);//更新分页组件
                //生成的table总的查看原因按钮
                $('.fail_data_see_failreason').unbind('click').click(function(){
                    $(this).parent().siblings().toggleClass('hidden');
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

    $.get('/monkey/offerGoods/dataInput/sumFailData',function(sum){

        $('#upload_result_panel').removeClass('hidden');
        $('#data_see_btn').removeClass('hidden');
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
    $('#fail_btn_group').removeClass('hidden');
    //边框发光
    $('#fail_sum_number').parent().pulsate({
        color: '#d84a38',
        reach: 50,
        repeat: 10,
        speed: 1000,
        glow: true

    });
    $('#data_out_excle_btn').parent().pulsate({
        color: '#4cae4c',
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