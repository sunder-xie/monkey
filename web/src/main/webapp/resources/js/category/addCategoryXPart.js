/**
 * 添加分类
 * Created by LYJ on 20151215.
 */

var part_code = "";
var first_cate_code = "";
var second_cate_code = "";
var third_cate_code = "";

function to_add() {
    $('#modify_cate_tbody').empty();

    $('#add_cate_modal').modal('show');

    $('#add_cate_table').empty().html($('#add_cate_init_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_part_detail_thead').html());

    $('#add_cate_ok_button').unbind().click(function () {
        to_add_part();
    });

    init();
}

function to_add_part() {
    var formData = jq_serializeObject($('#add_cate_form'));
    delete formData.level;
    var checkFlag = true;
    var partName = replaceBracketsCN(formData.partName.replace(/\s+/g, ''));
    var partCode = formData.partCode.replace(/\s+/g, '');
    if (partName == '') {
        checkFlag = false;
        $("#add_cate_form input[name='partName']").css('border', 'solid 2px red');
    } else {
        $("#add_cate_form input[name='partName']").css('border', 'solid 1px #dcdcdc');
    }
    if (partCode == '') {
        checkFlag = false;
        $("#add_cate_form input[name='partCode']").css('border', 'solid 2px red');
    } else {
        if (!checkPartCode(partCode)) {
            checkFlag = false;
            $("#add_cate_form input[name='partCode']").css('border', 'solid 2px red');
        } else {
            $("#add_cate_form input[name='partCode']").css('border', 'solid 1px #dcdcdc');
        }
    }
    if (formData.thirdCatId == -1) {
        checkFlag = false;
        alert('请选择零件所属分类');
    }
    if (!checkFlag) {
        return;
    }
    var firstCatId = formData.firstCatId.split(',')[0];
    formData.firstCatId = firstCatId;
    var secondCatId = formData.secondCatId.split(',')[0];
    formData.secondCatId = secondCatId;
    var thirdCatId = formData.thirdCatId.split(',')[0];
    formData.thirdCatId = thirdCatId;
    formData.partName = partName;
    formData.partCode = partCode;
    formData.labelNameText = formData.labelNameText.replace(/\s+/g, '').replace(/，+/g, ',');
    formData.alissNameText = formData.alissNameText.replace(/\s+/g, '').replace(/，+/g, ',');

    var jsonString = JSON.stringify(formData);

    $.ajax({
        url: '/monkey/categoryXPart/toAddCategoryPart',
        type: 'POST',
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        data: jsonString,
        success: function (result) {
            if (result.success) {
                to_search_list(page_);
                $('#add_cate_modal').modal('hide');
            } else {
                alert(result.message);
            }
        },
        error: function () {
            alert("新增零件失败！");
        }
    });
}

function init() {
    part_code = "";
    first_cate_code = "";
    second_cate_code = "";
    third_cate_code = "";

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 1},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            $("#second_cate_add").get(0).options.length = 1;
            $("#third_cate_add").get(0).options.length = 1;
            var fcArray = result.categoryAttrList;
            var op;
            for (var j in fcArray) {
                op = $("<option></option>");
                op.val(fcArray[j].catId + "," + fcArray[j].catCode + "," + fcArray[j].vehicleCode);
                op.text(fcArray[j].vehicleCode + ' ' + fcArray[j].catName);
                fcSelect.append(op);
            }
        }
    });

    $("#first_cate_add").change(function () {
        $('#add_cate_detail_tbody').empty();

        var first_cate_add_val = $("#first_cate_add").val();
        var valArray = first_cate_add_val.split(',');
        var cateSelect = $("#second_cate_add");

        cateSelect.get(0).options.length = 1;
        $("#third_cate_add").get(0).options.length = 1;

        first_cate_code = valArray[1] == undefined ? "" : valArray[1];
        setPartSumCode(first_cate_code);

        if (valArray[0] == '-1') {
            return;
        }

        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 2, parentId: valArray[0]},
            success: function (result) {
                var dataArray = result.categoryAttrList;
                var op;
                for (var i in dataArray) {
                    op = $("<option></option>");
                    op.val(dataArray[i].catId + "," + dataArray[i].catCode + "," + dataArray[i].vehicleCode);
                    op.text(dataArray[i].catName);
                    cateSelect.append(op);
                }
            }
        });
    });

    $("#second_cate_add").change(function () {
        $('#add_cate_detail_tbody').empty();

        var second_cate_add = $("#second_cate_add").val();
        var valArray = second_cate_add.split(',');
        var cateSelect = $("#third_cate_add");
        cateSelect.get(0).options.length = 1;

        second_cate_code = valArray[1] == undefined ? "" : valArray[1];
        setPartSumCode(first_cate_code + second_cate_code);

        if (valArray[0] == '-1') {
            return;
        }

        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 3, parentId: valArray[0]},
            success: function (result) {
                var dataArray = result.categoryAttrList;
                var op;
                for (var i in dataArray) {
                    op = $("<option></option>");
                    op.val(dataArray[i].catId + "," + dataArray[i].catCode + "," + dataArray[i].vehicleCode);
                    op.text(dataArray[i].catName);
                    cateSelect.append(op);
                }
            }
        });
    });

    $("#third_cate_add").change(function () {
        $('#add_cate_detail_tbody').empty();

        var third_cate_add = $("#third_cate_add").val();
        var valArray = third_cate_add.split(',');

        third_cate_code = valArray[1] == undefined ? "" : valArray[1];
        setPartSumCode(first_cate_code + second_cate_code + third_cate_code);

        if (valArray[0] == '-1') {
            return;
        }

        refresh_add_part_detail_tbody(valArray[0]);
    });
}

