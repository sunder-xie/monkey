/**
 * Created by huangzhangting on 15/11/24.
 */

//修改分类

function refreshFirstCate(){
    $("#second_cate").get(0).options.length = 1;
    $("#third_cate").get(0).options.length = 1;

    $.ajax({
        url: '/monkey/category/toGetVehicleCode',
        type: 'POST',
        success: function (result) {
            //alert(result.vehicleCodeList.length);
            var vcSelect = $("#vehicle_code");
            vcSelect.get(0).options.length = 1;
            var vcArray = result.vehicleCodeList;
            var op;
            for(var i in vcArray){
                op = $("<option></option>");
                op.val(vcArray[i]);
                op.text(vcArray[i]);
                if(vcArray[i]==params_.vehicleCode){
                    op.attr('selected', 'selected');
                }
                vcSelect.append(op);
            }

            to_init_fcSelect(params_.vehicleCode);

            //var fcSelect = $("#first_cate");
            //fcSelect.get(0).options.length = 1;
            //var fcArray = result.firstCategoryList;
            //for(var j in fcArray){
            //    op = $("<option></option>");
            //    op.val(fcArray[j].catId);
            //    op.text(fcArray[j].catName);
            //    if(fcArray[j].catId==params_.categoryId){
            //        op.attr('selected', 'selected');
            //    }
            //    fcSelect.append(op);
            //}

            to_search_list(1);
        }
    });

}

function refreshSecondCate(){
    var pid = $('#first_cate').val();
    if(pid==-1){
        to_search_list(1);
        return;
    }

    var cateSelect = $("#second_cate");
    cateSelect.get(0).options.length = 1;
    $("#third_cate").get(0).options.length = 1;

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 2, catPid : pid},
        success: function (result) {
            var cateArray = result.categoryList;
            var op;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

        }
    });

    to_search_list(1);
}

function refreshThirdCate(){
    var pid = $('#second_cate').val();
    if(pid==-1){
        to_search_list(1);
        return;
    }

    var cateSelect = $("#third_cate");
    cateSelect.get(0).options.length = 1;

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 3, catPid : pid},
        success: function (result) {
            var cateArray = result.categoryList;
            var op;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

        }
    });

    to_search_list(1);
}


function to_modify() {
    //alert(modifyLevel_+"   "+modifyCateId_);
    if(modifyLevel_==-1){
        return;
    }

    $('#add_cate_table').empty();
    $('#modify_cate_modal').modal('show');
    $('#modify_cate_title').html(modify_cate_bt_text);
    $('#modify_cate_ok_button').unbind().click(function(){
        to_modify_category();
    });
    $('#cate_stop_use_button').unbind().click(function(){
        $('#make_sure_modal').modal('show');

        var msg = '您确定要停用 <span style="color:red;font-weight:bold;">'+cate_stop_use_msg+'</span> 吗？';
        $('#make_sure_msg').html(msg);

        $('#make_sure_ok_button').unbind().click(function(){
            $.ajax({
                url:'/monkey/category/toStopUseCategory',
                type:'POST',
                data:{cateId : modifyCateId_},
                success:function(result){
                    if(result.success){
                        switch(parseInt(modifyLevel_)){
                            case 1: refreshFirstCate(); break;
                            case 2: refreshSecondCate(); break;
                            case 3: refreshThirdCate(); break;
                            default: break;
                        }
                        $('#make_sure_modal').modal('hide');
                        $('#modify_cate_modal').modal('hide');
                    }
                }
            });
        });

    });
    $('#modify_cate_detail_tbody').empty();

    var level = parseInt(modifyLevel_);
    switch(level){
        case 1: to_modify_first_cate(); break;
        case 2: to_modify_second_cate(); break;
        case 3: to_modify_third_cate(); break;
        default: break;
    }

}

