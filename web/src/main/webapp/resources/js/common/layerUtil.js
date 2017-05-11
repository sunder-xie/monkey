/**
 * Created by huangzhangting on 16/11/2.
 */

var LayerUtil = function(){
    var backgroundColor = '#393D49';

    return{
        /** 页面中间冒泡 */
        msg: function(msg, time, callBack){
            if(time===undefined || time===null){
                time = 2000;
            }
            layer.msg(msg, {shade: [0.1, backgroundColor], time: time}, function(){
                if($.isFunction(callBack)){
                    callBack();
                }
            });
        },
        msgFun: function(msg, callBack){
            LayerUtil.msg(msg, undefined, callBack);
        },
        /** 在指定元素附近冒泡
         *  obj：指定元素，可以是js对象、jquery对象、jquery支持的选择器
         *  position：1-上边 2-右边 3-下边 4-左边
         * */
        tips: function(msg, obj, position){
            if(position===undefined || position===null){
                position = 1;
            }
            layer.tips(msg, obj, {tips: [position, backgroundColor]});
        }
    };
}();
