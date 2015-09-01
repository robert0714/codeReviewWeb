<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="pragma" content="no-cache"  />
		<meta http-equiv="cache-control" content="no-cache"  />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="description" content="人員名單" />
		<title>個人程式原始碼 findbugs 分析執行頁面 測試版</title>
		<c:set var="baseUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.servletContext.contextPath}" scope="page" />
       

       <script type="text/javascript" src="${baseUrl}/js/jquery-1.11.2.min.js"></script> 
       <c:set var="sonarUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}:9000" scope="page" />
		<jsp:useBean id="personCtr" class="com.iisigroup.java.tech.controller.PersonContoller" />
		<jsp:useBean id="manualCrCtr" class="com.iisigroup.java.tech.controller.ManualCRController" />
		<jsp:useBean id="operator" class="com.iisigroup.java.tech.controller.operation.UserFolderOp" />
		<jsp:useBean id="queueManager" class="com.iisigroup.java.tech.controller.operation.FileQueueManager" />
       <script>		
		function sonarAnalysis(projectKey ,empId , cmd) {
			try {
				console.log("projectKey :"+projectKey); 
			 	var selectName = 'select#encoding_'+empId;
			 	var selectNameForVersion = 'select#version_'+empId;
			 	var selectedVersion = $(selectNameForVersion).val();
				var encoding = $(selectName).val(); 	
				var statusName = 'input#status_' + empId;
				
				var servletCommand = "sonarAnalysis";				
				$.post(
                    servletCommand,
                            {   "command" : cmd,
                                "projectKey" : projectKey,
                                "encoding" :encoding ,
                                "projectVersion" : selectedVersion 
                            }, function(data) {
								var tmmp = String(data)  ;                      
                                console.log(tmmp);
                                var textArea = $.trim($("textarea#console").val()) ;
                                console.log(textArea);
                               $("textarea#console").val(textArea+tmmp);
                               var status = $(statusName).val();
                               console.log(status);
                               if('ANALYSIS' === cmd){
                            	   $(statusName).val('等待處理中');
                               }
                              
                            } );
                             
			} catch (err) {
				console.log(err);
			}
		};	
		function manualCR(projectKey ,empId ,selectedVersion, hasAttatchment ,cmd) {
			try {
				console.log("projectKey :"+projectKey); 
			 	var selectName = 'select#encoding_'+empId;
			 	 
				var encoding = $(selectName).val(); 
				var servletCommand = "manualCR"; 
				 
				var link =   '${baseUrl}/'+servletCommand + '?command=' + cmd    + '&projectKey=' + projectKey + '&encoding=' + encoding + '&projectVersion=' + selectedVersion;
				if('無上傳資料'===hasAttatchment){
					return ;
				}
				
				switch(cmd){
				 default:
					 break;
				 case 'GETUPLOADERFILE':
					 window.open(link, "_self", "scrollbars=1,resizable=1,height=500,width=1200");
					 break;
				 case 'GETFINISH':
					 window.open(link, "_self", "scrollbars=1,resizable=1,height=500,width=1200");
					 break;
				 case 'GETSTATICS':
					 window.open(link, "_self", "scrollbars=1,resizable=1,height=500,width=1200");
					 break;
				 case 'GETPSTATICS':
					 window.open(link, "_self", "scrollbars=1,resizable=1,height=500,width=1200");
					 break;
				}
				
                             
			} catch (err) {
				console.log(err);
			}
		};	
	</script>
