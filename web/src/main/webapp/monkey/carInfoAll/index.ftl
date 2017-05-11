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
    <link href="/resources/assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <!-- select框样式 -->
    <link href="/resources/assets/plugins/select2/select2_metro.css" rel="stylesheet" type="text/css"/>
    <!-- 弹出框样式 -->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>

<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .select_style{
            font-size:17px;
            width:100%;
        }

        .form_table th,.form_table td{
            padding: 0.5em 0.5em;
            border: solid 1px #dcdcdc;
        }
        .form_table th{
            text-align: right;
            font-weight: bold;
            vertical-align: middle;
        }
    </style>
</head>
<body class="page-header-fixed" style="overflow:auto;">

    <div class="portlet" align="center">
        <div class="portlet-title">
            <div class="caption">查看车型</div>

        </div>

        <div class="portlet-body" style="display: block;">
            <div class = "row" style="width:90%;margin-bottom:20px;margin-top:20px;">
                <div class="col-md-3">
                    <select id="carBrand" style="margin-right:20px;" class="select_style select2me">
                        <option value="-1">[ 请选择品牌 ]</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="factoryName" style="margin-right:20px;" class="select_style select2me">
                        <option value="-1">[ 请选择厂家 ]</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="carSeries" style="margin-right:20px;" class="select_style select2me">
                        <option value="-1">[ 请选择车系 ]</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <select id="vehicleType" style="" class="select_style select2me">
                        <option value="-1">[ 请选择车型 ]</option>
                    </select>
                </div>
            </div>
            <!--
            <div class = "row" style="width:700px;">

                <div class="col-md-6">
                    <div class="input-icon">
                        <input class="form-control search" maxlength="20" style="height:35px;width:300px;" type="search" id="carModel" placeholder="请填写车型">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="btn btn-danger" style="width:150px;" id="search_button">
                        搜索
                        <i class="m-icon-swapright m-icon-white"></i>
                    </div>
                    <div class="btn default" style="width:150px;margin-left:15px;" id="reset_button">重置</div>
                </div>
            </div>
            -->
            <div class = "row" style="width:90%;">
                <div class="col-md-3"></div>
                <div class="col-md-3">
                    <div class="btn btn-danger" style="width:100%;" id="search_button">
                        搜索
                        <i class="m-icon-swapright m-icon-white"></i>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="btn default" style="width:100%;" id="reset_button">重置</div>
                </div>
                <div class="col-md-3"></div>
            </div>
        </div>

    </div>

    <div class="portlet" id="search-result-portlet">
        <a href="javascript:void(0);" id="modify_version_button" class="modify_button_style">
            <i class="fa fa-edit" ></i>
                    <span>
                       更新版本号
                    </span>
        </a>
        <a href="javascript:void(0);" id="re_create_excel_button" class="modify_button_style">
            <i class="fa fa-edit" ></i>
                    <span>
                       重新生成全量EXCEL
                    </span>
        </a>
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-reorder"></i>
                <span>数据列表:</span>
                <span id = "export_version" style="color: red"></span>
                <a href="javascript:void(0);" style="margin-left:15px;" onclick="toExportExcelAll();">[ 导出全部数据 ]</a>
                <a href="javascript:void(0);" style="margin-left:15px;" onclick="toExportExcel();">[ 根据条件导出数据 ]:</a>
                <span id="select_condition" style="margin-left:5px;font-size:15px;color:red;"></span>
            </div>

        </div>
        <div class="portlet-body" style="display: block;">
            <div class="table-scrollable">
                <table class="table table-striped table-bordered table-hover" align="center" >
                    <thead>
                    <tr >
                        <th style="vertical-align: middle;">力洋ID</th>
                        <th style="vertical-align: middle;">销售名称</th>
                        <th style="vertical-align: middle;text-align: center;">生产年份</th>
                        <th style="vertical-align: middle;text-align: center;">上市年份</th>
                        <th style="vertical-align: middle;text-align: center;">指导价格<br>(万元)</th>
                        <th style="vertical-align: middle;text-align: center;">排量(升)</th>
                        <th style="vertical-align: middle;text-align: center;">变速器类型</th>
                        <th style="vertical-align: middle;">进气形式</th>
                        <th style="vertical-align: middle;">燃料类型</th>
                        <th style="vertical-align: middle;text-align: center;">详细信息</th>
                    </tr>
                    </thead>

                    <tbody id="list_tbody">
                        <tr>
                            <td colspan="10" style="font-size: 15px;">请先选择车型</td>
                        </tr>
                    </tbody>
                </table>

            </div>
            <!-- 分页 -->
            <div style="text-align: center; padding: 10px;">
                <div class="pagination">
                    <a href="#" class="first" data-action="first" >首页</a>
                    <a href="#" class="previous" data-action="previous" >上一页</a>
                    <input type="text" readonly="readonly" data-max-page="40"/>
                    <a href="#" class="next" data-action="next" >下一页</a>
                    <a href="#" class="last" data-action="last">末页</a>
                </div>
            </div>

            <div style="height:50px;" class="note note-danger" >
                <div style="float:left" id="result_show">请选填搜索项，获得数据</div>
                <div style="float:right">

                </div>
            </div>

        </div>
    </div>

    <!-- ================================== 确认框 ================================== -->
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

    <!-- 弹出框 -->
    <div id="car_info_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="800" data-height="480">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">详细信息</h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <div class="row" id="car_info_detail">

            </div>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

    <!-- 自定义的模板 -->
    <script type="text/template" id="list_template">
        {{#data}}
        <tr>
            <td style="">{{leyelId}}</td>
            <td>{{marketName}}</td>
            <td style="text-align: center;">{{createYear}}</td>
            <td style="text-align: center;">{{publicYear}}</td>
            <td style="text-align: center;">{{guidePrice}}</td>
            <td style="text-align: center;">{{displacement}}</td>
            <td style="text-align: center;">{{transmissionType}}</td>
            <td>{{intakeStyle}}</td>
            <td>{{fuelType}}</td>
            <td style="text-align: center;">
                <a href="javascript:void(0);" onclick="to_show_detail({{carModel}})">明细</a>
            </td>
        </tr>
        {{/data}}
    </script>

    <script type="text/template" id="car_info_template">
        {{#data}}
        <table class="form_table" style="width:100%;">
            <tr>
                <th style="width:150px;">力洋ID</th>
                <td>{{leyelId}}</td>
                <th style="width:150px;">厂家</th>
                <td>{{factoryName}}</td>
            </tr>
            <tr>
                <th>品牌</th>
                <td>{{carBrand}}</td>
                <th>车系</th>
                <td>{{carSeries}}</td>
            </tr>
            <tr>
                <th>车型</th>
                <td>{{vehicleType}}</td>
                <th>销售名称</th>
                <td>{{marketName}}</td>
            </tr>
            <tr>
                <th>年款</th>
                <td>{{modelYear}}</td>
                <th>排放标准</th>
                <td>{{envStandard}}</td>
            </tr>
            <tr>
                <th>车辆类型</th>
                <td>{{carType}}</td>
                <th>车辆级别</th>
                <td>{{carLevel}}</td>
            </tr>
            <tr>
                <th>指导价格(万元)</th>
                <td>{{guidePrice}}</td>
                <th>上市年份</th>
                <td>{{publicYear}}</td>
            </tr>
            <tr>
                <th>生产年份</th>
                <td>{{createYear}}</td>
                <th>上市月份</th>
                <td>{{publicMonth}}</td>
            </tr>
            <tr>
                <th>停产年份</th>
                <td>{{stopYear}}</td>
                <th>生产状态</th>
                <td>{{productionStatus}}</td>
            </tr>
            <tr>
                <th>国别</th>
                <td>{{productionCountry}}</td>
                <th>销售状态</th>
                <td>{{marketStatus}}</td>
            </tr>
            <tr>
                <th>国产合资进口</th>
                <td>{{productionType}}</td>
                <th>气缸容积</th>
                <td>{{cylinderCapacity}}</td>
            </tr>
            <tr>
                <th>排量(升)</th>
                <td>{{displacement}}</td>
                <th>燃料类型</th>
                <td>{{fuelType}}</td>
            </tr>
            <tr>
                <th>进气形式</th>
                <td>{{intakeStyle}}</td>
                <th>燃油标号</th>
                <td>{{fuelFlag}}</td>
            </tr>
            <tr>
                <th>最大马力(ps)</th>
                <td>{{maxHorsepower}}</td>
                <th>最大功率(kw)</th>
                <td>{{maxPower}}</td>
            </tr>
            <tr>
                <th>最大功率转速(rpm)</th>
                <td>{{maxPowerSpeed}}</td>
                <th>最大扭矩(N·m)</th>
                <td>{{maxTorque}}</td>
            </tr>
            <tr>
                <th>最大扭矩转速(rpm)</th>
                <td>{{maxTorqueSpeed}}</td>
                <th>气缸排列形式</th>
                <td>{{cylinderStyle}}</td>
            </tr>
            <tr>
                <th>气缸数(个)</th>
                <td>{{cylinderNum}}</td>
                <th>每缸气门数(个)</th>
                <td>{{valvePerCylinder}}</td>
            </tr>
            <tr>
                <th>压缩比</th>
                <td>{{compressionRatio}}</td>
                <th>供油方式</th>
                <td>{{fuelWay}}</td>
            </tr>
            <tr>
                <th>工信部综合油耗</th>
                <td>{{fuelConsumptionAverage}}</td>
                <th>市区工况油耗</th>
                <td>{{fuelConsumptionDowntown}}</td>
            </tr>
            <tr>
                <th>市郊工况油耗</th>
                <td>{{fuelConsumptionSuburbs}}</td>
                <th>加速时间(0-100km/h)</th>
                <td>{{accelerationTime}}</td>
            </tr>
            <tr>
                <th>最高车速</th>
                <td>{{topSpeed}}</td>
                <th>发动机特有技术</th>
                <td>{{engineUniqueTech}}</td>
            </tr>
            <tr>
                <th>三元催化器</th>
                <td>{{catalyticConverter}}</td>
                <th>冷却方式</th>
                <td>{{coolStyle}}</td>
            </tr>
            <tr>
                <th>缸径</th>
                <td>{{bore}}</td>
                <th>行程</th>
                <td>{{stroke}}</td>
            </tr>
            <tr>
                <th>发动机描述</th>
                <td>{{engineDesc}}</td>
                <th>变速器类型</th>
                <td>{{transmissionType}}</td>
            </tr>
            <tr>
                <th>档位数</th>
                <td>{{stallNum}}</td>
                <th>变速器描述</th>
                <td>{{transmissionDesc}}</td>
            </tr>
            <tr>
                <th>前制动器类型</th>
                <td>{{frontBrakeType}}</td>
                <th>后制动器类型</th>
                <td>{{backBrakeType}}</td>
            </tr>
            <tr>
                <th>前悬挂类型</th>
                <td>{{frontSuspensionNum}}</td>
                <th>后悬挂类型</th>
                <td>{{rearSuspensionType}}</td>
            </tr>
            <tr>
                <th>转向机形式</th>
                <td>{{steeringStyle}}</td>
                <th>助力类型</th>
                <td>{{boosterType}}</td>
            </tr>
            <tr>
                <th>最小离地间隙</th>
                <td>{{minClearance}}</td>
                <th>最小转弯半径</th>
                <td>{{minTurningRadius}}</td>
            </tr>
            <tr>
                <th>离去角</th>
                <td>{{departureAngle}}</td>
                <th>接近角</th>
                <td>{{approachAngle}}</td>
            </tr>
            <tr>
                <th>发动机位置</th>
                <td>{{enginePosition}}</td>
                <th>驱动方式</th>
                <td>{{driveWay}}</td>
            </tr>
            <tr>
                <th>驱动形式</th>
                <td>{{driveStyle}}</td>
                <th>车身型式</th>
                <td>{{bodyStyle}}</td>
            </tr>
            <tr>
                <th>长度(mm)</th>
                <td>{{length}}</td>
                <th>宽度(mm)</th>
                <td>{{width}}</td>
            </tr>
            <tr>
                <th>高度(mm)</th>
                <td>{{height}}</td>
                <th>轴距(mm)</th>
                <td>{{wheelbase}}</td>
            </tr>
            <tr>
                <th>前轮距(mm)</th>
                <td>{{frontTread}}</td>
                <th>后轮距(mm)</th>
                <td>{{rearTread}}</td>
            </tr>
            <tr>
                <th>整备质量(kg)</th>
                <td>{{curbWeight}}</td>
                <th>最大载重质量(kg)</th>
                <td>{{fullyLoadedWeight}}</td>
            </tr>
            <tr>
                <th>油箱容积(L)</th>
                <td>{{tankCapacity}}</td>
                <th>行李厢容积(L)</th>
                <td>{{trunkCapacity}}</td>
            </tr>
            <tr>
                <th>车顶型式</th>
                <td>{{roofType}}</td>
                <th>车篷型式</th>
                <td>{{hoodType}}</td>
            </tr>
            <tr>
                <th>车门数</th>
                <td>{{doorNum}}</td>
                <th>座位数</th>
                <td>{{seatNum}}</td>
            </tr>
            <tr>
                <th>前轮胎规格</th>
                <td>{{frontTireStyle}}</td>
                <th>后轮胎规格</th>
                <td>{{rearTireStyle}}</td>
            </tr>
            <tr>
                <th>前轮毂规格</th>
                <td>{{frontWheelStyle}}</td>
                <th>后轮毂规格</th>
                <td>{{rearWheelStyle}}</td>
            </tr>
            <tr>
                <th>轮毂材料</th>
                <td>{{wheelMaterial}}</td>
                <th>备胎规格</th>
                <td>{{spareTireStyle}}</td>
            </tr>
            <tr>
                <th>电动天窗</th>
                <td>{{electronicSkylights}}</td>
                <th>全景天窗</th>
                <td>{{panoramicSunroof}}</td>
            </tr>
            <tr>
                <th>氙气大灯</th>
                <td>{{xenonLamp}}</td>
                <th>前雾灯</th>
                <td>{{frontFogLamp}}</td>
            </tr>
            <tr>
                <th>空调</th>
                <td>{{airConditioning}}</td>
                <th>后雨刷</th>
                <td>{{backWindshielWiper}}</td>
            </tr>
            <tr>
                <th>自动空调</th>
                <td>{{autoAirConditioning}}</td>
            </tr>
        </table>
        {{/data}}
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
    <!-- 模板解析 -->
    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>
    <!-- 分页组件 -->
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>
    <!-- 通用js -->
    <script src="/resources/js/common/common.js" type="text/javascript"></script>
    <script src="/resources/js/common/monkey.js" type="text/javascript"></script>
    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

    <script>
        jQuery(document).ready(function() {
            // 初始化布局和插件
            App.init();
        });

    </script>

    <!-- mainJS -->
    <script src="/resources/js/car/carInfo.js?v=1123" type="text/javascript"></script>

</body>
</html>