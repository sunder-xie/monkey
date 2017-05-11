
var NotifyUtil = function () {
    var notify_time = 5000;

    return {
        sendNotify:function(reqParam) {
            var param = $.extend({
                title: '桌面提醒',
                content: '桌面提醒',
                //icon: '/resources/otherres/im_80_80.png',
                icon: '/resources/otherres/monkey_logo.jpg',
                time: notify_time,
                onclick: function(){}
            }, reqParam);

            var voice_html = '<audio id="im_audio_mp3" autoplay="autoplay"><source src="/resources/otherres/taoqi.mp3" /></audio>';
            $("body").append(voice_html);
            setTimeout(function(){
                $("#im_audio_mp3").remove();
            }, param.time);

            if (window.webkitNotifications) {
                //chrome老版本
                if (window.webkitNotifications.checkPermission() == 0) {
                    var notif = window.webkitNotifications.createNotification(param.icon, param.title, param.content);
                    notif.display = function() {};
                    notif.onerror = function() {};
                    notif.onclose = function() {};
                    notif.onclick = function() {
                        param.onclick();
                    };
                    notif.replaceId = 'Meteoric';
                    notif.show();
                } else {
                    window.webkitNotifications.requestPermission(notify);
                }
            }
            else if("Notification" in window){
                // 判断是否有权限
                if (Notification.permission === "granted") {
                    var notification = new Notification(param.title, {
                        "icon": param.icon,
                        "body": param.content
                    });

                    notification.onshow = function(){
                        setTimeout(function(){
                            notification.close();
                        }, param.time);
                    };
                    notification.onclick = function(){
                        param.onclick();
                    };
                }
                //如果没权限，则请求权限
                else if (Notification.permission !== 'denied') {
                    Notification.requestPermission(function(permission) {
                        // Whatever the user answers, we make sure we store the
                        // information
                        if (!('permission' in Notification)) {
                            Notification.permission = permission;
                        }
                        //如果接受请求
                        if (permission === "granted") {
                            var notification = new Notification(param.title, {
                                "icon": param.icon,
                                "body": param.content
                            });
                            notification.onshow = function(){
                                setTimeout(function(){
                                    notification.close();
                                }, param.time);
                            };
                            notification.onclick = function(){
                                param.onclick();
                            };
                        }
                    });
                }
            }
        }
    }
}();
