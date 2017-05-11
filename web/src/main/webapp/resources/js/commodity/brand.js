var pageSize = 10;
var deleteObj,saveObj,resetObj;

$(function(){
    //初始化分页组件
    initPage(1, 1, 0, getBrand);

    getBrand(1);

    $('#search_button').click(function(){
        $("#this_page_index").val('0');
        getBrand(1);
    });

    //table
    $("#table_add_brand").click(function(){
        $('#edit_model_nameCh').val("");
        $('#edit_model_nameEn').val("");
        $('#edit_model_firstLetter').val("");
        $('#edit_model_web').val("");
        $('#edit_model_code').val("");
        $('#edit_model_country').val("0");
        initTableModalSpan();

        $('#edit_model_pro').val("");

        $('#edit_model_id').val("");

        $('#edit_model_title').text("新建品牌");

        $('#edit_model').modal('show');
    });

    $('#edit_model_sure_button').click(function(){
        confirm_fuc("是否确定保存 "+$('#edit_model_title').text()+" ？",saveTableBrand);
    });

    //book
    $('#add_new_box').click(function(){
        console.log('========add_new_box========');
        var params = {};
        params['nameCh'] = "中文名";
        params['country'] = "0";
        var list = [];
        list.push(params);
        var map ={brandList:list};
        var html = getMainBrandBook(map);
        $('#brand_show_body').prepend(html);

        table_button_click();
    });

    //绑定回车事件
    $('#search_nameCh').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            $('#search_button').trigger('click');
        }
    });


});

//获得品牌数据
function getBrand(index,size){

    if(size == undefined || size == null){
        size = pageSize
    }


    var this_page_index = $("#this_page_index").val();

    if(Number(this_page_index) != Number(index)) {
        var loadingEL = $('body').parent();
        App.blockUI(loadingEL);

        var params = {};
        params['index'] = index;
        params['pageSize'] = size;

        var countryValue = $('#select_country').val();
        if(countryValue != '-1'){
            params['country'] = countryValue;
        }
        var searchName = $('#search_nameCh').val().trim();
        if(searchName != ''){
            params['nameKey'] = searchName;
        }

        $.get("/monkey/commodity/brand/searchBrandData", params, function (resultMap) {
            App.unblockUI(loadingEL);
            var totleNumber = resultMap.totalNumber;

            if(Number(totleNumber) == 0){
                $('#brand_show_body').html("");
                $('#brand_table_tbody').html("");
                table_button_click();
                alert_fuc("无此搜索数据~!");
                return false;
            }
            //书本版
            var html = getMainBrandBook(resultMap);
            $('#brand_show_body').html(html);
            //表格版
            var html = getMainBrandTable(resultMap);
            $('#brand_table_tbody').html(html);

            $("#this_page_index").val(index);
            updatePage(totleNumber, resultMap.totalPages, index);//更新分页组件

            table_button_click();
        });
    }
}

