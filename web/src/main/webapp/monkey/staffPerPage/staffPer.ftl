<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title></title>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="/resources/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME STYLES -->
    <link href="/resources/assets/css/style-metronic.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <!-- select框优化 -->
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css"/>
    <!-- 弹出框 -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet"
          type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>

    <!--公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        table {
            table-layout: fixed;
        }

        td {
            word-break: break-all;
            word-wrap: break-word;
        }

        th {
            vertical-align: middle;
            text-align: center;
        }

        td {
            vertical-align: middle;
            text-align: center;
        }

    </style>
</head>
<body class="page-header-fixed" style="overflow:auto;">
<div class="portlet" align="center">
    <div class="portlet" align="center">
        <table>
            <tr>
                <td>
                    <button onclick="showSmStaffPerGraph(1)">伪效率值</button>
                    <button onclick="showSmStaffActualPerGraph(1)">效率值</button>
                    <button onclick="showSomeMonthAveragePerGraph()">轨迹-平均效率值</button>
                    <button onclick="showSomeMonthAverageErrorRatePerGraph()">轨迹-平均效率值</button>
                    <button onclick="showSomeMonthTotalNumPerGraph()">轨迹-月处理总量</button>
                </td>
            </tr>
            <tr>
                <td>
                    <div id="smStaffDataGraph">加载伪效率值图...</div>
                </td>
            </tr>
        </table>
    </div>

    <div class="portlet" id="search-result-portlet">
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-reorder"></i>

                <span>所有员工:</span>

                <span id="time_sm" style="color: red">${timeYearMonth!""}</span>

                <#if dateFormatFlag?? && dateFormatFlag == "yyyy_MM" >
                    <button id="dateFormatFlag" onclick="changeDateFormatFlag()" title="关闭员工全月可输入">关闭</button>
                <#else>
                    <button id="dateFormatFlag" onclick="changeDateFormatFlag()" title="开启员工全月可输入">开启</button>
                </#if>

            <#--<a href="javascript:void(0);" id="add_cate_button" class="modify_button_style">-->
            <#--<i class="fa fa-plus-square" ></i>-->
            <#--新增员工-->
            <#--</a>-->
            </div>

        </div>
        <div class="portlet-body" style="display: block;">
            <div class="table-scrollable">
                <table id="mainTable" class="table table-striped table-bordered table-hover" align="center">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <#--<th>工号</th>-->
                        <th class="sorting" title="使用'前错误率'列计算">伪效率值</th>
                        <th title="使用'错误率'列计算">真效率值</th>
                        <th class="sorting">前错误率</th>
                        <th>起始日期</th>
                        <th>实际天数</th>
                        <th>处理总数</th>
                        <th>出错数量</th>
                        <th>错误率</th>
                        <#--<th>备注</th>-->
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if dataList?? && dataList?size != 0 >
                        <#assign index = 1/>
                        <#list dataList as data>
                        <tr>
                            <td>${index!""}</td>
                            <td><a href="javascript:showChartForCurrentDataOnNowMonth('${data.staffPerStaffDO.id!""}', '${data.staffPerStaffDO.staffName!""}')">${data.staffPerStaffDO.staffName!""}</a></td>
                            <#--<td>${data.staffPerStaffDO.staffNo!""}</td>-->
                            <td title="使用'前错误率'计算">${data.scorePerv!""}</td>
                            <td title="使用'错误率'列计算">${data.scoreActual!""}</td>
                            <td>${data.pervMonthErrorRate!""}</td>
                            <td>${data.staffPerHandleDO.startDay!""}</td>
                            <td>${data.staffPerHandleDO.actualDays!""}</td>
                            <td>${data.staffPerHandleDO.totalNum!""}</td>
                            <td>${data.staffPerHandleDO.errorNum!""}</td>
                            <td>${data.monthErrorRate!""}</td>
                            <#--<td>${data.staffPerHandleDO.remarks!""}</td>-->
                            <td>
                                <div onclick="toModifyHandle('${data.staffPerHandleDO.id!""}', '${data.staffPerStaffDO.staffName!""}');"
                                     class="btn btn-sm yellow" style="padding:2px 4px;">
                                    更新
                                </div>
                                <div onclick="toViewPerTrail('${data.staffPerStaffDO.id!""}', '${data.staffPerStaffDO.staffName!""}', '${data.staffPerStaffDO.staffNo!""}');"
                                     class="btn btn-sm green" style="padding:2px 4px;">
                                    轨迹
                                </div>
                            </td>
                            <#assign index = index + 1/>
                        </tr>
                        </#list>
                    <#else>
                    </#if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div style="text-align: left">
        <span>1、统计数据的前一个月， 没有统计数据（数据库没有记录） 或者 员工处理总数为0（该员工没有做这个工作），则前一个月的错误率按照0.1来算;</span>
        <br/>
        <span>2、统计数据的该月，实际天数为0，则实际用1来计算（因为公式中需要错误数量为分母）;</span>
        <br/>
        <span>3、统计数据的该月，处理总数为0，则伪效率值和真效率值都为0，错误率为0;</span>
        <br/>
        <span>4、每月月初，初始生成的员工处理信息，默认，实际天数为0，错误数量为0，起始日期为0000-00-00，月份时间为该月的时间，处理总数为0，备注为空。</span>
    </div>

    <div id="msg_modal" class="modal fade" tabindex="9999" data-width="300">
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12" id="msg_content" style="font-size: 20px;text-align: center;">
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn red" style="margin-left:8px;">知道了</button>
        </div>
    </div>

    <div id="make_sure_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="300">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">确认框</h5>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12" id="make_sure_msg">
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn red" id="make_sure_ok_button">确认</button>
            <button type="button" data-dismiss="modal" class="btn default" style="margin-left:8px;">关闭</button>
        </div>
    </div>

    <div id="main_data_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="43%"
         data-height="430">
        <div id="main_modal_content" class="modal-body" style="padding-top:10px;padding-bottom:0px;">
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="main_modal_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn default" style="margin-left:8px;">关闭</button>
        </div>
    </div>

    <div id="trail_data_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="60%"
         data-height="430">
        <div id="trail_modal_content" class="modal-body" style="padding-top:10px;padding-bottom:0px;">
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="trail_modal_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn default" style="margin-left:8px;">关闭</button>
        </div>
    </div>

    <div id="currentDataOnNowMonth_data_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="75%"
         data-height="430">
        <div id="currentDataOnNowMonth_modal_content" class="modal-body" style="padding-top:10px;padding-bottom:0px;">
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="trail_modal_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn default" style="margin-left:8px;">关闭</button>
        </div>
    </div>

    <script type="text/template" id="mainDataTemplate">
        <table class="table table-bordered" cellpadding="5px">
            <tbody>
            <input id="data_handle_id" type="hidden">
            <tr>
                <th>员工姓名</th>
                <td>
                    <span id="data_staff_staffName" style="color: red"></span>
                </td>
            </tr>
            <tr>
                <th>节假日期</th>
                <td style="text-align: left">
                    <span id="data_holiday_dayStr"></span>
                </td>
            </tr>
            <tr>
                <th>实际天数</th>
                <td>
                    <span id="data_handle_actualDays"></span>
                </td>
            </tr>
            <tr>
                <th>起始日期</th>
                <td>
                    <span id="data_handle_start_month" style="width: 50%;height: 30px;"></span>
                    <input id="data_handle_start_day" maxlength="2" type="text"
                           style="width: 50%;height: 30px;ime-mode: Disabled;color: red" onkeydown="onlyNum();"
                           onchange="toChangeActualDays()" onmouseout="toChangeActualDays()" readonly="readonly"/>
                </td>
            </tr>

            <tr>
                <th>处理总数</th>
                <td>
                    <input id="data_handle_totalNum" type="text" maxlength="10"
                           style="width: 100%;height: 30px;;ime-mode: Disabled;color: red" placeholder="输入前月的处理总数"
                           onkeydown="onlyNum();" onchange="toChangeErrorRate()" onmouseout="toChangeErrorRate()"
                           onkeyup="toChangeErrorRate()" readonly="readonly"/>
                </td>
            </tr>
            <tr>
                <th>错误数量</th>
                <td>
                    <input id="data_handle_errorNum" type="text" maxlength="10"
                           style="width: 100%;height: 30px;;ime-mode: Disabled;color: red" placeholder="输入前月的错误数量"
                           onkeydown="onlyNum();" onchange="toChangeErrorRate()" onmouseout="toChangeErrorRate()"
                           onkeyup="toChangeErrorRate()"/>
                </td>
            </tr>
            <tr>
                <th>错误率</th>
                <td>
                    <span id="data_handle_errorRate"></span>
                </td>
            </tr>
            <tr>
                <th>备注</th>
                <td>
                    <textarea id="data_handle_remarks" maxlength="256" style="height: 60px;resize: none;"
                              placeholder="备注说明,该员工在这个月的一些特殊情况等"></textarea>
                </td>
            </tr>
            </tbody>
        </table>
    </script>

    <script type="text/template" id="trailTemplate">
        <table class="table table-bordered" cellpadding="5px">
            <tbody>
            <tr>
                <th>员工姓名</th>
                <td>
                    <span id="trail_staffName" style="color: red"></span>
                </td>
            </tr>
            <tr>
                <td style="text-align: left" colspan="2">
                    <div id="trail_months_per">加载历史效率值图...</div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <div id="trail_months_error">加载历史错误率图...</div>
                </td>
            </tr>
            </tbody>
        </table>
    </script>

    <script type="text/template" id="currentDataOnNowMonthTemplate">
        <table class="table table-bordered" cellpadding="5px">
            <tbody>
            <tr>
                <th>员工姓名</th>
                <td>
                    <span id="daily_staffName" style="color: red"></span>
                </td>
            </tr>
            <tr>
                <td style="text-align: left" colspan="2">
                    <div id="daily_count">加载当月, 日工作量走势图...</div>
                </td>
            </tr>
            </tbody>
        </table>
    </script>
</div>

<!-- ================================== js文件引入 ================================== -->
<script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
        type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
<!-- select框优化 -->
<script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>
<!-- 弹出框 -->
<script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js"
        type="text/javascript"></script>
<script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<!-- 模板解析 -->
<script src="/resources/js/lib/mustache.js" type="text/javascript"></script>
<!-- 分页组件 -->
<script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript"></script>

<script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>

<script type="text/javascript" src="/resources/js/lib/jscharts.js"></script>

<!-- 表格排序 -->
<script type="text/javascript" src="/resources/js/lib/jquery.tablesorter.min.js"></script>

<!-- 通用js -->
<script src="/resources/js/common/common.js" type="text/javascript"></script>

<script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

<script>
    jQuery(document).ready(function () {
        // initiate layout and plugins
        App.init();
    });
</script>

<script src="/resources/js/staffPer/staffPer.js" type="text/javascript"></script>

</body>
</html>