function to_modify_category(){
    var formData = jq_serializeObject($('#modify_cate_form'));
    var level = parseInt(modifyLevel_);
    var checkFlag = true;
    var catName = replaceBracketsCN(formData.catName.replace(/\s+/g,''));
    var code = formData.code.replace(/\s+/g,'');
    if(catName==''){
        checkFlag = false;
        $("#modify_cate_form input[name='catName']").css('border', 'solid 2px red');
    }else{
        $("#modify_cate_form input[name='catName']").css('border', 'solid 1px #dcdcdc');
    }
    if(code==''){
        checkFlag = false;
        $("#modify_cate_form input[name='code']").css('border', 'solid 2px red');
    }else{
        switch(level){
            case 1:
                if(!checkFirstCatCode(code)){
                    checkFlag = false;
                    $("#modify_cate_form input[name='code']").css('border', 'solid 2px red');
                }else{
                    $("#modify_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 2:
                if(!checkSecondCatCode(code)){
                    checkFlag = false;
                    $("#modify_cate_form input[name='code']").css('border', 'solid 2px red');
                }else{
                    $("#modify_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 3:
                if(!checkThirdCatCode(code)){
                    checkFlag = false;
                    $("#modify_cate_form input[name='code']").css('border', 'solid 2px red');
                }else{
                    $("#modify_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            default: break;
        }
    }

    //if(level==1){
    //    var vehicleCode = formData.vehicleCode.replace(/\s+/g,'');
    //    if(vehicleCode==''){
    //        checkFlag = false;
    //    }else{
    //        formData.vehicleCode = vehicleCode;
    //    }
    //}

    if(!checkFlag){
        return;
    }

    formData.catName = catName;
    formData.code = code;
    formData.level = level;

    var jsonString = JSON.stringify(formData);

    $.ajax({
        url:'/monkey/category/toModifyCategory',
        type:'POST',
        data:{cateJsonString : jsonString},
        success:function(result){
            if(result.success){
                switch(level){
                    case 1: refreshFirstCate(); break;
                    case 2: refreshSecondCate(); break;
                    case 3: refreshThirdCate(); break;
                    default: break;
                }
                $('#modify_cate_modal').modal('hide');
            }else{
                alert(result.message);
            }
        }
    });
}

function to_modify_first_cate(){
    $('#modify_cate_tbody').empty().html($('#first_cate_template').html());
    $('#modify_category_detail_text').html('已有的一级分类');

    $.ajax({
        url: '/monkey/category/toGetCategoryById',
        type: 'POST',
        data: {cateId : modifyCateId_, level : modifyLevel_},
        success: function (result) {
            var data = result.data;
            $("#modify_cate_form input[name='catId']").val(modifyCateId_);
            $("#modify_cate_form input[name='catName']").val(data.catName);
            $("#modify_cate_form input[name='code']").val(data.code);
            $("#modify_cate_form select[name='vehicleCode']").val(data.vehicleCode);
            $("#modify_cate_form select[name='catKind']").val(data.catKind);
        }
    });

    refresh_modify_cate_detail_tbody(-1);

}

function to_modify_second_cate(){
    var tbody = $('#modify_cate_tbody').empty();
    var vehicleCode = $('#vehicle_code :selected').text();
    var firstCateName = $('#first_cate :selected').text();
    var html = '<tr><th style="width:120px;">当前一级分类</th><td style="padding-left:10px;">'
        +'<input name="catId" type="hidden" value="'+modifyCateId_+'">'+vehicleCode+' '+firstCateName+'</td></tr>';

    tbody.append(html);
    tbody.append($('#second_cate_template').html());

    $('#modify_category_detail_text').html('已有的二级分类');

    $('#first_cate_add').unbind().change(function(){
        if(this.value==-1){
            $('#modify_cate_detail_tbody').empty();
            return;
        }
        refresh_modify_cate_detail_tbody(this.value);
    });

    $.ajax({
        url: '/monkey/category/toGetCategoryById',
        type: 'POST',
        data: {cateId : modifyCateId_, level : modifyLevel_},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.parentCategoryList;
            fcArray.sort(function(obj1, obj2){
                if(obj1.vehicleCode <= obj2.vehicleCode){
                    return -1;
                }
                return 1;
            });
            var op;
            for(var j in fcArray){
                op = $("<option></option>");
                op.val(fcArray[j].catId);
                op.text(fcArray[j].vehicleCode+' '+fcArray[j].catName);
                if(firstCateName==fcArray[j].catName && vehicleCode==fcArray[j].vehicleCode){
                    op.attr('selected', 'selected');
                }
                fcSelect.append(op);
            }

            var data = result.data;
            $("#modify_cate_form input[name='catName']").val(data.catName);
            $("#modify_cate_form input[name='code']").val(data.code);
            $("#modify_cate_form select[name='catKind']").val(data.catKind);

            refresh_modify_cate_detail_tbody(data.parentId);
        }
    });
}

function to_modify_third_cate(){
    var tbody = $('#modify_cate_tbody').empty();

    var secondCateName = $('#second_cate :selected').text();
    var html = '<tr><th style="width:120px;">当前二级分类</th><td style="padding-left:10px;">'
        +'<input name="catId" type="hidden" value="'+modifyCateId_+'">'+secondCateName+'</td></tr>';

    tbody.append(html);
    tbody.append($('#third_cate_template_modify').html());

    $('#modify_category_detail_text').html('已有的三级分类');

    $('#second_cate_add').unbind().change(function(){
        if(this.value==-1){
            $('#modify_cate_detail_tbody').empty();
            return;
        }
        refresh_modify_cate_detail_tbody(this.value);
    });

    $.ajax({
        url: '/monkey/category/toGetCategoryById',
        type: 'POST',
        data: {cateId : modifyCateId_, level : modifyLevel_},
        success: function (result) {
            var scSelect = $("#second_cate_add");
            scSelect.get(0).options.length = 1;
            var array = result.parentCategoryList;
            var op;
            for(var j in array){
                op = $("<option></option>");
                op.val(array[j].catId);
                op.text(array[j].catName);
                if(secondCateName==array[j].catName){
                    op.attr('selected', 'selected');
                }
                scSelect.append(op);
            }

            var data = result.data;
            $("#modify_cate_form input[name='catName']").val(data.catName);
            $("#modify_cate_form input[name='code']").val(data.code);
            $("#modify_cate_form select[name='catKind']").val(data.catKind);

            refresh_modify_cate_detail_tbody(data.parentId);
        }
    });
}

function refresh_modify_cate_detail_tbody(pid){
    var modify_cate_detail_tbody = $('#modify_cate_detail_tbody').empty();

    $.ajax({
        url:'/monkey/category/toGetCategoryList',
        type:'POST',
        data:{catLevel : modifyLevel_, catPid : pid},
        success:function(result){
            var array = result.categoryList;

            var detail_html = '';
            var color = "red";
            var j;
            if(modifyLevel_==1){
                array.sort(function(obj1, obj2){
                    if(obj1.vehicleCode <= obj2.vehicleCode){
                        return -1;
                    }
                    return 1;
                });
                for(j in array){
                    if (modifyCateId_ == array[j].catId) {
                        color = "green";
                    } else {
                        color = "red";
                    }
                    detail_html += "<tr><td>"+array[j].vehicleCode+' '+array[j].catName+"</td><td style='color:" + color + ";'>"+array[j].code+"</td></tr>";
                }
            }else {
                for (j in array) {
                    if (modifyCateId_ == array[j].catId) {
                        color = "green";
                    } else {
                        color = "red";
                    }
                    detail_html += "<tr><td>" + array[j].catName + "</td><td style='color:" + color + ";'>" + array[j].code + "</td></tr>";
                }
            }
            modify_cate_detail_tbody.html(detail_html);
        }
    });
}


//修改零件
function to_modify_part(target){

    var partId = $(target).prev().val();
    if(partId==''){
        alert('没有可修改的零件');
        return;
    }

    $('#modify_part_modal').modal('show');

    $.ajax({
        url: '/monkey/category/toGetCategoryPart',
        type: 'POST',
        data: {partId : partId},
        success:function(result){
            var partObj = result.data;
            $("#modify_part_form input[name='id']").val(partObj.id);
            $("#modify_part_form input[name='partName']").val(partObj.partName);
            $("#modify_part_form input[name='code']").val(partObj.code);
            $("#modify_part_form input[name='sumCode']").val(partObj.sumCode);
            $("#modify_part_form input[name='labelNameText']").val(partObj.labelNameText);
            $("#modify_part_form input[name='alissNameText']").val(partObj.alissNameText);
            $("#modify_part_form select[name='catKind']").val(partObj.catKind);

            $("#currentCateName").html(partObj.catName);

            var cateArray = result.cateList;
            var cateSelect = $("#modify_part_form select[name='cateId']");
            cateSelect.get(0).options.length = 1;
            var op;
            for(var i in cateArray){
                op = new Option();
                op.value = cateArray[i].catId;
                op.text = cateArray[i].catName;
                if(cateArray[i].catName==partObj.catName){
                    op.selected = true;
                }
                cateSelect.append(op);
            }
            cateSelect.unbind().change(function(){
                refresh_modify_part_detail(this, partId);
            });

            var partArray = result.partList;
            var modify_part_detail = $('#modify_part_detail').empty();
            var part_detail_html = '';
            var color = "red";
            for(var j in partArray){
                if(partArray[j].id==partId){
                    color = "green";
                }else{
                    color = "red";
                }
                part_detail_html += "<tr><td>"+partArray[j].partName+"</td><td style='color:"+color+";'>"+partArray[j].code+"</td></tr>";
            }
            modify_part_detail.html(part_detail_html);

        }
    });

    $("#modify_part_ok_button").unbind().click(function(){
        var formData = jq_serializeObject($('#modify_part_form'));

        var partName = replaceBracketsCN(formData.partName.replace(/\s+/g,''));
        var code = formData.code.replace(/\s+/g,'');
        var sumCode = formData.sumCode.replace(/\s+/g,'');

        var checkFlag = true;

        if(partName==''){
            checkFlag = false;
            $("#modify_part_form input[name='partName']").css('border', 'solid 2px red');
        }else{
            $("#modify_part_form input[name='partName']").css('border', 'solid 1px #dcdcdc');
        }
        if(code==''){
            checkFlag = false;
            $("#modify_part_form input[name='code']").css('border', 'solid 2px red');
        }else{
            if(!checkPartCode(code)){
                checkFlag = false;
                $("#modify_part_form input[name='code']").css('border', 'solid 2px red');
            }else{
                $("#modify_part_form input[name='code']").css('border', 'solid 1px #dcdcdc');
            }
        }

        if(sumCode==''){
            checkFlag = false;
        }

        if(!checkFlag){
            return;
        }

        formData.partName = partName;
        formData.code = code;
        formData.sumCode = sumCode;
        formData.labelNameText = formData.labelNameText.replace(/\s+/g,'').replace(/，+/g,',');
        formData.alissNameText = formData.alissNameText.replace(/\s+/g,'').replace(/，+/g,',');

        var jsonString = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/category/toModifyCategoryPart',
            type:'POST',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            data: jsonString,
            success:function(result){
                if(result.success){
                    to_search_list(page_);
                    $('#modify_part_modal').modal('hide');
                }else{
                    alert(result.message);
                }
            }
        });

    });


    var partName = $(target).prev().prev().val();
    $("#part_stop_use_button").unbind().click(function(){
        $('#make_sure_modal').modal('show');

        var msg = '您确定要停用 <span style="color:red;font-weight:bold;">'+partName+'</span> 吗？';
        $('#make_sure_msg').html(msg);

        $('#make_sure_ok_button').unbind().click(function(){
            $.ajax({
                url:'/monkey/category/toStopUsePart',
                type:'POST',
                data:{partId : partId},
                success:function(result){
                    if(result.success){
                        to_search_list(page_);
                        $('#make_sure_modal').modal('hide');
                        $('#modify_part_modal').modal('hide');
                    }
                }
            });
        });

    });

}

function refresh_modify_part_detail(target, partId){

    var modify_part_detail = $('#modify_part_detail').empty();

    var cateId = target.value;
    if(cateId==-1){
        return;
    }

    $.ajax({
        url:'/monkey/category/toGetCategoryPartByCateId',
        type:'POST',
        data:{cateId : cateId},
        success:function(result){
            var partArray = result.partList;

            var part_detail_html = '';
            var color = "red";
            for(var j in partArray){
                if(partArray[j].id==partId){
                    color = "green";
                }else{
                    color = "red";
                }
                part_detail_html += "<tr><td>"+partArray[j].partName+"</td><td style='color:"+color+";'>"+partArray[j].code+"</td></tr>";
            }
            modify_part_detail.html(part_detail_html);
        }
    });
}

