/**
 * 修改分类
 * Created by LYJ on 20151215.
 */
var modify_cate_bt_text = "";
var modifyLevel_ = -1;
var modifyCateId_ = -1;
var cate_stop_use_msg = "";

$(document).ready(function () {
    $('#modify_cate_button').click(function () {
        modify_cate_bt_text = "";
        modifyLevel_ = -1;
        modifyCateId_ = -1;
        cate_stop_use_msg = "";

        var vehicleCode = $("#vehicle_code").val();
        var levelF = $("#first_cate :selected").val();
        var levelS = $("#second_cate :selected").val();
        var levelT = $("#third_cate :selected").val();
        var partName = $("#partName").val().replace(/\s+/g, '');

        var flag = 1;
        if (vehicleCode != "") {
            modify_cate_bt_text += vehicleCode;
        }
        if (levelF != '') {
            flag = 0;
            modify_cate_bt_text += " > " + $("#first_cate :selected").text();
            modifyLevel_ = 1;
            modifyCateId_ = levelF;
        }
        if (levelS != '') {
            flag = 0;
            modify_cate_bt_text += " > " + $("#second_cate :selected").text();
            modifyLevel_ = 2;
            modifyCateId_ = levelS;
        }
        if (levelT != '') {
            flag = 0;
            modify_cate_bt_text += " > " + $("#third_cate :selected").text();
            modifyLevel_ = 3;
            modifyCateId_ = levelT;
        }
        if (flag == 1) {
            alert("请选择要修改的分类");
        } else {
            to_modify();
        }
    });

    $("#modify_version_button").click(function () {
        var msg = '确定更新导出excel时数据的版本号吗？';
        $('#make_sure_msg').html(msg);
        $('#make_sure_modal').modal('show');
        $('#make_sure_ok_button').unbind().click(function () {
            $.ajax({
                url: '/monkey/categoryXPart/refreshCatXPartExportExcelVersion',
                type: 'POST',
                data: {},
                success: function (result) {
                    $("#export_version").text("V"+result.data);
                    alert("版本号更新成功,当前的最新的版本是V" + result.data + " ,请下载使用最新的模板!");
                }
            });
            $('#make_sure_modal').modal('hide');
        });

    });
});

