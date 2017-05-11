
<#-- 隐藏文本域 -->
<input type="hidden" id="searchParamJson" value='${searchParamJson}'>

<div class="title-div">
    <span>急呼跟踪列表</span>
</div>
<hr>

<div class="tabbable-custom ">
    <ul class="nav nav-tabs ">
        <li class="active">
            <a href="#need_complete" data-toggle="tab" aria-expanded="true">待完善</a>
        </li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="need_complete">

            <#--查询条件-->
            <div class="row search-row" >
                <div class="col-md-4 col-sm-6 ">
                    门店名称:
                    <input type="text" class="form-control" id="shopName" placeholder="支持模糊搜索">
                </div>
                <div class="col-md-4 col-sm-6 ">
                    联系电话:
                    <input type="text" class="form-control" id="shopTel" placeholder="全量匹配，必须输全">
                </div>
                <div class="col-md-4 col-sm-6 ">
                    急呼车型:
                    <input type="text" class="form-control" id="carName" placeholder="支持模糊搜索">
                </div>

            </div>

            <div class="row search-row">
                <div class="col-md-4 col-sm-6 ">
                    状态:
                    <select class="select2me" id="selectStatus">
                        <option value="-1">--请选择--</option>
                        <option value="0">未跟进</option>
                        <option value="1">暂存</option>
                        <option value="2">生成需求单</option>
                        <option value="3">门店取消</option>
                    </select>
                </div>
                <div class="col-md-4 col-sm-6 left">
                    <button type="button" class="btn btn-block btn-danger" id="searchAvid_btn">搜索</button>
                </div>
            </div>
        </div>

        <br>
        <#--结果页-->
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>序号</th>
                        <th>城市</th>
                        <th>门店名称</th>
                        <th>联系电话</th>
                        <th>急呼车型</th>
                        <th>急呼时间</th>
                        <th>转需求单时间</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody id="show_tbody">

                </tbody>
            </table>

            <div class="center footer">
                <input type="hidden" id="this_page_index" value="0">
                <div class="pagination">
                    <a href="#" class="first" data-action="first" >首页</a>
                    <a href="#" class="previous" data-action="previous" >上一页</a>
                    <input type="text" readonly="readonly" data-max-page="40" />
                    <a href="#" class="next" data-action="next" >下一页</a>
                    <a href="#" class="last" data-action="last" >末页</a>
                </div>
            </div>
        </div>

    </div>
</div>



<script type="text/html" id="avidTableListTemplate">
    {{each showList as item idx}}
    <tr>
        <td><span>{{idx+1}}</span></td>
        <td><span>{{item.cityName}}</span></td>
        <td><span>{{item.shopName}}</span></td>
        <td><span>{{item.shopTel}}</span></td>
        <td><span>{{item.carName}}</span></td>
        <td><span>{{item.gmtCreate}}</span></td>
        <td><span>{{if item.turnWishTime}} {{item.turnWishTime}} {{else}} / {{/if}}
        </span></td>
        <td>
            {{if item.avidCallStatus == 0}}<span class="red">未跟进</span>
            {{else if item.avidCallStatus == 1}}<span style="color: #e07163">暂存</span>
            {{else if item.avidCallStatus == 2}}<span style="color: blue">生成需求单</span>
            {{else if item.avidCallStatus == 3}}<span>门店取消</span>
            {{/if}}
        </td>
        <td>
            {{if item.avidCallStatus < 2}}<a href="javascript:" class="edit-btn" data-id="{{item.id}}">编辑</a>
            {{else if item.avidCallStatus == 3}}<a href="javascript:" data-reason="{{item.cancelReason}}" class="fail_reason_btn">取消原因</a>
            {{/if}}
        </td>
    </tr>
    {{/each}}
</script>


