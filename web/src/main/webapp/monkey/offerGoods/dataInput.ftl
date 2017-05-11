<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-供应商数据导入</title>
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
    <script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
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

<#--上传文件 css-->
    <link href="/resources/assets/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet" />
    <#--异步表单-->
    <script src="/resources/assets/plugins/jquery-validation/lib/jquery.form.js" type="text/javascript"></script>
    <#--警告框，不断闪 js-->
    <script src="/resources/assets/plugins/jquery.pulsate.min.js" type="text/javascript"></script>

    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <script src="/resources/js/common/common.js" type="text/javascript" ></script>

    <#--reload-->
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>

<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">

    td,th{
        text-align: center;
        vertical-align: middle !important;
    }
    .modal {
        background-color: white;
    }

    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <#--错误评判标准-->
    <div class="row">
        <div class="col-md-12">
            <div class="portlet">
                <div class="portlet-title">
                    <div class="tools" style="float: left;">

                        <a href="javascript:;" class="collapse"></a>
                    </div>
                </div>
                <div class="portlet-body" style="display: block;">
                    <blockquote>
                        <p style="font-size:16px">
                            认定为错误数据：<br>
                            1.OE码:空、含有特殊字符（除-和()）、包含中文<br>
                            2.缺少“产品属性”<br>
                            3.缺少“建议零售价”或为“0”的商品<br>
                            4.配件名为空<br>
                            5.淘气价格包含单位<br>
                            6.“供应淘汽价格”大于“建议零售价”
                            <br>
                            注意事项：<br>
                            1.excle只识别前两个sheet，即sheet1和sheet2<br>
                            2.表格第一个字段序号必须有，否则该条记录无法保存<br>
                        <#--2.淘汽品牌和淘汽厂商不支持‘/’，淘汽车系和排量和年款支持‘/’<br>-->
                            3.商品相关的表头下属的数据中为‘0’均被替换为空<br>
                            4.供货价和建议价均在数据库中保存价格最低的值<br>
                            5.商品属性仅支持两种格式，如（a.相信 b.品牌/相信）（a.原厂 b.原厂/博士）。b的‘/’前为属性，后为品牌名<br>
                        </p>
                    </blockquote>
                </div>
            </div>

        </div>
    </div>

    <#--上传文件-->
    <form method="POST" id="fileupload" enctype="multipart/form-data" action="/monkey/offerGoods/dataInput/uploadExcle" class="form">
        <div class="row">
            <div class="col-md-12">
                <span class="btn red fileinput-button">
                    <i class="fa fa-plus"></i>
                    <span>添加excle文件</span>
                    <input type="file" name="excle_file" id="excle_file" multiple="">
                </span>
                <button class="btn blue" id="down_template_btn">
                    <i class="fa fa-download"></i>
                    <span>下载标准模板</span>
                </button>

                <span class="hidden" id="fail_btn_group">
                    <button type="button" class="btn btn-info" id="data_see_btn">
                        <i class="fa fa-eye"></i>
                        <span>
                            查看失败数据
                        </span>

                    </button>
                    <span>
                        <button type="button" class="btn btn-success" id="data_out_excle_btn">
                            <i class="fa fa-cloud-download"></i>
                            <span>导出失败数据</span>
                        </button>
                    </span>
                </span>
                <#--<button type="submit" class="start">Start upload</button>-->
                <#--<button type="reset" class="cancel">Cancel upload</button>-->
                <#--<button type="button" class="delete">Delete</button>-->

            </div>
        </div>
        <br>
        <#--文件预览栏-->
        <div class="row">
            <div class="table-responsive">
                <table class="table">
                    <tbody >
                        <tr id="table_result">

                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </form>

    <#--上传结果的展示-->
    <div class="panel panel-success hidden" id="upload_result_panel">
        <div class="panel-heading">
            <h3 class="panel-title">Excle解析结果</h3>
        </div>
        <div class="panel-body">
            <div class="table-scrollable table-responsive hidden" id="fail_data_table">
                <table class="table">
                    <thead>
                    <tr>
                        <th style="width: 1%">#</th>
                        <th>失败原因</th>
                        <th>导入的excle名</th>
                        <th>序号</th>
                        <th>供应商名称</th>
                        <th>商品名称</th>
                        <th>属性</th>
                        <th>OE码</th>
                        <th>供应价</th>
                        <th>建议价</th>
                        <th>标准零件名称</th>
                        <th>标准零件编号</th>
                        <th>淘汽品牌</th>
                        <th>淘汽厂商</th>
                        <th>淘汽车系</th>
                        <th>淘汽车型</th>
                        <th>排量</th>
                        <th>年款</th>
                        <th>购买单位</th>
                        <th>包装规格</th>
                        <th>规格型号</th>
                        <th>备注</th>
                        <th>易损件</th>
                    </tr>
                    </thead>
                    <tbody id="fail_data_table_tbody">

                    </tbody>
                </table>

            <#-- 分页 -->
                <div style="text-align: center; padding: 10px;">
                    <#--<ul class=" pagination">-->
                        <#--<li class="first" data-action="first"><a href="#">&laquo;</a></li>-->
                        <#--<li class="prev previous" data-action="previous"><a href="#">&lsaquo;</a></li>-->
                        <#--<li class="page">-->
                            <#--<input type="hidden" id="this_page_index" value="0">-->
                            <#--<input type="text"  data-max-page="40" />-->
                        <#--</li>-->
                        <#--<li class="next" data-action="next"><a href="#">&rsaquo;</a></li>-->
                        <#--<li class="last" data-action="last"><a href="#">&raquo;</a></li>-->
                    <#--</ul>-->


                    <div class="pagination">
                        <a href="#" class="first" data-action="first" >首页</a>
                        <a href="#" class="previous" data-action="previous" >上一页</a>
                        <input type="hidden" id="this_page_index" value="0">
                        <input type="text" readonly="readonly" data-max-page="40" />
                        <a href="#" class="next" data-action="next" >下一页</a>
                        <a href="#" class="last" data-action="last" >末页</a>
                    </div>
                </div>

            </div>

            <ul>
                <li>
                    解析记录总数：
                    <span id="sum_number" style="padding:10px;">0</span>
                </li>
                <br>
                <li>
                    成功记录数量：
                    <span id="success_number" style="padding:10px;">0</span>
                </li>
                <br>
                <li>
                    <span>
                        失败记录数量：
                        <span id="fail_number" style="padding:10px;">
                            0
                        </span>
                    </span>
                </li>
                <br>
                <li>
                    <span>
                        失败未导出总数：
                        <span id="fail_sum_number" style="padding:10px;">
                            0
                        </span>
                    </span>
                </li>

            </ul>

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
                <p id="confirm_model_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;">

                </p>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
            <button type="button"  class="btn btn-danger " id="confirm_model_sure_button">确定</button>
        </div>
    </div>

    <#--============================模板=======================================-->
    <#--上传显示的-->
    <script type="text/template" id="upload_body_template">
        <td style="width: 10%;">
            <img alt="" src="/resources/assets/img/excle.png" width="50px" height="50px">
        </td>
        <td style="width: 30%;">{{file_name}}</td>
        <td style="width: 10%;">{{fileSize}}</td>
        <td style="width: 30%;">
            <div class="progress progress-striped active" style="width: 100%;margin-bottom: 0px;">
                <div id="upload_progress" class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                    <span id="upload_progress_span" class="">0%</span>
                </div>
            </div>
        </td>
        <td style="width: 20%;">
            <button  class="btn red start"  type="submit" id="submit_btn"><i class="fa fa-upload"></i><span>上传</span></button>
            &nbsp;
            <div class="btn default"  id="reset_btn"><i class="fa fa-ban"></i><span>取消</span></div>
        </td>
    </script>

    <#--//失败信息的显示-->
    <script type="text/template" id="fail_data_template">
        {{#wrongDataList}}
            <tr>
                <td>
                    <span>{{id}}</span>
                </td>
                <td>
                    <span>
                        <button type="button" class="btn red btn-sm fail_data_see_failreason">查看</button>
                    </span>
                    <div class="hidden">
                        {{#failReasonArray}}
                        <br>
                        <span>{{.}}</span>
                        {{/failReasonArray}}
                    </div>
                </td>
                <td><span>{{recordName}}</span></td>
                <td><span>{{indexs}}</span></td>
                <td><span>{{providerName}}</span></td>
                <td><span>{{goodsName}}</span></td>
                <td><span>{{goodsAttribute}}</span></td>
                <td><span>{{oeNumber}}</span></td>
                <td><span>{{primaryPrice}}</span></td>
                <td><span>{{advicePrice}}</span></td>
                <td><span>{{partName}}</span></td>
                <td><span>{{partCode}}</span></td>
                <td><span>{{brand}}</span></td>
                <td><span>{{company}}</span></td>
                <td><span>{{series}}</span></td>
                <td><span>{{model}}</span></td>
                <td><span>{{displacement}}</span></td>
                <td><span>{{year}}</span></td>
                <td><span>{{measureUnit}}</span></td>
                <td><span>{{packageFormat}}</span></td>
                <td><span>{{goodsFormat}}</span></td>
                <td><span>{{remark}}</span></td>
                <td><span>{{quickWearLabel}}</span></td>
            </tr>
        {{/wrongDataList}}
    </script>


    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();
        });
    </script>


<#--mainJS-->
    <script src="/resources/js/offerFoods/dataInput.js" type="text/javascript"></script>
</body>


</html>