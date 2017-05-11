/**
 * Created by lyj on 16/2/24.
 */

/**
 * sm = statistical month(统计的月份)
 * @type {string}
 * @private
 */
var _dayStr = "";

$(document).ready(function () {

    mainTableSortStyle();

    showSmStaffPerGraph(0);
});

function mainTableSortStyle() {
    $("#mainTable").tablesorter();
}

//统计月所有员工伪效率值图
function showSmStaffPerGraph(type) {
    var url = "/monkey/staffPer/getCDSmStaffPer";
    var paramObj = {};
    paramObj.type = type;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    drawGraph(data, "smStaffDataGraph", "bar", "伪效率值图", "员工", "伪效率值", colors, 0.8, 500, 300, 50);
}

function showSmStaffActualPerGraph(type) {
    var url = "/monkey/staffPer/getCDSmStaffActualPer";
    var paramObj = {};
    paramObj.type = type;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    drawGraph(data, "smStaffDataGraph", "bar", "效率值图", "员工", "效率值", colors, 0.8, 500, 300, 50);
}

function showSomeMonthAveragePerGraph() {
    var url = "/monkey/staffPer/getCDSomeMonthAveragePer";
    var paramObj = {};
    paramObj.count = 5;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    var type = "line";
    if(data.length < 2){
        type = "bar";
    }
    drawGraph(data, "smStaffDataGraph", type, "轨迹-所有员工历史效率平均值", "年月", "平均效率值", colors, 0.8, 500, 300, 50);
}

function showSomeMonthPerByStaffGraph(staffId, staffName, count) {
    var url = "/monkey/staffPer/getCDSomeMonthPerByStaff";
    var paramObj = {};
    paramObj.staffId = staffId;
    paramObj.count = count;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    var type = "line";
    if(data.length < 2){
        type = "bar";
    }
    drawGraph(data, "trail_months_per", type, staffName + "历史效率值图", "年月", "效率值", colors, 0.8, 500, 300, 50);
}

function showSomeMonthErrorRateByStaffGraph(staffId, staffName, count) {
    var url = "/monkey/staffPer/getCDSomeMonthErrorRateByStaff";
    var paramObj = {};
    paramObj.staffId = staffId;
    paramObj.count = count;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    var type = "line";
    if(data.length < 2){
        type = "bar";
    }
    drawGraph(data, "trail_months_error", type, staffName + "历史错误率图", "年月", "错误率", colors, 0.8, 500, 300, 50);
}

function showSomeMonthAverageErrorRatePerGraph() {
    var url = "/monkey/staffPer/getCDSomeMonthAverageErrorRate";
    var paramObj = {};
    paramObj.count = 5;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    var type = "line";
    if(data.length < 2){
        type = "bar";
    }
    drawGraph(data, "smStaffDataGraph", type, "轨迹-所有员工历史错误率平均值", "年月", "平均错误率", colors, 0.8, 500, 300, 60);
}

function showSomeMonthTotalNumPerGraph() {
    var url = "/monkey/staffPer/getCDSomeMonthTotalNum";
    var paramObj = {};
    paramObj.count = 5;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    var type = "line";
    if(data.length < 2){
        type = "bar";
    }
    drawGraph(data, "smStaffDataGraph", type, "轨迹-所有员工月处理总量", "年月", "月处理总量", colors, 0.8, 500, 300, 60);
}

function toChangeActualDays() {
    var data_handle_start_day = $('#data_handle_start_day').val();
    if (data_handle_start_day == "" || data_handle_start_day == "00" || data_handle_start_day == "0") {
        $('#data_handle_start_day').val("01");
    } else {
        $('#data_handle_start_day').val(prefixInteger(data_handle_start_day, 2));
    }
    //获取其实日期
    var startDay = Number($("#data_handle_start_day").val());
    //获取上个月的最后一天
    var lastDay = getPrevMonthLastDay();

    //获取上个月的年月份
    //var timeYearMonth = $("#data_handle_start_month").html();

    //切割节假日具体的日期字符串
    var day = _dayStr.split(",");
    //实际天数初始为
    var actualDays = lastDay - startDay + 1;

    var len = day.length;
    if (len > 0) {
        for (var i = 0; i < len; i++) {
            var dayInt = Number(day[i]);
            if (dayInt >= startDay && dayInt <= lastDay) {
                actualDays--;
            }
        }
    }

    $("#data_handle_actualDays").html(actualDays);
}

