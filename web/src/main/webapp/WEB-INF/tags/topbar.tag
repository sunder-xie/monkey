<%@ tag language="java" pageEncoding="UTF-8" %>
<%@attribute name="pageName" required="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
    $(document).ready(function () {
        $("#logout").click(
                function () {
                    $.ajax({
                        type: 'GET',
                        url: '${idpurl}logout',
                        dataType: 'jsonp'
                    });
                }
        );
    });

</script>
<c:choose>
    <c:when test="${ not empty SESSION_USER_NAME }">
        <c:set var="showName" value="${ SESSION_USER_NAME }"/>
    </c:when>
</c:choose>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href=""><img src="${ config.logoImageUrl }"/> ${config.topbarTitle}</a>
            <c:if test="${ not empty pageName }">
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <c:choose>
                            <c:when test="${pageName == 'Home'}">
                                <li class="active"><a href="${ctx}">Home</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${ctx}">Home</a></li>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${pageName == 'crm'}">
                                <li class="active"><a href="${ctx}/crm/salesDashboard.html">CRM</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${ctx}/crm/salesDashboard.html">CRM</a></li>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${pageName == 'erp'}">
                                <li class="active"><a href="${ctx}/erp/intro.html">ERP</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${ctx}/erp/intro.html">ERP</a></li>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${pageName == 'buy'}">
                                <li class="active"><a href="">采购管理</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="#">采购管理</a></li>
                            </c:otherwise>
                        </c:choose>


                        <security:authorize access="hasRole('ROLE_USER')">

                            <li class="dropdown hidden-desktop"><a href="#"
                                                                   class="dropdown-toggle"
                                                                   data-toggle="dropdown">Action <b
                                    class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <o:actionmenu/>
                                </ul>
                            </li>

                        </security:authorize>

                    </ul>
                    <ul class="nav pull-right">
                        <security:authorize access="hasRole('ROLE_USER')">
                            <div class="btn-group">
                                <a class="btn btn-primary btn-small dropdown-toggle"
                                   data-toggle="dropdown" href=""><i
                                        class="icon-user icon-white"></i> ${ showName } <span
                                        class="caret"></span></a>
                                <ul class="dropdown-menu">
                                    <li><a>${ showName }</a></li>
                                    <li class="divider"></li>
                                    <li><a id="logout" href="${ctx }/logout"><i
                                            class="icon-remove"></i>
                                        Log out</a></li>
                                </ul>
                            </div>
                        </security:authorize>
                        <security:authorize access="!hasRole('ROLE_USER')">
                            <a class="btn btn-primary btn-small" href="login"><i
                                    class="icon-user icon-white"></i> Log in</a>
                        </security:authorize>
                    </ul>

                </div>
                <!--/.nav-collapse -->
            </c:if>
        </div>
    </div>
</div>

