<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title></title>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="/resources/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME STYLES -->
    <link href="/resources/assets/css/style-metronic.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/style.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/pages/portfolio.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <!-- select框样式 -->
    <link href="/resources/assets/plugins/select2/select2_metro.css" rel="stylesheet" type="text/css"/>
    <!-- 弹出框样式 -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .select-style{
            font-size:15px;
            width:100%;
            margin-bottom:15px;
            text-align:left;

        }
        .search-btn{
            margin-top: 20px;
            margin-bottom: 10px;
            font-weight: bold;
            font-size: 16px;
            padding-top: 10px;
            padding-bottom: 10px;
            -webkit-border-radius: 4px !important;
            -moz-border-radius: 4px !important;
        }
        .search-mile{
            font-size:15px;
        }
        .detail-table td{
            text-align: center;
            vertical-align: middle;
            padding: 15px 15px !important;
        }
        .mile-highlight{
            background-color: #F5F3C5;
        }
        .check-style{
            color:#b4b4b4;
            font-size:30px;
        }
        .selected-car-head{
            text-align: center;
            background-color: #515465 !important;
            color: #fff !important;
            padding-top: 15px;
            padding-bottom: 15px;
        }
        .selected-car{
            font-weight: bold;font-size: 18px;padding-bottom:5px;
        }

        .div-style{
            float:left;width:25%;padding: 5px;
        }
        .content-style{
            text-align:center;padding:5px;border:solid #E6E2E2 1px;height:150px;
        }
        .item-style{
            height: 85px;
            font-size: 16px;
            color: #c00;
            padding-top: 30px;
        }
        .suggest-style{
            color: #87868E;
        }

        .panel{
            margin-bottom: 10px;
        }

/*
        .carPartPic {
            position: relative;
            margin: 0 auto;
            width: 100%;
            height: 475px;
            overflow: hidden;
        }
        .carPar-item {
            position: absolute;
            top: 45px;
        }
        .carPar-item.l {
            left: 20px;
        }
        .carPar-item.r {
            right: 20px;
        }
        .carPartPic .p-a {
            position: absolute;
        }
        */

    </style>
