<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title></title>

    <link rel="stylesheet" type="text/css" href="/resources/jquery/jquery-easyui-1.4.2/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="/resources/jquery/jquery-easyui-1.4.2/themes/icon.css">
<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <table id="tg" style="height:700px;"></table>


    <!-- js文件引入 -->
    <script type="text/javascript" src="/resources/jquery/jquery-easyui-1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="/resources/jquery/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>

    <script type="text/javascript">

        function toReusePart(partId){
            $.ajax({
                url:'/monkey/category/toReusePart',
                type:'POST',
                data:{partId : partId},
                success:function(result){
                    if(result.success){
                        $('#tg').treegrid('reload');
                    }
                }
            });
        }

        function toReuseCategory(cateId){
            $.ajax({
                url:'/monkey/category/toReuseCategory',
                type:'POST',
                data:{cateId : cateId},
                success:function(result){
                    if(result.success){
                        $('#tg').treegrid('reload');
                    }
                }
            });
        }

        $(function(){
            $('#tg').treegrid({
                title:'已停用分类数据列表',
                rownumbers: true,
                animate:true,
                fitColumns:true,
                url:'/monkey/category/toGetDeletedCategory',
                idField:'id',
                treeField:'name',
                lines:true,
                columns:[[
                    {field:'name',title:'分类名称',width:150},
                    {field:'catType',title:'类型',width:100},
                    {field:'catCode',title:'编码',width:100},
                    {field:'isDeleted',title:'状态',width:80,align:'center',
                        formatter:function(value){
                            if (value){
                                var s = '<span style="color:red;">'+value+'</span>';
                                return s;
                            } else {
                                return '';
                            }
                        }
                    }
                ]],
                toolbar: [{
                    text: '重新启用',
                    iconCls: 'icon-add',
                    handler: function(){
                        var row = $('#tg').treegrid('getSelected');
                        if(row==null){
                            $.messager.alert('提示信息','请先选中一条记录！','info');
                            return;
                        }

                        var msg = '您确定要重新启用【 '+row.catType+' : '+row.name+' 】吗？';
                        $.messager.confirm('确认信息', msg, function(r){
                            if(r){
                                if(row.catLevel==-1){
                                    toReusePart(row.id);
                                }else{
                                    toReuseCategory(row.id);
                                }
                            }
                        });

                    }
                },'-']
            });

        });

    </script>

</body>
</html>