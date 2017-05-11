<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>
<%@ attribute name="pagePath" type="java.lang.String" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
int current =  page.getNumber() + 1;
int begin = Math.max(1, current - paginationSize/2);
int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());

request.setAttribute("current", current);
request.setAttribute("begin", begin);
request.setAttribute("end", end);
%>

<script type="text/javascript">
$(document).ready(
		function() {
			$("#go").click(function() {
				var page = $("#page").val();
				location.href = "${pagePath}?page="+page+"&sortType=${sortType}&${searchParams}" ;
			});

		});
</script>

<div class="pagination">
	<ul>
		 <% if (page.hasPreviousPage()){%>
               	<li><a href="${pagePath}?page=1&sortType=${sortType}&${searchParams}">&lt;&lt;</a></li>
                <li><a href="${pagePath}?page=${current-1}&sortType=${sortType}&${searchParams}">&lt;</a></li>
         <%}else{%>
                <li class="disabled"><a>&lt;&lt;</a></li>
                <li class="disabled"><a>&lt;</a></li>
         <%} %>
 
		<c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="${pagePath}?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pagePath}?page=${i}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
	  
	  	 <% if (page.hasNextPage()){%>
               	<li><a href="${pagePath}?page=${current+1}&sortType=${sortType}&${searchParams}">&gt;</a></li>
                <li><a href="${pagePath}?page=${page.totalPages}&sortType=${sortType}&${searchParams}">&gt;&gt;</a></li>
         <%}else{%>
                <li class="disabled"><a>&gt;</a></li>
                <li class="disabled"><a>&gt;&gt;</a></li>
         <%} %>

		 <li><a style="vertical-align: middle;">共${page.totalPages }页,共${page.totalElements }条记录,去第<input id="page" style="vertical-align:middle; text-align:center; vertical-align:middle; width: 40px;height: 12px;font-size:small;" value="${current }"/>页，</a></li>
		 <li><a id="go" href="#">确定</a></li>
	</ul>
	
</div>

