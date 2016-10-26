<%@ page pageEncoding="utf-8"%>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">  
<html>  
    <head>  
          <meta charset="utf-8">  
          <title>上传文件</title>  
    </head>  
    <body>  
         <form action="${pageContext.request.contextPath}/month_account/uploadFile" method="post" enctype="multipart/form-data">  
         <input type="file" name="fileName" />
          <input type="text" name="thirdPlatFormName"/>
          <input type="submit" value="Submit" /></form>  
    </body>  
</html>  
