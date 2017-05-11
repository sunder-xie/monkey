<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-供应商数据导出</title>
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

    <#--警告框，不断闪 js-->
    <script src="/resources/assets/plugins/jquery.pulsate.min.js" type="text/javascript"></script>
    <#--环形滚动条-->
    <script src="/resources/assets/plugins/jquery-knob/js/jquery.knob.js"></script>
    <#--reload-->
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">

    td{
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

        .progress{
            width: 100%;
            margin-bottom: 0px;
            background-color: #CAE4CA;
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <#--总的进度条-->
    <div class="row">
        <div class="col-md-4">

        </div>
        <div class="col-md-4">
            <input id="all_progress" data-width="200"  readonly
                   data-min="0" data-max="100" data-fgColor="red" data-thickness=.3 value="0"
                   style="background-color:none white;">

            <div class="row">
                <div class="col-md-2"></div>
                <h4 class="col-md-8">
                    <div  style="text-align: center;">
                        <button type="button" class="btn btn-danger" id="data_out_excle_btn">
                            <i class="fa fa-cloud-download"></i>
                            <span>导出供应商数据TO电商</span>
                        </button>
                    </div>
                </h4>
                <#--<div class="col-md-2"></div>-->

            </div>

        </div>
        <div class="col-md-4">

        </div>
    </div>
    <hr>
    <#--两个匹配、一个导入-->
    <div class="row">
        <div class="col-md-6">
            <#--车型匹配-->
            <div class="news-blocks">
                <h3>车型匹配</h3>
                <div class="news-block-tags">
                    <#--<strong>将供应商车型对应到电商线上库车型</strong>-->
                    <strong>
                        <span class="label label-danger">注意!</span>
                        若车型匹配失败，请联系开发人员后台查看
                    </strong>
                </div>
                <br>
                <div class="">
                    <span>
                        <button type="button" class="btn btn-danger hidden" id="start_car_btn">
                            <i class="fa fa-refresh"></i>
                            <span>
                                 开始车型匹配
                            </span>
                        </button>
                        <#--<button type="button" class="btn btn-danger hidden" id="downLoad_car_btn">-->
                            <#--<i class="fa fa-download"></i>-->
                                <#--<span>-->
                                    <#--下载失败数据-->
                                <#--</span>-->

                        <#--</button>-->
                    </span>
                </div>
                <br>
                <div class="">
                    <div class="progress progress-striped active">
                        <div id="car_progress" class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                            <span id="car_progress_span" class="">0%</span>
                        </div>
                    </div>
                </div>
                <br>
                <p>
                    未匹配的车型数：<span id="car_new_sum_span">0</span>
                    <br>
                    匹配失败未处理：<span id="car_not_excle_sum_span">0</span>
                </p>

            </div>
        </div>
        <div class="col-md-6">
            <#--分类匹配-->
            <div class="news-blocks">
                <h3>分类匹配</h3>
                <div class="news-block-tags">
                    <strong>将输入的分类对应到电商线上分类</strong>
                </div>
                <br>
                <div class="">
                    <span>
                        <button type="button" class="btn btn-danger" id="start_cate_btn">
                            <i class="fa fa-refresh"></i>
                            <span>
                                 开始分类匹配
                            </span>
                        </button>
                        <button type="button" class="btn btn-danger" id="downLoad_cate_btn">
                            <i class="fa fa-download"></i>
                                <span>
                                    下载失败数据
                                </span>

                        </button>
                    </span>
                </div>
                <br>
                <div class="">
                    <div class="progress progress-striped active">
                        <div id="cate_progress" class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                            <span id="cate_progress_span" class="">0%</span>
                        </div>
                    </div>
                </div>
                <br>
                <p>
                    未匹配的分类数：<span id="cate_new_sum_span">0</span>
                    <br>
                    匹配失败未导出：<span id="cate_not_excle_sum_span">0</span>
                </p>

            </div>
        </div>
       <#-- <div class="col-md-4">
            &lt;#&ndash;Pool池导入&ndash;&gt;
            <div class="news-blocks">
                <h3>导入pool池</h3>
                <div class="news-block-tags">
                    <strong>分类匹配成功的商品数据导入pool池</strong>
                </div>
                <br>
                <div class="">
                    <span>
                        <button type="button" class="btn btn-danger" id="start_goods_in_btn">
                            <i class="fa fa-truck"></i>
                            <span>
                                 导入商品
                            </span>
                        </button>
                        <button type="button" class="btn btn-danger" id="start_carRelation_in_btn">
                            <i class="fa fa-truck"></i>
                            <span>
                                导入车型对应关系
                            </span>
                        </button>

                    </span>
                </div>
                <br>
                <div class="">
                    <div class="progress progress-striped active">
                        <div id="pool_progress" class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="80" aria-valuemin="0" aria-valuemax="100" style="width: 0%">
                            <span id="pool_progress_span" class="">0%</span>
                        </div>
                    </div>
                </div>
                <br>
                <p>
                    未导入的商品数：<span id="pool_goods_span">0</span>
                    <br>
                    未导入的车型关系：<span id="pool_carRelation_span">0</span>
                </p>

            </div>
        </div>-->
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
    </script>



    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();
        });
    </script>


<#--mainJS-->
    <script src="/resources/js/offerFoods/dataOutput.js" type="text/javascript"></script>
</body>


</html>