function to_modify() {
    $('#add_cate_table').empty();
    $('#modify_cate_modal').modal('show');
    $('#modify_cate_title').html(modify_cate_bt_text);
    $('#modify_cate_ok_button').unbind().click(function () {
        to_modify_category();
    });
    $('#cate_stop_use_button').unbind().click(function () {
        $('#make_sure_modal').modal('show');

        var msg = '您确定要停用 <span style="color:red;font-weight:bold;">' + modify_cate_bt_text + '</span> 吗？';
        $('#make_sure_msg').html(msg);

        $('#make_sure_ok_button').unbind().click(function () {
            $.ajax({
                url: '/monkey/categoryXPart/toStopUseCategory',
                type: 'POST',
                data: {cateId: modifyCateId_, level: modifyLevel_},
                success: function (result) {
                    if (result.success) {
                        switch (parseInt(modifyLevel_)) {
                            case 1:
                                refreshFirstCate();
                                break;
                            case 2:
                                refreshSecondCate();
                                break;
                            case 3:
                                refreshThirdCate();
                                break;
                            default:
                                break;
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
    switch (level) {
        case 1:
            to_modify_first_cate();
            break;
        case 2:
            to_modify_second_cate();
            break;
        case 3:
            to_modify_third_cate();
            break;
        default:
            break;
    }

}

function refreshFirstCate() {
    $("#second_cate").get(0).options.length = 1;
    $("#third_cate").get(0).options.length = 1;

    var cateSelect = $("#first_cate");
    cateSelect.get(0).options.length = 1;

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 1, vehicleCode: $("#vehicle_code").val()},
        success: function (result) {
            var dataArray = result.categoryAttrList;
            var op;
            for (var i in dataArray) {
                op = $("<option></option>");
                op.val(dataArray[i].catId);
                op.text(dataArray[i].catName);
                cateSelect.append(op);
            }
        }
    });
    to_search_list(1);
}

function refreshSecondCate() {
    var pid = $('#first_cate').val();
    if (pid == -1) {
        to_search_list(1);
        return;
    }

    $("#third_cate").get(0).options.length = 1;

    var cateSelect = $("#second_cate");
    cateSelect.get(0).options.length = 1;

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 2, parentId: pid},
        success: function (result) {
            var cateArray = result.categoryAttrList;
            var op;
            for (var i in cateArray) {
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

        }
    });
    to_search_list(1);
}

function refreshThirdCate() {
    var pid = $('#second_cate').val();
    if (pid == -1) {
        to_search_list(1);
        return;
    }

    var cateSelect = $("#third_cate");
    cateSelect.get(0).options.length = 1;

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 3, parentId: pid},
        success: function (result) {
            var cateArray = result.categoryAttrList;
            var op;
            for (var i in cateArray) {
                op = $("<option></option>");
                op.val(cateArray[i].catId);
                op.text(cateArray[i].catName);
                cateSelect.append(op);
            }

        }
    });
    to_search_list(1);
}

function to_modify_category() {
    var formData = jq_serializeObject($('#modify_cate_form'));
    var level = parseInt(modifyLevel_);
    var checkFlag = true;
    var catName = replaceBracketsCN(formData.catName.replace(/\s+/g, ''));
    var catCode = formData.catCode.replace(/\s+/g, '');
    if (catName == '') {
        checkFlag = false;
        $("#modify_cate_form input[name='catName']").css('border', 'solid 2px red');
    } else {
        $("#modify_cate_form input[name='catName']").css('border', 'solid 1px #dcdcdc');
    }
    if (catCode == '') {
        checkFlag = false;
        $("#modify_cate_form input[name='catCode']").css('border', 'solid 2px red');
    } else {
        switch (level) {
            case 1:
                if (!checkFirstCatCode(catCode)) {
                    checkFlag = false;
                    $("#modify_cate_form input[name='catCode']").css('border', 'solid 2px red');
                } else {
                    $("#modify_cate_form input[name='catCode']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 2:
                if (!checkSecondCatCode(catCode)) {
                    checkFlag = false;
                    $("#modify_cate_form input[name='catCode']").css('border', 'solid 2px red');
                } else {
                    $("#modify_cate_form input[name='catCode']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 3:
                if (!checkThirdCatCode(catCode)) {
                    checkFlag = false;
                    $("#modify_cate_form input[name='catCode']").css('border', 'solid 2px red');
                } else {
                    $("#modify_cate_form input[name='catCode']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            default:
                break;
        }
    }
    if (!checkFlag) {
        return;
    }

    formData.catName = catName;
    formData.catCode = catCode;

    formData.catLevel = level;

    var jsonString = JSON.stringify(formData);

    $.ajax({
        url: '/monkey/categoryXPart/toModifyCategory',
        type: 'POST',
        data: {catJsonString: jsonString},
        success: function (result) {
            if (result.success) {
                switch (level) {
                    case 1:
                        refreshFirstCate();
                        break;
                    case 2:
                        refreshSecondCate();
                        break;
                    case 3:
                        refreshThirdCate();
                        break;
                    default:
                        break;
                }
                $('#modify_cate_modal').modal('hide');
            } else {
                alert(result.message);
            }
        }
    });
}

function to_modify_first_cate() {
    $('#modify_cate_tbody').empty().html($('#first_cate_template').html());
    $('#modify_category_detail_text').html('已有的一级分类');

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAndUpperCat',
        type: 'POST',
        data: {catId: modifyCateId_},
        success: function (result) {
            var data = result.data;
            $("#modify_cate_form input[name='catId']").val(modifyCateId_);
            $("#modify_cate_form input[name='catName']").val(data.catName);
            $("#modify_cate_form input[name='catCode']").val(data.catCode);
        }
    });

    refresh_modify_cate_detail_tbody(-1);

}

function to_modify_second_cate() {
    var tbody = $('#modify_cate_tbody').empty();
    var vehicleCode = $('#vehicle_code :selected').text();
    var firstCateName = $('#first_cate :selected').text();
    var html = '<tr><th style="width:120px;">当前一级分类</th><td style="padding-left:10px;">'
        + '<input name="catId" type="hidden" value="' + modifyCateId_ + '">' + vehicleCode + ' ' + firstCateName + '</td></tr>';

    tbody.append(html);
    tbody.append($('#second_cate_template').html());

    $('#modify_category_detail_text').html('已有的二级分类');

    $('#first_cate_add').unbind().change(function () {
        if (this.value == -1) {
            $('#modify_cate_detail_tbody').empty();
            return;
        }
        refresh_modify_cate_detail_tbody(this.value);
    });

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAndUpperCat',
        type: 'POST',
        data: {catId: modifyCateId_, level: modifyLevel_},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.parentCategoryList;
            fcArray.sort(function (obj1, obj2) {
                if (obj1.vehicleCode <= obj2.vehicleCode) {
                    return -1;
                }
                return 1;
            });
            var op;
            for (var j in fcArray) {
                op = $("<option></option>");
                op.val(fcArray[j].catId);
                op.text(fcArray[j].vehicleCode + ' ' + fcArray[j].catName);
                if (firstCateName == fcArray[j].catName && vehicleCode == fcArray[j].vehicleCode) {
                    op.attr('selected', 'selected');
                }
                fcSelect.append(op);
            }

            var data = result.data;
            $("#modify_cate_form input[name='catName']").val(data.catName);
            $("#modify_cate_form input[name='catCode']").val(data.catCode);

            refresh_modify_cate_detail_tbody(data.parentId);
        }
    });
}

function to_modify_third_cate() {
    var tbody = $('#modify_cate_tbody').empty();

    var secondCateName = $('#second_cate :selected').text();
    var html = '<tr><th style="width:120px;">当前二级分类</th><td style="padding-left:10px;">'
        + '<input name="catId" type="hidden" value="' + modifyCateId_ + '">' + secondCateName + '</td></tr>';

    tbody.append(html);
    tbody.append($('#third_cate_template_modify').html());

    $('#modify_category_detail_text').html('已有的三级分类');

    $('#second_cate_add').unbind().change(function () {
        if (this.value == -1) {
            $('#modify_cate_detail_tbody').empty();
            return;
        }
        refresh_modify_cate_detail_tbody(this.value);
    });

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAndUpperCat',
        type: 'POST',
        data: {catId: modifyCateId_, level: modifyLevel_},
        success: function (result) {
            var scSelect = $("#second_cate_add");
            scSelect.get(0).options.length = 1;
            var array = result.parentCategoryList;
            var op;
            for (var j in array) {
                op = $("<option></option>");
                op.val(array[j].catId);
                op.text(array[j].catName);
                if (secondCateName == array[j].catName) {
                    op.attr('selected', 'selected');
                }
                scSelect.append(op);
            }

            var data = result.data;
            $("#modify_cate_form input[name='catName']").val(data.catName);
            $("#modify_cate_form input[name='catCode']").val(data.catCode);

            refresh_modify_cate_detail_tbody(data.parentId);
        }
    });
}

