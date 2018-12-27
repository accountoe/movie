<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"  href="../css/css.css" />
<title>电影推荐</title>
<style type="text/css">
*{
	margin: 0;
	padding: 0;
	list-style: none;
	
}
body{
	background:#e6e8ea;
}
#navi{
	width: 500px;
	height: 40px;
	margin: 0px auto;
    margin-top: 50px;
	
}
#navi_main{
	float: left;

	margin-left: auto;
}
#navi_main li{
	
	float: left;
	width: 165px;
	height: 40px;
	line-height: 40px;
	text-align: center;
	
}
#navi_main li h3{
	font-weight: normal;
	color: #333;
	font-size: 24px;
	padding-right: 7px;
	text-align: right;
}
#navi_main li a{
	
	width: 100%;
	height: 100%;
	color: #aaa;
	font-size: 20px;
	text-decoration: none;
	font-weight: bold;
	padding-right: 6px;
}
#navi_main li a:hover{
	color: #333;
}
</style>
</head>
<body class="channel root mvcat swipeLeft scrollTop">
<div id="navi">
	<div id="navi_main">
		<ul>
			<li><h3>经典:</h3></li>
			<li><a href="movieList/0">喜剧/</a><a href="movieList/1">科幻/</a><a href="movieList/2">文艺/</a></li>
			<li><a href="movieList/3">励志/</a><a href="movieList/4">剧情/</a><a href="movieList/5">动作</a></li>
		</ul>
	</div>
</div>
<div class="right">
  <div class="insearch" id="insearch">
    <form action="searchM/${name }" method="post">
        <input id="word" name="inputSearch" placeholder="影片 / 类型 / 心情" />
        <input id="submit" type="submit" value="↲" />
    </form>
   </div>
</div>
</body>
</html>