</head>
<body style="padding: 15px !important;">
    <div class="row" style="margin: 0;">
        <div style="float:left;width:30%;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong>选择车款</strong>
                </div>
                <div class="panel-heading">

                    <select id="brandSelect" style="margin-top:5px;" class="select-style select2me">
                        <option value="-1">选择品牌</option>
                    </select>
                    <select id="modelSelect" style="" class="select-style select2me">
                        <option value="-1">选择车型</option>
                    </select>
                    <select id="carSelect" style="" class="select-style select2me">
                        <option value="-1">选择车款</option>
                    </select>
                    <input id="searchMile" class="form-control search-mile" maxlength="9" placeholder="请输入里程数（单位：公里）">
                    <div id="searchBtn" class="btn default btn-block search-btn" >
                        查询保养项目
                    </div>
                </div>
            </div>
        </div>
        <div id="maintainDetail" style="float:right;width:69%;">
            <div class="panel panel-default">
                <img src="/resources/css/img/carPartPic.png" style="width:100%;height:465px;">
            </div>

            <#--
            <div class="carPartPic">
                <img src="/resources/css/img/carPartPic.png" >

                <ul class="carPar-item l">
                    <li><a href="javascript:;" title="" id="101000000000000"><i></i>前保险杠</a></li>
                    <li><a href="javascript:;" title="" id="102000000000000"><i></i>后保险杠</a></li>
                    <li><a href="javascript:;" title="" id="103000000000000"><i></i>中网</a></li>
                    <li><a href="javascript:;" title="" id="104000000000000"><i></i>前照灯</a></li>
                    <li><a href="javascript:;" title="" id="105000000000000"><i></i>后灯</a></li>
                    <li><a href="javascript:;" title="" id="106000000000000"><i></i>车内灯</a></li>
                    <li><a href="javascript:;" title="" id="107000000000000"><i></i>后视镜</a></li>

                    <li><a href="javascript:;" title="" id="109000000000000"><i></i>前叶子板</a></li>
                    <li><a href="javascript:;" title="" id="110000000000000"><i></i>后叶子板</a></li>

                    <li><a href="javascript:;" title="" id="112000000000000"><i></i>前门</a></li>
                    <li><a href="javascript:;" title="" id="113000000000000"><i></i>后门</a></li>

                    <li><a href="javascript:;" title="" id="115000000000000"><i></i>尾门/行李箱盖</a></li>
                    <li><a href="javascript:;" title="" id="116000000000000"><i></i>前车身</a></li>
                    <li><a href="javascript:;" title="" id="117000000000000"><i></i>后车身</a></li>
                    <li><a href="javascript:;" title="" id="118000000000000"><i></i>车顶</a></li>
                    <li><a href="javascript:;" title="" id="119000000000000"><i></i>车身及侧围</a></li>
                    <li><a href="javascript:;" title="" id="120000000000000"><i></i>车架/地板</a></li>
                    <li><a href="javascript:;" title="" id="121000000000000"><i></i>安全气囊</a></li>

                    <li><a href="javascript:;" title="" id="123000000000000"><i></i>座椅及安全带</a></li>
                </ul>
                <ul class="carPar-item r">
                    <li><a href="javascript:;" title="" id="124000000000000"><i></i>空调系统</a></li>
                    <li><a href="javascript:;" title="" id="125000000000000"><i></i>冷却系</a></li>
                    <li><a href="javascript:;" title="" id="126000000000000"><i></i>传动系</a></li>
                    <li><a href="javascript:;" title="" id="127000000000000"><i></i>制动系</a></li>
                    <li><a href="javascript:;" title="" id="128000000000000"><i></i>转向系</a></li>
                    <li><a href="javascript:;" title="" id="129000000000000"><i></i>发动机</a></li>
                    <li class=""><a href="javascript:;" title="" id="130000000000000"><i></i>供电系统及起动机</a></li>
                    <li><a href="javascript:;" title="" id="131000000000000"><i></i>点火系</a></li>
                    <li><a href="javascript:;" title="" id="132000000000000"><i></i>燃油供给系</a></li>
                    <li><a href="javascript:;" title="" id="133000000000000"><i></i>进气系统</a></li>
                    <li><a href="javascript:;" title="" id="134000000000000"><i></i>排气系统</a></li>
                    <li><a href="javascript:;" title="" id="135000000000000"><i></i>电器</a></li>
                    <li><a href="javascript:;" title="" id="136000000000000"><i></i>线束</a></li>
                    <li><a href="javascript:;" title="" id="137000000000000"><i></i>变速箱及离合器</a></li>
                    <li><a href="javascript:;" title="" id="138000000000000"><i></i>前桥及前悬架</a></li>
                    <li><a href="javascript:;" title="" id="139000000000000"><i></i>后桥及后悬架</a></li>
                    <li><a href="javascript:;" title="" id="140000000000000"><i></i>车轮</a></li>


                </ul>

                <div style="left:291px;top:120px;" class="p-a"><a title="后门" href="javascript:;" id="113000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:222px;top:173px;" class="p-a"><a title="前门" href="javascript:;" id="112000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:155px;top:248px;" class="p-a"><a title="前叶子板" href="javascript:;" id="109000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:198px;top:304px;" class="p-a"><a title="前车身" href="javascript:;" id="116000000000000"><i class=""></i><s></s><b></b></a></div>

                <div style="left:261px;top:414px;" class="p-a"><a title="空调系统" href="javascript:;" id="124000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:231px;top:458px;" class="p-a"><a title="冷却系" href="javascript:;" id="125000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:204px;top:491px;" class="p-a"><a title="中网" href="javascript:;" id="103000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:170px;top:536px;" class="p-a"><a title="前保险杠" href="javascript:;" id="101000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:301px;top:184px;" class="p-a"><a title="车顶" href="javascript:;" id="118000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:379px;top:214px;" class="p-a"><a title="车身及侧围" href="javascript:;" id="119000000000000"><i></i><s></s><b></b></a></div>
                <div style="left:450px;top:140px;" class="p-a"><a title="车架/地板" href="javascript:;" id="120000000000000"><i></i><s></s><b></b></a></div>
                <div style="left:421px;top:202px;" class="p-a"><a title="后叶子板" href="javascript:;" id="110000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:402px;top:357px;" class="p-a"><a title="发动机" href="javascript:;" id="129000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:452px;top:418px;" class="p-a"><a title="前照灯" href="javascript:;" id="104000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:337px;top:557px;" class="p-a"><a title="车内灯" href="javascript:;" id="106000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:356px;top:637px;" class="p-a"><a title="前桥及前悬架" href="javascript:;" id="138000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:482px;top:118px;" class="p-a"><a title="后车身" href="javascript:;" id="117000000000000"><i></i><s></s><b></b></a></div>
                <div style="left:425px;top:598px;" class="p-a"><a title="安全气囊" href="javascript:;" id="121000000000000"><i></i><s></s><b></b></a></div>


                <div style="left:538px;top:302px;" class="p-a"><a title="座椅及安全带" href="javascript:;" id="123000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:595px;top:335px;" class="p-a"><a title="后视镜" href="javascript:;" id="107000000000000"><i class=""></i><s></s><b></b></a></div>

                <div style="left:552px;top:433px;" class="p-a"><a title="车轮" href="javascript:;" id="140000000000000"><i class=""></i><s></s><b></b></a></div>

                <div style="left:698px;top:157px;" class="p-a"><a title="尾门/行李箱盖" href="javascript:;" id="115000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:739px;top:128px;" class="p-a"><a title="后保险杠" href="javascript:;" id="102000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:764px;top:200px;" class="p-a"><a title="后灯" href="javascript:;" id="105000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:790px;top:360px;" class="p-a"><a title="燃油供给系" href="javascript:;" id="132000000000000"><i></i><s></s><b></b></a></div>
                <div style="left:787px;top:408px;" class="p-a"><a title="后桥及后悬架" href="javascript:;" id="139000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:705px;top:428px;" class="p-a"><a title="变速箱及离合器" href="javascript:;" id="137000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:787px;top:456px;" class="p-a"><a title="点火系" href="javascript:;" id="131000000000000"><i></i><s></s><b></b></a></div>
                <div style="left:688px;top:480px;" class="p-a"><a title="排气系统" href="javascript:;" id="134000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:710px;top:528px;" class="p-a"><a title="电器" href="javascript:;" id="135000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:633px;top:536px;" class="p-a"><a title="供电系统及起动机" href="javascript:;" id="130000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:680px;top:583px;" class="p-a"><a title="线束" href="javascript:;" id="136000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:580px;top:658px;" class="p-a"><a title="制动系" href="javascript:;" id="127000000000000"><i class=""></i><s></s><b></b></a></div>
                <div style="left:498px;top:598px;" class="p-a"><a title="进气系统" href="javascript:;" id="133000000000000"><i></i><s></s><b></b></a></div>
                <div style="left:518px;top:663px;" class="p-a"><a title="传动系" href="javascript:;" id="126000000000000"><i class=""></i><s></s><b></b></a></div>


                <div style="left:380px;top:570px;" class="p-a"><a title="转向系" href="javascript:;" id="128000000000000"><i class=""></i><s></s><b></b></a></div>
            </div>
            -->
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading" >
            <strong>其他保养项目</strong>
        </div>
        <div id="commonMaintain" class="panel-body">

        </div>
    </div>


    <!-- 弹出框 -->
    <div id="make_sure_modal" class="modal fade" tabindex="-1" data-width="300">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h4 class="modal-title">确认框</h4>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12" id="make_sure_msg">
                    您确认吗？
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" class="btn red" id="make_sure_ok_button">确认</button>
        </div>
    </div>


    <!-- =============================自定义的模板============================= -->
    <script type="text/html" id="maintainDetailTemplate">
        <div class="panel panel-default">
            <div class="panel-heading selected-car-head">
                <div id="selectedCar" class="selected-car">
                    {{selectedCar}}
                </div>
                <div>
                    (建议您下次保养在{{nextMile}}公里)
                </div>
            </div>
            <div class="panel-body" style="padding: 0;">
                <div id="maintainDetailTable" class="table-scrollable" style="margin: 0 !important;">
                    <table class="table table-hover table-bordered detail-table" align="center">
                        <tbody>
                        {{each maintainDetail as list idx}}
                        {{if idx==0}}
                        <tr>
                            {{each list}}
                            <td class="{{if nextMileIdx==$index}} mile-highlight {{/if}}">{{$value}}</td>
                            {{/each}}
                        </tr>
                        {{else}}
                        <tr>
                            {{each list as value i}}
                            {{if i==0}}
                            <td>{{value}}</td>
                            {{else}}
                            <td title="{{value.title}}" class="{{if nextMileIdx==i}} mile-highlight {{/if}}">
                                {{if value.flag}}
                                <i class="fa fa-check check-style"></i>
                                {{/if}}
                            </td>
                            {{/if}}
                            {{/each}}
                        </tr>
                        {{/if}}
                        {{/each}}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </script>

    <script type="text/html" id="carBrandTemplate">
        <option value="-1">选择品牌</option>
        {{each data}}
        <option value="{{$value.id}}">{{$value.firstWord}} {{$value.name}}</option>
        {{/each}}
    </script>

    <script type="text/html" id="carModelTemplate">
        <option value="-1">选择车型</option>
        {{each data}}
        <option value="{{$value.id}}">{{$value.name}}</option>
        {{/each}}
    </script>

    <script type="text/html" id="carTemplate">
        <option value="-1">选择车款</option>
        {{each data}}
        <option value="{{$value.id}},{{$value.brand}} {{$value.model}}">{{$value.name}}</option>
        {{/each}}
    </script>

    <script type="text/html" id="commonMaintainTemplate">
        {{each commonItemList as item idx}}
        <div class="div-style">
            <div class="content-style">
                <div class="item-style">{{item.name}}</div>
                <div class="suggest-style">{{item.suggest}}</div>
            </div>
        </div>
        {{/each}}
    </script>


    <!-- ================================== js文件引入 ================================== -->
    <script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
    <!-- select框优化 -->
    <script src="/resources/assets/plugins/select2/select2.min.js" type="text/javascript"></script>
    <!-- 弹出框 -->
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>

    <script src="/resources/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
    <!-- 模板解析 -->
    <script src="/resources/js/lib/template.js" type="text/javascript"></script>
    <!-- 通用js -->
    <script src="/resources/js/common/common.js" type="text/javascript"></script>

    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

    <!-- mainJS -->
    <script src="/resources/js/carMaintain/index.js" type="text/javascript"></script>

</body>
</html>