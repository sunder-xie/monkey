var currentAccreditUserId = 0;

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
        selectedMulti: false,
        showIcon: false
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

    $('#reset_button').click(function(){
        $('#search_name').val('');
        $('#search_cn_name').val('');


    });

    $('#search_button').click(function(){
        to_search_user_list(1);
    });

    $('#add_user_button').click(function(){
        to_add_user();
    });

    $('#accredit_button').click(function(){
        to_accredit_user();
    });
}

function to_accredit_user(){
    currentAccreditUserId = 0;
    zTree = $('#roleTree').empty();
    $('#accredit_user_modal').modal('show');

    $.ajax({
        url:'/monkey/role/toSearchRoleByUserId',
        type:'POST',
        data:{userId : 0},
        success:function(result){
            $.fn.zTree.init(zTree, zTreeSetting, result);
        }
    });

    var select = $('#accredit_user_form select[name="userId"]');
    select.get(0).options.length = 1;

    $.ajax({
        url:'/monkey/user/toSearchAllUsers',
        type:'POST',
        success:function(result){
            var array = result.userlist;
            var op;
            for(var i in array){
                op = new Option();
                op.value = array[i].id;
                op.text = array[i].userName+'【'+array[i].nickName+'】';
                select.append(op);
            }
            select.unbind().change(function(){
                change_config_user(this.value);
            });

            $('#accredit_user_ok_button').unbind().click(function(){
                to_save_user_role();
            });
        }
    });

}
function change_config_user(id){
    currentAccreditUserId = id;
    $.ajax({
        url:'/monkey/role/toSearchRoleByUserId',
        type:'POST',
        data:{userId : id},
        success:function(result){
            $.fn.zTree.init(zTree, zTreeSetting, result);
        }
    });
}
function to_save_user_role(){
    if(currentAccreditUserId==0){
        alert('请先选择一个用户！');
        return false;
    }
    var treeObj = $.fn.zTree.getZTreeObj("roleTree");
    var nodes = treeObj.getChangeCheckedNodes();//得到checkbox变化过的节点
    var addArray = new Array();
    var deleteArray = new Array();
    var data;
    for(var i=0; i<nodes.length; i++){
        data = new Object();
        data.userId = currentAccreditUserId;
        data.roleId = nodes[i].id;

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
            url:'/monkey/user/toModifyUserRole',
            type:'POST',
            data:{jsonString : jsonStr},
            success:function(result){
                if(result.success){
                    ok_bt.disabled = "";
                    $('#make_sure_modal').modal('hide');
                    $('#accredit_user_modal').modal('hide');
                }else{
                    ok_bt.disabled = "";
                }
            }
        });

    });

}


function to_add_user(){
    $('#add_user_modal').modal('show');
    $("#add_user_form input[name='userName']").val('');
    $("#add_user_form input[name='nickName']").val('');
    $("#add_user_form input[name='passWord']").val('');

    $('#add_user_ok_button').unbind().click(function(){
        var userFormData = jq_serializeObject($('#add_user_form'));

        var userName = userFormData.userName.replace(/\s+/g,'');
        var nickName = userFormData.nickName.replace(/\s+/g,'');
        var passWord = userFormData.passWord.replace(/\s+/g,'');
        var checkFlag = true;
        if(userName==''){
            checkFlag = false;
        }
        if(nickName==''){
            checkFlag = false;
        }
        if(passWord==''){
            checkFlag = false;
        }

        if(!checkFlag){
            return false;
        }

        userFormData.userName = userName;
        userFormData.nickName = nickName;
        userFormData.passWord = passWord;

        var jsonString = JSON.stringify(userFormData);

        $.ajax({
            url:'/monkey/user/toAddUser',
            type:'POST',
            data:{userJsonString : jsonString},
            success:function(result){
                if(result.success){
                    to_search_user_list(1);
                    $('#add_user_modal').modal('hide');
                }else{
                    alert(result.message);
                }
            }
        });
    });
}

