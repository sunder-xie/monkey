/**
 * Created by lyj on 16/2/29.
 */

//全局h5 session缓存
var storage = window.sessionStorage;//localStorage;

var _staffId = -1;
var _timeYearMonth = "";
var _dateFormatFlag = "";

$(document).ready(function () {
    _staffId = $('#staffId').val();
    _timeYearMonth = $('#timeYearMonth').val();
    _dateFormatFlag = $('#dateFormatFlag').val();

    initCalendarStyle();
});

function initCalendarStyle() {
    if (!jQuery().fullCalendar) {
        return;
    }

    var h = {};
    if ($('#calendar').width() <= 400) {
        $('#calendar').addClass("mobile");
        h = {
            left: 'title',
            center: '',
            right: 'today'
        };
    } else {
        $('#calendar').removeClass("mobile");
        if (App.isRTL()) {
            h = {
                right: 'title',
                center: '',
                left: 'today'
            };
        } else {
            h = {
                left: 'title',
                center: '',
                right: 'today'
            };
        }
    }

    $('#calendar').fullCalendar('destroy'); // destroy the calendar
    $('#calendar').fullCalendar({ //re-initialize the calendar
        header: h,
        firstDay: 0,
        monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
        buttonText: {
            today: '今天',
            month: '月',
            prev: '上一月',
            next: '下一月'
        },
        events: '/monkey/staffPer/getStaffEventsByStaffAndTime?staffId=' + _staffId + '&timeYearMonth=' + _timeYearMonth,
        dayClick: function (date, allDay, jsEvent, view) {
            clickDoSome(date);
        },
        eventClick: function (calEvent, jsEvent, view) {
            clickDoSome(calEvent.start);
        }
        //disableDragging: false,//Boolean类型, 默认false, 所有的event可以拖动, 必须editable = true
        //editable: true,//Boolean类型, 默认值false, 用来设置日历中的日程是否可以编辑.  可编辑是指可以移动, 改变大小等.
    });
}

function clickDoSome(date) {
    if (_staffId == -1) {
        alert_fuc("不是需要统计的员工!");
        return;
    }
    //if(date > new Date()){
    //    return;
    //}
    if (formatDate(date, _dateFormatFlag) != formatDate(new Date(), _dateFormatFlag)) {
        return;
    }
    $.ajax({
        url: '/monkey/staffPer/getDailyByStaffAndTime',
        type: 'GET',
        data: {
            staffId: _staffId,
            timeYearMonth: formatDate(date, dateFormat_yyyy_MM),
            timeDay: formatDate(date, dateFormat_dd)
        },
        dataType: 'json',
        success: function (result) {
            var daily = {};
            if (result.success) {
                daily = result.data;
                showModifyModal(daily, date, 1);//修改原有的 daily
            } else {
                if (result.code == "001") {
                    daily.id = -1;
                    daily.percentOfDay = 1;
                    daily.totalNum = 0;
                    daily.remarks = "";
                    showModifyModal(daily, date, 2);//新增 daily
                } else {
                    alert_fuc("获取员工每日数据失败!");
                }
            }
        },
        error: function () {
            alert_fuc("获取员工每日数据失败!");
        }
    });
}

function toChangeTotalNum() {
    if ($('#data_daily_totalNum').val() == "") {
        $('#data_daily_totalNum').val(0)
    }
}

function checkSubmitData() {
    //缺少数据
    var model = $("#search_model").val();
    var classType = $("#search_model").attr("class");
    if (classType.indexOf("hidden") > -1 || model == "-1") {
        alert_fuc("请选择:品牌-厂商-车系-车型 数据!");
        return false;
    }

    //非法数据
    var totalNum = replaceAllSpace($('#data_daily_totalNum').val());
    if (!/^[0-9]*$/i.test(totalNum) || Number(totalNum) < 0) {
        alert_fuc("'今日总数'必须为正整数!")
        return false;
    }

    var time = replaceAllSpace($('#data_daily_percentOfDay').val());
    if (time == 0) {
        if (Number(totalNum) > 0) {
            alert_fuc("不花任何时间却做出了成绩?请填写'工作时间'!");
            return false;
        }
    }

    return true;
}

