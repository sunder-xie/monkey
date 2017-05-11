<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-商品库数据导入</title>
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

    .news-blocks {
        padding: 10px;
        margin-bottom: 10px;
        background: #F9F9F9 ;
        border-top: solid 2px #faf6ea;
    }

    .news-blocks:hover {
        background: #fff;
        border-color: red;
        transition: all 0.4s ease-in-out 0s;
        -moz-transition: all 0.4s ease-in-out 0s;
        -webkit-transition: all 0.4s ease-in-out 0s;
    }

    .news-blocks h3 {
        margin: 0 0 5px 0;
        font-size: 23px;
        line-height: 32px;
    }

    .news-blocks h3:hover {
        color: red;
        text-decoration: none;
    }

    .news-blocks p {
        overflow: hidden;
    }

    .news-blocks button {
        font-size: 14px;
        text-align: right;
        text-decoration: none;
    }
        .news-blocks .body{
            max-height: 300px;
            min-height: 100px;
            overflow-y: auto
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <#--错误评判标准-->
    <section class="row">
        <div class="col-md-12">
            <div class="portlet">
                <div class="portlet-title">
                    <div class="tools" style="float: left;">

                        <a href="javascript:;" class="collapse"></a>
                    </div>
                </div>
                <div class="portlet-body" style="display: block;">
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
                                        <td>标识位</td>
                                        <td>否</td>
                                        <td class="td-left">不可重复</td>
                                    </tr>
                                    <tr>
                                        <td>02</td>
                                        <td>商品名称</td>
                                        <td>否</td>
                                        <td class="td-left"></td>
                                    </tr>
                                    <tr>
                                        <td>03</td>
                                        <td>商品属性</td>
                                        <td>否</td>
                                        <td class="td-left">正厂原厂、正厂配套、正厂下线、全新拆车、旧件拆车、副厂、品牌，必须从中选一；</td>
                                    </tr>
                                    <tr>
                                        <td>04</td>
                                        <td>商品品牌</td>
                                        <td>是</td>
                                        <td class="td-left">当【商品属性】为“品牌”时，必填字段；</td>
                                    </tr>
                                    <tr>
                                        <td>05</td>
                                        <td>商品编码</td>
                                        <td>是</td>
                                        <td class="td-left">1.若无编码，则将选一oe码填入此栏<br>
                                            2.若oe码没有，则填入规格型号
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>06</td>
                                        <td>标准零件名称</td>
                                        <td>否</td>
                                        <td class="td-left"></td>
                                    </tr>
                                    <tr>
                                        <td>07</td>
                                        <td>标准零件编码</td>
                                        <td>否</td>
                                        <td class="td-left">若编码跟名称不符合数据库，会报错</td>
                                    </tr>
                                    <tr>
                                        <td>08</td>
                                        <td>销售单位</td>
                                        <td>是</td>
                                        <td class="td-left"></td>
                                    </tr>
                                    <tr>
                                        <td>09</td>
                                        <td>易损件标识</td>
                                        <td>否</td>
                                        <td class="td-left">填写：易损件或全车件</td>
                                    </tr>
                                    <tr>
                                        <td>10</td>
                                        <td>质保期</td>
                                        <td>是</td>
                                        <td class="td-left"></td>
                                    </tr>
                                    <tr>
                                        <td>11</td>
                                        <td>销售状态</td>
                                        <td>是</td>
                                        <td class="td-left"></td>
                                    </tr>
                                    <tr>
                                        <td>12</td>
                                        <td>规格型号</td>
                                        <td>是</td>
                                        <td class="td-left">1、当属于但不限于，油类、液类及电瓶等有明确规格的商品时，必填；
                                            <br>
                                            2、无OE码商品，必填；</td>
                                    </tr>
                                    <tr>
                                        <td>13</td>
                                        <td>备注</td>
                                        <td>是</td>
                                        <td class="td-left"></td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </blockquote>
                </div>
            </div>

        </div>
    </>

    <#--上传文件-->

    <form method="POST" id="fileupload" enctype="multipart/form-data" action="/monkey/commodity/input/uploadExcle" class="form">
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
   <hr>
    <div class="row">
        <div class="col-md-6 hidden" id="fail_col">
            <div class="news-blocks">
                <h3>失败数据</h3>
                <div class="news-block-tags">
                    <span class="label label-danger">注意!</span>
                    <strong>
                        添加成功的商品编码数：<span id="successNum">0</span>
                        &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;
                        失败的记录数(包括sheet1/2/3):<span id="failNum">0</span>
                    </strong>
                </div>
                <br>
                <div class="body" id="fail_body">

                </div>

            </div>
        </div>

        <div class="col-md-6 hidden" id="update_col">
            <div class="news-blocks">
                <h3 id="update_header">需更新的数据</h3>
                <div class="news-block-tags">
                    <span class="label label-danger">注意!</span>
                    <strong>
                        此处为冲突更新的商品(规格型号+品牌名),更新商品数：<span id="upGoodsNum">0</span>
                    </strong>
                </div>
                <br>
                <div class="body">
                    <div class="" id="need_up_body">

                    </div>
                </div>

            </div>
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
                <p id="confirm_model_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;">

                </p>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn_default" id="confirm_model_reset_button">取消</button>
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

    <script type="text/template" id="fail_template">
        {{#failList}}
            {{.}} <br>
        {{/failList}}
    </script>

    <script type="text/template" id="update_template">
        {{#listArray}}

        {{#list}}
            <div class="row">
                        <span class="col-md-12" >
                            {{.}}
                        </span>

            </div>
        {{/list}}
        {{/listArray}}
    </script>


    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();
        });
    </script>


<#--mainJS-->
    <script src="/resources/js/commodity/commodityInput.js" type="text/javascript"></script>
</body>


</html>