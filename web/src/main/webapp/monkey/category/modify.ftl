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
        <div class="portlet-title">
            <div class="caption">
                <a href="javascript:void(0);" id="add_cate_button" class="modify_button_style">
                    <i class="fa fa-plus-square" ></i>
                    新增分类
                </a>
                <a href="javascript:void(0);" id="modify_cate_button" class="modify_button_style">
                    <i class="fa fa-edit" ></i>
                    <span>
                        请选择要修改的分类
                    </span>
                </a>
            </div>
        </div>

        <div class="portlet-body" style="display: block;">
            <div class = "row" style="width:95%;margin-bottom:20px;margin-top:20px;">
                <div class="col-md-3">
                    <select id="vehicle_code" style="" class="select_style">
                        <option value="">[车辆种类码]</option>
                    </select>
                </div>
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

    <div class="portlet" id="search-result-portlet">
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-reorder"></i>
                <span>分类列表:</span>
                <a href="javascript:void(0);" style="margin-left:15px;" onclick="toExportExcel('all');">[ 导出全部数据 ]</a>
                <a href="javascript:void(0);" style="margin-left:15px;" onclick="toExportExcel();">[ 根据条件导出数据 ]:</a>
                <span id="select_condition" style="margin-left:5px;font-size:15px;color:red;">默认查询全部数据</span>
            </div>

        </div>
        <div class="portlet-body" style="display: block;">
            <div class="table-scrollable">
                <table class="table table-striped table-bordered table-hover" align="center" >
                    <thead>
                    <tr>
                        <th style="text-align: center;vertical-align: middle;">序号</th>
                        <th style="text-align: center;vertical-align: middle;">操作</th>
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
                        <th style="text-align: center;vertical-align: middle;">状态</th>
                    </tr>
                    </thead>

                    <tbody id="list_tbody">

                    </tbody>
                </table>
            </div>

                <!-- 分页 -->
                <div style="text-align: center; padding: 10px;">
                    <div class="pagination">
                        <a href="#" class="first" data-action="first" style="">首页</a>
                        <a href="#" class="previous" data-action="previous" style="">上一页</a>
                        <input type="text" readonly="readonly" data-max-page="40" style=""/>
                        <a href="#" class="next" data-action="next" style="">下一页</a>
                        <a href="#" class="last" data-action="last" style="">末页</a>
                    </div>
                </div>

                <div style="height:50px;" class="note note-danger" >
                    <div style="float:left" id="result_show">请选填搜索项，点击<span style="color:red">搜索按钮</span>获得数据</div>
                    <div style="float:right">

                    </div>
                </div>

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

    <div id="modify_part_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="800">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">修改零件</h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <div class="row">
                <div class="col-md-6" style="width:520px;">
                    <form id="modify_part_form">
                        <table class="common_modal_table" style="width:100%;">
                            <tr>
                                <th style="width:120px;">当前上级分类</th>
                                <td id="currentCateName" style="padding-left:15px;">
                                </td>
                            </tr>
                            <tr>
                                <th>可改上级分类</th>
                                <td>
                                    <select name="cateId" class="" style="width:220px;height:30px;" onchange="">
                                        <option value="-1">--请选择上级分类--</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <th>零件名称<span style="color:red;margin-left:3px;">*</span></th>
                                <td>
                                    <input type="hidden" name="id" value="">
                                    <input name="partName" maxlength="15" type="text" class="" style="width:220px;height:30px;">
                                    <span style="margin-left:5px;color:green;">必填</span>
                                </td>
                            </tr>
                            <tr>
                                <th>位置码<span style="color:red;margin-left:3px;">*</span></th>
                                <td>
                                    <input name="code" maxlength="2" type="text" class="" style="width:220px;height:30px;">
                                    <span style="margin-left:5px;color:green;">必填，2位数字</span>
                                </td>
                            </tr>
                            <tr>
                                <th>零件编码</th>
                                <td><input readonly name="sumCode" maxlength="9" class="" style="width:220px;height:30px;"></td>
                            </tr>
                            <tr>
                                <th>标签</th>
                                <td>
                                    <input name="labelNameText" maxlength="" type="text" class="" style="width:220px;height:30px;">
                                    <span style="margin-left:5px;color:green;">多个值用,隔开</span>
                                </td>
                            </tr>
                            <tr>
                                <th>零件别名</th>
                                <td>
                                    <input name="alissNameText" maxlength="" type="text" class="l" style="width:220px;height:30px;">
                                    <span style="margin-left:5px;color:green;">多个值用,隔开</span>
                                </td>
                            </tr>
                            <tr>
                                <th>类型</th>
                                <td>
                                    <select name="catKind" class="" style="width:220px;height:30px;">
                                        <option value="1">全车件</option>
                                        <option value="0">易损件</option>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <div class="col-md-6" style="padding:0px;width:250px;height:290px;overflow:auto;">
                    <table class="common_modal_table" style="width:100%;">
                        <thead>
                            <tr>
                                <td colspan="2" id="" style="font-size:16px;font-weight:bold;color:blue;">已有的零件</td>
                            </tr>
                            <tr>
                                <td style="font-weight:bold;">零件名称</td>
                                <td style="font-weight:bold;width:70px;">位置码</td>
                            </tr>
                        </thead>
                        <tbody id="modify_part_detail"></tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="modify_part_ok_button">保存</button>
            <button type="button" class="btn yellow" id="part_stop_use_button" style="margin-left:20px;margin-right:20px;">停用</button>
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

    <div id="modify_cate_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="800">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title" id="modify_cate_title">修改分类</h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <div class="row">
                <div class="col-md-6" style="width:520px;" >
                    <form id="modify_cate_form">
                        <table class="common_modal_table" style="width:100%;">
                            <tbody id="modify_cate_tbody"></tbody>
                        </table>
                    </form>
                </div>
                <div id="modify_cate_detail_div" class="col-md-6" style="padding:0px;width:250px;height:290px;overflow:auto;border:solid #dcdcdc 1px;">
                    <table class="common_modal_table" style="width:100%;">
                        <thead id="modify_cate_detail_thead">
                            <tr>
                                <td id="modify_category_detail_text" colspan="2" style="font-size:16px;font-weight:bold;color:blue;">已有的分类</td>
                            </tr>
                            <tr>
                                <td style="font-weight:bold;">名称</td>
                                <td style="font-weight:bold;width:70px;">编码</td>
                            </tr>
                        </thead>
                        <tbody id="modify_cate_detail_tbody"></tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="modify_cate_ok_button">保存</button>
            <button type="button" class="btn yellow" id="cate_stop_use_button" style="margin-left:20px;margin-right:20px;">停用</button>
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

    <div id="add_cate_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="800">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">新增分类</h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <div class="row">
                <div class="col-md-6" style="width:520px;" >
                    <form id="add_cate_form">
                        <table class="common_modal_table" id="add_cate_table" style="width:100%;">

                        </table>
                    </form>
                </div>
                <div id="add_cate_detail_div" class="col-md-6" style="padding:0px;width:250px;height:290px;overflow:auto;border:solid #dcdcdc 1px;">
                    <table class="common_modal_table" style="width:100%;">
                        <thead id="add_cate_detail_thead">

                        </thead>
                        <tbody id="add_cate_detail_tbody"></tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="add_cate_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

    <!-- ================================== 自定义模板 ================================== -->
    <script type="text/template" id="list_template">
        {{#data}}
        <tr>
            <td style="text-align:center;">{{seqNumber}}</td>
            <td style="text-align:center;">
                <input type="hidden" value="{{partName}}">
                <input type="hidden" value="{{partId}}">
                <a href="javascript:void(0);" onclick="to_modify_part(this);">
                    修改
                </a>
            </td>
            <td style="text-align:center;">{{vehicleCode}}</td>
            <td>{{firstCatName}}</td>
            <td style="text-align:center;">{{firstCatCode}}</td>
            <td>{{secondCatName}}</td>
            <td style="text-align:center;">{{secondCatCode}}</td>
            <td>{{thirdCatName}}</td>
            <td style="text-align:center;">{{thirdCatCode}}</td>
            <td>{{partName}}</td>
            <td style="text-align:center;">{{partCode}}</td>
            <td>{{sumCode}}</td>
            <td>{{labelNameText}}</td>
            <td>{{alissNameText}}</td>
            <td style="text-align:center;">{{partKind}}</td>
            <td style="text-align:center;">{{isDeleted}}</td>
        </tr>
        {{/data}}
    </script>

    <script type="text/template" id="add_cate_init_template">
        <tbody>
        <tr>
            <th style="width:100px;">新增分类</th>
            <td>
                <select name="level" class="" style="width:220px;height:30px;" onchange="chooseCateLevel(this);">
                    <option value="-1">零件</option>
                    <option value="3">三级分类</option>
                    <option value="2">二级分类</option>
                    <option value="1">一级分类</option>
                </select>
            </td>
        </tr>
        </tbody>
        <tbody id="special_part">
        <tr>
            <th>一级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="first_cate_add" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择一级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>二级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="second_cate_add" class="" style="width:220px;height:30px;" name="cateParentId">
                    <option value="-1">--请选择二级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>三级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="third_cate_add" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择三级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>零件名称<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input id="cateId_add" type="hidden" name="cateId" value="-1">
                <input name="partName" maxlength="15" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填</span>
            </td>
        </tr>
        <tr>
            <th>位置码<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="code" maxlength="2" type="text" class="" style="width:220px;height:30px;" onkeyup="partCodeChange(this);">
                <span style="margin-left:5px;color:green;">必填，2位数字</span>
            </td>
        </tr>
        <tr>
            <th>零件编码</th>
            <td>
                <input id="sumCode_add" readonly name="sumCode" maxlength="9" class="" style="width:220px;height:30px;">
            </td>
        </tr>
        <tr>
            <th>标签</th>
            <td>
                <input name="labelNameText" maxlength="" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">多个值用,隔开</span>
            </td>
        </tr>
        <tr>
            <th>零件别名</th>
            <td>
                <input name="alissNameText" maxlength="" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">多个值用,隔开</span>
            </td>
        </tr>
        <tr>
            <th>类型</th>
            <td>
                <select name="catKind" class="" style="width:220px;height:30px;">
                    <option value="1">全车件</option>
                    <option value="0">易损件</option>
                </select>
            </td>
        </tr>
        </tbody>
    </script>

    <script type="text/template" id="part_template">
        <tr>
            <th>一级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="first_cate_add" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择一级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>二级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="second_cate_add" class="" style="width:220px;height:30px;" name="cateParentId">
                    <option value="-1">--请选择二级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>三级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="third_cate_add" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择三级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>零件名称<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input id="cateId_add" type="hidden" name="cateId" value="-1">
                <input name="partName" maxlength="15" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填</span>
            </td>
        </tr>
        <tr>
            <th>位置码<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="code" maxlength="2" type="text" class="" style="width:220px;height:30px;" onkeyup="partCodeChange(this);">
                <span style="margin-left:5px;color:green;">必填，2位数字</span>
            </td>
        </tr>
        <tr>
            <th>零件编码</th>
            <td>
                <input id="sumCode_add" readonly name="sumCode" maxlength="9" class="" style="width:220px;height:30px;">
            </td>
        </tr>
        <tr>
            <th>标签</th>
            <td>
                <input name="labelNameText" maxlength="" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">多个值用,隔开</span>
            </td>
        </tr>
        <tr>
            <th>零件别名</th>
            <td>
                <input name="alissNameText" maxlength="" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">多个值用,隔开</span>
            </td>
        </tr>
        <tr>
            <th>类型</th>
            <td>
                <select name="catKind" class="" style="width:220px;height:30px;">
                    <option value="1">全车件</option>
                    <option value="0">易损件</option>
                </select>
            </td>
        </tr>
    </script>

    <script type="text/template" id="third_cate_template">
        <tr>
            <th>一级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="first_cate_add" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择一级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>二级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="second_cate_add" name="parentId" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择二级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>三级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="catName" maxlength="10" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填</span>
            </td>
        </tr>
        <tr>
            <th>三级编码<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="code" maxlength="3" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填，3位数字</span>
            </td>
        </tr>
        <tr>
            <th>类型</th>
            <td>
                <select name="catKind" class="" style="width:220px;height:30px;">
                    <option value="1">全车件</option>
                    <option value="0">易损件</option>
                </select>
            </td>
        </tr>
    </script>

    <script type="text/template" id="second_cate_template">
        <tr>
            <th>一级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="first_cate_add" name="parentId" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择一级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>二级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="catName" maxlength="10" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填</span>
            </td>
        </tr>
        <tr>
            <th>二级编码<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="code" maxlength="2" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填，2位数字</span>
            </td>
        </tr>
        <tr>
            <th>类型</th>
            <td>
                <select name="catKind" class="" style="width:220px;height:30px;">
                    <option value="1">全车件</option>
                    <option value="0">易损件</option>
                </select>
            </td>
        </tr>
    </script>

    <script type="text/template" id="first_cate_template">
        <tr>
            <th>车辆种类码</th>
            <td>
                <select name="vehicleCode" class="" style="width:220px;height:30px;">
                    <option value="C">C</option>
                    <option value="CH">CH</option>
                    <option value="H">H</option>
                    <option value="X">X</option>
                </select>
                <input type="hidden" name="catId">
            </td>
        </tr>
        <tr>
            <th>一级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="catName" maxlength="6" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填</span>
            </td>
        </tr>
        <tr>
            <th>一级编码<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="code" maxlength="1" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填，1位数字或字母</span>
            </td>
        </tr>
        <tr>
            <th>类型</th>
            <td>
                <select name="catKind" class="" style="width:220px;height:30px;">
                    <option value="1">全车件</option>
                    <option value="0">易损件</option>
                </select>
            </td>
        </tr>
    </script>

    <script type="text/template" id="third_cate_template_modify">
        <tr>
            <th>二级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <select id="second_cate_add" name="parentId" class="" style="width:220px;height:30px;">
                    <option value="-1">--请选择二级分类--</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>三级分类<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="catName" maxlength="10" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填</span>
            </td>
        </tr>
        <tr>
            <th>三级编码<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="code" maxlength="3" type="text" class="" style="width:220px;height:30px;">
                <span style="margin-left:5px;color:green;">必填，3位数字</span>
            </td>
        </tr>
        <tr>
            <th>类型</th>
            <td>
                <select name="catKind" class="" style="width:220px;height:30px;">
                    <option value="1">全车件</option>
                    <option value="0">易损件</option>
                </select>
            </td>
        </tr>
    </script>

    <script type="text/template" id="add_part_detail_thead">
        <tr>
            <td colspan="2" style="font-size:16px;font-weight:bold;color:blue;">已有的零件</td>
        </tr>
        <tr>
            <td style="font-weight:bold;">零件名称</td>
            <td style="font-weight:bold;width:70px;">位置码</td>
        </tr>
    </script>

    <script type="text/template" id="add_category_detail_thead">
        <tr>
            <td id="add_category_detail_text" colspan="2" style="font-size:16px;font-weight:bold;color:blue;">已有的分类</td>
        </tr>
        <tr>
            <td style="font-weight:bold;">名称</td>
            <td style="font-weight:bold;width:70px;">编码</td>
        </tr>
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
    <script src="/resources/js/category/category.js" type="text/javascript"></script>
    <script src="/resources/js/category/addCategory.js" type="text/javascript"></script>
    <script src="/resources/js/category/modifyCategory.js" type="text/javascript"></script>


</body>
</html>