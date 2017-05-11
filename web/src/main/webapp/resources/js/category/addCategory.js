/**
 * Created by huangzhangting on 15/11/24.
 */

//添加分类

var add_vehicle_code;

function to_add(){
    $('#modify_cate_tbody').empty();

    $('#add_cate_modal').modal('show');

    $('#add_cate_table').empty().html($('#add_cate_init_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_part_detail_thead').html());

    $('#add_cate_ok_button').unbind().click(function(){
        to_add_part();
    });

    init_first_cate();
}

function to_add_part(){
    var formData = jq_serializeObject($('#add_cate_form'));
    delete formData.level;
    var checkFlag = true;
    var partName = replaceBracketsCN(formData.partName.replace(/\s+/g,''));
    var code = formData.code.replace(/\s+/g,'');
    if(partName==''){
        checkFlag = false;
        $("#add_cate_form input[name='partName']").css('border', 'solid 2px red');
    }else{
        $("#add_cate_form input[name='partName']").css('border', 'solid 1px #dcdcdc');
    }
    if(code==''){
        checkFlag = false;
        $("#add_cate_form input[name='code']").css('border', 'solid 2px red');
    }else{
        if(!checkPartCode(code)){
            checkFlag = false;
            $("#add_cate_form input[name='code']").css('border', 'solid 2px red');
        }else{
            $("#add_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
        }
    }
    if(formData.cateId==-1){
        checkFlag = false;
        alert('请选择零件所属分类');
    }
    if(!checkFlag){
        return;
    }

    var cateParentId = formData.cateParentId.split(',')[0];
    formData.cateParentId = cateParentId;
    formData.partName = partName;
    formData.code = code;
    formData.labelNameText = formData.labelNameText.replace(/\s+/g,'').replace(/，+/g,',');
    formData.alissNameText = formData.alissNameText.replace(/\s+/g,'').replace(/，+/g,',');
    //formData.vehicleCode = add_vehicle_code;

    var jsonString = JSON.stringify(formData);

    $.ajax({
        url:'/monkey/category/toAddCategoryPart',
        type:'POST',
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        data: jsonString,
        success:function(result){
            if(result.success){
                to_search_list(page_);
                $('#add_cate_modal').modal('hide');
            }else{
                alert(result.message);
            }
        }
    });
}

function init_first_cate(){

    first_cate_code = "";
    second_cate_code = "";
    third_cate_code = "";
    part_code = "";

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel: 1},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.categoryList;
            fcArray.sort(function(obj1, obj2){
                if(obj1.vehicleCode <= obj2.vehicleCode){
                    return -1;
                }
                return 1;
            });
            var op;
            for(var j in fcArray){
                op = $("<option></option>");
                op.val(fcArray[j].catId+","+fcArray[j].code+","+fcArray[j].vehicleCode);
                op.text(fcArray[j].vehicleCode+' '+fcArray[j].catName);
                fcSelect.append(op);
            }

            fcSelect.unbind().change(function(){
                init_second_cate(this.value);
            });
        }
    });
}

function init_second_cate(pid){
    $('#add_cate_detail_tbody').empty();

    var cateSelect = $("#second_cate_add");
    cateSelect.get(0).options.length = 1;

    $("#third_cate_add").get(0).options.length = 1;
    $("#cateId_add").val(-1);

    second_cate_code = "";
    third_cate_code = "";

    if(pid == -1){
        first_cate_code = "";
        setPartSumCode("");
        return;
    }

    var array = pid.split(',');
    //first_cate_code = array[2]+array[1];
    first_cate_code = array[1];
    add_vehicle_code = array[2];
    setPartSumCode(first_cate_code);

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 2, catPid : parseInt(array[0])},
        success: function (result) {
            var cateArray = result.categoryList;
            var op;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId+","+cateArray[i].code);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

            cateSelect.unbind().change(function(){
                init_third_cate(this.value);
            });
        }
    });

}