function refresh_modify_cate_detail_tbody(pid) {
    var modify_cate_detail_tbody = $('#modify_cate_detail_tbody').empty();

    var params = {};
    params.level = modifyLevel_;
    if (pid != -1) {
        params.parentId = pid;
    }
    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: params,
        success: function (result) {
            var array = result.categoryAttrList;

            var detail_html = '';
            var color = "red";
            var j;
            if (modifyLevel_ == 1) {
                array.sort(function (obj1, obj2) {
                    if (obj1.vehicleCode <= obj2.vehicleCode) {
                        return -1;
                    }
                    return 1;
                });
                for (j in array) {
                    if (modifyCateId_ == array[j].catId) {
                        color = "green";
                    } else {
                        color = "red";
                    }
                    detail_html += "<tr><td>" + array[j].vehicleCode + ' ' + array[j].catName + "</td><td style='color:" + color + ";'>" + array[j].catCode + "</td></tr>";
                }
            } else {
                for (j in array) {
                    if (modifyCateId_ == array[j].catId) {
                        color = "green";
                    } else {
                        color = "red";
                    }
                    detail_html += "<tr><td>" + array[j].catName + "</td><td style='color:" + color + ";'>" + array[j].catCode + "</td></tr>";
                }
            }
            modify_cate_detail_tbody.html(detail_html);
        }
    });
}

//-----------------Start modify part-----------------------------------------//
function initCatSelect(catArray, catSelect, catId, catName) {
    var op;
    catSelect.get(0).options.length = 1;
    for (var i in catArray) {
        op = new Option();
        op.value = catArray[i].catId + "," + catArray[i].catCode;
        op.text = catArray[i].catName;
        if (catArray[i].catId == catId && catArray[i].catName == catName) {
            op.selected = true;
        }
        catSelect.append(op);
    }
}

function reInitCatSelect(catSelect, level, parentId, vehicleCode) {
    catSelect.get(0).length = 1;
    if (level == 2) {
        $("#modify_part_form select[name='thirdCatId']").get(0).length = 1;
    }
    if (parentId != null && parentId == -1) {
        return;
    }
    if (vehicleCode != null && vehicleCode == -1) {
        return;
    }
    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: level, parentId: parentId, vehicleCode: vehicleCode},
        success: function (result) {
            var dataArray = result.categoryAttrList;
            var op;
            for (var i in dataArray) {
                op = $("<option></option>");
                op.val(dataArray[i].catId + "," + dataArray[i].catCode);
                op.text(dataArray[i].catName);
                catSelect.append(op);
            }
        }
    });
}