function refresh_add_part_detail_tbody(thirdCatId) {
    var add_cate_detail_tbody = $('#add_cate_detail_tbody').empty();
    $.ajax({
        url: '/monkey/categoryXPart/getPartAttrToUseInTable',
        type: 'POST',
        data: {thirdCatId: thirdCatId},
        success: function (result) {
            var array = result.partAttrList;

            initAddModelCodeText(4, array);

            var detail_html = '';
            for (var j in array) {
                detail_html += "<tr><td>" + array[j].partName + "</td><td style='color:red;'>" + array[j].partCode + "</td></tr>";
            }
            add_cate_detail_tbody.html(detail_html);
        }
    });
}

function setPartSumCode(code) {
    $("#sumCode_add").val(code + part_code);
}

function partCodeChange(target) {
    part_code = target.value.replace(/\s+/g, '');
    $(target).val(part_code);
    setPartSumCode(first_cate_code + second_cate_code + third_cate_code);
}


function refresh_add_cate_detail_tbody(level, pid, vehicleCode) {
    var add_cate_detail_tbody = $('#add_cate_detail_tbody').empty();

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: level, parentId: pid, vehicleCode: vehicleCode},
        success: function (result) {
            var array = result.categoryAttrList;

            initAddModelCodeText(level, array);

            var detail_html = '';
            var j;
            if (level == 1) {
                array.sort(function (obj1, obj2) {
                    if (obj1.vehicleCode <= obj2.vehicleCode) {
                        return -1;
                    }
                    return 1;
                });
                for (j in array) {
                    detail_html += "<tr><td>" + array[j].vehicleCode + ' ' + array[j].catName + "</td><td style='color:red;'>" + array[j].catCode + "</td></tr>";
                }
            } else {
                for (j in array) {
                    detail_html += "<tr><td>" + array[j].catName + "</td><td style='color:red;'>" + array[j].catCode + "</td></tr>";
                }
            }
            add_cate_detail_tbody.html(detail_html);
        }
    });
}

function initAddModelCodeText(level, array) {
    if (level != 1 && level != 2 && level != 3 && level != 4) {
        return;
    }

    var num, codeTemp;

    if (array.length == 0) {
        num = 0;
    } else {
        if (level == 4) {
            num = Number(array[array.length - 1].partCode) + 1;
        } else {
            num = Number(array[array.length - 1].catCode) + 1;
        }

    }

    if (level == 3) {
        codeTemp = (Array(3).join(0) + num).slice(-3);
    } else {
        codeTemp = (Array(2).join(0) + num).slice(-2);
    }

    if (level == 4) {
        $("#add_cate_table input[name='partCode']").val(codeTemp);
        part_code = codeTemp;
        setPartSumCode(first_cate_code + second_cate_code + third_cate_code);
    } else {
        $("#add_cate_table input[name='catCode']").val(codeTemp);
    }

}

//选择添加类型
function chooseCateLevel(target) {
    $('#add_cate_detail_tbody').empty();

    var level = parseInt(target.value);

    switch (level) {
        case 1:
            getFirstCateHtml();
            break;
        case 2:
            getSecondCateHtml();
            break;
        case 3:
            getThirdCateHtml();
            break;
        default:
            getPartHtml();
            break;
    }

}

