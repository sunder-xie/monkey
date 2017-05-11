$(function(){
    //var loadingEL = $('body');
    //App.blockUI(loadingEL);

    var headerUrl = '';
    //弹性更改浏览器高度
    changeHeight();
    $(window).resize(function () {          //当浏览器大小变化时
        //alert($(window).height());          //浏览器时下窗口可视区域高度
        //alert($(document).height());        //浏览器时下窗口文档的高度
        //alert($(document.body).height());   //浏览器时下窗口文档body的高度
        //alert($(document.body).outerHeight(true)); //浏览器时下窗口文档body的总高度 包括border padding margin
        changeHeight();
        return;
    });


    //菜单栏的点击事件
    $('#headerUrl').val(headerUrl);
    $('#mainIframe').attr("src",headerUrl+"/monkey/main/index?username="+$('#userAccount').val());
	//首页显示
	$('.start').click(function(e){
        $('#mainIframe').attr("src",headerUrl+"/monkey/main/index?username="+$('#userAccount').val());

        $('li').removeClass('active');
        $(this).addClass('active');

        $('.rightSidebar').removeClass('selected');
        $(this).children('a').children('.rightSidebar').addClass('selected');

    });
    //左边菜单栏的显示
    left_li(headerUrl);

    //App.unblockUI(loadingEL);

    /** 管家急呼未跟进数据提醒 */
    bindAvidCallNotify();

});

//动态变幻高度
function changeHeight(){
    var height = document.documentElement.clientHeight;
    var headHeight = $('#main_header').height();
    var footHeight = $('#main_footer').height();

    var newHeight = Number(height)-Number(headHeight)-Number(footHeight)-13;
    $('#mainIframe').css('min-height',newHeight+'px');
    $('#mainIframe').parent().css('min-height',newHeight+'px');
    return;
}
//左边菜单栏的显示

function left_li(headerUrl){
    $.ajax({
        type: "GET",
        url: "/loginController/getLeftLi",
        async:false,
        success: function(result){

            if(result.success){
                var data = result.data;
                var html = Mustache.render($('#match_left_template').html(), result);
                $('#left_side').append(html);

                $('.sub-menu > li > a').click(function(){
                    var url = $(this).children('span').attr('target_href');
                    $('#mainIframe').attr("src",headerUrl+url+"?username="+$('#userAccount').val());
                    $('li').removeClass('active');
                    $('.rightSidebar').removeClass('selected');

                    var parent =  $(this).parent('li');
                    parent.addClass('active');
                    parent.parent('.sub-menu').parent('li').addClass('active');
                    parent.parent('.sub-menu').siblings('a').children('.rightSidebar').addClass('selected');
                });
            }else{
                alert(result.message);
            }

        }
    });
}

/** 修改密码 */
function toModifyLoginUser(){
    var modify_dialog = $('#modifyLoginUser_template').html();

    $('#modifyLoginUser_dialog').dialog({
        title: '修改密码',
        width: 400,
        height: 230,
        closed: false,
        closable: true,
        cache: false,
        content:modify_dialog,
        modal: true,
        buttons:[{
            text:'提交',
            handler:function(){
                var form = jq_serializeObject($('#modifyLoginUserForm'));
                var password = form.passWord;
                var repeatPassword = $('#repeatPassword').val();
                var checkFlag = true;
                if(password==''){
                    checkFlag = false;
                }
                if(repeatPassword==''){
                    checkFlag = false;
                }
                if(checkFlag) {
                    if (repeatPassword != password) {
                        checkFlag = false;
                        alert('密码不一致！');
                    }
                }
                if(!checkFlag){
                    return;
                }

                var jsonString = JSON.stringify(form);

                $.ajax({
                    url:'/monkey/user/toModifyUser',
                    type:'POST',
                    data:{userJsonString : jsonString},
                    success:function(result){
                        if(result.success){
                            $('#modifyLoginUser_dialog').dialog('close');
                        }
                    }
                });
            }
        },{
            text:'关闭',
            handler:function(){
                $('#modifyLoginUser_dialog').dialog('close');
            }
        }],
        onOpen:function(){
            $.ajax({
                url:'/monkey/user/toGetLoginUser',
                type:'POST',
                success:function(result){
                    if(result.success){
                        var data = result.data;
                        $('#loginUserName').text(data.userName);
                        $('#loginUserTrueName').text(data.nickName);
                        $('#modifyLoginUserForm input[name="id"]').val(data.id);

                    }else{
                        alert(result.message);
                    }
                }
            });
        }
    });

}

/** 管家急呼未跟进数据提醒 */
function bindAvidCallNotify(){

    notifyAllNewData();

    setInterval('notifyAllNewData()', 60000*2);

    //setInterval('notifyFiveMinNewData()', 60000*2);

}
var avidCallNotifyTitle = '数据平台-管家急呼提醒';
function notifyAllNewData(){
    $.ajax({
        url: '/monkey/avidCall/getAllNewDataNum',
        success: function(result){
            if(result.success){
                NotifyUtil.sendNotify({
                    title: avidCallNotifyTitle,
                    content: '有 '+result.data+' 条急呼数据未跟进，请去急呼跟踪列表查看处理',
                    onclick: function(){

                    }
                });
            }
        }
    });
}
function notifyFiveMinNewData(){
    $.ajax({
        url: '/monkey/avidCall/getFiveMinNewDataNum',
        success: function(result){
            if(result.success){
                NotifyUtil.sendNotify({
                    title: avidCallNotifyTitle,
                    content: '有人急呼了，赶紧去急呼跟踪列表看看~~',
                    onclick: function(){

                    }
                });
            }
        }
    });
}