//表格中的按钮点击事件
function table_button_click(){

    //============table版的js=========================================
    $('.edit_brand_btn').unbind('click').click(function () {
        var obj = $(this).parent().parent();

        $('#edit_model_nameCh').val(obj.find(".nameCh").text().trim());
        $('#edit_model_nameEn').val(obj.find(".nameEn").text().trim());
        $('#edit_model_firstLetter').val(obj.find(".firstLetter").text().trim());
        $('#edit_model_web').val(obj.find(".onlineWebsite").text().trim());
        $('#edit_model_code').val(obj.find(".code").text().trim());
        $('#edit_model_pro').val(obj.find(".profile_body").text());

        $('#edit_model_country').val(obj.find(".country").val());
        initTableModalSpan();

        $('#edit_model_id').val(obj.find(".id").val());

        $('#edit_model_title').text("品牌："+obj.find(".nameCh").text());

        $('#edit_model').modal('show');
    });

    $(".profile_btn").click(function(){
       $(this).siblings("div").toggleClass("hidden");
    });

    $(".delete_brand_btn").unbind('click').click(function () {
        var obj = $(this).parent().parent();
        deleteObj = obj;
        confirm_fuc("是否确定删除 "+obj.find(".nameCh").text()+" 此品牌记录？",deleteTableBrand);
    });

    $('#edit_model_nameEn').keyup(function(){
        //中文都为空格
        var value = $(this).val().replace(/[\u4E00-\u9FA5]/g, '').replace(" ","");
        $(this).val(value);
    });

    $('#edit_model_firstLetter').keyup(function(){
        //非单词字符都为空格,并大写
        var value = $(this).val().replace(/[\W]/g, '').toUpperCase();
        $(this).val(value);
    });

    $('#edit_model_code').keyup(function(){
        //非单词字符都为空格
        var value = $(this).val().replace(/[\W]/g, '');
        $(this).val(value);
    });

    //============book 版的js=========================================
    //数据变动，保存和重置按钮出现
    $('#brand_show_body > .box').unbind("mouseout").mouseout(function(e){
        e.stopPropagation();
        var obj = $(this);
        var old_value = obj.find(".brandAllValue").val();

        var nameCh = obj.find(".nameCh").html().trim();
        var nameEn = obj.find(".nameEn").val().trim();
        var onlineWebsite = obj.find(".onlineWebsite").val().trim();
        var code = obj.find(".code").val().trim();
        var country = obj.find(".country").val().trim();
        var profile = obj.find(".profile").val().trim();

        var id = obj.find(".id").val().trim();
        //
        //if(id != "" && (nameCh == "" || nameEn == "" || code == "")){
        //    alert_fuc("品牌的中文名、英文名、编码为必填项");
        //    return false;
        //}

        var new_value = nameCh+"-"+nameEn+"-"+onlineWebsite+"-"+code+"-"+country+"-"+profile;
        if(old_value != new_value){
            obj.find(".operate_btn_div").removeClass("hidden");
        }else{
            obj.find(".operate_btn_div").addClass("hidden");

        }
    });

    $('.box > form > .row > .nameEn').keyup(function(){
        //非单词字符都为空格
        var value = $(this).val().replace(/[\u4E00-\u9FA5]/g, '').replace(" ","");
        $(this).val(value);
    });

    $('.box > form > .row > .firstLetter').keyup(function(){
        //非单词字符都为空格,并大写
        var value = $(this).val().replace(/[\W]/g, '').toUpperCase();
        $(this).val(value);
    });

    $('.box > form > .row > .code').keyup(function(){
        //非单词字符都为空格
        var value = $(this).val().replace(/[\W]/g, '');
        $(this).val(value);
    });
    //span标签点击
    $('span.edit').unbind('click').click(function(e){
        e.stopPropagation();
        var obj = $(this);
        obj.addClass("active");
        obj.siblings("span").removeClass("active");

        var value = obj.data("value");
        obj.siblings("input").val(value);
    });

    //save
    $('.save_btn').unbind('click').click(function(e){
        e.stopPropagation();
        saveObj = $(this).parent().parent();
        var title = saveObj.find(".nameCh").html().trim();
        confirm_fuc("是否确定保存 "+title+" 此品牌记录？",saveBookBrand);

    });

    //resert_btn
    $('.reset_btn').unbind('click').click(function(e){
        e.stopPropagation();
        resetObj = $(this).parent().parent();
        var title = resetObj.find(".nameCh").html().trim();
        confirm_fuc("是否确定重置 "+title+" 此品牌记录？",resetBookBrand);

    });

    //删除按钮
    $('.deleted_btn').unbind('click').click(function(e){
        e.stopPropagation();
        deleteObj = $(this);
        var title = deleteObj.parent().parent().find(".nameCh").html().trim();
        confirm_fuc("是否确定删除 "+title+" 此品牌记录？",deleteBookBrand);
    });


}


//===========================二级相关操作function========================================================================
function saveTableBrand(){

    var params = {};

    params['id'] = $('#edit_model_id').val().trim();
    params['nameCh'] = $('#edit_model_nameCh').val().trim();
    params['nameEn'] = $('#edit_model_nameEn').val().trim();
    params['code'] = $('#edit_model_code').val().trim();
    params['firstLetter'] = $('#edit_model_firstLetter').val().trim();
    params['onlineWebsite'] = $('#edit_model_web').val().trim();
    params['country'] = $('#edit_model_country').val().trim();
    params['profile'] = $('#edit_model_pro').val().trim();

    if(params['nameCh'] == "" && params['nameEn'] == ""){
        alert_fuc("品牌的中文名、英文名必须填写一项");
        return false;
    }
    if(params['code'] == ""){
        alert_fuc("编码为必填项");
        return false;
    }
    if(params['firstLetter'] == ""){
        alert_fuc("首字母不能为空");
        return false;
    }
    if(params['firstLetter'].length > 1){
        alert_fuc("首字母有且仅有一位");
        return false;
    }
    $.post("/monkey/commodity/brand/saveBrand",params,function(result){
        if(result.success){
            $('#search_button').trigger('click');
            $("#edit_model").modal("hide");
            alert_fuc("保存成功");

        }else{
            alert_fuc("保存失败,原因："+result.message);
        }
    });

}

function deleteTableBrand(){
    var obj = deleteObj;
    var id = obj.find(".id").val();
    $.post("/monkey/commodity/brand/deleteCommodityBrand",{id:id},function(result){
        var is_ok = result.data;
        if(is_ok) {
            $('#search_button').trigger('click');
            alert_fuc("删除成功！")
        }else{
            alert_fuc("删除失败！")
        }
    });
}

