<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title></title>

    <link href="/resources/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

    <link rel="stylesheet" type="text/css" href="/resources/jquery/jquery-easyui-1.4.2/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/jquery/jquery-easyui-1.4.2/themes/icon.css">

    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .common_table{
            border-spacing: 0px;
        }
        .common_table th,.common_table td{
            padding: 0.5em 0.5em;
            border: solid 1px #dcdcdc;
        }
        .common_table th{
            text-align: right;
            font-weight: bold;
            font-size: 14px;
            vertical-align: middle;
        }
        .common_table input,select{
            font-size: 14px;
            width: 220px;
        }
        .modal {
            background-color: #fff;
        }
    </style>
</head>
<body>
    <table id="tg" style="height:700px;"></table>

    <div id="add_dialog"></div>

    <!-- 自定义模板 -->
    <script type="text/template" id="add_template">
        <form id="add_form">
            <table class="common_table" style="width:100%;">
                <tr>
                    <th style="width:90px;">资源名称</th>
                    <td>
                        <input name="resourceName" maxlength="50" style="" value="">
                        <span style="color:green;margin-left:5px;">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>资源路径</th>
                    <td>
                        <input name="url" maxlength="100" style="" value="">
                    </td>
                </tr>
                <tr>
                    <th>上级资源</th>
                    <td>
                        <select name="parentId" style="">
                            <option value="0">默认为一级资源</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>资源类型</th>
                    <td>
                        <select name="type" style="">
                            <option value="1">菜单</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>排序</th>
                    <td>
                        <input name="priority" maxlength="5" style="" value="1">
                        <span style="color:green;margin-left:5px;">* 请输入正整数</span>
                    </td>
                </tr>
            </table>
        </form>
    </script>

    <script type="text/template" id="modify_template">
        <form id="modify_form">
            <table class="common_table" style="width:100%;">
                <tr>
                    <th style="width:90px;">资源名称</th>
                    <td>
                        <input name="resourceName" maxlength="50" style="" value="{{resourceName}}">
                        <span style="color:green;margin-left:5px;">* 必填</span>
                    </td>
                </tr>
                <tr>
                    <th>资源路径</th>
                    <td>
                        <input name="url" maxlength="100" style="" value="{{url}}">
                    </td>
                </tr>
                <tr>
                    <th>上级资源</th>
                    <td>
                        <select name="parentId" style="" >
                            <option value="0">默认为一级资源</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>资源类型</th>
                    <td>
                        <select name="type" style="">
                            <option value="1">菜单</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>排序</th>
                    <td>
                        <input name="priority" maxlength="5" style="" value="{{priority}}">
                        <span style="color:green;margin-left:5px;">* 请输入正整数</span>
                    </td>
                </tr>
            </table>
        </form>
    </script>


    <script type="text/javascript" src="/resources/jquery/jquery-easyui-1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/jquery/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>

    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>

    <script src="/resources/js/common/common.js" type="text/javascript"></script>

    <script type="text/javascript" src="/resources/js/sys/resource.js"></script>

</body>

</html>