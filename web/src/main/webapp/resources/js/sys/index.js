var pageSize = 10;
$(function(){
    var loadingEL = $('body');
    App.blockUI(loadingEL);
    //初始化分页组件
    initPage(1, 1, 0, getRecord);

    getRecord(1);

    //button or span click
    toolsClick();
    App.unblockUI(loadingEL);


});

function toolsClick(){
    $("#reload_record_table").unbind("click").click(function(e){
        e.preventDefault();
        var el = jQuery(this).closest(".portlet").children(".portlet-body");
        App.blockUI(el);
        $("#this_page_index").val("0");
        getRecord(1,null,el);
    });

    jQuery('body').on('click', '.portlet > .portlet-title > .tools > .collapse, .portlet .portlet-title > .tools > .expand', function (e) {
        e.preventDefault();
        var el = jQuery(this).closest(".portlet").children(".portlet-body");
        if (jQuery(this).hasClass("collapse")) {
            jQuery(this).removeClass("collapse").addClass("expand");
            el.slideUp(200);
        } else {
            jQuery(this).removeClass("expand").addClass("collapse");
            el.slideDown(200);
        }
    });
}

//get record table
function getRecord(index,size,reloadEl){
    if(size == undefined || size == null){
        size = pageSize
    }


    var this_page_index = $("#this_page_index").val();

    if(Number(this_page_index) != Number(index)) {

        var params = {};
        params['index'] = index;
        params['pageSize'] = size;
        $.get("/monkey/main/getRecordPage", params, function (resultMap) {
            if(reloadEl != undefined){
                App.unblockUI(reloadEl);
            }
            var totleNumber = resultMap.totalNumber;

            if(Number(totleNumber) == 0){
                //var string = "暂时无数据~~！！";
                //alert_fuc(string);
                $('#record_table_tbody').html("无");
                return false;
            }

            //自定义函数
            var map = {
                recordList:resultMap.recordList,
                statusRenderer:function(){
                    var status = Number(this.status);
                    if(status == 0){
                        return '<span class="label label-success">'+this.statusString+'</span>'
                    }else if(status == 1){
                        return '<span class="label label-danger">'+this.statusString+'</span>'
                    }else if(status == 2){
                        return '<span class="label label-default">'+this.statusString+'</span>'
                    }
                }
            }
            var html = Mustache.render($('#record_table_body_template').html(), map);
            $('#record_table_tbody').html(html);

            $("#this_page_index").val(index);

            updatePage(totleNumber, resultMap.totalPages, index);//更新分页组件

        });
    }
}




function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return;
}