function init_third_cate(pid){
    $('#add_cate_detail_tbody').empty();

    var cateSelect = $("#third_cate_add");
    cateSelect.get(0).options.length = 1;
    $("#cateId_add").val(-1);

    third_cate_code = "";

    if(pid == -1){
        second_cate_code = "";
        setPartSumCode(first_cate_code);
        return;
    }

    var array = pid.split(',');
    second_cate_code = array[1];
    setPartSumCode(first_cate_code+second_cate_code);

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel : 3, catPid : parseInt(array[0])},
        success: function (result) {
            var cateArray = result.categoryList;
            if(cateArray.length==0){
                third_cate_code = default_third_cate_code;
                setPartSumCode(first_cate_code+second_cate_code+third_cate_code);
                $("#cateId_add").val(array[0]);
                return;
            }
            var op;
            for(var i in cateArray){
                op = $("<option></option>");
                op.val(cateArray[i].catId+","+cateArray[i].code);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

            cateSelect.unbind().change(function(){

                if(this.value==-1){
                    third_cate_code = "";
                    setPartSumCode(first_cate_code+second_cate_code);
                    $("#cateId_add").val(-1);
                    $('#add_cate_detail_tbody').empty();

                    return;
                }
                var array = this.value.split(',');
                $("#cateId_add").val(array[0]);
                third_cate_code = array[1];
                setPartSumCode(first_cate_code+second_cate_code+third_cate_code);

                refresh_add_part_detail_tbody(array[0]);
            });
        }
    });

}

function setPartSumCode(code){
    $("#sumCode_add").val(code+part_code);
}

function partCodeChange(target){
    part_code = target.value.replace(/\s+/g,'');
    $(target).val(part_code);
    setPartSumCode(first_cate_code+second_cate_code+third_cate_code);
}


function refresh_add_cate_detail_tbody(level, pid){
    var add_cate_detail_tbody = $('#add_cate_detail_tbody').empty();

    $.ajax({
        url:'/monkey/category/toGetCategoryList',
        type:'POST',
        data:{catLevel : level, catPid : pid},
        success:function(result){
            var array = result.categoryList;

            var detail_html = '';
            var j;
            if(level==1){
                array.sort(function(obj1, obj2){
                    if(obj1.vehicleCode <= obj2.vehicleCode){
                        return -1;
                    }
                    return 1;
                });
                for(j in array){
                    detail_html += "<tr><td>"+array[j].vehicleCode+' '+array[j].catName+"</td><td style='color:red;'>"+array[j].code+"</td></tr>";
                }
            }else{
                for(j in array){
                    detail_html += "<tr><td>"+array[j].catName+"</td><td style='color:red;'>"+array[j].code+"</td></tr>";
                }
            }
            add_cate_detail_tbody.html(detail_html);
        }
    });
}

function refresh_add_part_detail_tbody(cateId){
    var add_cate_detail_tbody = $('#add_cate_detail_tbody').empty();

    $.ajax({
        url:'/monkey/category/toGetCategoryPartByCateId',
        type:'POST',
        data:{cateId : cateId},
        success:function(result){
            var array = result.partList;

            var detail_html = '';
            for(var j in array){
                detail_html += "<tr><td>"+array[j].partName+"</td><td style='color:red;'>"+array[j].code+"</td></tr>";
            }
            add_cate_detail_tbody.html(detail_html);
        }
    });
}


//选择添加类型
function chooseCateLevel(target){
    $('#add_cate_detail_tbody').empty();

    var level = parseInt(target.value);

    switch(level){
        case 1: getFirstCateHtml(); break;
        case 2: getSecondCateHtml(); break;
        case 3: getThirdCateHtml(); break;
        default: getPartHtml(); break;
    }

}

function getPartHtml(){

    $("#special_part").empty().html($('#part_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_part_detail_thead').html());

    $('#add_cate_ok_button').unbind().click(function(){
        to_add_part();
    });

    init_first_cate();
}

