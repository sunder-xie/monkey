var pageSize = 20;

$(document).ready(function(){
    //初始化分页组件
    initPage(1, 1, 0, init_table);

    var searchParamJson = $('#searchParamJson').val();
    if(searchParamJson != ''){
        var param = JSON.parse(searchParamJson);
        $("#shopName").val(param.shopName);
        $("#shopTel").val(param.shopTel);
        $("#carName").val(param.carName);
        $("#selectStatus").select2('val', param.avidCallStatus);
    }

    init_table(1);

    //MONKEY.alertFunc({"content":"hhhassd是我"})
    search_click();

    $("#selectStatus").change(function(){
        $('#searchAvid_btn').trigger('click');
    });

});

function search_click(){
    //绑定回车事件
    $('input').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            $('#searchAvid_btn').trigger('click');
        }
    });

    $("#searchAvid_btn").click(function () {
        $("#this_page_index").val("0");
        init_table(1);
    })
}

var searchParam = null;
function init_table(page_index,size){
    if(size == undefined || size == null){
        size = pageSize
    }

    var this_page_index = $("#this_page_index").val();

    if(Number(this_page_index) != Number(page_index)) {
        var loadingEL = $('body').parent();
        App.blockUI(loadingEL);

        searchParam = {
            "shopName": $("#shopName").val().trim(),
            "shopTel": $("#shopTel").val().trim(),
            "carName": $("#carName").val().trim(),
            "avidCallStatus": $("#selectStatus").val(),
            "pageIndex": page_index,
            "pageSize": size
        };

        Ajax.post({
            url: '/monkey/avidCall/getAvidPage',
            data: searchParam,
            success: function (result) {
                App.unblockUI(loadingEL);
                if (!result.success) {
                    var html = '<tr><td style="font-size: 15px;color: red;">'+result.message+'</td></tr>';
                    $("#show_tbody").html(html);
                    //MONKEY.alertFunc({"content": result.message});
                    updatePage(0, 1, 1);
                    return false;
                }
                //生存数据到表格
                var the_data = result.data;
                var totalNumber = the_data.totalNumber;

                var body_html = template("avidTableListTemplate", {"showList": the_data.items});
                $("#show_tbody").html(body_html);

                //debugger;
                $("#this_page_index").val(page_index);
                updatePage(totalNumber, the_data.totalPage, page_index);//更新分页组件

                table_button_click();
            }
        });

    }
}


function table_button_click(){
    $(".fail_reason_btn").unbind("click").click(function () {
        MONKEY.alertFunc({"content": $(this).data("reason")});
        return false;
    });

    $('.edit-btn').click(function(){
        var id = $(this).data('id');
        window.location.href = '/monkey/avidCall/detail?id='+id+'&searchParamJson='+JSON.stringify(searchParam);
    });
}