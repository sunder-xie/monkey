<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统-商品品牌维护</title>
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

    <#--分页-->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <script src="/resources/js/common/common.js" type="text/javascript" ></script>

    <#--reload-->
    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
    <#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>


    <style type="text/css">
    body {
        /*background: #2c3e50;*/
        /*color: #ecf0f1;*/
        font-size: 100%;
        line-height: 1.25;
        font-family: 'Lato', Arial, sans-serif;
    }
    section {
        padding: 4em 2em;
        text-align: center;
    }
    .modal {
        background-color: white;
    }
    .box {
        width: 300px;
        height: 460px;
        position: relative;
        background: #2c3e50 ;
        display: inline-block;
        margin: 0 10px;
        cursor: pointer;
        color: rgba(255,255,255,1);
        border-color:white ;
        box-shadow: inset 0 0 0 3px white;
        -webkit-transition: background 0.4s 0.5s;
        transition: background 0.4s 0.5s;
    }

    .box:hover,.box:active {
        background: rgba(255,255,255,1);
        -webkit-transition-delay: 0s;
        transition-delay: 0s;
    }
    .box:hover input,.box:active input,
    .box:hover textarea,.box:active textarea{
        color: #2c3e50;
        -webkit-transition-delay: 0s;
        transition-delay: 0s;
    }

    .box h3 {
        font-family: "Ruthie", cursive;
        font-size: 220px;
        width: 100%;
    }
    .box h4
    {
        font-family: "Ruthie", cursive;
        font-size: 71px;
        margin: 0;
        font-weight: 400;
        width: 100%;
        padding-bottom: 15px;
    }
    .box .row{
        margin-left:inherit;
        padding-left: 20px;
        padding-right: 20px;
        padding-top: 6px;
    }

    .box span,.box input,.box textarea {
        display: block;
        font-weight: 400;
        /*text-transform: uppercase;*/
        letter-spacing: 1px;
        font-size: 13px;
        padding: 5px;
        text-align: center;
    }
    .box input,.box textarea{
        border: 0;
        background-color: rgba(255,255,255,0);
        color: white;
     }

    .box h3,
    .box h4,
    .box span
    ,.box input,.box textarea {
        -webkit-transition: color 0.4s 0.5s;
        transition: color 0.4s 0.5s;
    }


    .box:hover h3,
    .box:hover h4,
    .box:hover span {
        color: #2c3e50;
        -webkit-transition-delay: 0s;
        transition-delay: 0s;
    }

    .box svg {
        position: absolute;
        top: 0;
        left: 0;
    }

    .box svg line {
        stroke-width: 3;
        stroke: #2c3e50;
        fill: none;
        -webkit-transition: all .8s ease-in-out;
        transition: all .8s ease-in-out;
    }

    .box:hover svg line {
        -webkit-transition-delay: 0.1s;
        transition-delay: 0.1s;
    }

    .box svg line.top,
    .box svg line.bottom {
        stroke-dasharray: 330 240;
    }

    .box svg line.left,
    .box svg line.right {
        stroke-dasharray: 490 400;
    }

    .box:hover svg line.top {
        -webkit-transform: translateX(-600px);
        transform: translateX(-600px);
    }

    .box:hover svg line.bottom {
        -webkit-transform: translateX(600px);
        transform: translateX(600px);
    }

    .box:hover svg line.left {
        -webkit-transform: translateY(920px);
        transform: translateY(920px);
    }

    .box:hover svg line.right {
        -webkit-transform: translateY(-920px);
        transform: translateY(-920px);
    }
        .box form{
            position: absolute;
            top: 6%;
            display: block;
            width: 100%;
            text-align: center;
        }

        .box form  .edit{
             text-align: left;
         }
        .edit.active{
            background-color:  #2c3e50;
            color: white !important;
            transition: border linear .2s,box-shadow linear .5s;
            -moz-transition: border linear .2s,-moz-box-shadow linear .5s;
            -webkit-transition: border linear .2s,-webkit-box-shadow linear .5s;
            outline: none;
            /* border-color: rgb(77, 144, 254); */
            box-shadow: 0 0 10px;
            -moz-box-shadow: 0 0 10px;
            -webkit-box-shadow: 0 0 10px;
    }
        .search{
            border: 1px solid #e5e5e5 ;
            width: 100%;
            padding: 6px 12px;
            font-size: 16px;
        }
       /*删除按钮*/
        .box .deleted_btn_div{
            position: absolute;
            top: 91.9%;
            display: block;
            width: 100%;
            text-align: center;
            padding-left: 3px;
            padding-right: 3px;
        }
    .box .deleted_btn_div button{
        background-color: rgba(44, 62, 80, 0);

        color: rgba(255, 255, 255, 0);
        -webkit-transition: color 0.4s 0.5s,background-color 0.4s 0.5s;
        transition: color 0.4s 0.5s,background-color 0.4s 0.5s;
    }
    .box:hover .deleted_btn_div button,
    .box:active .deleted_btn_div button{
        color: rgba(255, 255, 255, 1);
        background-color: rgba(44, 62, 80, 1);

        /*过渡从何处开始*/
        -webkit-transition-delay: 0s;
        transition-delay: 0s;
    }

    .table-btn{
        background-color:white;
        color:black;
        border:1px solid #C0C0C0;
    }
    #edit_model input{
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
        #edit_model .edit{
            padding: 6px 12px;
            font-size: 14px;
            cursor: pointer;
        }
        .profile_btn{
            padding: 1px 5px !important;
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">
    <#--搜索-->
    <div class="row">
        <div class="col-md-2" style="/*padding-left: 40px;*/">
            <select style="width: 100%;  height: 34px;" id="select_country">
                <option value="-1">-不限-</option>
                <option value="0">国内</option>
                <option value="1">国外</option>
            </select>
        </div>
        <div class="col-md-10">
            <div class="input-group">
                <div class="input-cont">
                    <input type="text" placeholder="搜索中文名称/英文名称..." class="search" id="search_nameCh">
                </div>
                    <span class="input-group-btn">
                        <button type="button" class="btn red" id="search_button">
                            搜索 &nbsp;
                            <i class="m-icon-swapright m-icon-white"></i>
                        </button>
                    </span>
            </div>
        </div>
    </div>
    <br>

    <div class="tabbable-custom ">
        <ul class="nav nav-tabs ">
            <li class="active"><a href="#tab_table" data-toggle="tab">表格版</a></li>
            <li class=""><a href="#tab_book" data-toggle="tab">书本版</a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="tab_table">
                <button class="btn red" id="table_add_brand">新增品牌</button>
                <br>

                <div class="table table-responsive" id="brand_table">
                    <table class="table">
                        <thead>
                        <thead>
                            <tr>
                                <th style="width: 1%">#</th>
                                <th style="width: 4%;">首字</th>
                                <th style="width: 10%;">中文名称</th>
                                <th style="width: 10%;">英文名称</th>
                                <th style="width: 20px;">官网地址</th>
                                <th style="width: 10%;">编码</th>
                                <th style="width: 5%;">国别</th>
                                <th style="width: 25%;">简介</th>
                                <th style="width: 15%;">操作</th>
                            </tr>
                        </thead>
                        </thead>
                        <tbody id="brand_table_tbody">

                        </tbody>
                    </table>
                </div>

            </div>
            <div class="tab-pane" id="tab_book">

                <section class="" >
                    <div class="box" id="add_new_box">
                        <svg  width="100%" height="100%" >
                            <line class="top" x1="0" y1="0" x2="900" y2="0"></line>
                            <line class="left" x1="0" y1="460" x2="0" y2="-920"></line>
                            <line class="bottom" x1="300" y1="460" x2="-600" y2="460"></line>
                            <line class="right" x1="300" y1="0" x2="300" y2="1380"></line>
                        </svg>
                        <form>
                            <h3>+</h3>
                        </form>
                    </div>
                    <span id="brand_show_body">

                    </span>

                </section>

            </div>

        </div>
    </div>

    <section>
        <input type="hidden" id="this_page_index" value="0">
        <div class="pagination">
            <a href="#" class="first" data-action="first" >首页</a>
            <a href="#" class="previous" data-action="previous" >上一页</a>
            <input type="text" readonly="readonly" data-max-page="40" />
            <a href="#" class="next" data-action="next" >下一页</a>
            <a href="#" class="last" data-action="last" >末页</a>
        </div>
    </section>


    <#--确定取消的_Model-->
    <div id="make_sure_model" class="modal fade" tabindex="-1">
        <div class="modal-body" style="overflow-x: hidden;">

            <div class="row">
                <p id="make_sure_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;  color: black;"></p>
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
                <p id="confirm_model_content" style="font-size: 20px;text-align: center;vertical-align: middle !important;  color: black;">

                </p>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
            <button type="button"  class="btn btn-danger " id="confirm_model_sure_button">确定</button>
        </div>
    </div>

    <#--编辑页面-->
    <div id="edit_model" class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" style="overflow-x: hidden;">
        <div class="modal-header" style="text-align: center;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h4 class="modal-title" id="edit_model_title">品牌</h4>
        </div>

        <div class="modal-body" style="overflow-x: hidden;padding-bottom: 0;">

            <div class="row">
                <form class="form-horizontal">
                    <input type="hidden" id="edit_model_id">

                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            中文
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="" placeholder="输入品牌中文" id="edit_model_nameCh">
                            <#--<span class="help-block">A block of help text.</span>-->
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            英文
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="" placeholder="输入品牌英文" id="edit_model_nameEn">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            <i class="fa fa-asterisk" style="color:red;"></i>
                            编码
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="" placeholder="输入品牌编码" id="edit_model_code">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            首字母
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="" placeholder="请输入首字母" id="edit_model_firstLetter">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            官网网址
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="" placeholder="输入官网网址" id="edit_model_web">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            国别
                        </label>
                        <div class="col-md-9">
                            <input type="hidden" class="country" id="edit_model_country" value="0">
                            <span class="col-md-2 edit in-country active"  data-value="0" style="text-align: center">国内</span>
                            <span class="col-md-2 out-country edit" data-value="1" style="text-align: center">国外</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">
                            简介
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="" placeholder="输入简介简要描述" id="edit_model_pro">
                        </div>
                    </div>

                </form>
            </div>

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn_default">取消</button>
            <button type="button"  class="btn btn-danger " id="edit_model_sure_button">确定</button>
        </div>
    </div>
    <#--============================模板=======================================-->

    <script type="text/template" id="brand_book_show_template">
        {{#brandList}}
            <div class="box">
                <input type="hidden" class="brandAllValue" value="{{nameCh}}-{{nameEn}}-{{onlineWebsite}}-{{code}}-{{country}}-{{profile}}">
                <input type="hidden" class="id" value="{{id}}">
                <svg  width="100%" height="100%">
                    <line class="top" x1="0" y1="0" x2="900" y2="0"></line>
                    <line class="left" x1="0" y1="460" x2="0" y2="-920"></line>
                    <line class="bottom" x1="300" y1="460" x2="-600" y2="460"></line>
                    <line class="right" x1="300" y1="0" x2="300" y2="1380"></line>
                </svg>
                <form>
                    <h4 class="nameCh" contenteditable>{{nameCh}}</h4>
                    <div class="row">
                        <span class="col-md-4">英文名称:</span>
                        <input class="col-md-8 edit nameEn" value="{{nameEn}}" placeholder="点击输入-必填" >
                    </div>
                    <div class="row">
                        <span class="col-md-3">编码:</span>
                        <input class="col-md-9  edit code" value="{{code}}" placeholder="点击输入-必填">
                    </div>
                    <div class="row">
                        <span class="col-md-3">首字:</span>
                        <input class="col-md-9  edit firstLetter" value="{{firstLetter}}" placeholder="点击输入">
                    </div>
                    <div class="row">
                        <span class="col-md-3">官网:</span>
                        <input class="col-md-9  edit onlineWebsite" value="{{onlineWebsite}}" placeholder="点击输入">
                    </div>
                    <div class="row">
                        <span class="col-md-3">国别:</span>
                        {{{statusRenderer}}}
                    </div>
                    <div class="row">
                        <span class="col-md-3">简介:</span>
                        <input class="col-md-9  edit profile" value="{{profile}}" placeholder="点击输入">
                    </div>

                </form>
                <div class="operate_btn_div hidden" >
                    <button data-id="{{id}}" class="btn red col-md-6 save_btn">保存</button>
                    <button class="btn col-md-6 reset_btn">重置</button>
                </div>
                <div class="deleted_btn_div">
                    <button data-id="{{id}}" class="btn red col-md-12 deleted_btn">
                        删除
                    </button>
                </div>
            </div>
        {{/brandList}}

    </script>

    <script type="text/template" id="brand_table_show_template">
        {{#brandList}}
            <tr>
                <input type="hidden" class="id" value="{{id}}">
                <td>
                    <span>{{{statusRenderer}}}</span>
                </td>
                <td>
                    <span class="firstLetter">{{firstLetter}}</span>
                </td>
                <td>
                    <span class="nameCh">{{nameCh}}</span>
                </td>
                <td>
                    <span class="nameEn">{{nameEn}}</span>
                </td>
                <td>
                    <a href="{{onlineWebsite}}" target="_blank">
                        <span class="onlineWebsite">
                            {{onlineWebsite}}
                        </span>
                    </a>
                </td>
                <td>
                    <span class="code">{{code}}</span>
                </td>
                <td>
                    <input type="hidden" class="country" value="{{country}}">
                    <span >{{{countryRenderer}}}</span>
                </td>
                <td>
                    <span class="profile">
                        {{{profileRender}}}
                    </span>
                </td>
                <td>
                    <button class="table-btn delete_brand_btn">
                        <span class="glyphicon glyphicon-minus" style="color: red;"></span>
                        删除
                    </button>
                    <button class="table-btn edit_brand_btn">编辑</button>
                </td>
            </tr>
        {{/brandList}}

    </script>



    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
    <script>
        jQuery(document).ready(function() {
            App.init();

        });
    </script>


<#--mainJS-->
    <script src="/resources/js/commodity/brand.js" type="text/javascript"></script>
</body>


</html>