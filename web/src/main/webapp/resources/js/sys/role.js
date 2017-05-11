var currentConfigRoleId = 0;

var zTree;

var zTreeSetting = {
    callback: {
        beforeClick:function(treeId, treeNode) {

        },
        onClick:function(event, treeId, treeNode){

        }
    },
    check: {
        enable: true,
        chkStyle: "checkbox",
        chkboxType: { "Y": "ps", "N": "s" }
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0
        }
    },
    view: {
        dblClickExpand: true,
        showLine: true,
        selectedMulti: false
    }
};


$(document).ready(function(){
    button_click();
    to_init();
});


//按钮点击事件
function button_click(){

    //绑定回车事件
    $('.search').bind('keypress',function(event){
        if(event.keyCode == "13")
        {
            $('#search_button').trigger('click');
        }
    });
    //搜索框正则
    $('.search').keyup(function(){
        var value = $(this).val().replace(/[ ]/g,'');
        $(this).val(value);
    });

    //start 查询按钮
    $('#search_button').click(function(){
        to_search_list(1);
    });

    $('#add_button').click(function(){
        to_add();
    });

    $('#config_source').click(function(){
        to_config_source();
    });
}

function to_config_source(){
    currentConfigRoleId = 0;
    zTree = $('#resourceTree').empty();
    $('#config_source_modal').modal('show');

    $.ajax({
        url:'/monkey/resource/toSearchResourceByRoleId',
        type:'POST',
        data:{roleId : 0},
        success:function(result){
            $.fn.zTree.init(zTree, zTreeSetting, result.zTreeList);
        }
    });

    var select = $("#config_source_form select[name='roleId']");
    select.get(0).options.length = 1;

    $.ajax({
        url:'/monkey/role/toSearchAllRoles',
        type:'POST',
        success:function(result){
            var array = result.data;
            var op;
            for(var i=0; i<array.length; i++){
                op = $('<option>');
                op.text(array[i].roleName+'【'+array[i].description+'】');
                op.val(array[i].id);
                select.append(op);
            }

            select.unbind().change(function(){
                change_config_role(this.value);
            });

            $('#config_source_ok_button').unbind().click(function(){
                to_save_role_resource();
            });

        }
    });

}

function change_config_role(id){
    currentConfigRoleId = id;
    $.ajax({
        url:'/monkey/resource/toSearchResourceByRoleId',
        type:'POST',
        data:{roleId : id},
        success:function(result){
            $.fn.zTree.init(zTree, zTreeSetting, result.zTreeList);
        }
    });
}

function to_save_role_resource(){
    if(currentConfigRoleId==0){
        alert('请先选择一个角色！');
        return false;
    }

    var treeObj = $.fn.zTree.getZTreeObj("resourceTree");
    var nodes = treeObj.getChangeCheckedNodes();//得到checkbox变化过的节点
    var addArray = new Array();
    var deleteArray = new Array();
    var data;
    for(var i=0; i<nodes.length; i++){
        data = new Object();
        data.roleId = currentConfigRoleId;
        data.resourceId = nodes[i].id;

        if(nodes[i].checked){
            addArray.push(data);
        }else{
            deleteArray.push(data);
        }
    }

    if(addArray.length==0 && deleteArray.length==0){
        alert('数据没有改动！');
        return false;
    }

    var msg = '<span style="color:red;font-weight:bold;">删除记录数：'+deleteArray.length+'条<br>添加记录数：'+addArray.length+'条</span><br>您确定要保存吗？'
    $('#make_sure_modal').modal('show');
    $('#make_sure_msg').html(msg);
    $('#make_sure_ok_button').unbind().click(function() {
        var ok_bt = this;
        ok_bt.disabled = "disabled";

        data = {
            'addArray' : addArray,
            'deleteArray' : deleteArray
        };

        var jsonStr = JSON.stringify(data);

        $.ajax({
            url:'/monkey/role/toModifyRoleResource',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    ok_bt.disabled = "";
                    $('#make_sure_modal').modal('hide');
                    $('#config_source_modal').modal('hide');
                }else{
                    ok_bt.disabled = "";
                }
            }
        });

    });

}