</head>
<body>
	<h1>個人程式原始碼 findbugs 分析執行頁面 測試版</h1>
	<img src="${baseUrl}/img/flow2015_0107.jpg" />
		<input type="button"   id="downloadF_000"   name="downloadF_000"    value="下載被Reviewer的Measurement"  
						onclick="manualCR('000' ,'000' , '000' , '000', 'GETSTATICS' )" >	
     <table align="left" border="1"> 
     
     <tr><th>員工編號</th><th>姓名</th> <th>sonar執行按鈕</th><th>sonar執行進度</th><th>執行log</th><th>查核表單</th><th>查核表單完成版本</th><th>sonar連結</th></tr>
    <tr><td> </td><td> </td> <td>要挑選編碼方式<br/>以及日期(step 2)</td><td>step 2</td><td>觀看為何無法正常分析</td><td>1.查核同仁(step 2 , 4):<br/> (1)上傳:第一次審查、複查<br/>(2)下載用<br/>2.組長為了確認有無複查日期</td><td>組長上傳(step 5),<br/>協理觀看</td><td>進入sonar(step 2 )</td></tr>
    
     <c:forEach items="${personCtr.getAllUserForCodeReview()}" var="userFolder">
    	<tr> <td>${userFolder['info']['empdata']['empId']}</td>
    	<td>
    		${userFolder['info']['empdata']['chtName']}
    		<br/>
    		${userFolder['info']['empdata']['engName']}
    	
    	</td> 
    	    
	    <td>
	    	<select id="encoding_${userFolder['info']['empdata']['empId']}" name="encoding_${userFolder['info']['empdata']['empId']}"              >
			  <option value="UTF-8"  >UTF-8</option>			  
			  <option value="MS950"  >MS950</option>
			</select>
			<select id="version_${userFolder['info']['empdata']['empId']}" name="version_${userFolder['info']['empdata']['empId']}"              >
			  <c:forEach items="${operator.getAllVersion(userFolder['info'])}" var="version">
			 	 <option value="${version}"  >${version}</option>			  
			   </c:forEach>
			</select>
			
	    	<input type="button"   id="sonarExec_${userFolder['info']['projectKey']}"   name="sonarExec_${userFolder['info']['projectKey']}"    value="分析" 
	    	
	    	onclick="sonarAnalysis('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}',  'ANALYSIS' )" />
	    	 
		</td>
		<td >
			<input id ="status_${userFolder['info']['empdata']['empId']}" name ="status_${userFolder['info']['empdata']['empId']}"  
			 type="text" disabled="disabled" value="${queueManager.getStateByEnpUid(userFolder['info']['empdata']['empId'])}" width="8"  />
			 
			
		 </td>
		<td>
			<input type="button"   id="log_${userFolder['info']['projectKey']}"   name="log_${userFolder['info']['projectKey']}"    value="LOG"
			onclick="sonarAnalysis('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}',  'LOG' )" />
		</td>
		<td>
			<c:forEach items="${operator.getAllVersion(userFolder['info'])}" var="version">
				<c:set var="hasAttatchment" value="${manualCrCtr.getUloadersFilesState(userFolder['info']['projectKey'] ,version  )}" scope="page" />				 			
			 	 ${version}	: ${hasAttatchment} 
			 	 <c:if test="${hasAttatchment eq '已上傳' }">
			 	  
			 	  	<input type="button"   id="downloadF_${userFolder['info']['projectKey']}"   name="downloadF_${userFolder['info']['projectKey']}"    value="下載"  
						onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','${hasAttatchment}', 'GETUPLOADERFILE' )" >		
					
			 	  </c:if>
			 	  	<form action="manualCR" method="post"  enctype="multipart/form-data" id="step24${userFolder['info']['projectKey']}" name="step24${userFolder['info']['projectKey']}"  >
						<input  type="file" name="fileSelect"  ID="fileSelect" runat="server" size="50" accept="application/vnd.ms-excel ,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet " />
						<br/>					
						<input  type="submit" value="上傳" />
						<input type="hidden" id ="projectKey" name="projectKey"  value="${userFolder['info']['projectKey']}"  size="0" height="0" />
						<input type="hidden" id ="projectVersion" name="projectVersion"  value="${version}"   size="0" height="0"   /> 
						<input type="hidden" id ="command" name="command"  value="upload"    size="0" height="0"  /> 
						<input type="hidden" id ="encoding" name="encoding"  value="UTF-8"    size="0" height="0"  /> 
						<input type="hidden" id ="step" name="step"  value="2,4"    size="0" height="0"  /> 
				 	</form> 
			 	 <br/>			 	 
			</c:forEach>
		</td>
		<td>
			<c:forEach items="${operator.getAllVersion(userFolder['info'])}" var="version">
				<c:set var="hasAttatchment" value="${manualCrCtr.getManualCRState(userFolder['info']['projectKey'] ,version  )}" scope="page" />
			 			
			 	 ${version}	: ${hasAttatchment}
			 	  <c:if test="${hasAttatchment eq '已上傳' }">
			 	  
			 	  	<input type="button"   id="downloadF_${userFolder['info']['projectKey']}"   name="downloadF_${userFolder['info']['projectKey']}"    value="下載"  
						onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','${hasAttatchment}', 'GETFINISH' )" >		
					
			 	  </c:if>
			 	 	<form action="manualCR" method="post"  enctype="multipart/form-data"  id="step5${userFolder['info']['projectKey']}" name="step5${userFolder['info']['projectKey']}" >
						<input  type="file" name="fileSelect"  ID="fileSelect" runat="server" size="50" accept="application/vnd.ms-excel ,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet " />
						<br/>					
						<input  type="submit" value="上傳" />
						<input type="hidden" id ="projectKey" name="projectKey"  value="${userFolder['info']['projectKey']}"  size="0" height="0" />
						<input type="hidden" id ="projectVersion" name="projectVersion"  value="${version}"   size="0" height="0"   /> 
						<input type="hidden" id ="command" name="command"  value="upload"    size="0" height="0"  /> 
						<input type="hidden" id ="encoding" name="encoding"  value="UTF-8"    size="0" height="0"  />
						<input type="hidden" id ="step" name="step"  value="5"    size="0" height="0"  /> 
				 	</form> 
			 	 <br/>			 	 
			</c:forEach>
		</td>
    	<td> 
		 <c:set var="projectUrl" value="${userFolder['info'].getSonarURL(sonarUrl)}" scope="page" />
  		<img alt="${userFolder['info'].getSonarURL(sonarUrl)}" src="${baseUrl}/img/sonar-wave.png" onclick='javascript:window.open("${projectUrl}", "_blank", "scrollbars=1,resizable=1,height=500,width=1200");' title="${userFolder['info']['empdata']['chtName']}"  /> 
    	
						
		<img alt="${userFolder['info'].getSonarURL(sonarUrl)}" src="${baseUrl}/img/excel_32_icon.png" onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','000', 'GETPSTATICS' )"  /> 
    	
    	</td>
    	
   		 </tr>
     </c:forEach>
     </table>
     <br/> 
     <table>
     	<tr><td>console</td></tr>
     	<tr><td><textarea id ="console" name="console" rows="30" cols="200">
				 
		</textarea></td></tr>
     </table>
</body>
</html>