function checkWarningSubmitData() {
    //警告数据
    var model = $("#search_model").val();
    var classType = $("#search_model").attr("class");
    if (classType.indexOf("hidden") > -1 || model == "-1") {
        alert_fuc("请选择:品牌-厂商-车系-车型 数据!");
        return false;
    }

    var totalNum = replaceAllSpace($('#data_daily_totalNum').val());
    if (Number(totalNum) == 0) {
        return false;
    }

    var model = $("#search_model").val();
    var classType = $("#search_model").attr("class");
    if (classType == "hidden" || model == "-1") {
        return false;
    }

    return true;
}

function getSubmitData(date) {
    if (_staffId == -1) {
        alert_fuc("不是需要统计的员工!");
        return;
    }

    var dataObj = {};
    dataObj.id = $("#data_daily_id").val();
    dataObj.staffId = _staffId;
    dataObj.timeYearMonth = formatDate(date, dateFormat_yyyy_MM);
    dataObj.timeDay = formatDate(date, dateFormat_dd);
    dataObj.percentOfDay = $("#data_daily_percentOfDay").val();
    dataObj.totalNum = $("#data_daily_totalNum").val() == "" ? 0 : $("#data_daily_totalNum").val();
    dataObj.remarks = replaceAllSpace($("#data_daily_remarks").val());

    var carType = $("#search_brand").val() + "-"
        + $("#search_factory").val() + "-"
        + $("#search_series").val() + "-"
        + $("#search_model").val();

    var carTypeName = carType;

    dataObj.carType = carType;
    dataObj.carTypeName = carTypeName;
    return dataObj;
}

function showModifyModal(daily, date, type) {
    var html = Mustache.render($('#mainDataTemplate').html());
    $('#main_modal_content').html(html);

    liyangCarSelectInit(type, daily.carType);

    $("#data_daily_id").val(daily.id);

    $("#data_daily_time").html(formatDate(date, dateFormat_yyyy_MM_dd));
    $("#data_daily_percentOfDay").val(daily.percentOfDay);
    $("#data_daily_totalNum").val(daily.totalNum);
    $("#data_daily_remarks").val(daily.remarks);

    $("#data_daily_percentOfDay").select2();
    $('#main_data_modal').modal('show');

    $('#main_modal_ok_button').text('更新').unbind().click(function () {
        var flag = checkSubmitData();
        if (flag == true) {
            $('#make_sure_modal').modal('show');
            $('#make_sure_modal .modal-title').html('更新');
            var msg = "您确定要更新 <span style='color: red'>" + formatDate(date, dateFormat_yyyy_MM)
                + "-" + formatDate(date, dateFormat_dd) + "</span> 的工作数据吗？";

            var flagWarning = checkWarningSubmitData();
            var warning = ""
            if (flagWarning == false) {
                warning = "</br><span style='color: orange;font-size: 8px;'>警告:输入中</span>";
                var totalNum = replaceAllSpace($('#data_daily_totalNum').val());
                if (Number(totalNum) == 0) {
                    warning += "<span style='color: orange'> 今日总数 为0,</span>"
                }

                var time = replaceAllSpace($('#data_daily_percentOfDay').val());
                if (time == 0) {
                    warning += "<span style='color: orange'> 工作时间 为0</span>"
                }

                var model = $("#search_model").val();
                var classType = $("#search_model").attr("class");
                if (classType == "hidden" || model == "-1") {
                    warning += "<span style='color: orange'> 车型没选</span>"
                }
            }

            $('#make_sure_msg').html(msg + warning);
            $('#make_sure_ok_button').text('确认').unbind().click(function () {
                modifyData(date, type);
            });
        }
    });
}

