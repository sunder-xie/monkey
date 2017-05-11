<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-配件库数据查看</title>
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
    <#--<script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>-->
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
    <#--<script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>-->
    <script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>

    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->


    <#--模板-->
    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>


    <#--取消弹出框-->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>
    <!-- END PAGE LEVEL STYLES -->
    <!-- BEGIN PAGE LEVEL PLUGINS -->
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript" ></script>

    <#--select2 drop-->
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css" />
    <script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>

    <#--分页控件-->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <#--tree树-->
    <link rel="stylesheet" type="text/css" href="/resources/css/zTreeStyle.css"/>
    <script src="/resources/js/lib/jquery.ztree.core-3.5.js" type="text/javascript"></script>
    <script src="/resources/js/lib/jquery.ztree.exhide-3.5.js" type="text/javascript"></script>

    <#--reload-->
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">

        .table-btn {
            background-color: white;
            color: black;
            border: 1px solid #C0C0C0;
        }
        .modal {
            background-color: white;
        }
        td,th{
            text-align: center;
            vertical-align: middle !important;
        }
        input{
            border: 1px solid #e5e5e5 ;
            width: 100%;
            padding: 6px 12px;
            font-size: 14px;
        }
        input:focus
        {
            webkit-transition: border linear .2s,-webkit-box-shadow linear .5s !important;
            outline: none;
            border-color: rgb(216, 5, 5) !important;
            box-shadow: 0 0 8px rgb(252, 40, 40) !important;
            -moz-box-shadow: 0 0 8px rgb(252, 40, 40) !important;
            -webkit-box-shadow: 0 0 8px rgb(252, 40, 40) !important;
        }


        /*力洋ID的显示*/
        #liyangIDDiv_body {
            max-height: 250px;
            overflow-y: auto;
            overflow-x: hidden;
        }

        #liyangIDDiv{

            -webkit-transition: all 0.4s ease-in-out 0s;
            margin: 0 0 20px 0;
            padding: 15px 30px 15px 15px;
            border-top: 5px solid #ed4e2a;
            background-color: #F5F5F5;
            border-color: #ed4e2a;
        }

        #liyangIDDiv>.row{
            text-align: center;
        }
        .spanRow{
            border-top: 1px dashed #dedede;
        }

        .spanRow>.span{
            border: 1px solid;
            border-radius: 5px !important;
            text-align: center;
            margin-top: 10px;
            margin-left: 30px;
            margin-bottom: 10px;
            cursor:pointer;
            border-color: #F5F5F5;
            color: green;
        }

        /*闪光效果*/
        .spanRow>.span.active{
            background-color: rgb(217, 83, 79);
            color: white;
            transition: border linear .2s,box-shadow linear .5s;
            -moz-transition: border linear .2s,-moz-box-shadow linear .5s;
            -webkit-transition: border linear .2s,-webkit-box-shadow linear .5s;
            outline: none;
            border-color: rgb(216, 5, 5);
            box-shadow: 0 0 8px rgb(252, 40, 40);
            -moz-box-shadow: 0 0 8px rgb(252, 40, 40);
            -webkit-box-shadow: 0 0 8px rgb(252, 40, 40);

        }

    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">
<#--数据统计显示-->
<div>
    配件: <span id="statisticalData_oe" style="color: red"></span>
</div>
<div class="row " style="width: 100%;">
    <div class="col-md-3">
        品牌: <span id="statisticalData_brand" style="color: red"></span>
    </div>
    <div class="col-md-3">
        厂商: <span id="statisticalData_factory" style="color: red"></span>
    </div>
    <div class="col-md-3">
        车系: <span id="statisticalData_series" style="color: red"></span>
    </div>
    <div class="col-md-3">
        车型: <span id="statisticalData_model" style="color: red"></span>
    </div>
</div>

<br>

<#--力洋的车型选择-->
    <div class="row " style="width: 100%;">
        <input type="hidden" id="choose_l_id" value="0">
        <div class="col-md-3">
            <select id="search_brand" style="display: block;"></select>

        </div>
        <div class="col-md-3">
            <select id="search_factory" style="display: block;" ></select>

        </div>
        <div class="col-md-3">
            <select id="search_series" style="display: block;" ></select>

        </div>
        <div class="col-md-3">
            <select id="search_model" style="display: block;"></select>
        </div>
    </div>

    <br>
