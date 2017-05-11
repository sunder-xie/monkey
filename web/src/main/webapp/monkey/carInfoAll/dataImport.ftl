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
    <!-- 弹出框 -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>
    <!-- 文件上传 -->
    <link href="/resources/assets/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet" />
<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <!--错误评判标准-->
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

                        </p>
                    </blockquote>
                </div>
            </div>

        </div>
    </div>

    <!--上传文件-->
    <form method="POST" id="fileupload" enctype="multipart/form-data" action="/monkey/carInfoAll/toUploadExcle" class="form">
        <div class="row">
            <div class="col-md-12">
                <span class="btn red fileinput-button">
                    <i class="fa fa-plus"></i>
                    <span>添加excle文件</span>
                    <input type="file" name="excle_file" id="excle_file" multiple="">
                </span>

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
                <!--<button type="submit" class="start">Start upload</button>-->
                <!--<button type="reset" class="cancel">Cancel upload</button>-->
                <!--<button type="button" class="delete">Delete</button>-->

            </div>
        </div>
        <br>
        <!--文件预览栏-->
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

    <!--上传结果的展示-->
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

            <!-- 分页 -->
                <div style="text-align: center; padding: 10px;">


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

    <!--确定取消的_Model-->
    <div id="make_sure_model" class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">
        <div class="modal-body" style="overflow-x: hidden;">

            <div class="row">
                <p id="make_sure_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;"></p>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn-danger">确定</button>
            <!--<button type="button"  class="btn blue " id="make_sure_model_sure_button">确定</button>-->
        </div>
    </div>

    <!--confirm-->
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

    <!--============================模板=======================================-->
    <!--上传显示的-->
    <script type="text/template" id="upload_body_template">
        <td style="width:10%;vertical-align:middle;">
            <img alt="" src="/resources/assets/img/excle.png" width="50px" height="50px">
        </td>
        <td style="width:30%;vertical-align:middle;">{{fileName}}</td>
        <td style="width:10%;vertical-align:middle;">{{fileSize}}</td>
        <td style="width:30%;vertical-align:middle;">
            <div class="progress progress-striped active" style="width: 100%;margin-bottom: 0px;">
                <div id="upload_progress" class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width:0%">
                    <span id="upload_progress_span" class="">0%</span>
                </div>
            </div>
        </td>
        <td style="width:20%;vertical-align:middle;">
            <button class="btn red start" type="submit" id="submit_btn"><i class="fa fa-upload"></i><span>上传</span></button>
            &nbsp;
            <div class="btn default" id="reset_btn"><i class="fa fa-ban"></i><span>取消</span></div>
            <div class="btn green hidden" id="loading_btn"><i class="fa fa-spinner"></i> 文件上传中...</div>
        </td>
    </script>

    <!-- 失败信息的显示 -->
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

    <!-- ================================== js文件引入 ================================== -->
    <script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
    <!-- 弹出框 -->
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript" ></script>
    <!-- 异步表单 -->
    <script src="/resources/assets/plugins/jquery-validation/lib/jquery.form.js" type="text/javascript"></script>
    <!-- 警告框，不断闪 js -->
    <script src="/resources/assets/plugins/jquery.pulsate.min.js" type="text/javascript"></script>
    <!-- 模板解析 -->
    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>
    <!-- 分页组件 -->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <!-- 通用js -->
    <script src="/resources/js/common/common.js" type="text/javascript" ></script>

    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

    <script>
        jQuery(document).ready(function() {
            // 初始化布局和插件
            App.init();
        });
    </script>

    <!-- mainJS -->
    <script src="/resources/js/car/dataImport.js" type="text/javascript"></script>

</body>
</html>