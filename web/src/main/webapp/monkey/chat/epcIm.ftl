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

<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <div class="row">
        <button class="btn btn-danger" data-sysid="1" id="epc_yong_btn">
            全车件客服-阿勇
        </button>
    </div>


</body>


<script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>

<input type="hidden" value="${monkHost}" id="monkHost">
<input type="hidden" value="${userId}" id="userId">
<!-- 必须引入 拦截框样式-->
<link href="${monkHost}/monk-release/resources/chat/chat-min.css?v=${.now?string('yyyy-MM-dd')}" rel="stylesheet" type="text/css"/>
<!-- 必须引入 -->
<script src="${monkHost}/monk-release/resources/chat/chat-min.js?v=${.now?string('yyyy-MM-dd')}" type="text/javascript"></script>

<script>
    $(function(){
        $("#epc_yong_btn").click(function(){
            var GET_TOKEN_URL = $("#monkHost").val() + "/monk/rest/getToken?sysname=imserver";
            $.ajax({
                url: GET_TOKEN_URL,
                async: false,
                success: function (result) {
                    var _param = {
                        header_url: $("#monkHost").val(),
                        sys_id: $("#epc_yong_btn").data("sysid"),
                        sys_name: "imserver",
                        time: result.data.time,
                        token: result.data.token,
                        opened_url: '/monkey/monkChat/index'
                    };
                    openWindow(_param);
                }
            });
        });
    });

    function openWindow(param){
        //打开聊天窗口
        var isOpenFun = function () {
            TimChat.focusChatWindow();
        };
        TimChat.initOpenChatPage(param, isOpenFun);
    }
</script>

</html>