function toChangeErrorRate() {
    if ($('#data_handle_totalNum').val() == "") {
        $('#data_handle_totalNum').val(0)
    }
    if ($('#data_handle_errorNum').val() == "") {
        $('#data_handle_errorNum').val(0)
    }
    var totalNum = Number($('#data_handle_totalNum').val());
    var errorNum = Number($('#data_handle_errorNum').val());
    var result = totalNum == 0 ? 0 : errorNum / totalNum;

    $("#data_handle_errorRate").html(result);
}

function checkSubmitData() {
    //非法数据
    var startDay = replaceAllSpace($('#data_handle_start_day').val());
    if (!/^[0-9]*$/i.test(startDay)) {
        alert("起始日期必须为数字!")
        return false;
    }
    var totalNum = replaceAllSpace($('#data_handle_totalNum').val());
    if (!/^[0-9]*$/i.test(totalNum)) {
        alert("处理总数必须为数字!")
        return false;
    }
    var errorNum = replaceAllSpace($('#data_handle_errorNum').val());
    if (!/^[0-9]*$/i.test(errorNum)) {
        alert("错误数量必须为数字!")
        return false;
    }

    if (startDay > getPrevMonthLastDay()) {
        alert("起始日期不能超过当月最大日期!")
        return false;
    }

    if (Number(totalNum) < Number(errorNum)) {
        alert("错误数量不能多余处理总量!")
        return false;
    }

    return true;
}

function getSubmitData() {
    var startDay = $("#data_handle_start_day").val();
    var startDayHtml = "";
    if (startDay == "") {
        startDayHtml = $("#data_handle_start_month").html() + "01";
    } else {
        startDayHtml = $("#data_handle_start_month").html() + prefixInteger(startDay, 2);
    }

    var dataObj = {};
    dataObj.id = $("#data_handle_id").val();
    dataObj.actualDays = $("#data_handle_actualDays").html();
    dataObj.startDay = startDayHtml;
    dataObj.totalNum = $("#data_handle_totalNum").val() == "" ? 0 : $("#data_handle_totalNum").val();
    dataObj.errorNum = $("#data_handle_errorNum").val() == "" ? 0 : $("#data_handle_errorNum").val();
    dataObj.remarks = replaceAllSpace($("#data_handle_remarks").val());
    return dataObj;
}

