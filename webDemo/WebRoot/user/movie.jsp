<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选电影</title>
<link rel="stylesheet" href="../../css/xiju.css" type="text/css"/>
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
		</ul>
		</header>
		<div id="navi">
			<div id="navi-main">
				<ul>
			        <li><a href="show" id="jingdian">经典</a></li>
			        <li>&gt;</li>
			        <li><a href="movieList/${type }">${type }</a></li>
		        </ul>
		        
			</div>
		</div>
		<div id="content">
			<div id="content-main">
				<ul>
					<c:forEach items="${movies }" var="movie">
					<li>
					  		<a href="../detail/${movie.id }-${name }">
							<img src="../../img/movieIMG/${movie.id }.jpg" alt="${movie.name }" title="${movie.name }"/>
						</a>
						<a href="../detail/${movie.id }-${name }">${movie.name }</a>
					</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div id="footer">
			<span>© 2005－2018 douban.com, all rights reserved 北京豆网科技有限公司</span>
			<ul>
				<li><a href="#">关于网站</a></li>
				<b>|</b>
				<li><a href="#">联系我们</a></li>
				<b>|</b>
				<li><a href="#">免责声明</a></li>
				<b>|</b>
				<li><a href="#">帮助中心</a></li>
				<b>|</b>
				<li><a href="#">移动应用</a></li>
				<b>|</b>
				<li><a href="#">广告中心</a></li>
			</ul>
		</div>
	</body>
</html>