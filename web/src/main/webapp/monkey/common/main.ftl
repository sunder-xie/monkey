<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="UTF-8"/>
    <title>全车件数据管理平台</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <meta name="MobileOptimized" content="320">
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="/resources/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<#--<link href="/resources/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>-->
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME STYLES -->
<#--<link href="/resources/assets/css/style-metronic.css" rel="stylesheet" type="text/css"/>-->
    <link href="/resources/assets/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
<#--<link href="/resources/assets/css/plugins.css" rel="stylesheet" type="text/css"/>-->
<#--<link href="/resources/assets/css/pages/email.css" rel="stylesheet" type="text/css"/>-->
    <link href="/resources/assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
<#--<link href="/resources/assets/css/custom.css" rel="stylesheet" type="text/css"/>-->
    <!-- END THEME STYLES -->
    <link rel="shortcut icon" href="/resources/assets/img/project/monkey_24x24.ico"/>

    <link href="/resources/css/common.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/resources/jquery/jquery-easyui-1.4.2/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/jquery/jquery-easyui-1.4.2/themes/icon.css">


<#--模板-->
    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>


</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed" style="overflow: hidden;">
<input type="hidden" id="headerUrl" value="${basePath}">
<!-- BEGIN HEADER -->
<div class="header navbar navbar-inverse navbar-fixed-top" id="main_header">
    <!-- BEGIN TOP NAVIGATION BAR -->
    <div class="header-inner">
        <!-- BEGIN LOGO -->
        <a class="navbar-brand start" href="#">
            <span style="font-size: 20px;font-weight: bold;color: white;">全车件数据管理</span>
            <span style="font-size: 20px;font-weight: bold;color: red;">平台</span>
        </a>

        <div style="margin-top: 15px ;margin-right: 100px;float: left" class="dropdown setting">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                <span style="color :white" class="setting">工具</span>
            </a>
            <ul class="dropdown-menu">
                <li><a href="javascript:void(0);" onclick="window.open('/monkey/tools/webtool.html');">转码工具</a></li>

            </ul>
        </div>
        <!-- END LOGO -->

        <!-- BEGIN TOP NAVIGATION MENU -->
        <ul class="nav navbar-nav pull-right">
            <!-- BEGIN USER LOGIN DROPDOWN -->

            <li class="dropdown user">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
                   data-close-others="true">
                    <input type="hidden" id="userAccount" value="${account}">
                    <span class="username">${name}</span>
                    <i class="fa fa-angle-down"></i>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="toModifyLoginUser();"><i class="fa fa-key"></i>修改密码</a>
                    </li>
                    <li><a href="/loginController/logout"><i class="fa fa-key"></i>登出</a></li>
                </ul>
            </li>
            <!-- END USER LOGIN DROPDOWN -->
        </ul>
        <!-- END TOP NAVIGATION MENU -->
    </div>
    <!-- END TOP NAVIGATION BAR -->
</div>
<!-- END HEADER -->
<div class="clearfix"></div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN SIDEBAR -->
    <div class="page-sidebar navbar-collapse collapse">
        <!-- BEGIN SIDEBAR MENU -->
        <ul class="page-sidebar-menu" id="left_side">
            <li>
                <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                <div class="sidebar-toggler hidden-phone"></div>
                <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
            </li>

            <li class="start active">
                <a href="#">
                    <i class="fa fa-home"></i>
                    <span class="title">首页</span>
                    <span class="rightSidebar selected"></span>
                </a>
            </li>

        </ul>
        <!-- END SIDEBAR MENU -->
    </div>
    <!-- END SIDEBAR -->
    <!-- BEGIN PAGE -->
    <div class="page-content" style="padding: 0 0 0 3px !important;min-height:700px;">
        <iframe width="100%" style="min-height:700px;" frameborder="no" scrolling=auto border=0 src="" id="mainIframe"
                name="mainIframe"></iframe>
    </div>
    <!-- BEGIN PAGE -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<div class="footer" id="main_footer">
    <div class="footer-inner">
        2015-2016 &copy; 全车件数据管理平台 - 杭州轩潮科技有限公司.
    </div>
    <div class="footer-tools">
			<span class="go-top">
			<i class="fa fa-angle-up"></i>
			</span>
    </div>
</div>


<div id="alert_model" class="modal fade modal-scroll" tabindex="-1" data-keyboard="false" aria-hidden="true">
    <a href="#" class="model-close btn" style="color:#FFF;background-color:#c63927" data-dismiss="modal"
       aria-hidden="true">
        <i class="fa fa-times"></i>
    </a>

    <div class="modal-section">
        <div class="modal-header">
            <h4 class="modal-title"></h4>
        </div>
        <div class="modal-body" style="overflow-x: hidden;">

        </div>
    </div>
</div>

<div id="confirmModel" class="modal fade modal-scroll" tabindex="-1" data-keyboard="false" aria-hidden="true">
    <a href="#" class="model-close btn" style="color:#FFF;background-color:#c63927" data-dismiss="modal"
       aria-hidden="true">
        <i class="fa fa-times"></i>
    </a>

    <div class="modal-section">
        <div class="modal-header">
            <h4 class="modal-title">确认框</h4>
        </div>
        <div class="modal-body" style="overflow-x: hidden;">

        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" class="btn red confirm-ok-btn">确认</button>
        </div>
    </div>
</div>


<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->   <!--[if lt IE 9]>
<script src="/resources/assets/plugins/respond.min.js"></script>
<script src="/resources/assets/plugins/excanvas.min.js"></script>
<![endif]-->
<script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<#--<script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>-->
<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="/resources/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
        type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
<#--<script src="/resources/assets/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>-->

<script type="text/javascript" src="/resources/jquery/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
<!-- END CORE PLUGINS -->
<script src="/resources/assets/scripts/app.js"></script>
<script src="/resources/js/common/common.js" type="text/javascript"></script>
<script src="/resources/js/common/notify.js" type="text/javascript"></script>
<script src="/resources/js/main.js?v=1122" type="text/javascript"></script>
<script>
    jQuery(document).ready(function () {
        App.init();
    });


</script>
<!-- END JAVASCRIPTS -->


<div id="modifyLoginUser_dialog">

</div>

<script type="text/javascript">

</script>

</body>
<!-- END BODY -->

<script type="text/template" id="match_left_template">
    {{#data}}
    <li class=" ">
        <a href="javascript:;">
            <i class="fa fa-cogs"></i>
            <span class="title">{{firstName}}</span>
            <span class="rightSidebar"></span>
            <span class="arrow"></span>
        </a>
        <ul class="sub-menu">
            {{#list}}
            <li class="">
                <a href="#">
                    <span class="badge badge-roundless badge-important"
                          target_href="{{url}}"></span>{{resourceName}}</a>
            </li>
            {{/list}}
        </ul>
    </li>
    {{/data}}
</script>

<script type="text/template" id="modifyLoginUser_template">
    <form id="modifyLoginUserForm">
        <table class="common_modal_table" style="width:100%;">
            <tr>
                <th>用户名</th>
                <td id="loginUserName"></td>
            </tr>
            <tr>
                <th>真实姓名</th>
                <td id="loginUserTrueName"></td>
            </tr>
            <tr>
                <th>新密码</th>
                <td>
                    <input type="hidden" name="id">
                    <input style="width:220px;" type="password" name="passWord" value="">
                </td>
            </tr>
            <tr>
                <th>重复密码</th>
                <td>
                    <input id="repeatPassword" style="width:220px;" type="password" name="" value="">
                </td>
            </tr>
        </table>
    </form>
</script>

</html>