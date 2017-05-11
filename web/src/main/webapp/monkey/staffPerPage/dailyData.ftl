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
    <!-- calendar -->
    <link href="/resources/assets/plugins/fullcalendar/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"/>

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
<input id="staffId" type="hidden" value="${staffId!-1}"/>
<input id="timeYearMonth" type="hidden" value="${timeYearMonth!"2016-03"}"/>
<input id="dateFormatFlag" type="hidden" value="${dateFormatFlag!"yyyy_MM_dd"}"/>

<div class="portlet" align="center">
    <!-- BEGIN PORTLET-->
    <div class="portlet box blue calendar"><#--style="width: 450px;height: 403px"-->
        <div class="portlet-title">
            <div class="caption"><i class="fa fa-calendar"></i>工作日历</div>
        </div>
        <div class="portlet-body light-grey">
            <div id="calendar"></div>
        </div>
    </div>
    <!-- END PORTLET-->

    <!--确定取消的_Model-->
    <div id="make_sure_model" class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">
        <div class="modal-body" style="overflow-x: hidden;">

            <div class="row">
                <p id="make_sure_content"
                   style="font-size: 20px;text-align: center;vertical-align: middle !important;"></p>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn-danger">确定</button>
        <#--<button type="button"  class="btn blue " id="make_sure_model_sure_button">确定</button>-->
        </div>
    </div>

    <!-- ================================== 弹出框 ================================== -->
    <div id="make_sure_modal" class="modal fade" tabindex="-1" data-width="300">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h4 class="modal-title">确认框</h4>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12" id="make_sure_msg">
                    您确认吗？
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" class="btn red" id="make_sure_ok_button">确认</button>
        </div>
    </div>

    <div id="main_data_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="60%"
         data-height="300">
        <div id="main_modal_content" class="modal-body" style="padding-top:10px;padding-bottom:0px;">
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="main_modal_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn default" style="margin-left:8px;">关闭</button>
        </div>
    </div>

    <script type="text/template" id="mainDataTemplate">
        <table class="table table-bordered" cellpadding="5px">
            <tbody>
            <input id="data_daily_id" type="hidden">
            <tr>
                <div class="panel-heading">
                <#--力洋的车型选择-->
                    <div class="panel-title" style="width:100%;">
                        <div class="row " style="width: 100%;">
                            <div class="col-md-3">
                                <select id="search_brand" style="display: block;"></select>

                            </div>
                            <div class="col-md-3">
                                <select id="search_factory" style="display: block;"></select>

                            </div>
                            <div class="col-md-3">
                                <select id="search_series" style="display: block;"></select>

                            </div>
                            <div class="col-md-3">
                                <select id="search_model" style="display: block;"></select>
                            </div>
                        </div>
                    </div>
                </div>
            </tr>
            <tr>
                <th>统计日期</th>
                <td>
                    <span id="data_daily_time" style="color: red"></span>
                </td>
            </tr>
            <tr>
                <th>工作时间</th>
                <td>
                    <select id="data_daily_percentOfDay" style="width:90%;">
                        <option value="0">0天 (该天未做,不进行统计)</option>
                        <option value="1">1天</option>
                    </select>
                <#--<span id="data_daily_percentOfDay"></span>-->
                </td>
            </tr>
            <tr>
                <th>今日总数</th>
                <td style="text-align: left">
                    <input id="data_daily_totalNum" type="text" maxlength="10"
                           style="width: 100%;height: 30px;;ime-mode: Disabled;color: red" placeholder="输入今日的处理总数"
                           onkeydown="onlyNum();" onchange="toChangeTotalNum()" onmouseout="toChangeTotalNum()"
                           onkeyup="toChangeTotalNum()"/>
                </td>
            </tr>
            <tr>
                <th>备注</th>
                <td>
                    <textarea id="data_daily_remarks" maxlength="256" style="width: 100%; height: 60px;resize: none;"
                              placeholder="备注说明, 你在今天的一些特殊情况等"></textarea>
                </td>
            </tr>
            </tbody>
        </table>
    </script>

    <#--liyang-->
    <script type="text/template" id="car_select_template">
        <option value="-1">{{placeholder}}</option>
        {{#list}}
        <option value="{{.}}">{{.}}</option>
        {{/list}}
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

<script src="/resources/js/lib/jscharts.js" type="text/javascript"></script>

<!--时间格式化-->
<script src="/resources/js/lib/jquery-dateFormat.min.js" type="text/javascript"></script>

<!-- 表格排序 -->
<script src="/resources/js/lib/jquery.tablesorter.min.js" type="text/javascript"></script>

<!-- IMPORTANT! fullcalendar depends on jquery-ui-1.10.3.custom.min.js for drag & drop support -->
<script src="/resources/assets/plugins/fullcalendar/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery.sparkline.min.js" type="text/javascript"></script>

<!-- 通用js -->
<script src="/resources/js/common/common.js" type="text/javascript"></script>

<script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

<script>
    jQuery(document).ready(function () {
        // initiate layout and plugins
        App.init();
    });
</script>

<script src="/resources/js/staffPer/dailyData.js" type="text/javascript"></script>

</body>
</html>