function to_modify_user(userId){
    $('#modify_user_modal').modal('show');

    $.ajax({
        url:'/monkey/user/toSearchUserById',
        type:'POST',
        data:{userId: userId},
        async:false,
        success:function(result){
            if(result.success){
                var user = result.data;
                $("#modify_user_form input[name='id']").val(user.id);
                $("#modify_user_form input[name='userName']").val(user.userName);
                $("#modify_user_form input[name='nickName']").val(user.nickName);
                $("#modify_user_form input[name='passWord']").val(user.passWord);

            }
        }
    });

    $('#modify_user_ok_button').unbind().click(function(){
        var userFormData = jq_serializeObject($('#modify_user_form'));

        var nickName = userFormData.nickName.replace(/\s+/g,'');
        var passWord = userFormData.passWord.replace(/\s+/g,'');
        var checkFlag = true;

        if(nickName==''){
            checkFlag = false;
        }
        if(passWord==''){
            checkFlag = false;
        }

        if(!checkFlag){
            return false;
        }

        userFormData.nickName = nickName;
        userFormData.passWord = passWord;

        var jsonString = JSON.stringify(userFormData);

        $.ajax({
            url:'/monkey/user/toModifyUser',
            type:'POST',
            data:{userJsonString : jsonString},
            success:function(result){
                if(result.success){
                    to_search_user_list(1);
                    $('#modify_user_modal').modal('hide');
                }
            }
        });
    });
}

function to_delete_user(userId, userName){
    $('#make_sure_modal').modal('show');

    var msg = '您确定要将 <span style="color:red;font-weight:bold;">'+userName+'</span> 删除吗？';
    $('#make_sure_msg').html(msg);

    var params = {};
    params.userId = userId;

    $('#make_sure_ok_button').unbind().click(function(){
        $.ajax({
            url:'/monkey/user/toDeleteUser',
            type:'POST',
            data:params,
            success:function(result){
                if(result.success){
                    to_search_user_list(1);
                    $('#make_sure_modal').modal('hide');
                }
            }
        });
    });
}

function user_list_button_click(){
    $('.delete_user_button').unbind().click(function(){
        var children = $(this).parent().children();
        var userId = children.eq(0).val();
        var userName = children.eq(1).val()
        to_delete_user(userId, userName);
    });

    $('.modify_user_button').unbind().click(function(){
        var userId = $(this).parent().children(':first').val();
        to_modify_user(userId);
    });
}

function to_init(){
    var params = {};
    params.pageIndex = 1;
    params.search_name = '';
    params.search_cn_name = '';

    $('#user_list_tbody').html(loadingHtml);

    $.ajax({
        url:'/monkey/user/toSearchUsers',
        type:'POST',
        data:params,
        success:function(result){
            var html = '';
            var resultHtml = '';

            var length = result.data.length;

            if(length > 0){
                html = Mustache.render($('#user_list_template').html(), result);
            }else{
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';

                $('#result_show').html(resultHtml);
            }

            $('#user_list_tbody').html(html);

            user_list_button_click();

            initPage(result.totalPages, 1, result.totalRows, to_search_user_list);//初始化分页组件
        }
    });
}

function to_search_user_list(pageIndex){
    var params = {};
    params.pageIndex = pageIndex;
    params.search_name = $('#search_name').val().trim();
    params.search_cn_name = $('#search_cn_name').val().trim();

    $('#user_list_tbody').html(loadingHtml);

    $.ajax({
        url:'/monkey/user/toSearchUsers',
        type:'POST',
        data:params,
        success:function(result){
            var html = '';
            var resultHtml = '';

            var length = result.data.length;

            if(length > 0){
                html = Mustache.render($('#user_list_template').html(), result);
            }else{
                resultHtml = '<p>当前搜索条件下，<span style="color:red">无数据</span>，若想获得更多数据，请更改搜索条件。</p>';

                $('#result_show').html(resultHtml);
            }

            $('#user_list_tbody').html(html);

            user_list_button_click();

            updatePage(result.totalRows, result.totalPages, pageIndex);
        }
    });

}