<#--力洋的属性并列-->
<div id="liyangExt" class="hidden">
    <div class="row " style="width: 100%;">
        <div class="col-md-3">
            <select id="search_display_year" style="display: block;"></select>

        </div>
        <div class="col-md-3">
            <select id="search_trans" style="display: block;" ></select>

        </div>
        <div class="col-md-3">
            <span class="btn btn-default" id="liyangId_button">
                <i class="fa fa-eye"></i>
                <span>力洋ID</span>
            </span>
        </div>

        <div class="col-md-3">
            <span class="btn btn-danger" id="sure_search_button" style="" >
                <span>确定筛选</span>
            </span>
            <span class="btn btn-success hidden" id="delete_all_button" style="" >
                <span>删除记录</span>
            </span>
        </div>
    </div>

    <br>
    <div id="liyangIDDiv" class=" hidden" style="">

        <div class="row" style="">
            <span class="btn green" id="liyangIDDiv_resert_button" style="margin-bottom: 5px;" >
                <span>全部撤销</span>
            </span>
        </div>

        <div id="liyangIDDiv_body">

        </div>

    </div>

    <br>
</div>
    <#--分类-->

    <#--<input type="hidden" id="search_sum_code" value="">-->

    <input type="hidden" id="search_firstCate" value="">
    <input type="hidden" id="search_secondCate" value="">
    <input type="hidden" id="search_thirdCate" value="">


    <#--显示数据-->
    <div class="row">
        <div class="col-md-12">
            <div class="portlet">
                <div class="portlet-title">
                    <div class="tools" style="float: left;">

                        <a href="javascript:;" class="collapse"></a>
                    </div>
                </div>
                <div class="portlet-body hidden" style="" id="search_result_body">
                    <div class="row">
                         <#--分类选项-->
                        <div class="col-md-3" style="width: 20%;">
                            <div id="cate_select_box_div" class="panel  panel-default" style="">
                                <div class="panel-heading" style="text-align:center">配件总数：
                                    <span style="color: red;" id="part_sum_number">0</span>
                                </div>
                                <div class="panel-heading">
                                    <div class="input-group">
                                        <input type="text" class="" placeholder="搜一级分类" id="cate_search_keyword">
                                        <span class="input-group-btn">
                                            <button class="btn red " id="search_cate_btn" type="button">筛选</button>
                                        </span>
                                    </div>
                                </div>
                                <div id="cate_select_div" class="panel-body" style="overflow-y:scroll;height:400px;">
                                    <ul id="cate_select_tree" class="ztree"></ul>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-9" style="width: 80%;">
                            <div class="panel  panel-default" style="margin-bottom:1px;">
                                <div class="panel-heading" style="text-align:center">
                                    <span> 配件结果</span>
                                </div>
                                <div class="panel-body">
                                    <div class="table-scrollable table-responsive" id="result_table">
                                        <table class="table">
                                            <thead>
                                            <tr>
                                                <th style="width: 1%">#</th>
                                                <th>图号</th>
                                                <th>图序号</th>
                                                <th>原厂OE码</th>
                                                <th>标准名称</th>
                                                <th>标准编码</th>
                                                <th>用量</th>
                                                <th>备注</th>
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
                                                <a href="#" class="first" data-action="first" >首页</a>
                                                <a href="#" class="previous" data-action="previous" >上一页</a>
                                                <input type="text" readonly="readonly" />
                                                <a href="#" class="next" data-action="next" >下一页</a>
                                                <a href="#" class="last" data-action="last" >末页</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>


    <#--查看力洋车型弹窗-->
    <div id="liyang_car_model" class="modal container fade" tabindex="-1">
        <input type="hidden"  id="liyang_car_model_goodsId">
        <input type="hidden"  id="liyang_car_model_picId">
        <input type="hidden"  id="liyang_car_model_pageSize">

        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h4 class="modal-title">
                匹配的力洋车型：
                <span class="label label-danger" id="liyang_car_model_title"></span>
            </h4>
        </div>
        <div class="modal-body">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>力洋ID</th>
                        <th>销售名称</th>
                        <th>生产年份</th>
                        <th>上市年份</th>
                        <th>指导价格(万)</th>
                        <th>排量(升)</th>
                        <th>变速器类型</th>
                        <th>进气形式</th>
                        <th>燃料类型</th>
                    </tr>
                    </thead>
                    <tbody id="liyang_car_model_table_tbody">

                    </tbody>
                </table>

            <#-- 分页 -->
                <div style="text-align: center;">
                    <input type="hidden" id="liyang_car_model_page_index" value="0">
                    <div class="pagination" id="liyang_car_model_pagination">
                        <a href="#" class="first" data-action="first" >首页</a>
                        <a href="#" class="previous" data-action="previous" >上一页</a>
                        <input type="text" readonly="readonly" />
                        <a href="#" class="next" data-action="next" >下一页</a>
                        <a href="#" class="last" data-action="last" >末页</a>
                    </div>
                </div>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn-default">关闭</button>
            <#--<button type="button" class="btn blue">Save changes</button>-->
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
            <#--<button type="button"  class="btn blue " id="make_sure_model_sure_button">确定</button>-->
        </div>
    </div>

    <#--confirm-->
    <div id="confirm_model" class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">
        <div class="modal-body" style="overflow-x: hidden;">

            <div class="row">
                <p id="confirm_model_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;  color: black;">

                </p>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
            <button type="button"  class="btn btn-danger " id="confirm_model_sure_button">确定</button>
        </div>
    </div>


    <#--============================模板=======================================-->
    <script type="text/template" id="car_select_template">
        <option value="-1">{{placeholder}}</option>
        {{#list}}
        <option value="{{.}}">{{.}}</option>
        {{/list}}
    </script>

    <script type="text/template" id="liyang_select_body">
        {{#listArray}}
            <div class="row spanRow">
                {{#list}}
                    <span class="col-md-2 span" >
                        <span class="span_id">{{id}}</span>
                        <br>
                        <span>{{name}}</span>
                    </span>
                {{/list}}
            </div>
        {{/listArray}}
    </script>

    <script type="text/template" id="first_cate_select_template">
        <option value="-1">{{placeholder}}</option>
        {{#list}}
        <option value="{{catId}}">{{vehicleCode}}-{{catName}}</option>
        {{/list}}
    </script>

    <script type="text/template" id="cate_select_template">
        <option value="-1">{{placeholder}}</option>
        {{#list}}
        <option value="{{catId}}">{{catName}}</option>
        {{/list}}
    </script>

    <script type="text/template" id="result_data_template">
        {{#resultDataList}}
        <tr>
            <td>
                <span>{{{indexesFunc}}}</span>
            </td>
            <td><span>{{pictureNum}}</span></td>
            <td><span>{{pictureIndex}}</span></td>
            <td><span>{{oeNumber}}</span></td>
            <td><span>{{partName}}</span></td>
            <td><span>{{partCode}}</span></td>
            <td><span>{{applicationAmount}}</span></td>
            <td  style="text-align:left;"><span>{{remarks}}</span></td>
            <td style="text-align:center;">
                <input type="hidden" value="{{goodsId}}" class="goodsId">
                <input type="hidden" value="{{picId}}" class="picId">

                <button class="table-btn see_liyang_car_btn">
                    <i class="fa fa-truck" style="color: #ff0000;"></i>
                    适配车型
                </button>
            </td>
        </tr>
        {{/resultDataList}}
    </script>

    <script type="text/template" id="liyang_car_data_template">
        {{#resultDataList}}
        <tr>
            <td>{{leyelId}}</td>
            <td>{{marketName}}</td>
            <td>{{createYear}}</td>
            <td>{{publicYear}}</td>
            <td>{{guidePrice}}</td>
            <td>{{displacement}}</td>
            <td>{{transmissionType}}</td>
            <td>{{intakeStyle}}</td>
            <td>{{fuelType}}</td>
        </tr>
        {{/resultDataList}}
    </script>

    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();
            $("#search_display").select2();
            $("#search_year").select2();
            $("#search_trans").select2();
        });
    </script>


<#--mainJS-->
    <script src="/resources/js/part/dataShow.js" type="text/javascript"></script>
</body>


</html>