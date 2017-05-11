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
    <!--select框-->
    <link rel="stylesheet" type="text/css" href="/resources/assets/plugins/select2/select2_metro.css" />
    <!--弹出框-->
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css"/>
    <link href="/resources/assets/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css"/>

    <link href="/resources/css/common.css" rel="stylesheet" type="text/css"/>
<#--//公用css-->
    <link href="/resources/css/mycss/common.css" rel="stylesheet" type="text/css"/>

    <link rel="stylesheet" type="text/css" href="/resources/css/zTreeStyle.css"/>

    <style type="text/css">
        .table-btn{
            background-color:white;
            color:black;
            border:1px solid #C0C0C0;
        }
        .modal {
            background-color: #fff;
        }

    </style>
</head>
<body class="page-header-fixed" style="overflow-x: hidden;">

    <div class="portlet" align="center">
        <div class="portlet-title">
            <div class="caption">用户管理</div>
            <div class="tools">
                <a href="javascript:;" class="collapse"></a>
                &nbsp;&nbsp;
                
            </div>
        </div>

        <div class="portlet-body" style="display: block;">
            <div class = "row" style="width:70%;">
                <div class="col-md-6">
                    <div class="input-icon">
                        <input class="form-control input search" style="height: 35px;" type="search" id="search_name" placeholder="用户名">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="input-icon">
                        <input class="form-control input search" style="height: 35px;" type="search" id="search_cn_name" placeholder="真实姓名">
                    </div>
                </div>
            </div>
            <br/>
            <div class = "row" style="width:45%;">
                <div class="col-md-6">
                    <div class="btn btn-danger btn-block" id="search_button">
                        搜索
                        <i class="m-icon-swapright m-icon-white"></i>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="btn default input btn-block" id="reset_button">重置</div>
                </div>
            </div>
        </div>

    </div>

    <div class="portlet" id="search-result-portlet">
        <div class="portlet-title">
            <div class="caption">
                <i class="fa fa-reorder"></i>
                <span>用户列表</span>
            </div>
            <div class="tools">
                <a href="javascript:;" class="collapse"></a>
                &nbsp;&nbsp;
            </div>
        </div>
        <div class="portlet-body" style="display: block;">
            <div class="table-responsive">
                <table class="table table-hover" align="center" >
                    <thead>
                    <tr>
                        <th style="width:10%;text-align:center;">序号</th>
                        <th style="width:30%;">用户名</th>
                        <th style="width:30%;">真实姓名</th>
                        <th style="width:30%;text-align:center;">操作</th>
                    </tr>
                    </thead>

                    <tbody id="user_list_tbody">

                    </tbody>
                </table>
                
                <!-- 分页 -->
                <div style="text-align: center; padding: 10px;">
                    <div class="pagination">
                        <a href="#" class="first" data-action="first" >&laquo;</a>
                        <a href="#" class="previous" data-action="previous" >&lsaquo;</a>
                        <input type="text" readonly="readonly" data-max-page="40" />
                        <a href="#" class="next" data-action="next" >&rsaquo;</a>
                        <a href="#" class="last" data-action="last" >&raquo;</a>
                    </div>
                </div>

                <div style="height:50px;" class="note note-danger" >
                    <div style="float:left" id="result_show">请选填搜索项，点击<span style="color:red">搜索按钮</span>获得数据</div>
                    <div style="float:right">
                        <button class="table-btn" id="add_user_button">
                            <span class="glyphicon glyphicon-plus" style="color: #ff0000;"></span>
                            添加用户
                        </button>
                        &nbsp;
                        <button class="table-btn" id="accredit_button">
                            <span class="" style="color: #ff0000;"></span>
                            用户授权
                        </button>
                    </div>
                </div>

            </div>
            
        </div>
    </div>

    <!-- 弹出框 -->
    <div id="add_user_modal" class="modal fade" tabindex="-1" data-width="400">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">添加用户</h5>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12">
                    <form id="add_user_form">
                        <table class="common_modal_table">
                            <tr>
                                <th style="width:90px;">用户名<span style="color:red;margin-left:3px;">*</span></th>
                                <td>
                                    <input name="userName" maxlength="15" type="text" class="col-md-12 form-control" style="width:260px;">
                                </td>
                            </tr>
                            <tr>
                                <th>真实姓名<span style="color:red;margin-left:3px;">*</span></th>
                                <td><input name="nickName" maxlength="15" type="text" class="col-md-12 form-control" style="width:260px;"></td>
                            </tr>
                            <tr>
                                <th>密码<span style="color:red;margin-left:3px;">*</span></th>
                                <td><input name="passWord" maxlength="50" type="password" class="col-md-12 form-control" style="width:260px;"></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" class="btn red" id="add_user_ok_button">提交</button>
        </div>
    </div>

    <div id="modify_user_modal" class="modal fade" tabindex="-1" data-width="400">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">修改用户</h5>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="col-md-12">
                    <form id="modify_user_form">
                        <table class="common_modal_table">
                            <tr>
                                <th style="width:90px;">用户名<span style="color:red;margin-left:3px;">*</span></th>
                                <td>
                                    <input type="hidden" name="id" value="">
                                    <input readonly name="userName" type="text" class="col-md-12 form-control" style="width:260px;">
                                </td>
                            </tr>
                            <tr>
                                <th>真实姓名<span style="color:red;margin-left:3px;">*</span></th>
                                <td><input name="nickName" maxlength="15" type="text" class="col-md-12 form-control" style="width:260px;"></td>
                            </tr>
                            <tr>
                                <th>密码<span style="color:red;margin-left:3px;">*</span></th>
                                <td><input name="passWord" maxlength="50" type="password" class="col-md-12 form-control" style="width:260px;"></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" class="btn red" id="modify_user_ok_button">提交</button>
        </div>
    </div>

    <div id="accredit_user_modal" class="modal fade" tabindex="-1" data-width="450">
        <div class="modal-header" style="padding-bottom:5px;">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">用户授权</h5>
        </div>
        <div class="modal-body" style="padding-top:10px;padding-bottom:0px;">
            <div class="row">
                <div class="col-md-12">
                    <form id="accredit_user_form">
                        <table class="common_modal_table">
                            <tr>
                                <th style="width:75px;">用户<span style="color:red;margin-left:3px;">*</span></th>
                                <td>
                                    <select name="userId" style="width:320px;">
                                        <option value="0">--请选择用户--</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <th>角色<span style="color:red;margin-left:3px;">*</span></th>
                                <td>
                                    <ul id="roleTree" class="ztree" style="height:250px;width:100%;overflow:auto;"></ul>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
        </div>
        <div class="modal-footer" style="height:45px;padding-top:5px;">
            <button type="button" data-dismiss="modal" class="btn">关闭</button>
            <button type="button" class="btn red" id="accredit_user_ok_button">提交</button>
        </div>
    </div>


    <div id="make_sure_modal" class="modal fade" tabindex="-1" data-width="300">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
            <h5 class="modal-title">确认框</h5>
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
    
    <!-- 自定义模板 -->
    <script type="text/template" id="user_list_template">
        {{#data}}
        <tr>
            <td style="text-align:center;">
                {{seqNumber}}
            </td>
            <td>{{userName}}</td>
            <td>{{cnName}}</td>
            <td style="text-align:center;">
                <input type="hidden" value="{{userId}}">
                <input type="hidden" value="{{userName}}">
                <button class="table-btn delete_user_button">
                    <span class="glyphicon glyphicon-minus" style="color: #ff0000;"></span>
                    删除
                </button>
                &nbsp;&nbsp;
                <button class="table-btn modify_user_button">
                    <span class="" style="color: #ff0000;"></span>
                    修改
                </button>
            </td>
        </tr>
        {{/data}}
    </script>

    <!-- ================================== js文件引入 ================================== -->
    <script src="/resources/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="/resources/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>

    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
    <script src="/resources/assets/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript" ></script>
    
    <script type="text/javascript" src="/resources/assets/plugins/select2/select2.min.js"></script>

    <script src="/resources/js/lib/mustache.js" type="text/javascript"></script>

    <script src="/resources/js/lib/jquery.jqpagination.js" type="text/javascript" ></script>

    <script src="/resources/js/lib/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>

    <script src="/resources/js/common/common.js" type="text/javascript" ></script>
    
    <script src="/resources/assets/scripts/app.js" type="text/javascript"></script>
    
    <script>
        jQuery(document).ready(function() {
            // initiate layout and plugins
            App.init();
        });
    </script>
    
    <!--mainJS-->
    <script src="/resources/js/sys/user.js" type="text/javascript"></script>

</body>
</html>