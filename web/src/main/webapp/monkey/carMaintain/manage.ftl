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
    <!-- 分页组件样式 -->
    <link href="/resources/css/jqpagination.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/css/common.css" rel="stylesheet" type="text/css">

    <link rel="stylesheet" type="text/css" href="/resources/css/zTreeStyle.css"/>

    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <style type="text/css">
        .select_style{
            font-size:14px;
            font-weight: bold;
            width:100%;
            margin-bottom:15px;
            text-align:center;
        }

        .selected-style{
            color: white !important;
            text-shadow: none;
            background-color: #3385ff !important;
        }

        .modal {
            background-color: #fff;
        }

    </style>
</head>
<body class="">

    <div class="row" style="">
        <div class="col-md-4" style="padding-right:8px;">
            <div class="row">
                <div class="col-md-6" style="padding-left:20px;padding-right:5px;">
                    <select id="carBrand" style="" class="select_style select2me">
                        <option value="-1">[ 请选择品牌 ]</option>
                    </select>
                </div>
                <div class="col-md-6" style="padding-left:5px;padding-right:20px;">
                    <select id="factoryName" style="" class="select_style select2me">
                        <option value="-1">[ 请选择厂家 ]</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6" style="padding-left:20px;padding-right:5px;">
                    <select id="carSeries" style="" class="select_style select2me">
                        <option value="-1">[ 请选择车系 ]</option>
                    </select>
                </div>
                <div class="col-md-6" style="padding-left:5px;padding-right:20px;">
                    <select id="vehicleType" style="" class="select_style select2me">
                        <option value="-1">[ 请选择车型 ]</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" >
                            保养方案列表
                            <span style="float:right;">
                                <button class="btn red" style="padding:2px 4px 2px 4px;" id="" onclick="to_add_model_plan();">
                                    添加数据
                                </button>
                            </span>
                        </div>
                        <div class="panel-body" style="height:300px;overflow:auto;padding:5px;" id="maintain_plan_table">
                            请先选择车型！
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-8" style="padding-left:8px;">
            <div class="tabbable-custom" >
                <ul class="nav nav-tabs">
                    <li id="tab_mileage_li" class=""><a href="#tab_mileage" data-toggle="tab" onclick="init_tab_content('tab_mileage');">保养里程</a></li>
                    <li id="tab_plan_li" class="active"><a href="#tab_plan" data-toggle="tab" onclick="init_tab_content('tab_plan');">保养方案详情</a></li>
                    <span style="float:right;padding-right:10px;">
                        <button class="btn btn-info" id="refreshBt" style="padding:2px 8px;">
                            刷新
                        </button>
                        <button class="btn green hidden" id="addMileBt" style="margin-left:10px;padding:2px 4px;">
                            添加数据
                        </button>
                        <button class="btn red hidden" id="delSomeMileBt" style="margin-left:10px;padding:2px 4px;">
                            批量删除
                        </button>
                        <button class="btn red" style="margin-left:10px;padding:2px 4px;" id="edit_plan_detail_button">
                            编辑方案
                        </button>
                        <button class="btn green" id="save_plan_detail_button" style="margin-left:10px;padding:2px 4px;">
                            保存方案
                        </button>
                        <button class="btn red" id="copyPlanBt" style="margin-left:10px;padding:2px 4px;">
                            拷贝方案
                        </button>
                    </span>
                </ul>
                <div class="tab-content" style="height:400px;overflow:auto;">
                    <div class="tab-pane " id="tab_mileage">

                    </div>
                    <div class="tab-pane active" id="tab_plan">
                        请先选择一个保养方案！
                    </div>
                </div>
            </div>

        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading" >
            <span style="font-weight:bold;font-size:17px;">
                配置车款
            </span>
            &nbsp;&nbsp;
            <span style="padding:2px 4px 2px 4px;color:red;font-weight:bold;font-size:16px;" id="currentCarModel"></span>
            &nbsp;&nbsp;
            <span style="padding:2px 4px 2px 4px;color:red;font-weight:bold;font-size:16px;" id="currentMaintainPlan"></span>
            <span style="float:right;">
                <button class="btn green" style="padding:2px 4px 2px 4px;" id="" onclick="to_save_car_maintain_plan();">
                    保存配置
                </button>
            </span>
        </div>
        <div class="panel-body" >
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-success" style="margin-bottom:0px;">
                        <div class="panel-heading" style="padding: 8px;">
                            当前方案车款
                            <span style="float:right;">
                                <select name="" id="car_info_ft" style="width:85px;" onchange="change_car_info_ft(this);">
                                    <option value="-1">选择燃料</option>
                                </select>
                                &nbsp;
                                <select name="" id="car_info_year" style="" onchange="change_car_info_year(this);">
                                    <option value="-1">选择年款</option>
                                </select>
                                &nbsp;
                                <select name="" id="car_info_power" style="" onchange="change_car_info_power(this);">
                                    <option value="-1">选择排量</option>
                                </select>
                                &nbsp;&nbsp;
                                <input type="checkbox" id="car_info_checkbox" checked style="width:15px;height:15px;margin-right:2px;" onclick="car_info_all_check(this,'car_info_table');">
                                全选
                            </span>
                        </div>
                        <div class="panel-body" style="padding:5px;overflow:auto;height:350px;" id="car_info_table">
                        </div>
                    </div>
                </div>
                <div class="col-md-6" style="padding-left: 0;">
                    <div class="panel panel-warning" style="margin-bottom:0px;">
                        <div class="panel-heading" style="padding: 8px;">
                            未配置方案的车款
                            <span style="float:right;">
                                <select name="" id="not_related_ft" style="width:85px;" onchange="change_not_related_ft(this);">
                                    <option value="-1">选择燃料</option>
                                </select>
                                &nbsp;
                                <select name="" id="not_related_year" style="" onchange="change_not_related_year(this);">
                                    <option value="-1">选择年款</option>
                                </select>
                                &nbsp;
                                <select name="" id="not_related_power" style="" onchange="change_not_related_power(this);">
                                    <option value="-1">选择排量</option>
                                </select>
                                &nbsp;&nbsp;
                                <input type="checkbox" id="not_related_checkbox" style="width:15px;height:15px;margin-right:2px;" onclick="car_info_all_check(this,'not_related_car_info_table');">
                                全选
                            </span>
                        </div>
                        <div class="panel-body" style="padding:5px;overflow:auto;height:350px;" id="not_related_car_info_table">
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>


    <!-- ===============弹出框================ -->
    <div id="make_sure_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="300">
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
            <button type="button" class="btn red" id="make_sure_ok_button">确认</button>
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
        </div>
    </div>

    <div id="makeSureModal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="400">
        <div class="modal-header">
            <h4 class="modal-title">确认框</h4>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12" id="makeSureMsg">
                    您确认吗？
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn red" id="makeSureOkBt">确认</button>
            <button type="button" class="btn default" id="makeSureCancelBt">关闭</button>
        </div>
    </div>

    <div id="add_data_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="500">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title" id="add_data_title"></h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <form id="add_data_form">
                <table class="common_modal_table">

                </table>
            </form>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="add_data_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

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

    <div id="add_mileage_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="500">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title" id="">添加保养里程</h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <form id="">
                <table class="common_modal_table">
                    <tbody>
                        <tr>
                            <th style="width:100px;">车型<span style="color:red;margin-left:3px;">*</span></th>
                            <td>
                                <span id="add_mileage_car"></span>
                            </td>
                        </tr>
                        <tr>
                            <th style="width:100px;">方案名<span style="color:red;margin-left:3px;">*</span></th>
                            <td>
                                <span id="add_mileage_plan"></span>
                            </td>
                        </tr>
                        <tr>
                            <th style="width:100px;">添加方式<span style="color:red;margin-left:3px;">*</span></th>
                            <td>
                                <select name="addType" onchange="change_add_mileage_type(this);" style="width:340px;">
                                    <option value="1" selected>单条录入</option>
                                    <option value="2">批量录入</option>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                    <tbody id="add_mileage_tbody">

                    </tbody>
                </table>
            </form>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="add_mileage_ok_button">保存</button>
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

    <div id="copy_plan_modal" class="modal fade" data-backdrop="static" tabindex="-1" data-width="500">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">拷贝保养方案 - 选择目标车型</h5>
        </div>
        <div id="copy_plan_content" class="modal-body" style="padding-top:10px;padding-bottom:0px;">

        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" class="btn red" id="copy_plan_ok_button">提交</button>
            <button type="button" data-dismiss="modal" class="btn" style="">关闭</button>
        </div>
    </div>

    <!-- =============================自定义的模板============================= -->
    <script type="text/template" id="maintain_plan_template">
        <div class="table-scrollable">
            <table class="table table-bordered " align="center">
                <thead>
                <tr>
                    <th>方案名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                {{#data}}
                <tr>
                    <td>{{maintainPlan}}</td>
                    <td style="">
                        <a href="javascript:void(0);" onclick="show_maintain_plan_detail({{id}}, '{{maintainPlan}}', this);">查看</a>
                        &nbsp;&nbsp;
                        <a href="javascript:void(0);" onclick="to_delete_model_plan({{id}}, this);">删除</a>
                        &nbsp;&nbsp;
                        <a href="javascript:void(0);" onclick="to_modify_model_plan({{id}}, '{{maintainPlan}}');">修改</a>
                    </td>
                </tr>
                {{/data}}
                </tbody>
            </table>
        </div>
    </script>

    <script type="text/template" id="car_info_template">
        <#--<div class="table-scrollable">-->
            <table class="table table-bordered table-hover " align="center" style="margin-bottom: 0;">
                <thead>
                <tr>
                    <th style="vertical-align:middle;">力洋ID</th>
                    <th style="vertical-align:middle;">车款</th>
                    <th style="vertical-align:middle;">燃料</th>
                    <th style="vertical-align:middle;text-align:center;">
                        操作
                    </th>
                </tr>
                </thead>
                <tbody>
                {{#carInfoList}}
                <tr class="{{fuelType}}">
                    <td style="vertical-align:middle;">
                        <a title="查看详细信息" href="javascript:void(0);" onclick="to_show_detail({{carModel}});" style="">{{leyelId}}</a>
                    </td>
                    <td style="vertical-align: middle;">
                        {{modelYear}}款&nbsp;{{marketName}}
                    </td>
                    <td style="vertical-align: middle;">{{fuelType}}</td>
                    <td style="vertical-align:middle;text-align:center;">
                        <input type="checkbox" name="" value="{{leyelId}}" checked style="width:20px;height:20px;">
                    </td>
                </tr>
                {{/carInfoList}}
                </tbody>
            </table>
        <#--</div>-->
    </script>

    <script type="text/template" id="not_related_car_info_template">
        <#--<div class="table-scrollable">-->
            <table class="table table-bordered table-hover " align="center" style="margin-bottom: 0;">
                <thead>
                <tr>
                    <th style="vertical-align:middle;">力洋ID</th>
                    <th style="vertical-align:middle;">车款</th>
                    <th style="vertical-align:middle;">燃料</th>
                    <th style="vertical-align:middle;text-align:center;">
                        操作
                    </th>
                </tr>
                </thead>
                <tbody>
                {{#notRelatedCarInfoList}}
                <tr class="{{fuelType}}">
                    <td style="vertical-align:middle;">
                        <a title="查看详细信息" href="javascript:void(0);" onclick="to_show_detail({{carModel}});" style="">{{leyelId}}</a>
                    </td>
                    <td style="vertical-align: middle;">
                        {{modelYear}}款&nbsp;{{marketName}}
                    </td>
                    <td style="vertical-align: middle;">{{fuelType}}</td>
                    <td style="vertical-align:middle;text-align:center;">
                        <input type="checkbox" name="" value="{{leyelId}}" style="width:20px;height:20px;">
                    </td>
                </tr>
                {{/notRelatedCarInfoList}}
                </tbody>
            </table>
        <#--</div>-->
    </script>
    <!-- 力洋车型详细信息 -->
    <script type="text/template" id="car_info_detail_template">
        {{#data}}
        <table class="common_modal_table" style="width:100%;">
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

    <script type="text/template" id="add_model_plan_template">
        <tr>
            <th style="width:100px;">车型<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <span id="car_model_span"></span>
            </td>
        </tr>
        <tr>
            <th>方案名称<span style="color:red;margin-left:3px;">*</span></th>
            <td><input name="maintainPlan" maxlength="15" type="text" class="form-control" style="width:340px;"></td>
        </tr>
    </script>
    <script type="text/template" id="modify_model_plan_template">
        <tr>
            <th style="width:100px;">车型<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <span id="car_model_span"></span>
            </td>
        </tr>
        <tr>
            <th>方案名称<span style="color:red;margin-left:3px;">*</span></th>
            <td><input name="maintainPlan" value="{{maintainPlan}}" maxlength="15" type="text" class="form-control" style="width:340px;"></td>
        </tr>
    </script>

    <!-- 保养里程 -->
    <script type="text/template" id="add_mileage_template">
        <tr>
            <th>保养里程<span style="color:red;margin-left:3px;">*</span></th>
            <td><input name="mileage" maxlength="15" type="text" class="form-control" style="width:340px;"></td>
        </tr>
    </script>
    <script type="text/template" id="add_mileage_batch_template">
        <tr>
            <th>保养里程<span style="color:red;margin-left:3px;">*</span></th>
            <td><input name="mileage" maxlength="15" type="text" class="form-control" style="width:340px;"></td>
        </tr>
        <tr>
            <th>增量<span style="color:red;margin-left:3px;">*</span></th>
            <td><input name="addStep" maxlength="5" type="text" class="form-control" style="width:340px;"></td>
        </tr>
        <tr>
            <th>条数<span style="color:red;margin-left:3px;">*</span></th>
            <td><input name="addNum" maxlength="2" type="text" class="form-control" style="width:340px;"></td>
        </tr>
    </script>
    <script type="text/template" id="modify_mileage_template">
        <tr>
            <th style="width:100px;">车型<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <span id="car_model_span"></span>
            </td>
        </tr>
        <tr>
            <th style="width:100px;">方案名<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <span id="maintain_plan_span"></span>
            </td>
        </tr>
        <tr>
            <th>保养里程<span style="color:red;margin-left:3px;">*</span></th>
            <td>
                <input name="mileage" value="{{mileage}}" maxlength="15" type="text" class="form-control" style="width:340px;">
            </td>
        </tr>
    </script>

    <!-- 数据列表：保养里程列表 -->
    <script type="text/template" id="maintain_mileage_template">
        <div class="table-responsive">
            <table class="table table-hover" align="center">
                <thead>
                    <tr>
                        <th style="text-align:center;vertical-align:middle;width:50px;">
                            <div class="btn btn-sm btn-default" style="padding:1px 4px;" onclick="selectAllMileage(this);" >
                                全选
                            </div>
                        </th>
                        <th style="text-align:center;vertical-align:middle;">序号</th>
                        <th style="vertical-align:middle;">里程</th>
                        <th style="vertical-align:middle;">保养项目</th>
                        <th style="text-align:center;vertical-align:middle;width:130px;">操作</th>
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>

    </script>
    <script type="text/template" id="maintain_mileage_data_template">
        {{#dataList}}
        <tr>
            <td style="text-align:center;vertical-align:middle;">
                <input type="checkbox" value="{{id}}" style="height:20px;width:20px;">
            </td>
            <td style="text-align:center;vertical-align:middle;">{{{seqFun}}}</td>
            <td style="vertical-align:middle;">{{mileage}}</td>
            <td style="vertical-align:middle;">{{{itemFun}}}</td>
            <td style="text-align:center;vertical-align:middle;">
                <button class="table-btn delete_button" onclick="to_delete_maintain_mileage({{id}},{{mileage}},this);">
                    删除
                </button>
                &nbsp;&nbsp;
                <button class="table-btn modify_button" onclick="to_modify_maintain_mileage({{id}},{{mileage}});">
                    修改
                </button>
            </td>
        </tr>
        {{/dataList}}
    </script>

    <!-- 保养方案明细 -->
    <script type="text/template" id="maintain_plan_detail_template">
        <div class="table-scrollable">
            <table class="table table-hover table-bordered" align="center">
                <tbody></tbody>
            </table>
        </div>
    </script>

    <script type="text/template" id="copy_plan_template">
        <div class="row">
            <div class="col-md-6" style="padding-left:20px;padding-right:5px;">
                <select id="carBrand_cp" style="" class="select_style">
                <option value="-1">[ 请选择品牌 ]</option>
                </select>
            </div>
            <div class="col-md-6" style="padding-left:5px;padding-right:20px;">
                <select id="factoryName_cp" style="" class="select_style">
                <option value="-1">[ 请选择厂家 ]</option>
                </select>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6" style="padding-left:20px;padding-right:5px;">
                <select id="carSeries_cp" style="" class="select_style">
                <option value="-1">[ 请选择车系 ]</option>
                </select>
            </div>
            <div class="col-md-6" style="padding-left:5px;padding-right:20px;">
                <select id="vehicleType_cp" style="" class="select_style">
                <option value="-1">[ 请选择车型 ]</option>
                </select>
            </div>
        </div>
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
    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript"></script>
    <!-- 通用js -->
    <script src="/resources/js/common/common.js" type="text/javascript"></script>

    <script src="/resources/js/lib/jquery.ztree.core-3.5.js" type="text/javascript"></script>
    <script src="/resources/js/lib/jquery.ztree.exhide-3.5.js" type="text/javascript"></script>

    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>

    <script>
        jQuery(document).ready(function() {
            // 初始化布局和插件
            App.init();
        });

    </script>

    <!-- mainJS -->
    <script src="/resources/js/carMaintain/manage.js" type="text/javascript"></script>

</body>
</html>