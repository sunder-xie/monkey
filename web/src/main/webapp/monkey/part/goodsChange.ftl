<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-EPC配件数据修改</title>
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


    <#--select2 drop-->
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css" />
    <script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>

    <#--reload-->
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
    <style type="text/css">
        .search {
            border: 1px solid #e5e5e5;
            width: 100%;
            padding: 6px 12px;
            font-size: 16px;
        }
        .modal {
            background-color: white;
        }

    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <div class="row">
        <div class="col-md-10">
            <div class="input-group">
                <div class="input-cont">
                    <input type="text" placeholder="搜索oe..." class="search" id="oe_search">
                </div>
                    <span class="input-group-btn">
                        <button type="button" class="btn blue" id="oe_search_btn">
                            <i class="fa fa-search"></i>
                            搜索
                        </button>
                        <button type="button" class="btn red" id="change_btn">
                            <i class="fa fa-pencil"></i>
                            更改
                        </button>
                    </span>
             </span>
            </div>
        </div>
    </div>
    <br/>
    <div class="row">
        <div class="panel  panel-default" style="margin-bottom:1px;">
            <div class="panel-heading" style="text-align:center">
                <span> 当前配件的标准零件名称</span>
            </div>
            <div class="panel-body" id="now_part">

            </div>
        </div>
    </div>

    <#--确定取消的_Model-->
    <div id="make_sure_model" class="modal fade" tabindex="-1" data-keyboard="false">
        <div class="modal-body" style="overflow-x: hidden;">

            <div class="row">
                <p id="make_sure_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;"></p>
            </div>

        </div>
    </div>

    <#--confirm-->
    <div id="confirm_model" class="modal fade" tabindex="-1" data-backdrop="static" data-keyboard="false">
        <div class="modal-header" style="text-align:center;overflow-x: hidden;">
            要变更的oe：<span id="the_model_oe">dsads</span>
        </div>
        <div class="modal-body" style="overflow-x: hidden;">

            <div class="row">
                <div class="form-group">
                    <label class="col-md-3 control-label">更改后的标准零件编码</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control input-inline input-medium" id="changed_part_code" placeholder="要更改的标准零件编码">
                    </div>
                </div>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
            <button type="button"  class="btn btn-danger " id="confirm_model_sure_button">确定</button>
        </div>
    </div>

    <#--============================模板=======================================-->


    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();

            $('#oe_search').bind('keypress', function (event) {
                if (event.keyCode == "13") {
                    $('#oe_search_btn').trigger('click');
                }
            });

            $('#oe_search').keyup(function(){
                //非单词字符都为空格,并大写
                var value = $(this).val().replace(/[\W]/g, '').toUpperCase();
                if($(this).val() != value){
                    $(this).val(value);
                }
            });

            $('#oe_search').keydown(function(){
                $("#now_part").html("");
            })

            $("#oe_search_btn").click(function(){
                var oe_obj = $('#oe_search');
                var oe = oe_obj.val().trim();
                if(oe == ""){
                    oe_obj.val("");
                    return false;
                }
                App.blockUI();
                $.get("/monkey/part/goodsChange/getGoodsByOE",{oe:oe}, function (result) {
                    App.unblockUI();
                    if(result.success){
                        var goodsDO = result.data;
                        $("#now_part").html(goodsDO['partName']+" "+goodsDO['partCode']);

                    }else{
                        $("#now_part").html("");
                        $("#make_sure_content").html(result.message);
                        $("#make_sure_model").modal("show");
                    }
                })
            });


            $("#change_btn").click(function () {
                var oe_obj = $('#oe_search');
                var oe = oe_obj.val().trim();
                if(oe == ""){
                    oe_obj.val("");
                    return false;
                }
                $("#the_model_oe").text(oe);
                $("#confirm_model").modal("show");
            });

            $("#confirm_model_sure_button").click(function () {
                var oe_obj = $('#oe_search');
                var oe = oe_obj.val().trim();
                var changed_part_code = $("#changed_part_code").val().trim();

                $.post("/monkey/part/goodsChange/changePart",{oe:oe,partCode:changed_part_code}, function (result) {
                    App.unblockUI();
                    if(result.success){
                        var result = result.data;
                        if(result){
                            $("#make_sure_content").html("更改成功");
                            $('#oe_search_btn').trigger('click');
                        }else{
                            $("#make_sure_content").html("更改失败，后台保存错误，请看日志");
                        }
                        $("#confirm_model").modal("hide");

                    }else{
                        $("#now_part").html("");
                        $("#make_sure_content").html(result.message);
                    }


                    $("#make_sure_model").modal("show");
                })




            });
        });
    </script>
</body>


</html>