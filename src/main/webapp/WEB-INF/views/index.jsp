<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="layout/header.jsp"%>

<div class="container">
	<c:forEach var="board" items="${boardList.content }">
		<div class="card m-2">
			<div class="card-body">
				<h4 class="card-title">${board.title }</h4>
				<a href="/board/${board.id }" class="btn btn-primary">상세보기</a>
			</div>
		</div>
	</c:forEach>

	<ul class="pagination justify-content-center">
		<c:choose>
			<c:when test="${!boardList.first }">
				<li class="page-item"><a class="page-link" href="?page=${boardList.number }">Previous</a></li>
			</c:when>
		</c:choose>

		<c:forEach var="page" items="${pageList }">
			<li class="page-item"><a class="page-link" href="?page=${page }">${page }</a></li>
		</c:forEach>

		<c:choose>
			<c:when test="${!boardList.last }">
				<li class="page-item"><a class="page-link" href="?page=${boardList.number+2 }">Next</a></li>
			</c:when>
		</c:choose>
	</ul>
</div>

<%@include file="layout/footer.jsp"%>