function to_add(){
    $('#add_modal').modal('show');
    $("#add_form input[name='roleName']").val('');
    $("#add_form input[name='description']").val('');

    $('#add_ok_button').unbind().click(function(){
        var formData = jq_serializeObject($('#add_form'));

        var roleName = formData.roleName.replace(/\s+/g,'');
        var description = formData.description.replace(/\s+/g,'');
        var checkFlag = true;
        if(roleName==''){
            checkFlag = false;
        }
        if(description==''){
            checkFlag = false;
        }
        if(!checkFlag){
            return;
        }

        formData.roleName = roleName;
        formData.description = description;

        var jsonStr = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/role/toAddRole',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_list(1);
                    $('#add_modal').modal('hide');
                }else{
                    alert(result.message);
                }
            }
        });
    });
}

function to_modify(id){
    $('#modify_modal').modal('show');

    $.ajax({
        url:'/monkey/role/toSearchRoleById',
        type:'POST',
        data:{roleId: id},
        async:false,
        success:function(result){
            if(result.success){
                var role = result.data;
                $("#modify_form input[name='id']").val(role.id);
                $("#modify_form input[name='roleName']").val(role.roleName);
                $("#modify_form input[name='description']").val(role.description);
            }
        }
    });

    $('#modify_ok_button').unbind().click(function(){
        var formData = jq_serializeObject($('#modify_form'));

        var description = formData.description.replace(/\s+/g,'');
        var checkFlag = true;
        if(description==''){
            checkFlag = false;
        }
        if(!checkFlag){
            return;
        }

        formData.description = description;

        var jsonStr = JSON.stringify(formData);

        $.ajax({
            url:'/monkey/role/toModifyRole',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    to_search_list(1);
                    $('#modify_modal').modal('hide');
                }
            }
        });
    });
}

function to_delete(id, name){
    $('#make_sure_modal').modal('show');

    var msg = '您确定要将 <span style="color:red;font-weight:bold;">'+name+'</span> 删除吗？';
    $('#make_sure_msg').html(msg);

    $('#make_sure_ok_button').unbind().click(function(){
        $.ajax({
            url:'/monkey/role/toDeleteRole',
            type:'POST',
            data:{roleId : id},
            success:function(result){
                if(result.success){
                    to_search_list(1);
                    $('#make_sure_modal').modal('hide');
                }
            }
        });
    });
}

function list_button_click(){
    $('.delete_button').unbind().click(function(){
        var children = $(this).parent().children();
        var id = children.eq(0).val();
        var name = children.eq(1).val()
        to_delete(id, name);
    });

    $('.modify_button').unbind().click(function(){
        var id = $(this).parent().children(':first').val();
        to_modify(id);
    });
}

function to_init(){
    var params = {};
    params.pageIndex = 1;
    params.search_name = '';

    $('#list_tbody').html(loadingHtml);

    $.ajax({
        url:'/monkey/role/toSearchRoles',
        type:'POST',
        data:params,
        success:function(result){
            var html = '';
            var resultHtml = '';

            var length = result.data.length;

            if(length > 0){
                html = Mustache.render($('#list_template').html(), result);
            }else{
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';

                $('#result_show').html(resultHtml);
            }

            $('#list_tbody').html(html);

            list_button_click();

            initPage(result.totalPages, 1, result.totalRows, to_search_list);//初始化分页组件
        }
    });
}

function to_search_list(pageIndex){
    var params = {};
    params.pageIndex = pageIndex;
    params.search_name = $('#search_name').val().trim();

    $('#list_tbody').html(loadingHtml);

    $.ajax({
        url:'/monkey/role/toSearchRoles',
        type:'POST',
        data:params,
        success:function(result){
            var html = '';
            var resultHtml = '';

            var length = result.data.length;

            if(length > 0){
                html = Mustache.render($('#list_template').html(), result);
            }else{
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';

                $('#result_show').html(resultHtml);
            }

            $('#list_tbody').html(html);

            list_button_click();

            updatePage(result.totalRows, result.totalPages, pageIndex);
        }
    });

}