function toModifyHandle(handleId, staffName) {
    if (handleId == "") {
        alert("无法更新!");
        return;
    }
    $.ajax({
        url: '/monkey/staffPer/getHandleAndHoliday',
        type: 'GET',
        data: {id: handleId},
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                var handle = result.data.handleResult;
                var holiday = result.data.holidayResult;
                var html = Mustache.render($('#mainDataTemplate').html());

                var dayStrHtml = "";
                _dayStr = "";
                if (holiday != null && holiday.dayStr != null && holiday.dayStr.length > 0) {
                    _dayStr = holiday.dayStr;

                    var dayStr = holiday.dayStr.split(",");
                    var len = dayStr.length - 1;
                    for (var i = 0; i < len; i++) {
                        if (i % 5 == 0 && i > 0) {
                            dayStrHtml += "<br/>";
                        }
                        dayStrHtml += dayStr[i] + "日, ";
                    }
                    if (len % 5 == 0) {
                        dayStrHtml += "<br/>";
                    }
                    dayStrHtml += dayStr[i] + "日";
                }

                $('#main_modal_content').html(html);

                $("#data_handle_id").val(handle.id);
                $("#data_staff_staffName").html(staffName);
                $("#data_holiday_dayStr").html(dayStrHtml);
                $("#data_handle_actualDays").html(handle.actualDays);
                $("#data_handle_start_month").html(handle.timeYearMonth + "-");
                $("#data_handle_start_day").val(handle.startDay == null ? "01" : handle.startDay.substring(handle.startDay.length - 2, handle.startDay.length));
                $("#data_handle_totalNum").val(handle.totalNum);
                $("#data_handle_errorNum").val(handle.errorNum);
                $("#data_handle_errorRate").html(handle.totalNum == 0 ? 0 : handle.errorNum * 1.0 / handle.totalNum);
                $("#data_handle_remarks").val(handle.remarks);

                $('#main_data_modal').modal('show');
                $('#main_modal_ok_button').text('更新').unbind().click(function () {
                    var flag = checkSubmitData();
                    if (flag == true) {
                        $('#make_sure_modal').modal('show');
                        $('#make_sure_modal .modal-title').html('更新');
                        var msg = "您确定要更新 <span style='color: red'>" + staffName + "</span> 的工作数据吗？";
                        $('#make_sure_msg').html(msg);
                        $('#make_sure_ok_button').text('确认').unbind().click(function () {
                            modifyData();
                        });
                    }
                });
            }
        },
        error: function () {
        }
    });
}

