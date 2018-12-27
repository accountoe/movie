<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/register.css" type="text/css" />
<title>欢迎注册</title>
<script type="text/javascript">
	function checkForm(form) {
		if(form.password.value == "" && form.username.value == "") {
			windows.alert("请输入用户名或密码！");
			return false;
		}else {
			windows.alert("注册成功！");
			return true;
		}
	}
</script>

</head>
  <body>
	<div id="header">
		<h3>欢迎注册</h3>
	</div>
	<div id="content">
	  <form action="submitR" method="post">
		<div class="line">
			<span class="text"><b>*</b>用户名：</span>
			<span class="ipt"><input class="kuang1 user" type="text" name="user.name"/></span>
			<span class="dsp">1-10位字母、数字、汉字、'-'、'_'组合</span>
		</div>
		<div class="line">
			<span class="text"><b>*</b>密码：</span>
			<span class="ipt"><input class="kuang1 psw" type="password" name="user.password"/></span>
			<span class="dsp">6-20位字符，建议字母、数字和符号</span>
		</div>
		<div class="line">
			<span class="text"><b>*</b>请确认密码：</span>
			<span class="ipt"><input class="kuang1 psw" type="password" name="Repassword"/></span>
			<span class="dsp">请再次输入密码</span>
		</div>
		<div class="line">
			<span class="text"><b>*</b>验证手机：</span>
			<span class="ipt"><input class="kuang1 phone" type="text"/></span>
			<span class="dsp">验证完成后，可以使用该手机找回</span>
		</div>
		<div class="line">
			<span class="text"><b>*</b>短信验证码：</span>
			<span class="ipt"><input class="kuang1 yanzheng" type="text"/></span>
			<span><input type="button" value="获取短信验证码"class="yz_btn" /></span>
		</div>
		<div class="line">
			<span class="ipt"><input class="xieyi" value="注册协议" type="checkbox"/>
			<b  class="xz">我已同意并阅读<a href="#">&lt;&lt;注册协议&gt;&gt;</a></b></span>
		</div>
		<div class="line">
			<span class="ipt zhuce">
				<input class="tijiao" type="submit" value="注册" >
				<input class="chongzhi" type="reset" value="重置">
			</span>
		</div>
	  </form>
	</div>
  </body>
</html>