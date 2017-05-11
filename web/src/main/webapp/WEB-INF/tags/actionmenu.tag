<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<security:authorize access="hasRole('ROLE_ADMIN')">

</security:authorize>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<li class="nav-header">打单拣货</li>
<li><a href="${ctx }/erp/picking.html">拣货</a><li>

<li class="nav-header">派车管理</li>
<li><a href="${ctx }/erp/assign_edit.html">新增派车</a><li>
<li><a href="${ctx }/erp/waitForAssign_list.html">未发车派车</a><li>
<li><a href="${ctx }/erp/car_follow_list.html">车辆跟踪</a><li>
<li><a href="${ctx }/erp/assign_list.html">历史派车查询</a><li>


<li class="nav-header">派车返回</li>
<li><a href="${ctx }/erp/return_list.html">返回货物管理</a><li>
<li><a href="${ctx }/erp/assignFee_list.html">返回费用管理</a><li>

<li class="nav-header">派车返回收款</li>
<li><a href="${ctx }/erp/finace_manage.html">返回财务收款</a><li>
<li><a href="${ctx }/erp/orders_received.html">返回财务收款查询</a><li>

<li class="nav-header">基础管理</li>
<li><a href="${ctx }/erp/car_list.html">车辆管理</a><li>
<li><a href="${ctx }/erp/GPS_list.html">GPS设备管理</a><li>
<li class="divider"></li>
