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

    <link href="/resources/css/common.css" rel="stylesheet" type="text/css"/>
<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .select_style{
            font-size: 17px;
            width:100%;
        }

        .modify_button_style{
            font-weight:bold;
            font-size:20px;
            margin-left:30px;
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow:auto;">

<div class="portlet" align="center">
    <div class="portlet-body" style="display: block;">
        <div class = "row" style="width:95%;margin-bottom:20px;margin-top:20px;">
            <div class="col-md-3">
                <select id="first_cate" style="" class="select_style">
                    <option value="-1">[一级分类]</option>
                </select>
            </div>
            <div class="col-md-3">
                <select id="second_cate" style="" class="select_style">
                    <option value="-1">[二级分类]</option>
                </select>
            </div>
            <div class="col-md-3">
                <select id="third_cate" style="" class="select_style">
                    <option value="-1">[三级分类]</option>
                </select>
            </div>
        </div>
        <div class = "row" style="width:95%;">
            <div class="col-md-6">
                <input class="form-control search" maxlength="20" style="margin:0px;height:35px;width:100%;" type="search" id="partName" placeholder="填写零件名称">
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

<div class="portlet-body" style="display: block;">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i>
            <span>停用列表:</span>
            <span id="select_condition" style="margin-left:5px;font-size:15px;color:red;">默认查询一级已停用的全部数据</span>
        </div>

    </div>
    <div class="table-scrollable">
        <table class="table table-striped table-bordered table-hover" align="center">
            <thead>
            <tr>
                <th style="text-align: center;vertical-align: middle;">序号</th>
                <th style="text-align: center;vertical-align: middle;">操作</th>
                <th style="text-align: center;vertical-align: middle;">种类码</th>
                <th style="vertical-align: middle;">所属级别</th>
                <th style="text-align: center;vertical-align: middle;">名称</th>
                <th style="vertical-align: middle;">编码</th>
            </tr>
            </thead>
            <tbody id="list_tbody">
            </tbody>
        </table>
    </div>
    <div style="height:50px;" class="note note-danger" >
        <div style="float:left" id="result_show">请选填搜索项，点击<span style="color:red">搜索按钮</span>获得数据</div>
        <div style="float:right">

        </div>
    </div>
    <!-- ================================== 自定义模板 ================================== -->
    <script type="text/template" id="list_template">
        {{#data}}
        <tr>
            <td style="text-align:center;">{{{indexRenderer}}}</td>
            <td style="text-align:center;">
                <input type="hidden" value="{{partName}}">
                <input type="hidden" value="{{partId}}">
                <div onclick="reuseCategory(this,{{catId}});" class="btn btn-sm green " style="padding:2px 4px;" id="">
                    <i class="fa fa-plus-square"></i>
                    复用
                </div>
            </td>
            <td style="text-align:center;">{{vehicleCode}}</td>
            <td>{{{levelName}}}</td>
            <td style="text-align:center;">{{catName}}</td>
            <td>{{catCode}}</td>
        </tr>
        {{/data}}
    </script>

    <script type="text/template" id="list_part_template">
        {{#data}}
        <tr>
            <td style="text-align:center;">{{{indexRenderer}}}</td>
            <td style="text-align:center;">
                <input type="hidden" value="{{partName}}">
                <input type="hidden" value="{{partId}}">
                <div onclick="reuseCategoryPart(this,{{id}});" class="btn btn-sm green " style="padding:2px 4px;" id="">
                    <i class="fa fa-plus-square"></i>
                    复用
                </div>
            </td>
            <td style="text-align:center;">{{vehicleCode}}</td>
            <td>标准零件</td>
            <td style="text-align:center;">{{partName}}</td>
            <td>{{partCode}}</td>
        </tr>
        {{/data}}
    </script>

    <script type="text/template" id="add_cate_init_template">
        <tbody id="special_part">
        <tr>
            <th>一级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="firstCat" name="firstCatId" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择一级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>二级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="firstCat" name="firstCatId" class="" style="width:220px;height:30px;"
                        name="secondCatId"><#--name="cateParentId"-->
                    <option value="-1">--请选择二级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>三级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="third_cate_add" class="" style="width:220px;height:30px;" name="thirdCatId"><#---->
                    <option value="-1">--请选择三级分类--</option>
                </select>
            </td>
        </tr>
        </tbody>
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
    <script src="/resources/js/category/reuseXPart.js" type="text/javascript"></script>
</body>
</html>