<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-品牌库数据查看</title>
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
    <script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
            type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>

    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->


<#--模板-->
    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>


<#--取消弹出框-->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet"
          type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>
    <!-- END PAGE LEVEL STYLES -->
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js"
            type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>

<#--select2 drop-->
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css"/>
    <script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>

<#--分页控件-->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript"></script>
<#--reload-->
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
<#--autocomplete-->
    <link href="/resources/jquery/jquery-ui.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">

        .table-btn {
            background-color: white;
            color: black;
            border: 1px solid #C0C0C0;
        }

        .modal {
            background-color: white;
        }
        td, th {
            text-align: center;
            vertical-align: middle !important;
        }

        input {
            border: 1px solid #e5e5e5;
            width: 100%;
            padding: 6px 12px;
            font-size: 14px;
            margin-bottom: 4px;
        }

        input:focus {
            webkit-transition: border linear .2s, -webkit-box-shadow linear .5s !important;
            outline: none;
            border-color: rgb(216, 5, 5) !important;
            box-shadow: 0 0 8px rgb(252, 40, 40) !important;
            -moz-box-shadow: 0 0 8px rgb(252, 40, 40) !important;
            -webkit-box-shadow: 0 0 8px rgb(252, 40, 40) !important;
        }

        .remark_btn {
            padding: 1px 5px !important;
        }

        section {
            border-bottom: 1px solid #eee;
            padding-left: 10px;
        }

        .ui-widget-content {
            background-color: white;
            z-index: 9999999;
            overflow: auto;
            min-height: 100px;
            max-height: 300px;
        }

        #attr_model_body .attr_show {
            position: relative;
            border: none;
        }

        #attr_model_body .attr_show.active {
            background-color: rgb(217, 83, 79);
            color: white;
            transition: border linear .2s, box-shadow linear .5s;
            -moz-transition: border linear .2s, -moz-box-shadow linear .5s;
            -webkit-transition: border linear .2s, -webkit-box-shadow linear .5s;
            outline: none;
            border-color: rgb(216, 5, 5);
            box-shadow: 0 0 8px rgb(252, 40, 40);
            -moz-box-shadow: 0 0 8px rgb(252, 40, 40);
            -webkit-box-shadow: 0 0 8px rgb(252, 40, 40);
        }

        #attr_model_body .attr_show>.delete_show {
            opacity: 0.0;
        }
        #attr_model_body .attr_show.active>.delete_show,
        #attr_model_body .attr_show.active:hover>.delete_show{
            position: absolute;
            left: 30%;
            font-size: 40px;
            color: white;
            opacity: 0.5;
        }

        #attr_model_body .attr_show:hover>.delete_show {
            position: absolute;
            left: 30%;
            font-size: 40px;
            color: rgb(217, 83, 79);
            opacity: 1.0;
        }

        #add_attr_body{
            overflow-y: auto;
            overflow-x: hidden;
            max-height:187px ;
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

<div>
    <div class="row">
        <div class="col-md-2">
            <select id="search_brand" style="display: block;">
                <option value="-1">-品牌-</option>
            </select>
        </div>
        <div class="col-md-2">
            <select id="search_part" style="display: block;">
                <option value="-1">-标准零件名称-</option>
            </select>
        </div>

        <div class="col-md-4">
            <input type="text" id="search_format" placeholder="商品的规格型号">
        </div>

        <div class="col-md-4">
                 <span class="btn btn-danger btn-block" id="sure_search_button" style="">
                    <span>确定筛选</span>
                </span>
        </div>

    </div>
</div>

<br>

<div class="row">
    <div class="col-md-2">
            <span class="btn blue btn-block" id="downLoad_goods_button" style="">
                <span>下载商品数据</span>
            </span>
    </div>

    <div class="col-md-2">
            <span class="btn blue btn-block" id="downLoad_car_button" style="">
                <span>下载车型数据</span>
            </span>
    </div>

    <#--新增数据隐藏-->
    <#--<div class="col-md-2">-->
            <#--<span class="btn blue btn-block" id="add_data_button" style="">-->
                <#--<span>新增数据</span>-->
            <#--</span>-->
    <#--</div>-->

</div>