function to_add_category(){
    var formData = jq_serializeObject($('#add_cate_form'));
    var level = parseInt(formData.level);
    var checkFlag = true;
    var catName = replaceBracketsCN(formData.catName.replace(/\s+/g,''));
    var code = formData.code.replace(/\s+/g,'');
    if(catName==''){
        checkFlag = false;
        $("#add_cate_form input[name='catName']").css('border', 'solid 2px red');
    }else{
        $("#add_cate_form input[name='catName']").css('border', 'solid 1px #dcdcdc');
    }
    if(code==''){
        checkFlag = false;
        $("#add_cate_form input[name='code']").css('border', 'solid 2px red');
    }else{
        switch(level){
            case 1:
                if(!checkFirstCatCode(code)){
                    checkFlag = false;
                    $("#add_cate_form input[name='code']").css('border', 'solid 2px red');
                }else{
                    $("#add_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 2:
                if(!checkSecondCatCode(code)){
                    checkFlag = false;
                    $("#add_cate_form input[name='code']").css('border', 'solid 2px red');
                }else{
                    $("#add_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 3:
                if(!checkThirdCatCode(code)){
                    checkFlag = false;
                    $("#add_cate_form input[name='code']").css('border', 'solid 2px red');
                }else{
                    $("#add_cate_form input[name='code']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            default: break;
        }
    }

    if(level==1){
        //var vehicleCode = formData.vehicleCode.replace(/\s+/g,'');
        //if(vehicleCode==''){
        //    checkFlag = false;
        //}else{
        //    formData.vehicleCode = vehicleCode;
        //}
    }else{
        var parentId = formData.parentId;
        if(parentId==-1){
            checkFlag = false;
            alert('请选择上级分类');
        }
    }

    if(!checkFlag){
        return;
    }

    formData.catName = catName;
    formData.code = code;

    var jsonString = JSON.stringify(formData);

    $.ajax({
        url:'/monkey/category/toAddCategory',
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
                $('#add_cate_modal').modal('hide');
            }else{
                alert(result.message);
            }
        }
    });
}

function getThirdCateHtml(){
    $("#special_part").empty().html($('#third_cate_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_category_detail_thead').html());
    $('#add_category_detail_text').html('已有的三级分类');

    $('#second_cate_add').unbind().change(function(){
        if(this.value==-1){
            $('#add_cate_detail_tbody').empty();
            return;
        }
        refresh_add_cate_detail_tbody(3, this.value);
    });

    $('#add_cate_ok_button').unbind().click(function(){
        to_add_category();
    });

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel: 1},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.categoryList;
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
                fcSelect.append(op);
            }

            fcSelect.unbind().change(function(){
                $('#add_cate_detail_tbody').empty();
                var pid = this.value;
                var cateSelect = $("#second_cate_add");
                cateSelect.get(0).options.length = 1;
                if(pid == -1){
                    return;
                }

                $.ajax({
                    url: '/monkey/category/toGetCategoryList',
                    type: 'POST',
                    data: {catLevel : 2, catPid : pid},
                    success: function (result) {
                        var cateArray = result.categoryList;
                        for(var i in cateArray){
                            op = $("<option></option>");
                            op.val(cateArray[i].catId);
                            op.text(cateArray[i].catName);
                            cateSelect.append(op);
                        }

                    }
                });

            });

        }
    });

}

function getSecondCateHtml(){
    $("#special_part").empty().html($('#second_cate_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_category_detail_thead').html());
    $('#add_category_detail_text').html('已有的二级分类');

    $('#first_cate_add').unbind().change(function(){
        if(this.value==-1){
            $('#add_cate_detail_tbody').empty();
            return;
        }
        refresh_add_cate_detail_tbody(2, this.value);
    });

    $('#add_cate_ok_button').unbind().click(function(){
        to_add_category();
    });

    $.ajax({
        url: '/monkey/category/toGetCategoryList',
        type: 'POST',
        data: {catLevel: 1},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.categoryList;
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
                fcSelect.append(op);
            }

        }
    });

}

function getFirstCateHtml(){
    $("#special_part").empty().html($('#first_cate_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_category_detail_thead').html());
    $('#add_category_detail_text').html('已有的一级分类');

    refresh_add_cate_detail_tbody(1, -1);

    $('#add_cate_ok_button').unbind().click(function(){
        to_add_category();
    });
}