function changeSumCode() {
    var firstCatCode = $("#modify_part_form select[name='firstCatId']").val().split(",")[1];
    var secondCatCode = $("#modify_part_form select[name='secondCatId']").val().split(",")[1];
    var thirdCatCode = $("#modify_part_form select[name='thirdCatId']").val().split(",")[1];
    var partCode = $("#modify_part_form input[name='partCode']").val();
    var sumCodeNew = (firstCatCode == undefined ? " " : firstCatCode) + (secondCatCode == undefined ? "  " : secondCatCode) + (thirdCatCode == undefined ? "   " : thirdCatCode) + partCode;
    $("#modify_part_form input[name='sumCode']").val(sumCodeNew);
}
//修改零件
function to_modify_part(target) {
    var partId = $(target).prev().val();
    if (partId == '') {
        alert('没有可修改的零件');
        return;
    }

    $('#modify_part_modal').modal('show');

    $.ajax({
        url: '/monkey/categoryXPart/toGetCategoryPart',
        type: 'POST',
        data: {partId: partId},
        success: function (result) {
            var partObj = result.data;

            $("#modify_part_form select[name='vehicleCode']").val(partObj.vehicleCode);
            $("#modify_part_form input[name='id']").val(partObj.id);
            $("#modify_part_form input[name='partName']").val(partObj.partName);
            $("#modify_part_form input[name='partCode']").val(partObj.partCode);
            $("#modify_part_form input[name='sumCode']").val(partObj.sumCode);
            $("#modify_part_form input[name='labelNameText']").val(partObj.labelNameText);
            $("#modify_part_form input[name='alissNameText']").val(partObj.alissNameText);
            $("#modify_part_form select[name='catKind']").val(partObj.catKind);

            $("#currentCateName").html(partObj.firstCatName + " > " + partObj.secondCatName + " > " + partObj.thirdCatName + " > " + partObj.partName);

            //1J
            var catArrayFirst = result.categoryAttrListFirst;
            var catSelectFirst = $("#modify_part_form select[name='firstCatId']");
            initCatSelect(catArrayFirst, catSelectFirst, partObj.firstCatId, partObj.firstCatName);
            catSelectFirst.unbind().change(function () {
                var catId = $(this).val().split(",")[0];
                var catCode = $(this).val().split(",")[1];
                var selectTemp = $("#modify_part_form select[name='secondCatId']");
                reInitCatSelect(selectTemp, 2, catId, null);
                changeSumCode(0, 1, catCode);
            });

            //2J
            var catArraySecond = result.categoryAttrListSecond;
            var catSelectSecond = $("#modify_part_form select[name='secondCatId']");
            initCatSelect(catArraySecond, catSelectSecond, partObj.secondCatId, partObj.secondCatName);
            catSelectSecond.unbind().change(function () {
                var catId = $(this).val().split(",")[0];
                var catCode = $(this).val().split(",")[1];
                var selectTemp = $("#modify_part_form select[name='thirdCatId']");
                reInitCatSelect(selectTemp, 3, catId, null);
                changeSumCode(1, 3, catCode);
            });

            //3J
            var catArrayThird = result.categoryAttrListThird;
            var catSelectThird = $("#modify_part_form select[name='thirdCatId']");
            initCatSelect(catArrayThird, catSelectThird, partObj.thirdCatId, partObj.thirdCatName);

            catSelectThird.unbind().change(function () {
                var catCode = $(this).val().split(",")[1];
                changeSumCode(3, 6, catCode);
                refresh_modify_part_detail(this, partId);
            });

            //初始化右侧的 已有的零件
            var partArray = result.partList;
            var modify_part_detail = $('#modify_part_detail').empty();
            var part_detail_html = '';
            var color = "red";
            for (var j in partArray) {
                if (partArray[j].id == partId) {
                    color = "green";
                } else {
                    color = "red";
                }
                part_detail_html += "<tr><td>" + partArray[j].partName + "</td><td style='color:" + color + ";'>" + partArray[j].partCode + "</td></tr>";
            }
            modify_part_detail.html(part_detail_html);

        }
    });

    $("#modify_part_ok_button").unbind().click(function () {
        var formData = jq_serializeObject($('#modify_part_form'));
        $('#make_sure_modal').modal('show');

        var msg = '<span style="color:red;font-weight:bold;">' + replaceBracketsCN(formData.partName.replace(/\s+/g, '')) + '</span>的更改会涉及供应商、配件库或商品库的商品适配零件的更改，确定保存更改吗？';
        $('#make_sure_msg').html(msg);

        $('#make_sure_ok_button').unbind().click(function () {
            var partName = replaceBracketsCN(formData.partName.replace(/\s+/g, ''));
            var partCode = formData.partCode.replace(/\s+/g, '');
            var sumCode = formData.sumCode.replace(/\s+/g, '');

            var checkFlag = true;

            if (partName == '') {
                checkFlag = false;
                $("#modify_part_form input[name='partName']").css('border', 'solid 2px red');
            } else {
                $("#modify_part_form input[name='partName']").css('border', 'solid 1px #dcdcdc');
            }
            if (partCode == '') {
                checkFlag = false;
                $("#modify_part_form input[name='partCode']").css('border', 'solid 2px red');
            } else {
                if (!checkPartCode(partCode)) {
                    checkFlag = false;
                    $("#modify_part_form input[name='partCode']").css('border', 'solid 2px red');
                } else {
                    $("#modify_part_form input[name='partCode']").css('border', 'solid 1px #dcdcdc');
                }
            }

            if (formData.thirdCatId == -1) {
                checkFlag = false;
                alert('请选择零件所属分类');
            }

            if (sumCode == '') {
                checkFlag = false;
            }

            if (!checkFlag) {
                return;
            }

            formData.partName = partName;
            formData.partCode = partCode;
            formData.sumCode = sumCode;
            formData.labelNameText = formData.labelNameText.replace(/\s+/g, '').replace(/，+/g, ',');
            formData.alissNameText = formData.alissNameText.replace(/\s+/g, '').replace(/，+/g, ',');
            var firstCatId = formData.firstCatId.split(",")[0];
            formData.firstCatId = firstCatId;
            formData.firstCatName = $("#modify_part_form select[name='firstCatId']").find("option:selected").text();
            var secondCatId = formData.secondCatId.split(",")[0];
            formData.secondCatId = secondCatId;
            formData.secondCatName = $("#modify_part_form select[name='secondCatId']").find("option:selected").text();
            var thirdCatId = formData.thirdCatId.split(",")[0];
            formData.thirdCatId = thirdCatId;
            formData.thirdCatName = $("#modify_part_form select[name='thirdCatId']").find("option:selected").text();

            var jsonString = JSON.stringify(formData);

            $.ajax({
                url: '/monkey/categoryXPart/toModifyCategoryPart',
                type: 'POST',
                contentType: 'application/json;charset=utf-8',
                dataType: 'json',
                data: jsonString,
                success: function (result) {
                    if (result.success) {
                        to_search_list(page_);
                        $('#modify_part_modal').modal('hide');
                        $('#reset_button').trigger('click');
                    } else {
                        alert(result.message);
                    }
                },
                error: function () {
                    alert("零件修改失败！");
                }
            });

            $('#make_sure_modal').modal('hide');
            $('#modify_part_modal').modal('hide');
        });
    });


    var partName = $(target).prev().prev().val();
    $("#part_stop_use_button").unbind().click(function () {
        $('#make_sure_modal').modal('show');

        var msg = '您确定要停用 <span style="color:red;font-weight:bold;">' + partName + '</span> 吗？';
        $('#make_sure_msg').html(msg);

        $('#make_sure_ok_button').unbind().click(function () {
            $.ajax({
                url: '/monkey/categoryXPart/toStopUsePart',
                type: 'POST',
                data: {partId: partId},
                success: function (result) {
                    if (result.success) {
                        to_search_list(page_);
                        $('#make_sure_modal').modal('hide');
                        $('#modify_part_modal').modal('hide');
                    }
                }
            });
        });

    });

}

function refresh_modify_part_detail(target, partId) {

    var modify_part_detail = $('#modify_part_detail').empty();

    var cateId = target.value.split(",")[0];
    if (cateId == -1) {
        return;
    }

    $.ajax({
        url: '/monkey/categoryXPart/getPartAttrToUseInTable',
        type: 'POST',
        data: {thirdCatId: cateId},
        success: function (result) {
            var partArray = result.partAttrList;

            var part_detail_html = '';
            var color = "red";
            for (var j in partArray) {
                if (partArray[j].id == partId) {
                    color = "green";
                } else {
                    color = "red";
                }
                part_detail_html += "<tr><td>" + partArray[j].partName + "</td><td style='color:" + color + ";'>" + partArray[j].partCode + "</td></tr>";
            }
            modify_part_detail.html(part_detail_html);
        }
    });
}

function modifyPartToChange(target) {
    var part_code = target.value.replace(/\s+/g, '');
    $(target).val(part_code);
    changeSumCode();
}