function getPartHtml() {

    $("#special_part").empty().html($('#part_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_part_detail_thead').html());

    $('#add_cate_ok_button').unbind().click(function () {
        to_add_part();
    });

    init();
}

function to_add_category() {
    var formData = jq_serializeObject($('#add_cate_form'));
    var level = parseInt(formData.level);
    var checkFlag = true;
    var catName = replaceBracketsCN(formData.catName.replace(/\s+/g, ''));
    var catCode = formData.catCode.replace(/\s+/g, '');
    if (catName == '') {
        checkFlag = false;
        $("#add_cate_form input[name='catName']").css('border', 'solid 2px red');
    } else {
        $("#add_cate_form input[name='catName']").css('border', 'solid 1px #dcdcdc');
    }
    if (catCode == '') {
        checkFlag = false;
        $("#add_cate_form input[name='catCode']").css('border', 'solid 2px red');
    } else {
        switch (level) {
            case 1:
                if (!checkFirstCatCode(catCode)) {
                    checkFlag = false;
                    $("#add_cate_form input[name='catCode']").css('border', 'solid 2px red');
                } else {
                    $("#add_cate_form input[name='catCode']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 2:
                if (!checkSecondCatCode(catCode)) {
                    checkFlag = false;
                    $("#add_cate_form input[name='catCode']").css('border', 'solid 2px red');
                } else {
                    $("#add_cate_form input[name='catCode']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            case 3:
                if (!checkThirdCatCode(catCode)) {
                    checkFlag = false;
                    $("#add_cate_form input[name='catCode']").css('border', 'solid 2px red');
                } else {
                    $("#add_cate_form input[name='catCode']").css('border', 'solid 1px #dcdcdc');
                }
                break;
            default:
                break;
        }
    }

    if (level == 1) {
    } else {
        var parentId = formData.parentId;
        if (parentId == -1) {
            checkFlag = false;
            alert('请选择上级分类');
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
        url: '/monkey/categoryXPart/toAddCategory',
        type: 'POST',
        data: {cateJsonString: jsonString},
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
                $('#add_cate_modal').modal('hide');
            } else {
                alert(result.message);
            }
        }
    });
}

function getThirdCateHtml() {
    $("#special_part").empty().html($('#third_cate_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_category_detail_thead').html());
    $('#add_category_detail_text').html('已有的三级分类');

    $('#second_cate_add').unbind().change(function () {
        if (this.value == -1) {
            $('#add_cate_detail_tbody').empty();
            return;
        }
        refresh_add_cate_detail_tbody(3, this.value);
    });

    $('#add_cate_ok_button').unbind().click(function () {
        to_add_category();
    });

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 1},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.categoryAttrList;
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
                fcSelect.append(op);
            }

            fcSelect.unbind().change(function () {
                $('#add_cate_detail_tbody').empty();
                var pid = this.value;
                var cateSelect = $("#second_cate_add");
                cateSelect.get(0).options.length = 1;
                if (pid == -1) {
                    return;
                }

                $.ajax({
                    url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
                    type: 'POST',
                    data: {level: 2, parentId: pid},
                    success: function (result) {
                        var cateArray = result.categoryAttrList;
                        for (var i in cateArray) {
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

function getSecondCateHtml() {
    $("#special_part").empty().html($('#second_cate_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_category_detail_thead').html());
    $('#add_category_detail_text').html('已有的二级分类');

    $('#first_cate_add').unbind().change(function () {
        if (this.value == -1) {
            $('#add_cate_detail_tbody').empty();
            return;
        }
        refresh_add_cate_detail_tbody(2, this.value);
    });

    $('#add_cate_ok_button').unbind().click(function () {
        to_add_category();
    });

    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 1},
        success: function (result) {
            var fcSelect = $("#first_cate_add");
            fcSelect.get(0).options.length = 1;
            var fcArray = result.categoryAttrList;
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
                fcSelect.append(op);
            }

        }
    });

}

function getFirstCateHtml() {
    $("#special_part").empty().html($('#first_cate_template').html());
    $('#add_cate_detail_thead').empty().html($('#add_category_detail_thead').html());
    $('#add_category_detail_text').html('已有的一级分类');

    refresh_add_cate_detail_tbody(1);

    $('#add_cate_ok_button').unbind().click(function () {
        to_add_category();
    });
}

