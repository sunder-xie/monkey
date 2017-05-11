var loadingHtml = '<img src="../../assets/img/ajax-loading.gif" alt="" class="loading">';

//表单序列化成对象 参数为jquery对象
var jq_serializeObject = function(form) {
    var o = {};
    $.each(form.serializeArray(), function(index) {
        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + "," + this['value'];
        } else {
            o[this['name']] = this['value'];
        }
    });
    return o;
};

function initPage(mp, cp, rc, doSomething){
    var string = "{current_page}/{max_page}";
    if(rc != null){
        string = string + "(总" + rc + ")";
    }
    //alert(cp);

    $('.pagination').jqPagination({
        current_page : cp, //不设置时 默认值1
        //link_string : '',
        max_page : mp,
        page_string : string,
        paged: function(page){
            //alert('paged '+page);
            doSomething(page);
            return false;
        }
    });

}

function updatePage(rows, maxPage, page){
    var str = "{current_page}/{max_page}";
    if(rows != null && rows !== undefined){
        str = str + "(共" + rows + "条)";
    }
    //alert(str)
    var jqPage = $('.pagination').data('jqPagination');
    jqPage.options.page_string = str;

    if(maxPage<page){
        if(page!=1){
            page=page-1;
        }
        if((page-getCurrentPage()) != 0){
            jqPage.setPage(page, true);//第二个参数必须是true，否则会去调用回调函数
        }
        if(maxPage==0){
            maxPage =1;
        }
        jqPage.setMaxPage(maxPage, true);//必须有一个得更新，否则更新不了 page_string
    }else{
        if((page-getCurrentPage()) != 0){
            jqPage.setPage(page, true);//第二个参数必须是true，否则会去调用回调函数
        }
        jqPage.setMaxPage(maxPage, true);//必须有一个得更新，否则更新不了 page_string
    }

}

function initCurrentPage(page){
    //设置当前页后会自动调用   paged 绑定的函数
    $('.pagination').jqPagination('option', 'current_page', page);
}

function getCurrentPage(){
    return $('.pagination').jqPagination('option', 'current_page');
}



function replaceAllSpace(value){
    return value.replace(/\s+/g,'');
}

function onMouseOver(target, color){
    if(color==undefined){
        color = 'blue';
    }
    target.style.color = color;
    target.style.textDecoration = 'underline';
}

function onMouseOut(target, color){
    if(color==undefined){
        color = 'black';
    }
    target.style.color = color;
    target.style.textDecoration = 'none';
}

function jqCopyObject(obj){
    return $.extend(true, {} ,obj);
}

function jqCopyArray(array){
    return $.map(array, function(obj){
        return $.extend(true,{},obj);
    });
}

function jqPropCount(obj){
    if(!$.isPlainObject(obj)){
        return 0;
    }
    return Object.getOwnPropertyNames(obj).length;
}

function jqDisable(el){
    $(el).attr('disabled', true);
}
function jqEnable(el){
    $(el).attr('disabled', false);
}

//
function jqBlockUI (el, msg, centerY) {
    if(msg==undefined){
        msg = '';
    }
    var el = $(el);
    if (el.height() <= 400) {
        centerY = true;
    }
    el.block({
        message: msg,
        centerY: centerY != undefined ? centerY : true,
        css: {
            top: '10%',
            border: 'none',
            padding: '2px',
            backgroundColor: 'none'
        },
        overlayCSS: {
            backgroundColor: 'grey',
            opacity: 0.3,
            cursor: 'wait'
        }
    });
}

//
function jqUnblockUI (el) {
    $(el).unblock({
        onUnblock: function () {
            $(el).css('position', '');
            $(el).css('zoom', '');
        }
    });
}

