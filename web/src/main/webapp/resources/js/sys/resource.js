
$(document).ready(function(){
    //初始化资源列表
    $('#tg').treegrid({
        title:'资源列表',
        rownumbers: true,
        animate:true,
        fitColumns:true,
        url:'/monkey/resource/toSearchAllResource',
        idField:'id',
        treeField:'name',
        lines:true,
        columns:[[
            {field:'name',title:'资源名称',width:100},
            {field:'url',title:'资源路径',width:150},
            {field:'type',title:'资源类型',width:80},
            {field:'order',title:'排序',width:80,align:'center'}
        ]],
        toolbar: [{
            text: '添加资源',
            iconCls: 'icon-add',
            handler: function(){
                toAdd();
            }
        },'-',{
            text: '修改资源',
            iconCls: 'icon-edit',
            handler: function(){
                var row = $('#tg').treegrid('getSelected');
                if(row==null){
                    $.messager.alert('提示信息','请先选中一条记录！','info');
                    return;
                }

                toModify(row.id);
            }
        },'-',{
            text: '删除资源',
            iconCls: 'icon-cancel',
            handler: function(){
                var row = $('#tg').treegrid('getSelected');
                if(row==null){
                    $.messager.alert('提示信息','请先选中一条记录！','info');
                    return;
                }

                var msg = '您确定要删除【 '+row.name+' 】吗？';
                $.messager.confirm('确认信息', msg, function(r){
                    if(r){

                        var formData = new Object();
                        formData.Id = row.id;
                        formData.available = 1;

                        var jsonStr = JSON.stringify(formData);
                        $.ajax({
                            url:'/monkey/resource/toModifyResource',
                            type:'POST',
                            data:{jsonString : jsonStr},
                            success:function(result){
                                if(result.success){
                                    $('#tg').treegrid('reload');
                                    $('#add_dialog').dialog('close');
                                }else{

                                }
                            }
                        });

                    }
                });

            }
        },'-',{
            text: '回收站',
            iconCls: 'icon-help',
            handler: function () {
                $.messager.alert('提示信息','开发中！','info');
            }
        }]
    });


});


function toAdd(){
    var dialogContent = $('#add_template').html();

    $('#add_dialog').dialog({
        title: '添加资源',
        width: 450,
        height: 300,
        closed: false,
        closable: true,
        cache: false,
        content: dialogContent,
        modal: true,
        buttons:[{
            text:'提交',
            handler:function(){

                var formData = jq_serializeObject($('#add_form'));
                var name = formData.resourceName.replace(/\s+/g,'');
                var priority = formData.priority.replace(/\s+/g,'');
                var checkFlag = true;
                if(priority==''){
                    checkFlag = false;
                }else{
                    checkFlag = /^[1-9]+\d*$/i.test(priority);
                }
                if(name==''){
                    checkFlag = false;
                }
                if(!checkFlag){
                    return false;
                }

                formData.resourceName = name;
                formData.priority = priority;
                formData.url = formData.url.replace(/\s+/g,'');

                var jsonStr = JSON.stringify(formData);
                $.ajax({
                    url:'/monkey/resource/toAddResource',
                    type:'POST',
                    data:{jsonString : jsonStr},
                    success:function(result){
                        if(result.success){
                            $('#tg').treegrid('reload');
                            $('#add_dialog').dialog('close');
                        }else{

                        }
                    }
                });

            }
        },{
            text:'关闭',
            handler:function(){
                $('#add_dialog').dialog('close');
            }
        }],
        onOpen:function(){

            $.ajax({
                url:'/monkey/resource/toSearchResourceByPid',
                type:'POST',
                data:{pid : 0},
                success:function(result){
                    if(result.success){
                        var select = $("#add_form select[name='parentId']");
                        var array = result.data;
                        var op;
                        for(var i=0; i<array.length; i++){
                            op = $('<option>');
                            op.text(array[i].resourceName);
                            op.val(array[i].id);
                            select.append(op);
                        }
                    }else{

                    }
                }
            });

        }
    });

}


function toModify(id){

    $.ajax({
        url:'/monkey/resource/toSearchResourceById',
        type:'POST',
        data:{id : id},
        success:function(result){
            if(result.success){
                var dialogContent = Mustache.render($('#modify_template').html(), result.data);
                var pid = result.data.parentId;

                $('#add_dialog').dialog({
                    title: '修改资源',
                    width: 450,
                    height: 300,
                    closed: false,
                    closable: true,
                    cache: false,
                    content: dialogContent,
                    modal: true,
                    buttons:[{
                        text:'提交',
                        handler:function(){

                            var formData = jq_serializeObject($('#modify_form'));
                            var name = formData.resourceName.replace(/\s+/g,'');
                            var priority = formData.priority.replace(/\s+/g,'');
                            var checkFlag = true;
                            if(priority==''){
                                checkFlag = false;
                            }else{
                                checkFlag = /^[1-9]+\d*$/i.test(priority);
                            }
                            if(name==''){
                                checkFlag = false;
                            }
                            if(!checkFlag){
                                return false;
                            }

                            formData.resourceName = name;
                            formData.priority = priority;
                            formData.url = formData.url.replace(/\s+/g,'');
                            formData.Id = id;
                            formData.available = 0;

                            var jsonStr = JSON.stringify(formData);
                            $.ajax({
                                url:'/monkey/resource/toModifyResource',
                                type:'POST',
                                data:{jsonString : jsonStr},
                                success:function(result){
                                    if(result.success){
                                        $('#tg').treegrid('reload');
                                        $('#add_dialog').dialog('close');
                                    }else{

                                    }
                                }
                            });

                        }
                    },{
                        text:'关闭',
                        handler:function(){
                            $('#add_dialog').dialog('close');
                        }
                    }],
                    onOpen:function(){

                        $.ajax({
                            url:'/monkey/resource/toSearchResourceByPid',
                            type:'POST',
                            data:{pid : 0},
                            success:function(result){
                                if(result.success){
                                    var select = $("#modify_form select[name='parentId']");
                                    var array = result.data;
                                    var op;
                                    for(var i=0; i<array.length; i++){
                                        op = $('<option>');
                                        op.text(array[i].resourceName);
                                        op.val(array[i].id);
                                        if(pid==array[i].id){
                                            op.attr('selected', true);
                                        }
                                        select.append(op);
                                    }
                                }else{

                                }
                            }
                        });

                    }
                });

            }else{

            }
        }
    });

}