function saveBookBrand(){
    var obj = saveObj;

    var params = {};
    params['id'] = obj.find(".id").val().trim();
    params['nameCh'] = obj.find(".nameCh").html().trim();
    params['nameEn'] = obj.find(".nameEn").val().trim();
    params['firstLetter'] = obj.find(".firstLetter").val().trim();
    params['onlineWebsite'] = obj.find(".onlineWebsite").val().trim();
    params['code'] = obj.find(".code").val().trim();
    params['country'] = obj.find(".country").val().trim();
    params['profile'] = obj.find(".profile").val().trim();

    if(params['nameCh'] == "" && params['nameEn'] == ""){
        alert_fuc("品牌的中文名、英文名必须填写一项");
        return false;
    }
    if(params['code'] == ""){
        alert_fuc("编码为必填项");
        return false;
    }
    if(params['firstLetter'] == ""){
        alert_fuc("首字母不能为空");
        return false;
    }
    if(params['firstLetter'].length > 1){
        alert_fuc("首字母有且仅有一位");
        return false;
    }
    $.post("/monkey/commodity/brand/saveBrand",params,function(result){
        if(result.success){
            //var id = result.data;
            //
            //obj.find(".id").val(id);
            //obj.find(".operate_btn_div").addClass("hidden");
            //
            //var new_value = params['nameCh']+"-"+params['nameEn']+"-"+params['onlineWebsite']+"-"+params['code']+"-"+params['country']+"-"+params['profile'];
            //obj.find(".brandAllValue").val(new_value);
            $('#search_button').trigger('click');

            alert_fuc("保存成功");
        }else{
            alert_fuc("保存失败");
        }
    });


}

function resetBookBrand(){
    var obj = resetObj;
    var old_value = obj.find(".brandAllValue").val();

    var old_array = old_value.split("-");
    var country = old_array[4];
    obj.find(".nameCh").html(old_array[0]);
    obj.find(".nameEn").val(old_array[1]);
    obj.find(".onlineWebsite").val(old_array[2]);
    obj.find(".code").val(old_array[3]);
    obj.find(".country").val(country);
    obj.find(".profile").val(old_array[5]);

    if(country == "0"){
        obj.find(".in-country").addClass("active");
        obj.find(".out-country").removeClass("active");
    }else{
        obj.find(".out-country").addClass("active");
        obj.find(".in-country").removeClass("active");
    }
    obj.find(".operate_btn_div").addClass("hidden");
}

function deleteBookBrand(){
    var obj = deleteObj;
    var id = obj.data("id");
    if(id != undefined && id != ""){
        $.post("/monkey/commodity/brand/deleteCommodityBrand",{id:id},function(result){
            var is_ok = result.data;
            if(is_ok) {
                $('#search_button').trigger('click');

                alert_fuc("删除成功！");
            }else{
                alert_fuc("删除失败！")
            }
        });
    }else{
        //$('#search_button').trigger('click');
        obj.parent().parent().remove();
        alert_fuc("删除成功！")
    }
}

//获得品牌页面的每个box数据--书本版
function getMainBrandBook(Map){
    //自定义函数
    var map = {
        brandList:Map.brandList,
        statusRenderer:function(){
            var country = Number(this.country);
            var html = '<input type="hidden" class="country" value="'+country+'">';
            if(country == 0){
                html += '<span class="col-md-2 edit in-country active" data-value="0" style="text-align: center" >国内</span>';
                html +=    '<span class="col-md-2 out-country edit" data-value="1" style="text-align: center">国外</span>';
                return html;
            }else if(country == 1){
                html += '<span class="col-md-2 in-country edit " data-value="0" style="text-align: center" >国内</span>';
                html +=    '<span class="col-md-2 out-country edit active" data-value="1" style="text-align: center">国外</span>';
                return html;
            }
        }
    };

    var html = Mustache.render($('#brand_book_show_template').html(), map);
    return html;
}

//获得品牌页面的每个box数据--表格版
function getMainBrandTable(Map){
    //自定义函数
    var index = 0;
    var map = {
        brandList:Map.brandList,
        statusRenderer:function(){
            index ++;
            return index;
        },
        countryRenderer:function(){
            var country = Number(this.country);
            if(country == 0){
                return "国内";
            }else{
                return "国外";
            }
        },
        profileRender: function () {
            var profile = this.profile.trim();
            var html = '<div class="profile_body hidden">'+profile+'</div>';
            if("" != profile){
                html = '<span class="profile_btn btn red">more</span>'+html;
            }
            return html;
        }
    };

    var html = Mustache.render($('#brand_table_show_template').html(), map);
    return html;
}

//table 弹窗初始化span
function initTableModalSpan(){
    var obj = $("#edit_model_country");
    var country = Number(obj.val());
    if(country == "0"){
        obj.siblings(".in-country").addClass("active");
        obj.siblings(".out-country").removeClass("active");
    }else{
        obj.siblings(".out-country").addClass("active");
        obj.siblings(".in-country").removeClass("active");
    }
}
function alert_fuc(text){
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');
    return;
}

function confirm_fuc(text,successFuction){
    $('#confirm_model_content').html(text);
    $('#confirm_model').modal('show');

    $('#confirm_model_sure_button').unbind('click').click(function(){
        successFuction();
        $('#confirm_model').modal('hide');
    });

    return false;
}