<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${movie.name }</title>
<link rel="stylesheet" href="../../css/xiangqingye.css" type="text/css"/>
<script type="text/javascript">
	function reView() {
		window.alert("评分成功！");
	}
	function hid() {
		document.getElementById("mymovie").hidden = false;
	}
</script>
	</head>
	<body>
		<header id="header-main">
		  <ul>
			<li><h1>选电影:</h1></li>
			<li><a href="../movieList/0">喜剧/</a><a href="../movieList/1">科幻/</a><a href="../movieList/2">文艺/</a></li>
			<li><a href="../movieList/3">励志/</a><a href="../movieList/4">剧情/</a><a href="../movieList/5">动作</a></li>
			<c:if test="${name == null }">
				<li><a href="../login">请先登录</a></li>
				<li><a href="../register">立即注册</a></li>
			</c:if>
			<li><a href="#" onclick="hid();"><embed src="${movie.movieurl }" id="mymovie" autostart="false" width="200px;" height="150px;" title="播放" hidden="true">播放</a></li>
	      </ul>
		</header>
		<div id="navi">
			<div id="navi-main">
				<ul>
			        <li><a href="../show" id="../jingdian">经典</a></li>
			        <li>&gt;</li>
			        <li><a href="#">${movie.type }</a></li>
			        <li>&gt;</li>
			        <li><a href="#">${movie.name }</a></li>
		        </ul>
		        <div id="navi-right">
		          <c:if test="${name != null && score == null }">
	      		   <form action="../addReview/${movie.id }" method="post">
	      	  		<input type="hidden" name="review.username" value="${name }">
	      	 		<input type="hidden" name="review.mid" value="${movie.id }">
	      	  		<input type="text" name="review.myreview" value="${review.myreview }" placehoder="请输入您的评分">
	      	  		<input type="submit" id="submit" value="评分" onclick="reView();">
	      		   </form>
		 		  </c:if>
		 		  <c:if test="${name != null && score != null }">
		   			<div id="text">我的评分：${score }</div>
	      			<form action="../updateReview/${movie.id }" method="post">
	      	  		<!-- 存在小bug，在进行更新时，主键也会被更新，如何不更新主键。去掉id的话，就会报错 -->
	      	  	    <input type="hidden" name="review.id" value="${movie.id }">
	      	        <input type="hidden" name="review.username" value="${name }">
	      	        <input type="hidden" name="review.mid" value="${movie.id }">
	      	  		<input type="text" name="review.myreview" value="${review.myreview }" placehoder="请输入您的评分">
	      	  		<input type="submit" id="submit" value="重新评分" onclick="reView();">
	      			</form>
		 		  </c:if>
		         </div>
			</div>
		</div>
		<div id="content">
			<div id="content-title">
				<h1>${movie.name }</h1>
				<p>经典电影，从未被超越</p>
			</div>
			<div id="details">
				<div id="img">
					<img src="../../img/movieIMG/${movie.id}.jpg" />
				</div>
				<div id="introduce">
					<table cellspacing="0">
						<tr>
							<td>导演:</td>
							<td>${movie.director }</td>
						</tr>
						<tr>
							<td>编剧:</td>
							<td>${movie.editor }</td>
						</tr>
						<tr>
							<td>主演:</td>
							<td>${movie.actor } </td>
						</tr>
						<tr>
							<td>类型:</td>
							<td>${movie.type }</td>
						</tr>
						<tr>
							<td>制片国家/地区:</td>
							<td>${movie.place }</td>
						</tr>
						<tr>
							<td>语言:</td>
							<td>${movie.language }</td>
						</tr>
						<tr>
							<td>上映日期:</td>
							<td>${movie.date }</td>
						</tr>
						<tr>
							<td>片长:</td>
							<td>${movie.time }分钟</td>
						</tr>
						<tr>
							<td>又名:</td>
							<td>${movie.rename }</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="juqing">
				<div>
					<strong>剧情简介:</strong><br />
					<p>${movie.detail }</p>
				</div>			
			</div>
		</div>
		<div id="footer">
		    <table cellspacing="0">
		    	<th colspan="4">
		    	    相关推荐
		    	</th>
		    	<tr>
		    		<td>电影名</td>
		    		<td>类型</td>
		    		<td>导演</td>
		    		<td>评分</td>
		    	</tr>
		    	<!-- 用户未登录下的推荐，推荐同类型下的热搜 -->
		    	<c:if test="${name == null }">
		    	<!-- 循环开始 -->
			    	<!-- varStatus不是记录当前显示了多少条，而是当前记录的索引 -->
			    	<c:forEach items="${reMovies }" varStatus="status" var="reMovie">
			    	  <!-- 避免重复的影响 -->
			    	  <c:if test="${reMovie.id != movie.id }">
				    	<tr>
				    		<td><a href="../detail/${reMovie.id }-${name }" class="a1">${reMovie.name }</a></td>
				    		<td>${reMovie.type }</td>
				    		<td>${reMovie.director }</td>
				    		<td><a href="#" class="a2">豆瓣${reMovie.review }</a>/<a href="#" class="a3">IMDB ${reMovie.review }</a></td>
				    	</tr>
				      </c:if>
			    	</c:forEach>
			    <!-- 循环结束 -->
			    </c:if>
		    	<!--用户登录后的推荐，  -->
		    	<c:if test="${name != null }">
			    	<!-- varStatus不是记录当前显示了多少条，而是当前记录的索引 -->
			    	<c:forEach items="${similarMovies }"  var="similarMovie">
			    	  <!-- 避免重复的影响 -->
			    	  <c:if test="${similarMovie.id != movie.id }">
				    	<tr>
				    		<td><a href="../detail/${similarMovie.id }-${name }" class="a1">${similarMovie.name }</a></td>
				    		<td>${similarMovie.type }</td>
				    		<td>${similarMovie.director }</td>
				    		<td><a href="#" class="a2">豆瓣${similarMovie.review }</a>/<a href="#" class="a3">IMDB ${similarMovie.review }</a></td>
				    	</tr>
				      </c:if>
			    	</c:forEach>
			    </c:if>
		    </table>
		</div>
	</body>
</html>