function modifyData(date, type) {
    var modal = $('#main_data_modal');
    jqBlockUI(modal);

    var jsonStr = JSON.stringify(getSubmitData(date));
    $.ajax({
        url: '/monkey/staffPer/modifyDaily',
        type: 'POST',
        data: {jsonStr: jsonStr, type: type},
        dataType: 'json',
        success: function (result) {
            jqUnblockUI(modal);
            if (result.success) {
                modal.modal('hide');
                window.location.reload();
            } else {
                alert_fuc(result.message);
            }
            $('#make_sure_modal').modal('hide');
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

//js工具函数
var dateFormat_yyyy_MM_dd = 'yyyy-MM-dd';
var dateFormat_yyyy_MM = 'yyyy-MM';
var dateFormat_dd = 'dd';
function formatDate(date, dateFormat) {
    return $.format.date(date, dateFormat);
}

function onlyNum() {
    if (!(event.keyCode == 46) && !(event.keyCode == 8) && !(event.keyCode == 37) && !(event.keyCode == 39))
        if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)))
            event.returnValue = false;
}

function alert_fuc(text) {
    $('#make_sure_content').html(text);
    $('#make_sure_model').modal('show');

    return false;
}

//力洋库的车型数据展示
function liyangCarSelectInit(type, carTypeStr) {
    if (type == "1") {//修改
        var carType = carTypeStr.split("-");
        var brandValue = carType[0];
        var factoryValue = carType[1];
        var seriesValue = carType[2];
        var model = carType[3];

        //品牌
        var brandMap = JSON.parse(storage.getItem("car_allbrand"));
        if (brandMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {carType: 'brand'}, function (map) {
                map['placeholder'] = '-请选择品牌-';
                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_brand').html(html);

                $("#search_brand option[value='" + brandValue + "']").attr("selected", true);

                $("#search_brand").select2({
                    placeholder: "-请选择品牌-",
                    allowClear: true
                });
                storage.setItem("car_allbrand", JSON.stringify(map));

            });
        } else {
            var html = Mustache.render($('#car_select_template').html(), brandMap);
            $('#search_brand').html(html);

            $("#search_brand option[value='" + brandValue + "']").attr("selected", true);

            $("#search_brand").select2({
                placeholder: "-请选择品牌-"
            });
        }

        $.get("/monkey/car/getLiyangCar", {carType: 'factory', carBrand: brandValue}, function (map) {
            map['placeholder'] = '-请选择厂商-';

            var html = Mustache.render($('#car_select_template').html(), map);
            $('#search_factory').html(html);

            $("#search_factory option[value='" + factoryValue + "']").attr("selected", true);

            $("#search_factory").select2({
                placeholder: "-请选择厂商-"
            });
        });

        $.get("/monkey/car/getLiyangCar", {
            carType: 'series',
            carBrand: brandValue,
            factoryName: factoryValue
        }, function (map) {
            map['placeholder'] = '-请选择车系-';

            var html = Mustache.render($('#car_select_template').html(), map);
            $('#search_series').html(html);

            $("#search_series option[value='" + seriesValue + "']").attr("selected", true);

            $("#search_series").select2({
                placeholder: "-请选择车系-"
            });
        });

        $.get("/monkey/car/getLiyangCar", {
                carType: 'model', carBrand: brandValue,
                factoryName: factoryValue, carSeries: seriesValue
            },
            function (map) {
                map['placeholder'] = '-请选择车型-';

                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_model').html(html);

                $("#search_model option[value='" + model + "']").attr("selected", true);

                $("#search_model").select2({
                    placeholder: "-请选择车型-"
                });
            });
    } else if (type == "2") {
        $("#search_factory").addClass("hidden");
        $("#search_series").addClass("hidden");
        $("#search_model").addClass("hidden");

        //品牌
        var brandMap = JSON.parse(storage.getItem("car_allbrand"));
        if (brandMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {carType: 'brand'}, function (map) {
                map['placeholder'] = '-请选择品牌-';
                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_brand').html(html);
                $("#search_brand").select2({
                    placeholder: "-请选择品牌-",
                    allowClear: true
                });
                storage.setItem("car_allbrand", JSON.stringify(map));

            });
        } else {
            var html = Mustache.render($('#car_select_template').html(), brandMap);
            $('#search_brand').html(html);
            $("#search_brand").select2({
                placeholder: "-请选择品牌-"
            });
        }
    }

    liyangCarInitChangeEvent();
}

