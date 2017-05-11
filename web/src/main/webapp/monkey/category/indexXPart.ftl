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
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css" />
    <!-- 弹出框 -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>

<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .select_style{
            font-size: 17px;
            width:100%;
        }
        .modal {
            background-color: white;
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow:auto;">
    <div class="portlet" align="center">
        <div class="portlet-title">
            <div class="caption">查看分类</div>

        </div>
        <div class="portlet-body" style="display: block;">
            <div class = "row" style="width:90%;margin-bottom:20px;margin-top:20px;">
                <div class="col-md-3">
                    <select id="vehicle_code" style="margin-right:20px;" class="select_style">
                        <option value="">[车辆种类码]</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="first_cate" style="margin-right:20px;" class="select_style">
                        <option value="">[一级分类]</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="second_cate" style="margin-right:20px;" class="select_style">
                        <option value="">[二级分类]</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="third_cate" style="" class="select_style">
                        <option value="">[三级分类]</option>
                    </select>
                </div>
            </div>
            <div class = "row" style="width:90%;">
                <div class="col-md-6">
                    <div class="input-icon">
                        <input class="form-control search" maxlength="20" style="height:35px;width:100%;" type="search" id="partName" placeholder="填写零件名称">
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="btn btn-danger" style="width:100%;" id="search_button">
                        搜索
                        <i class="m-icon-swapright m-icon-white"></i>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="btn default" style="width:100%;" id="reset_button">重置</div>
                </div>
            </div>
        </div>

    </div>

    <div class="portlet" id="search-result-portlet">
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-reorder"></i>
                <span>所有零件:</span>
                <span id = "export_version" style="color: red"></span>
                <a href="javascript:void(0);" style="margin-left:15px;" onclick="toExportExcel('all');">[ 导出全部数据 ]</a>
                <a href="javascript:void(0);" style="margin-left:15px;" onclick="toExportExcel();">[ 根据条件导出数据 ]:</a>
                <span id="select_condition" style="margin-left:5px;font-size:15px;color:red;">默认查询全部数据</span>
            </div>

        </div>
        <div class="portlet-body" style="display: block;">
            <div class="table-scrollable">
                <table class="table table-striped table-bordered table-hover" align="center" >
                    <thead>
                    <tr >
                        <th style="text-align: center;vertical-align: middle;">序号</th>
                        <th style="text-align: center;vertical-align: middle;">车辆<br>种类码</th>
                        <th style="vertical-align: middle;">一级分类</th>
                        <th style="text-align: center;vertical-align: middle;">一级编码</th>
                        <th style="vertical-align: middle;">二级分类</th>
                        <th style="text-align: center;vertical-align: middle;">二级编码</th>
                        <th style="vertical-align: middle;">三级分类</th>
                        <th style="text-align: center;vertical-align: middle;">三级编码</th>
                        <th style="vertical-align: middle;">零件名称</th>
                        <th style="text-align: center;vertical-align: middle;">位置码</th>
                        <th style="vertical-align: middle;">零件编码</th>
                        <th style="vertical-align: middle;">标签</th>
                        <th style="vertical-align: middle;">零件别名</th>
                        <th style="text-align: center;vertical-align: middle;">类型</th>
                    </tr>
                    </thead>

                    <tbody id="list_tbody">

                    </tbody>
                </table>
            </div>

                <!-- 分页 -->
                <div style="text-align: center; padding: 10px;">
                    <div class="pagination">
                        <a href="#" class="first" data-action="first" >首页</a>
                        <a href="#" class="previous" data-action="previous" >上一页</a>
                        <input type="text" readonly="readonly" data-max-page="40" />
                        <a href="#" class="next" data-action="next" >下一页</a>
                        <a href="#" class="last" data-action="last">末页</a>
                    </div>
                </div>

                <div style="height:50px;" class="note note-danger" >
                    <div style="float:left" id="result_show">请选填搜索项，点击<span style="color:red">搜索按钮</span>获得数据</div>
                    <div style="float:right">

                    </div>
                </div>

        </div>
    </div>

    <!-- 自定义模板 -->
    <script type="text/template" id="list_template">
        {{#data}}
        <tr>
            <td style="text-align:center;">{{{indexRenderer}}}</td>
            <td style="text-align:center;">{{vehicleCode}}</td>
            <td>{{catNameF}}</td>
            <td style="text-align:center;">{{catCodeF}}</td>
            <td>{{catNameS}}</td>
            <td style="text-align:center;">{{catCodeS}}</td>
            <td>{{catNameT}}</td>
            <td style="text-align:center;">{{catCodeT}}</td>
            <td>{{partName}}</td>
            <td style="text-align:center;">{{partCode}}</td>
            <td>{{sumCode}}</td>
            <td>{{labelNameText}}</td>
            <td>{{alissNameText}}</td>
            <td style="text-align:center;">{{{catKindName}}}</td>
        </tr>
        {{/data}}
    </script>

    <!-- ================================== js文件引入 ================================== -->
    <script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
    <!-- select框优化 -->
    <script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>
    <!-- 弹出框 -->
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript" ></script>
    <!-- 模板解析 -->
    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>
    <!-- 分页组件 -->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>

    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>

    <!-- 通用js -->
    <script src="/resources/js/common/common.js" type="text/javascript" ></script>

    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

    <script>
        jQuery(document).ready(function() {
            // initiate layout and plugins
            App.init();
        });
    </script>

    <!-- mainJS -->
    <script src="/resources/js/category/categoryXPart.js" type="text/javascript"></script>

</body>
</html>