<section>
    <div class="table-scrollable table-responsive" id="result_table">
        <table class="table">
            <thead>
            <tr>
                <th style="width: 1%">#</th>
                <th>编码</th>
                <th>属性</th>
                <th>品牌</th>
                <th>商品名称</th>
                <th>零件标准名称</th>
                <th>规格</th>
                <th>类型</th>
                <th>备注</th>
                <th>销售单位</th>
                <th>销售状态</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="result_table_tbody">

            </tbody>
        </table>

    <#-- 分页 -->
        <input type="hidden" id="result_table_page_index" value="0">

        <div style="text-align: center; padding: 10px;">
            <div class="pagination" id="main_pagination">
                <a href="#" class="first" data-action="first">首页</a>
                <a href="#" class="previous" data-action="previous">上一页</a>
                <input type="text" readonly="readonly"/>
                <a href="#" class="next" data-action="next">下一页</a>
                <a href="#" class="last" data-action="last">末页</a>
            </div>
        </div>
    </div>
</section>

<#--查看力洋车型弹窗-->
<div id="liyang_car_model" class="modal container fade" tabindex="-1">
    <input type="hidden" id="liyang_car_model_uuId">
    <input type="hidden" id="liyang_car_model_pageSize">
    <input type="hidden" id="liyang_car_model_del_liyangId">

    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 class="modal-title">
            匹配的力洋车型
        </h4>
    </div>
    <div class="modal-body">
    <#--<button class="btn red" id="add_liyang_button"><i class="fa fa-plus"></i></button>-->
        <div class="row">
            <div class="col-md-4">
                <input type="text" placeholder="力洋Id，回车->搜索" class="search" id="liyang_car_model_search">
            </div>
            <#--<div class="col-md-2">-->
                <#--<button  class="btn red btn-block" id="add_liyang_btn">添加此力洋ID</button>-->
            <#--</div>-->
        </div>
        <div class="table-responsive">
            <table class="table">
                <thead>
                <tr>
                    <th>力洋ID</th>
                    <th>品牌</th>
                    <th>厂商</th>
                    <th>车系</th>
                    <th>车型</th>
                    <th>销售名称</th>
                    <th>生产年份</th>
                    <th>上市年份</th>
                    <th>排量(升)</th>
                    <th>变速器类型</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="liyang_car_model_table_tbody">

                </tbody>
            </table>

        <#-- 分页 -->
            <div style="text-align: center;">
                <input type="hidden" id="liyang_car_model_page_index" value="0">

                <div class="pagination" id="liyang_car_model_pagination">
                    <a href="#" class="first" data-action="first">首页</a>
                    <a href="#" class="previous" data-action="previous">上一页</a>
                    <input type="text" readonly="readonly"/>
                    <a href="#" class="next" data-action="next">下一页</a>
                    <a href="#" class="last" data-action="last">末页</a>
                </div>
            </div>
        </div>

    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
    </div>
</div>

<#--参数-->
<div id="attr_model" class="modal fade" tabindex="-1" style="overflow-x: hidden;">
    <div class="modal-body">
        <div class="row" style="max-height:200px ;">
            <div id="attr_model_body">

            </div>
            <div id="add_attr_body">

            </div>
        </div>

    </div>
    <div class="modal-footer">
        <button class="btn blue" id="add_attr_button"><i class="fa fa-plus"></i> 添加参数</button>

        <#--<button type="button" class="btn btn-danger " id="save_attr_btn">保存</button>-->
    </div>
</div>

<#--进度条——Model-->
<div class="modal fade" id="progress_model" tabindex="-1" data-backdrop="static" data-keyboard="false">
    <div class="progress progress-striped active" style="width: 100%;margin-bottom: 0px;">
        <div id="upload_progress" class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80"
             aria-valuemin="0" aria-valuemax="100" style="width: 0%">
            <span id="upload_progress_span" class="">0%</span>
        </div>
    </div>
</div>

<#--编辑弹窗-->
<#--编辑页面-->
<div id="edit_model" class="modal container fade"
     style="overflow-x: hidden;">
    <div class="modal-header" style="text-align: center;">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
        <h4 class="modal-title" id="edit_model_title">商品</h4>
    </div>

    <div class="modal-body" id="edit_model_body" style="overflow-x: hidden; padding-bottom: 0;">
    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
        <#--<button type="button" class="btn btn-danger " id="edit_model_sure_button">确定</button>-->
    </div>
</div>

<#--确定取消的_Model-->
<div id="make_sure_model" class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">
    <div class="modal-body" style="overflow-x: hidden;">

        <div class="row">
            <p id="make_sure_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;"></p>
        </div>

    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn btn-danger">确定</button>
    </div>
</div>

<#--confirm-->
<div id="confirm_model" class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">
    <div class="modal-body" style="overflow-x: hidden;">

        <div class="row">
            <p id="confirm_model_content"
               style="font-size: 20px;text-align: center;vertical-align: middle !important;  color: black;">

            </p>
        </div>

    </div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
        <button type="button" class="btn btn-danger " id="confirm_model_sure_button">确定</button>
    </div>
</div>


