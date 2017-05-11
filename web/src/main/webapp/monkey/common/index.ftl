<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-首页</title>
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
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
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


    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <script src="/resources/js/common/common.js" type="text/javascript" ></script>

    <#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">

    td,th{
        text-align: center;
        vertical-align: middle !important;
    }


    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <div class="row">
         <div class="col-md-12">
        <div class="portlet box green">
            <div class="portlet-title">
                <div class="caption">操作记录</div>
                <div class="tools">
                    <a href="javascript:;" class="collapse"></a>
                    <a href="javascript:;" class="reload" id="reload_record_table"></a>
                </div>
            </div>
            <div class="portlet-body util-btn-margin-bottom-5">
                <div class="table table-responsive" id="record_table">
                    <table class="table">
                        <thead>
                        <tr>
                            <th style="width: 1%">#</th>
                            <th>最近更新时间</th>
                            <th>操作模块</th>
                            <th>内容</th>
                            <th>状态</th>
                        </tr>
                        </thead>
                        <tbody id="record_table_tbody">

                        </tbody>
                    </table>

                <#-- 分页 -->
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
    <script type="text/template" id="record_table_body_template">
        {{#recordList}}
        <tr>
            <td>
                <span>{{indexs}}</span>
            </td>
            <td>
                <span>{{modifiedString}}</span>
            </td>
            <td>
                <span>{{resourceName}}</span>
            </td>
            <td>
                <span>{{content}}</span>
            </td>
            <td>
                {{{statusRenderer}}}
                <#--<span class="label label-danger">{{statusString}}</span>-->
            </td>
        </tr>
        {{/recordList}}
    </script>


    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
//            App.init();
        });
    </script>


<#--mainJS-->
    <script src="/resources/js/sys/index.js" type="text/javascript"></script>
</body>


</html>