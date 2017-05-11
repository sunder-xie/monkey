/**
 *
 * Created by LYJ on 20151225.
 */

$(document).ready(function () {
    button_click();
    getStoppedCategory(1, 0);

    initSearch();
});

//按钮点击事件
function button_click() {
    $('.search').bind('keypress', function (event) {
        if (event.keyCode == 13) {
            $('#search_button').trigger('click');
        }
    });

    $('#search_button').click(function () {
        to_search_list();
    });

    $('#reset_button').click(function () {
        $("#third_cate").get(0).options.length = 1;
        $("#second_cate").get(0).options.length = 1;
        $("#first_cate").val("-1");
        $("#partName").val('');
    });
}

function getDataMap(dataObj) {
    var index = 0;
    var dataMap = {
        data: dataObj,
        indexRenderer: function () {
            index++;
            return index;
        },
        levelName: function () {
            var result = "";
            switch (this.catLevel) {
                case 1:
                    result = "一级分类";
                    break;
                case 2:
                    result = "二级分类";
                    break;
                case 3:
                    result = "三级分类";
                    break;
                default :
                    break;
            }
            return result;
        }
    };
    return dataMap;
}

function getStoppedCategory(level, parentId) {
    $.ajax({
        url: '/monkey/categoryXPart/getStoppedCategory',
        type: 'POST',
        data: {level: level, parentId: parentId == -1 ? null : parentId},
        success: function (data) {
            var html = '';
            var resultHtml = '';
            var length = data.length;
            if (length > 0) {
                html = Mustache.render($('#list_template').html(), getDataMap(data));
            } else {
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';
                $('#result_show').html(resultHtml);
            }
            $('#list_tbody').html(html);
        }
    });
}

function getStoppedCategoryPart(firstCatId, secondCatId, thirdCatId, partName) {
    $.ajax({
        url: '/monkey/categoryXPart/getStoppedCategoryPart',
        type: 'POST',
        data: {firstCatId: firstCatId, secondCatId: secondCatId, thirdCatId: thirdCatId, partName: partName},
        success: function (data) {
            var html = '';
            var resultHtml = '';
            var length = data.length;
            if (length > 0) {
                html = Mustache.render($('#list_part_template').html(), getDataMap(data));
            } else {
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';
                $('#result_show').html(resultHtml);
            }
            $('#list_tbody').html(html);
        }
    });
}

function initSearch() {
    $.ajax({
        url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
        type: 'POST',
        data: {level: 1},
        success: function (result) {
            var cateSelect = $("#first_cate");
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

    $("#first_cate").change(function () {
        var firstCatId = $("#first_cate").val();
        var cateSelect = $("#second_cate");
        cateSelect.get(0).options.length = 1;
        $("#third_cate").get(0).options.length = 1;
        if (firstCatId == '-1') {
            return;
        }
        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 2, parentId: firstCatId},
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
    });

    $("#second_cate").change(function () {
        var secondCatId = $("#second_cate").val();
        var cateSelect = $("#third_cate");
        cateSelect.get(0).options.length = 1;
        if (secondCatId == '-1') {
            return;
        }
        $.ajax({
            url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
            type: 'POST',
            data: {level: 3, parentId: secondCatId},
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
    });
}

function to_search_list() {
    var firstVal = $("#first_cate").val();
    var secondVal = $("#second_cate").val();
    var thirdVal = $("#third_cate").val();
    var partName = $("#partName").val().replace(/\s+/g, '');

    if (partName == "") {
        if (thirdVal == -1) {
            if (secondVal == -1) {
                if(firstVal == -1){
                    getStoppedCategory(1, 0);
                }else{
                    getStoppedCategory(2, firstVal);
                }
            } else {
                getStoppedCategory(3, secondVal);
            }
        } else {
            getStoppedCategoryPart(firstVal == -1 ? null : firstVal, secondVal == -1 ? null : secondVal, thirdVal == -1 ? null : thirdVal, partName);
        }
    } else {
        getStoppedCategoryPart(firstVal == -1 ? null : firstVal, secondVal == -1 ? null : secondVal, thirdVal == -1 ? null : thirdVal, partName);
    }

    var msg = "";
    var flag = 1;
    if (firstVal != -1) {
        flag = 0;
        msg += $("#first_cate :selected").text() + " ";
    }
    if (secondVal != -1) {
        flag = 0;
        msg += $("#second_cate :selected").text() + " ";
    }
    if (thirdVal != -1) {
        flag = 0;
        msg += $("#third_cate :selected").text() + " ";
    }
    if (partName != '') {
        flag = 0;
        msg += partName + " ";
    }
    if (flag == 1) {
        msg = "默认查询一级已停用的全部数据";
    }
    $("#select_condition").html(msg);
}

//
function reuseCategory(obj, catId) {
    $.ajax({
        url: '/monkey/categoryXPart/reuseCategory',
        type: 'POST',
        data: {catId: catId},
        success: function (result) {
            if(result.success){
                $(obj).parent().parent().remove();

                $.ajax({
                    url: '/monkey/categoryXPart/getCategoryAttrToUseInSelect',
                    type: 'POST',
                    data: {level: 1},
                    success: function (result) {
                        var cateSelect = $("#first_cate");
                        cateSelect.get(0).options.length = 1;
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
                $("#third_cate").get(0).options.length = 1;
                $("#second_cate").get(0).options.length = 1;
                $("#first_cate").val("-1");
                $("#partName").val('');
            }
            alert(result.message);
        },
        error: function () {
            alert("系统错误，分类复用失败！");
        }
    });
}

function reuseCategoryPart(obj,id) {
    $.ajax({
        url: '/monkey/categoryXPart/reuseCategoryPart',
        type: 'POST',
        data: {id: id},
        success: function (result) {
            if(result.success){
                $(obj).parent().parent().remove();
            }
            alert(result.message);
        },
        error: function () {
            alert("系统错误，标准零件复用失败！");
        }
    });
}