<#--============================模板=======================================-->
<script type="text/template" id="search_select_template">
    <option value="-1">{{placeholder}}</option>
    {{#list}}
    <option value="{{key}}">{{value}}</option>
    {{/list}}
</script>

<script type="text/template" id="attr_show_template">
    {{#list}}
    <div class="row" style="text-align: center;">
        {{#colList}}
        <div class="col-md-3">
            <span class="btn btn-default attr_show">
                {{firstKey}}:{{firstValue}}
                <i class="fa fa-times delete_show"></i>
            </span>

        </div>
        {{/colList}}
    </div>
    <br>
    {{/list}}
</script>
<script type="text/template" id="attr_add_template">
    <div class="row">
        <div class="col-md-6">
            <input type="text" class="attr_key" placeholder="参数名,可空" style="width: 45%;">
            <i class="fa fa-minus"></i>
            <input type="text" class="attr_value" placeholder="参数值,可空" style="width: 45%;">
        </div>
        <div class="col-md-6">
            <input type="text" class="attr_key" placeholder="参数名,可空" style="width: 45%;">
            <i class="fa fa-minus"></i>
            <input type="text" class="attr_value" placeholder="参数值,可空" style="width: 45%;">
        </div>
    </div>

</script>

<script type="text/template" id="oe_show_template">
    {{#list}}
    <tr>
        <td class="success" style="width: 25%;">{{one}}</td>
        <td class="danger" style="width: 25%;">{{two}}</td>
        <td class="success" style="width: 25%;">{{three}}</td>
        <td class="danger" style="width: 25%;">{{four}}</td>
    </tr>
    {{/list}}
</script>


<script type="text/template" id="result_data_template">

    {{#goodsList}}
    <tr>
        <input type="hidden" class="id" value="{{id}}">
        <input type="hidden" class="goodsQualityType" value="{{goodsQualityType}}">
        <input type="hidden" class="brandId" value="{{brandId}}">
        <input type="hidden" class="cateKind" value="{{cateKind}}">
        <input type="hidden" class="guaranteeTime" value="{{guaranteeTime}}">
        <input type="hidden" class="partId" value="{{partId}}">
        <input type="hidden" class="partSumCode" value="{{partSumCode}}">
        <input type="hidden" class="remark" value="{{remark}}">

        <td>
            <span>{{indexs}}</span>
        </td>
        <td><span class="goodsCode">{{goodsCode}}</span></td>
        <td><span>{{{qualityName}}}</span></td>
        <td><span class="brandName">{{brandName}}</span></td>
        <td><span class="goodsName">{{goodsName}}</span></td>
        <td><span class="partName">{{partName}}</span></td>
        <td><span class="goodsFormat">{{goodsFormat}}</span></td>
        <td><span>{{{cateKindName}}}</span></td>
        <td><span>{{{remarkRender}}}</span></td>
        <td><span class="saleUnit">{{saleUnit}}</span></td>
        <td>{{{saleStatusName}}}</td>
        <td style="text-align:center;">
            <input type="hidden" value="{{uuId}}" class="uuId">

        <#--<button class="table-btn see_oe_btn">-->
        <#--OE-->
        <#--</button>-->
            <button class="table-btn edit_btn">
                编辑
            </button>
            <button class="table-btn see_attr_btn">
                参数
            </button>
            <button class="table-btn see_liyang_car_btn">
                <i class="fa fa-truck"></i>
                适配车型
            </button>
        </td>
    </tr>
    {{/goodsList}}
</script>

<script type="text/template" id="liyang_car_data_template">
    {{#resultDataList}}
    <tr>
        <td class="leyelId">{{leyelId}}</td>
        <td>{{carBrand}}</td>
        <td>{{factoryName}}</td>
        <td>{{carSeries}}</td>
        <td>{{vehicleType}}</td>
        <td>{{marketName}}</td>
        <td>{{createYear}}</td>
        <td>{{publicYear}}</td>
        <td>{{displacement}}</td>
        <td>{{transmissionType}}</td>
        <td>
            <button class="table-btn del_liyang_car_btn">
                <i class="fa fa-truck"></i>
                删除
            </button>
        </td>
    </tr>
    {{/resultDataList}}
</script>
<#-- ==============弹出编辑页面层的template=================-->

<script type="text/template" id="all_brand_select_template">
    <option value="-1">{{placeholder}}</option>
    {{#list}}
    <option value="{{id}}">{{{brandName}}}</option>
    {{/list}}
</script>


<script type="text/template" id="tips_template">
    <i class="{{tips}} tooltips" data-original-title="{{content}}"></i>
</script>

<script type="text/template" id="model_oe_template">
    <div class="col-md-5 edit_model_oe">
        <input type="text" class="" placeholder="输入OE码，可为空" value="{{oe_number}}">
    </div>
</script>

<#--编辑数据-->
<script type="text/template" id="edit_data_template">

    <form class="form-horizontal" style="overflow-x: hidden;">
        <input type="hidden" id="edit_model_id" value="{{edit_model_id}}">
        <input type="hidden" id="edit_model_uuId" value="{{edit_model_uuId}}">

        <section>
            <h4>基本属性</h4>

            <div class="form-group">
                <div class="row">
                    <label class="col-md-1 control-label">
                        商品名称<span class="required">*</span>
                    </label>

                    <div class="col-md-5">
                        <input type="text" class="" placeholder="输入商品名称" id="edit_model_goodsName"
                               value="{{edit_model_goodsName}}">
                    </div>
                    <label class="col-md-1 control-label">
                        商品属性
                    </label>

                    <div class="col-md-5">
                        <select id="edit_model_quality" style="width: 80%;">
                            <option value="0">品牌件</option>
                            <option value="1">正厂原厂</option>
                            <option value="2">正厂配套</option>
                            <option value="3">正厂下线</option>
                            <option value="4">全新拆车</option>
                            <option value="5">旧件拆车</option>
                            <option value="6">副厂</option>
                            <option value="9">高仿</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <label class="col-md-1 control-label">
                        商品编码<span class="required">*</span>
                    </label>

                    <div class="col-md-5">
                        <input type="text" class="" placeholder="输入商品编码" id="edit_model_code"
                               value="{{edit_model_code}}">
                    </div>
                    <label class="col-md-1 control-label">
                        品牌<span class="required">*</span>
                    </label>

                    <div class="col-md-5">
                        <select id="edit_model_brand" style="width: 80%;">

                        </select>
                    </div>
                </div>
                <div class="row">
                    <label class="col-md-1 control-label">
                        规格型号<span class="required">*</span>
                    </label>

                    <div class="col-md-5">
                        <input type="text" class="" placeholder="输入规格型号,可为空" id="edit_model_format"
                               value="{{edit_model_format}}">
                    </div>
                    <label class="col-md-1 control-label">
                        商品类型
                    </label>

                    <div class="col-md-5">
                        <select id="edit_model_cate" style="width: 80%;">
                            <option value="0">易损件</option>
                            <option value="1" selected>全车件</option>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <label class="col-md-1 control-label">
                        质保期
                    </label>

                    <div class="col-md-5">
                        <input type="text" class="" placeholder="输入质保期,可为空" id="edit_model_time"
                               value="{{edit_model_time}}">
                    </div>
                    <label class="col-md-1 control-label">
                        销售单位
                    </label>

                    <div class="col-md-5">
                        <input type="text" class="" placeholder="输入销售单位，例如'个'" id="edit_model_sale_unit"
                               value="{{edit_model_sale_unit}}">
                    </div>
                </div>
                <div class="row ">
                    <label class="col-md-1 control-label">
                        标准零件<span class="required">*</span>
                    </label>

                    <div class="">
                        <div class="col-md-5 input-icon right ">
                            <i class="" data-original-title="成功!"></i>
                            <input type="text" class="form-control" placeholder="输入标准零件名称或零件编码" id="edit_model_part"
                                   value="{{edit_model_part}}">
                            <input type="hidden" id="edit_model_part_id" value="{{edit_model_part_id}}">
                        </div>
                    </div>

                    <label class="col-md-1 control-label">
                        备注
                    </label>

                    <div class="col-md-5">
                        <input type="text" class="" placeholder="输入备注" id="edit_model_remark"
                               value="{{edit_model_remark}}">
                    </div>

                </div>
            </div>
        </section>
        <section>
            <h4>OE码
                <button class="btn red" id="add_oe_button"><i class="fa fa-plus"></i></button>
            </h4>
            <div class="row" id="edit_model_oe_body">

            </div>
        </section>
    <#--<section>-->
    <#--<h4>参数-->
    <#--<button class="btn red" id="add_atrr_button"><i class="fa fa-plus"></i></button>-->
    <#--</h4>-->
    <#--<div class="row" id="edit_model_attr_body">-->

    <#--</div>-->
    <#--</section>-->


    </form>
</script>

<script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

<script src="/resources/jquery/jquery-ui.js" type="text/javascript"></script>

<!-- END PAGE LEVEL SCRIPTS -->
<script>
    jQuery(document).ready(function () {
        App.init();
    });
</script>


<#--mainJS-->
<script src="/resources/js/commodity/show.js" type="text/javascript"></script>
</body>


</html>