<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
	<meta charset="UTF-8" />
	<title>全车件数据管理平台</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1.0" name="viewport" />
	<meta content="登录主界面" name="description" />
	<meta content="zhongxi" name="author" />
	<meta name="MobileOptimized" content="320">
	<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="/resources/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN PAGE LEVEL STYLES -->
	<link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css" />
	<!-- END PAGE LEVEL SCRIPTS -->
	<!-- BEGIN THEME STYLES -->
	<link href="/resources/assets/css/style-metronic.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/css/plugins.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link href="/resources/assets/css/pages/login-soft.css" rel="stylesheet" type="text/css"/>
	<link href="/resources/assets/css/custom.css" rel="stylesheet" type="text/css"/>
	<!-- END THEME STYLES -->
	<link rel="shortcut icon" href="/resources/assets/img/project/monkey_24x24.ico" />
    <link href="/resources/assets/css/animate.css" rel="stylesheet" type="text/css"/>

	<style>
		.logo-title-css{
            font-size: 33px;
            font-weight: bold;
            color: white;
		}

	</style>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
	<!-- BEGIN LOGO -->
	<div class="logo">
        <span id="logo-title" style="display: block;" class="">
			<h1 class="logo-title-css">全车件数据管理 平台</h1>
		</span>
	</div>
	<!-- END LOGO -->
	<!-- BEGIN LOGIN -->
	<div class="content">
	    <input type="hidden" id="message" value="${message}">
		<!-- BEGIN LOGIN FORM -->
		<form class="login-form" action="/loginController/checkLogin" method="post">
			<h3 class="form-title">登录您的账号</h3>
			<div class="alert alert-danger display-hide">
				<button class="close" data-close="alert"></button>
				<span>登录失败，请输入正确的账号和密码</span>
			</div>
			<div class="form-group">
				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
				<label class="control-label visible-ie8 visible-ie9">账号</label>
				<div class="input-icon">
					<i class="fa fa-user"></i>
					<input class="form-control placeholder-no-fix" type="text" autocomplete="off" placeholder="请在此处填写账号" name="username" id="username"/>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label visible-ie8 visible-ie9">密码</label>
				<div class="input-icon">
					<i class="fa fa-lock"></i>
					<input class="form-control placeholder-no-fix" type="password" autocomplete="off" placeholder="请在此处填写密码" name="password" id="password"/>
				</div>
			</div>
			<div class="form-actions">

				<button type="submit" class="btn red pull-right" style="  background-color: #E17668;">
				登录 	<i class="m-icon-swapright m-icon-white"></i>
				</button>

			</div>
			<div class="forget-password">
				<h4>忘记您的密码?</h4>
				<p>
					别担心，请联系<a href=mailto:xigeng.zhong@tqmall.com>管理员</a>重置您的密码。
				</p>
			</div>
			<div class="create-account">
				<p>
					暂时没有密码?&nbsp; 请联系
					<a href=mailto:xigeng.zhong@tqmall.com>管理员</a>
				</p>
			</div>
		</form>
		<!-- END LOGIN FORM -->

	</div>
	<!-- END LOGIN -->
	<!-- BEGIN COPYRIGHT -->
	<div class="copyright">
		2015-2016 &copy; 全车件数据管理平台 - 杭州轩潮科技有限公司.
	</div>
	<!-- END COPYRIGHT -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="/resources/assets/plugins/respond.min.js"></script>
	<script src="/resources/assets/plugins/excanvas.min.js"></script>
	<![endif]-->
	<script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
	<script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="/resources/assets/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script src="/resources/assets/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>
	<!-- END PAGE LEVEL PLUGINS -->
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<script>
		jQuery(document).ready(function() {
		  App.init();

            $('#logo-title').removeClass().addClass('shake animated').one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function(){
                $(this).removeClass();
            });
		});
	</script>
	<!-- END JAVASCRIPTS -->
    <script src="/resources/js/login.js" type="text/javascript"></script>
</body>
<!-- END BODY -->
</html>