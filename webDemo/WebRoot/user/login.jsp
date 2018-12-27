<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户登录</title>
<style type="text/css">
/*通用*/
body {
	font-size: 12px;
	font-family: "微软雅黑", arial, verdana;
	color: #666;
	padding: 0;
	margin: 0;
	border: 0;
}

ul, li, p, h1, h2, h3, h4, h5, h6 {
	margin: 0;
	padding: 0;
	list-style: none;
	border: none;
}

input {
	border: none;
}

.lf {
	float: left;
}

.rt {
	float: right;
}

.clear {
	clear: both;
}

a {
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

body {
	height: 700px;
	width: 100%;
	background: #ccc;
}

#denglu {
	height: 442px;
	width: 470px;
	margin-left: 300px;
	margin-top: 70px;
	border: 1px solid #ddd;
	box-shadow: 0 0 5px 2px #ddd;
}

#denglu_top {
	height: 50px;
	width: 100%;
	border: 1px solid #ddd;
}

#denglu_top {
	height: 50px;
	width: 450px;
	padding-left: 20px;
	background: #eaeff3;
	font-size: 16px;
	line-height: 50px;
}

.denglu {
	height: 300px;
	width: 100%;
	padding-top: 50px;
	background: #fff;
	border-bottom: 1px solid #ddd;
}

.line {
	width: 470px;
	height: 60px;
	padding-top: 9px;
}

.kuang1 {
	display: block;
	margin: auto;
	width: 238px;
	height: 16px;
	padding: 10px 25px 10px 35px;
	border: 1px solid #ddd;
	background-repeat: no-repeat;
	background-position: 5px center;
}

.user {
	background-image: url(img/user.jpg);
}

.pwd {
	background-image: url(img/pwd.jpg);
}

.xuanze {
	display: block;
	float: left;
	margin-left: 90px;
}

.line a {
	color: #d42830;
	padding-left: 160px;
}

.btn {
	display: block;
	height: 42px;
	width: 300px;
	margin: auto;
	color: #fff;
	background: red;
	border-radius: 3px;
	font-size: 25px;
	text-align: center;
}

#last {
	height: 40px;
	width: 100%;
	text-align: center;
	font-size: 16px;
	background: #eaeff3;
}

#last a {
	color: red;
}

footer {
	height: 300px;
	width: 100%;
	background: #ddd;
}
</style>
</head>
<body>
	<div id="denglu" class="lf">
		<div id="denglu_top">
			<span>账户密码登录</span>
		</div>
		<div class="denglu">
			<form action="submitL" method="post">
				<div class="line">
					<input type="text" class="kuang1 user" name="name" placeholder="用户名/邮箱、手机号" />
				</div>
				<div class="line">
					<input type="password" class="kuang1 pwd" name="password" placeholder="登录密码" />
				</div>
				<div class="line">
					<span><input type="checkbox" placeholder="登录密码" value="自动登录" class="xuanze" /> 
						<b>自动登录</b> 
						<a href="#">忘记密码？</a>
					</span>
				</div>
				<div>
					<input type="submit" class="btn" value="登录"/>
				</div>
			</form>
		</div>
		<div id="last">
			还未注册？<a href="register">立即注册</a>
		</div>
	</div>
</body>
</html>