function modifyData() {
    var modal = $('#main_data_modal');
    jqBlockUI(modal);

    var jsonStr = JSON.stringify(getSubmitData());
    $.ajax({
        url: '/monkey/staffPer/modifyHandle',
        type: 'POST',
        data: {jsonStr: jsonStr},
        dataType: 'json',
        success: function (result) {
            jqUnblockUI(modal);
            if (result.success) {
                modal.modal('hide');
                window.location.reload();
            } else {
                alert_fuc("更新失败!");
            }
            $('#make_sure_modal').modal('hide');
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function toViewPerTrail(staffId, staffName, staffNo) {
    var html = Mustache.render($('#trailTemplate').html());
    $('#trail_modal_content').html(html);

    showSomeMonthPerByStaffGraph(staffId, staffName, 5);
    showSomeMonthErrorRateByStaffGraph(staffId, staffName, 5);

    $("#trail_staffName").html(staffName);

    $('#trail_data_modal').modal('show');
}

//工具
function alert_fuc(text) {
    $('#msg_content').html(text);
    $('#msg_modal').modal('show');
    return;
}

var _dateFormat2 = 'yyyy-MM-dd';
function formatDate(date) {
    return $.format.date(date, _dateFormat2);
}

function onlyNum() {
    if (!(event.keyCode == 46) && !(event.keyCode == 8) && !(event.keyCode == 37) && !(event.keyCode == 39))
        if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105)))
            event.returnValue = false;
}

function prefixInteger(num, n) {
    return (Array(n).join(0) + num).slice(-n);
}

function getPrevMonthLastDay() {
    var dt = new Date();
    dt.setDate(1);
    dt.setMonth(dt.getMonth());
    var cdt = new Date(dt.getTime() - 1000 * 60 * 60 * 24);
    return Number(cdt.getDate());
}

//jschart
function getSomeColors(size) {
    var colors = ['#FFFF00', '#FFE7BA', '#FFBBFF', '#FF8247', '#FF34B3', '#F8F8FF', '#EEB422',
        '#EE82EE', '#EE6A50', '#EE0000', '#CDCD00', '#C0FF3E', '#BA55D3', '#6E8B3D', '#666666',
        '#54FF9F', '#242424', '#1E90FF', '#0000EE', '#006400', '#00B2EE', '#388E8E', '#7A378B'];
    if (size < 0) {
        return colors.slice(0, 1);
    }

    var len = colors.length;
    if (size > len) {
        var count1 = parseInt(size / len);
        var count2 = size % len;
        var temp = [];
        for (var i = 0; i < count1; i++) {
            temp = temp.concat(colors);
        }
        temp = temp.concat(colors.slice(0, count2));
        return temp;
    }

    var result = colors.slice(0, size);
    return result;
}

function getChartData(url, paramStr) {
    var reAyy = [];
    //同步请求
    $.ajax({
        url: url,
        type: 'GET',
        data: paramStr,
        dataType: 'json',
        async: false,
        timeout: 2000,//超时时间设置，单位毫秒
        success: function (result) {
            if (result.success) {
                var data = result.data;
                var len = data.length;
                var arrXY = [];
                for (var i = 0; i < len; i++) {
                    arrXY[i] = new Array();
                    arrXY[i][0] = data[i].key;
                    arrXY[i][1] = data[i].value;
                }
                reAyy = arrXY;
            }
        },
        error: function () {
        },
        complete: function (XMLHttpRequest, status) { //请求完成后最终执行参数
            if (status == 'timeout') {//超时,status还有success,error等值的情况
                alert("超时");
            }
        }
    });

    return reAyy;
}

function drawGraph(data, div, type, title, nameX, nameY, colors, colorDeep, width, height, paddingLeft) {
    if (data.length <= 0) {
        return;
    }

    if (width == null) {
        width = 500;
    }
    if (height == null) {
        height = 300;
    }
    if (paddingLeft == null) {
        paddingLeft = 50;
    }

    var chart = new JSChart(div, type);
    chart.setDataArray(data);
    chart.setTitleColor('#EEAD0E');
    chart.setAxisColor('#000000');
    chart.setAxisValuesColor('#000000');
    chart.setSize(width, height);
    chart.setTitle(title);
    chart.setAxisNameX(nameX);
    chart.setAxisNameY(nameY);
    chart.setAxisPaddingLeft(paddingLeft);

    if (type == 'bar') {
        chart.colorizeBars(colors);
        chart.setBarOpacity(colorDeep);
        chart.setBarBorderColor('#000000');
        chart.setBarValues(true);
    }
    if (type != 'pie') {
        chart.setAxisNameColor('#B03060');
    }

    chart.draw();
}

//
function changeDateFormatFlag() {
    var dateFormatFlag = $("#dateFormatFlag").text();
    if (dateFormatFlag == "开启") {
        $.ajax({
            url: '/monkey/staffPer/modifyDateFormatFlag',
            type: 'GET',
            data: {type: 1},
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    $("#dateFormatFlag").text("关闭");
                    alert_fuc("开启员工全月可输入成功!");
                } else {
                    alert_fuc("开启员工全月可输入失败!");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    } else {
        $.ajax({
            url: '/monkey/staffPer/modifyDateFormatFlag',
            type: 'GET',
            data: {type: 2},
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    $("#dateFormatFlag").text("开启");
                    alert_fuc("关闭员工全月可输入成功!");
                } else {
                    alert_fuc("关闭员工全月可输入失败!");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
}

function showCurrentDataOnNowMonthByStaffGraph(staffId, staffName) {
    var url = "/monkey/staffPer/getCurrentDataOnNowMonthByStaff";
    var paramObj = {};
    paramObj.staffId = staffId;
    var data = getChartData(url, paramObj);
    var colors = getSomeColors(data.length);
    var type = "line";
    if(data.length < 2){
        type = "bar";
    }
    drawGraph(data, "daily_count", type, staffName + "当月, 日工作量走势", "日期", "日工作量", colors, 0.8, 700, 300, 50);
}

function showChartForCurrentDataOnNowMonth(staffId, staffName){
    if(staffId == "" || Number(staffId)< 0){
        return;
    }

    var html = Mustache.render($('#currentDataOnNowMonthTemplate').html());
    $('#currentDataOnNowMonth_modal_content').html(html);

    showCurrentDataOnNowMonthByStaffGraph(staffId, staffName);

    $("#daily_staffName").html(staffName);

    $('#currentDataOnNowMonth_data_modal').modal('show');
}