function liyangCarInitChangeEvent() {
    //品牌变更
    $("#search_brand").change(function () {
        $("#search_series").addClass("hidden")
        $("#search_model").addClass("hidden")

        var value = $("#search_brand").val();
        if (value == '-1') {
            return;
        }
        var brandValue = $("#search_brand").val();
        var itemKey = "car_factory_" + brandValue;
        var factoryMap = JSON.parse(storage.getItem(itemKey));

        if (factoryMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {carType: 'factory', carBrand: brandValue}, function (map) {
                map['placeholder'] = '-请选择厂商-';

                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_factory').html(html);
                $("#search_factory").removeClass("hidden");
                $("#search_factory").select2({
                    placeholder: "-请选择厂商-"
                });
                storage.setItem(itemKey, JSON.stringify(map));
            })
        } else {
            var html = Mustache.render($('#car_select_template').html(), factoryMap);
            $('#search_factory').html(html);
            $("#search_factory").removeClass("hidden");
            $("#search_factory").select2({
                placeholder: "-请选择厂商-"
            });
        }
    });

    //厂商变更
    $("#search_factory").change(function () {
        $("#search_series").addClass("hidden")
        $("#search_model").addClass("hidden")

        var value = $("#search_factory").val();
        if (value == '-1') {
            return;
        }
        var brandValue = $("#search_brand").val();
        var factoryValue = $("#search_factory").val();
        var itemKey = "car_series_" + brandValue + factoryValue;
        var seriesMap = JSON.parse(storage.getItem(itemKey));

        if (seriesMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {
                carType: 'series',
                carBrand: brandValue,
                factoryName: factoryValue
            }, function (map) {
                map['placeholder'] = '-请选择车系-';

                var html = Mustache.render($('#car_select_template').html(), map);
                $('#search_series').html(html);
                $("#search_series").removeClass("hidden")
                $("#search_series").select2({
                    placeholder: "-请选择车系-"
                });
                storage.setItem(itemKey, JSON.stringify(map));
            })
        } else {
            var html = Mustache.render($('#car_select_template').html(), seriesMap);
            $('#search_series').html(html);
            $("#search_series").removeClass("hidden")
            $("#search_series").select2({
                placeholder: "-请选择车系-"
            });
        }
    });

    //车系变更
    $("#search_series").change(function () {
        $("#search_model").addClass("hidden")

        var value = $("#search_series").val();
        if (value == '-1') {
            return;
        }
        var brandValue = $("#search_brand").val();
        var factoryValue = $("#search_factory").val();
        var seriesValue = $("#search_series").val();
        var itemKey = "car_model_" + brandValue + factoryValue + seriesValue;
        var modelMap = JSON.parse(storage.getItem(itemKey));

        if (modelMap == undefined) {
            $.get("/monkey/car/getLiyangCar", {
                    carType: 'model', carBrand: brandValue,
                    factoryName: factoryValue, carSeries: seriesValue
                },
                function (map) {
                    map['placeholder'] = '-请选择车型-';

                    var html = Mustache.render($('#car_select_template').html(), map);
                    $('#search_model').html(html);
                    $("#search_model").removeClass("hidden");
                    $("#search_model").select2({
                        placeholder: "-请选择车型-"
                    });
                    storage.setItem(itemKey, JSON.stringify(map));
                });
        } else {
            var html = Mustache.render($('#car_select_template').html(), modelMap);
            $('#search_model').html(html);
            $("#search_model").removeClass("hidden");
            $("#search_model").select2({
                placeholder: "-请选择车型-"
            });
        }
    });
}