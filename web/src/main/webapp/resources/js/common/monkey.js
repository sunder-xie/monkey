var MONKEY =function(){
    var alertContent = function (heard_html,content, width) {
        var alert_model_obj = $('#alert_model', parent.document);

        var hearder_obj = alert_model_obj.find(".modal-header");
        if (heard_html == undefined) {
            hearder_obj.addClass('hidden');
        } else {
            hearder_obj.html(heard_html);
            hearder_obj.removeClass('hidden');
        }

        var body_obj = alert_model_obj.find(".modal-body");
        if (content == undefined) {
            body_obj.addClass('hidden');
        } else {
            body_obj.html(content);
            body_obj.removeClass('hidden');
        }


        if (width == undefined) {
            width = "500";
        }
        alert_model_obj.data("width", width);
        alert_model_obj.modal('show');

        return false;
    };

    var confirmContent = function(reqParam){
        var param = $.extend({
            width: 500,
            content: '',
            okFun: function(){}
        }, reqParam);

        var confirmModel = $('#confirmModel', parent.document);
        confirmModel.find('.modal-body').html(param.content);
        confirmModel.find('.confirm-ok-btn').unbind().click(function(){
            if($.isFunction(param.okFun)){
                param.okFun();
            }

            confirmModel.modal('hide');
        });
        if(param.width !== undefined){
            confirmModel.data('width', param.width);
        }
        confirmModel.modal('show');
    };


    return{

        alertFunc:function(params){
            //heard,content, width
            alertContent(params['heard'],params['content'],params['width'])
        },
        confirmFun: function(params){
            confirmContent(params);
        }
    }
}();