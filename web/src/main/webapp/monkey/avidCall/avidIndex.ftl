<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统</title>
    <#include "/monkey/common/header_css.ftl">

    <link href="/resources/monkey/avid/avidCall.css?t=110316" rel="stylesheet" type="text/css"/>

</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <#include "/monkey/avidCall/avidList.ftl">

    <#include "/monkey/common/footer_js.ftl">


    <#--分页-->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <script src="/resources/js/common/common.js" type="text/javascript"></script>

    <script>
        jQuery(document).ready(function () {
            App.init();
        });
    </script>

</body>
<script type="text/javascript" src="/resources/js/avidCall/avidList.js?t=110709"></script>

</html>