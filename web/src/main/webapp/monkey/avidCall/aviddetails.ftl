<!DOCTYPE html>
<html>
<head lang="zh">
    <meta charset="UTF-8">
    <title>全车件数据系统</title>
    <#include "/monkey/common/header_css.ftl">
    <#--自定义css-->
    <link href="/resources/jquery/jquery-ui.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/monkey/avid/avidCall.css?t=110316" rel="stylesheet" type="text/css"/>

</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <#-- 隐藏文本域 -->
    <input type="hidden" id="token" value="${token}">
    <input type="hidden" id="avidCallId" value="${avidCallDetail.id}">
    <input type="hidden" id="searchParamJson" value='${searchParamJson}'>

    <div class="title-div">
        <span>急呼编辑页</span>
    </div>
    <hr>
    <div class="row center-align">
        <div class="col-md-12">
            <button type="button" class="btn green save-btn">保存</button>
            <button type="button" class="btn blue turn-wish-btn">转需求单</button>
            <button type="button" class="btn yellow cancel-btn">门店取消</button>
            <button type="button" class="btn btn-info return-btn">返回列表页</button>
        </div>
    </div>

    <div class="sub_module">
        <div class="alert alert-success sub-module-title">
            一、车辆信息
        </div>
        <div class="row">
            <div class="col-md-4 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 车架号/VIN码
                </div>
                <div class="relative">
                    <input id="vinInput" maxlength="17" type="text" class="form-control" placeholder="请输入17位VIN码" value="${avidCallDetail.carVin}">
                    <span class="help_vin"><span class="red" id="vinLength">0</span>/17</span>
                </div>
            </div>
            <div class="col-md-4 col-sm-6">
                <div class="message_title">车辆备注</div>
                <label class="checkbox-inline">
                    <input <#if avidCallDetail.isModifyCar=1>checked</#if> type="checkbox" id="isModifyCar" >
                    改装
                </label>
            </div>
        </div>
        <hr>

        <div class="row">
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 品牌
                </div>
                <select class="form-control select2me " id="carBrand" data-id="${avidCallDetail.carBrandId}">
                    <option value="0">--请选择--</option>
                </select>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 车系
                </div>
                <select class="form-control select2me " id="carSeries" data-id="${avidCallDetail.carSeriesId}">
                    <option value="0">--请选择--</option>
                </select>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 车型
                </div>
                <select class="form-control select2me " id="carModel" data-id="${avidCallDetail.carModelId}">
                    <option value="0">--请选择--</option>
                </select>
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 排量年款
                </div>
                <select class="form-control select2me " id="carName" data-id="${avidCallDetail.carId}">
                    <option value="0">--请选择--</option>
                </select>
            </div>
        </div>

    </div>

    <div class="sub_module">
        <div class="alert alert-success sub-module-title">
            二、配件信息
        </div>
        <div class="row row-fix goods-title">
            <div class="col-md-2 col-sm-4 goods_div">
                <span class="red">*</span> 商品名称
            </div>
            <div class="col-md-2 col-sm-4 goods_div">
                oe码
            </div>
            <div class="col-md-1 col-sm-2 goods_div">
                <span class="red" >*</span> 数量
            </div>
            <div class="col-md-1 col-sm-2 goods_div">
                单位
            </div>
            <div class="col-md-2 col-sm-4 goods_div">
                <span class="red">*</span> 首选品质
            </div>
            <div class="col-md-2 col-sm-4 goods_div">
                备选品质
            </div>
            <div class="col-md-2 col-sm-4 goods_div" >
                商品备注
            </div>
        </div>
        <#if avidCallDetail.goodsList?size=0>
            <div class="row row-fix goods-row">
                <div class="col-md-2 col-sm-4 goods_div">
                    <input type="text" name="goodsName" class="form-control goods-name" >
                </div>
                <div class="col-md-2 col-sm-4 goods_div">
                    <input type="text" name="goodsOe" class="form-control" >
                </div>
                <div class="col-md-1 col-sm-2 goods_div">
                    <input type="number" name="goodsNumber" class="form-control" value="1">
                </div>
                <div class="col-md-1 col-sm-2 goods_div">
                    <input type="text" name="goodsUnit" class="form-control" >
                </div>
                <div class="col-md-2 col-sm-4 goods_div">
                    <select class="form-control select2me goods-quality" >
                        <option value="0">--请选择--</option>
                    </select>
                </div>
                <div class="col-md-2 col-sm-4 goods_div">
                    <select class="form-control select2me back-quality" >
                        <option value="0">--请选择--</option>
                    </select>
                </div>
                <div class="col-md-2 col-sm-4 goods_div" >
                    <input type="text" name="goodsRemark" class="form-control" >
                </div>
                <div class="del_goods_div">
                    <i title="删除商品" class="fa fa-times-circle red del-goods-btn" ></i>
                </div>
            </div>
        <#else>
            <#list avidCallDetail.goodsList as goods >
                <div class="row row-fix goods-row">
                    <div class="col-md-2 col-sm-4 goods_div">
                        <input type="text" name="goodsName" class="form-control goods-name" value="${goods.goodsName}">
                    </div>
                    <div class="col-md-2 col-sm-4 goods_div">
                        <input type="text" name="goodsOe" class="form-control" value="${goods.goodsOe}">
                    </div>
                    <div class="col-md-1 col-sm-2 goods_div">
                        <input type="number" name="goodsNumber" class="form-control" value="${goods.goodsNumber}">
                    </div>
                    <div class="col-md-1 col-sm-2 goods_div">
                        <input type="text" name="goodsUnit" class="form-control" value="${goods.goodsUnit}">
                    </div>
                    <div class="col-md-2 col-sm-4 goods_div">
                        <select class="form-control select2me goods-quality" data-id="${goods.goodsQualityId}">
                            <option value="0">--请选择--</option>
                        </select>
                    </div>
                    <div class="col-md-2 col-sm-4 goods_div">
                        <select class="form-control select2me back-quality" data-id="${goods.backQualityId}">
                            <option value="0">--请选择--</option>
                        </select>
                    </div>
                    <div class="col-md-2 col-sm-4 goods_div" >
                        <input type="text" name="goodsRemark" class="form-control" value="${goods.goodsRemark}">
                    </div>
                    <div class="del_goods_div">
                        <i title="删除商品" class="fa fa-times-circle red del-goods-btn" ></i>
                    </div>

                    <#-- 隐藏文本域 -->
                    <input type="hidden" class="hiddenGoodsId" value="${goods.id}">

                </div>
            </#list>
        </#if>

        <hr class="goods-info-hr">
        <div class="row">
            <div class="col-md-12">
                <button type="button" class="btn green add-goods-btn"> + 添加配件</button>
            </div>
        </div>
    </div>

    <div class="sub_module">
        <div class="alert alert-success sub-module-title">
            三、门店信息
        </div>
        <div class="row">
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 门店名称
                </div>
                <input readonly id="shopName" type="text" class="form-control" value="${avidCallDetail.shopName}">
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 联系电话
                </div>
                <input id="shopTel" type="text" class="form-control" value="${avidCallDetail.shopTel}">
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 填单人
                </div>
                <input id="fillInPerson" type="text" class="form-control" value="${avidCallDetail.fillInPerson}">
            </div>
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 填单人电话
                </div>
                <input id="fillInPersonTel" type="text" class="form-control" value="${avidCallDetail.fillInPersonTel}">
            </div>
        </div>
    </div>

    <div class="sub_module">
        <div class="alert alert-success sub-module-title">
            四、订单备注
        </div>
        <div class="row">
            <div class="col-md-3 col-sm-6">
                <div class="message_title">
                    <span class="red">*</span> 是否开票
                </div>
                <select class="form-control " id="invoiceType" >
                    <option <#if avidCallDetail.invoiceType=0>selected</#if> value="0">不开具发票</option>
                    <option <#if avidCallDetail.invoiceType=1>selected</#if> value="1">开普通发票</option>
                    <option <#if avidCallDetail.invoiceType=2>selected</#if> value="2">开增值税发票</option>
                </select>
            </div>
            <div class="col-md-9 col-sm-6"></div>
        </div>
        <hr>
        <div class="row">
            <div class="col-md-12">
                <div class="message_title">
                    订单备注
                </div>
                <textarea id="orderRemark" class="form-control order-remark">${avidCallDetail.orderRemark}</textarea>
            </div>
        </div>
    </div>

    <div class="row center-align">
        <div class="col-md-12">
            <button type="button" class="btn green save-btn">保存</button>
            <button type="button" class="btn blue turn-wish-btn">转需求单</button>
            <button type="button" class="btn yellow cancel-btn">门店取消</button>
            <button type="button" class="btn btn-info return-btn">返回列表页</button>
        </div>
    </div>

    <#-- 确认框 -->
    <div id="confirmModal" style="background: white;border-radius: 6px;" class="modal fade" tabindex="-1" data-keyboard="false">
        <div id="confirmModalContent" class="modal-body" style="overflow-x: hidden;">
        </div>
        <div class="modal-footer">
            <button id="confirmModalOkBtn" type="button" class="btn red hidden">提交</button>
            <button type="button" data-dismiss="modal" class="btn default">关闭</button>
        </div>
    </div>

    <#-- ================== 模板 ================= -->
    <script type="text/html" id="carBrandTemplate">
        <option value="0">--请选择--</option>
        {{each data as brand idx}}
        <option value="{{brand.id}}">{{brand.firstWord}} {{brand.name}}</option>
        {{/each}}
    </script>
    <script type="text/html" id="carSeriesTemplate">
        <option value="0">--请选择--</option>
        {{each data as car idx}}
        <option value="{{car.id}}">{{if car.importInfo=='进口'}}进口{{/if}} {{car.name}}</option>
        {{/each}}
    </script>
    <script type="text/html" id="subCarTemplate">
        <option value="0">--请选择--</option>
        {{each data as car idx}}
        <option value="{{car.id}}">{{car.name}}</option>
        {{/each}}
    </script>

    <#-- 品质模板 -->
    <script type="text/html" id="qualityTemplate">
        <option value="0">--请选择--</option>
        {{each data as obj idx}}
        <option value="{{obj.id}}">{{obj.name}}</option>
        {{/each}}
    </script>

    <#-- vin码解析车款数据模板 -->
    <script type="text/html" id="vinCarTemplate">
        <p style="font-size: 15px;">请选择车款</p>
        {{each data as obj idx}}
        <div class="btn btn-sm btn-default vin-car" data-id="{{obj.id}}">
            {{obj.brand}} {{obj.model}} {{obj.year}}款 {{obj.name}}
        </div>
        {{/each}}
    </script>

    <#-- 商品信息模板 -->
    <script type="text/html" id="goodsTemplate">
        <div class="row row-fix goods-row">
            <div class="col-md-2 col-sm-4 goods_div">
                <input type="text" name="goodsName" class="form-control goods-name" >
            </div>
            <div class="col-md-2 col-sm-4 goods_div">
                <input type="text" name="goodsOe" class="form-control" >
            </div>
            <div class="col-md-1 col-sm-2 goods_div">
                <input type="number" name="goodsNumber" class="form-control" value="1">
            </div>
            <div class="col-md-1 col-sm-2 goods_div">
                <input type="text" name="goodsUnit" class="form-control" >
            </div>
            <div class="col-md-2 col-sm-4 goods_div">
                <select class="form-control select2me " >
                    {{# qualityHtml}}
                </select>
            </div>
            <div class="col-md-2 col-sm-4 goods_div">
                <select class="form-control select2me " >
                    {{# qualityHtml}}
                </select>
            </div>
            <div class="col-md-2 col-sm-4 goods_div" >
                <input type="text" name="goodsRemark" class="form-control" >
            </div>
            <div class="del_goods_div">
                <i title="删除商品" class="fa fa-times-circle red del-goods-btn" ></i>
            </div>
        </div>
    </script>

    <script type="text/html" id="cancelTemplate">
        <p style="font-size: 18px;padding-bottom: 10px;">
            请选择取消原因
        </p>
        <p class="reason-p">
            <input type="radio" name="reason">
            <span class="reason-style">门店只是尝试一下</span>
        </p>
        <p class="reason-p">
            <input type="radio" name="reason">
            <span class="reason-style">门店已选择其他渠道</span>
        </p>
        <p class="reason-p">
            <input type="radio" name="reason">
            <span class="reason-style">门店觉得等待时间太长</span>
        </p>
        <p class="reason-p">
            <input type="radio" name="reason" class="other-reason-radio">
            <span class="reason-style">其他</span>
        </p>
        <p>
            <textarea maxlength="50" class="other-reason"></textarea>
        </p>
    </script>

    <#include "/monkey/common/footer_js.ftl">

    <script>
        jQuery(document).ready(function () {
            App.init();
        });
    </script>

    <script type="text/javascript" src="/resources/jquery/jquery-ui.js"></script>
    <script type="text/javascript" src="/resources/js/avidCall/avidCallDetail.js?t=110316"></script>

</body>

</html>