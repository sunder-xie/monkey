<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-EPC配件数据导入</title>
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
    <#--select2 drop-->
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css" />
    <script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>

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
                    <div style="height:50px;" class="note note-danger">
                        <div style="float:left" >1.一汽大众和一汽奥迪，OE前缀"<span style="color: red">L</span>",明确为国产，实际OE为除L后的编码 </div>
                    </div>
                    <blockquote>
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>字段属性</th>
                                    <th>是否允许为空</th>
                                    <th>规则说明</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>01</td>
                                    <td>序号</td>
                                    <td>否</td>
                                    <td class="td-left">即标志号、不可重复</td>
                                </tr>
                                <tr>
                                    <td>02</td>
                                    <td>原厂图号</td>
                                    <td>否</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>03</td>
                                    <td>原厂序号</td>
                                    <td>否</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>04</td>
                                    <td>OE码</td>
                                    <td>是</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>05</td>
                                    <td>原厂零件名称</td>
                                    <td>否</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>06</td>
                                    <td>用量</td>
                                    <td>否</td>
                                    <td class="td-left">需要注意特殊零件的用量，如进排气门等</td>
                                </tr>
                                <tr>
                                    <td>07</td>
                                    <td>车型</td>
                                    <td>否</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>08</td>
                                    <td>备注</td>
                                    <td>是</td>
                                    <td class="td-left">可以是排量、年款、颜色等</td>
                                </tr>
                                <tr>
                                    <td>09</td>
                                    <td>标准零件名称</td>
                                    <td>否</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>10</td>
                                    <td>标准零件编码</td>
                                    <td>是</td>
                                    <td class="td-left"></td>
                                </tr>
                                <tr>
                                    <td>11</td>
                                    <td>替换历史</td>
                                    <td>是</td>
                                    <td class="td-left">存储老的oe码</td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                    </blockquote>
                </div>
            </div>

        </div>
    </div>
    <br>
    <div class="row">
        <div class="col-md-4">
            <span class="btn blue" id="down_template_btn" style="margin-bottom: 20px;">
                            <i class="fa fa-download"></i>
                            <span>下载标准模板</span>
                        </span>
        </div>

    </div>
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading">
        <#--力洋的车型选择-->
            <div class="panel-title" style="width:100%;">
                <div class="row " style="width: 100%;">
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
            </div>
        </div>
    <#--上传文件-->
        <form method="POST" id="fileupload" enctype="multipart/form-data" action="/monkey/part/dataInput/uploadExcle" class="form hidden">
            <input type="hidden" id="brand" name="brand"/>
            <input type="hidden" id="factory" name="factory"/>
            <input type="hidden" id="series" name="series"/>
            <input type="hidden" id="model" name="model"/>

            <div class="panel-body">
                <div class="row ">
                    <div class="col-md-12">
                        <span class="btn red fileinput-button" style="margin-right: 20px;">
                            <i class="fa fa-plus"></i>
                            <span>添加excle文件</span>
                            <input type="file" name="excle_file" id="excle_file" multiple="">
                        </span>

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

            </div>
        </form>
    </div>

    <br>



    <#--上传结果的展示-->
    <div class="panel panel-success hidden" id="upload_result_panel">
        <div class="panel-heading">
            <h3 class="panel-title">失败原因</h3>
        </div>
        <div class="panel-body">
            <span id="faileReason">

            </span>

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


    <script type="text/template" id="car_select_template">
        <option value="-1">{{placeholder}}</option>
        {{#list}}
            <option value="{{.}}">{{.}}</option>
        {{/list}}
    </script>

    <script type="text/template" id="fail_template">
        {{#list}}
        <div >{{.}}</div>
        {{/list}}
    </script>

    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();
        });
    </script>


<#--mainJS-->
    <script src="/resources/js/part/dataInput.js" type="text/javascript"></script>
</body>


</html>