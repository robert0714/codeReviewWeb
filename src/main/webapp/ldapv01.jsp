<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<HTML>
<HEAD>
	<TITLE> 個人程式原始碼 findbugs 分析執行頁面 測試版 </TITLE>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<c:set var="baseUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.servletContext.contextPath}" scope="page" />
    <c:set var="sonarUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}:9000" scope="page" />   
	<jsp:useBean id="personCtr" class="com.iisigroup.java.tech.controller.PersonContoller" />
	<jsp:useBean id="manualCrCtr" class="com.iisigroup.java.tech.controller.ManualCRController" />
	<jsp:useBean id="operator" class="com.iisigroup.java.tech.controller.operation.UserFolderOp" />
	<jsp:useBean id="queueManager" class="com.iisigroup.java.tech.controller.operation.FileQueueManager" />
	
	<link rel="stylesheet" href="${baseUrl}/css/demo.css" type="text/css">
	<link rel="stylesheet" href="${baseUrl}/css/zTreeStyle.css" type="text/css">
	
	<script type="text/javascript" src="${baseUrl}/js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${baseUrl}/js/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript" src="${baseUrl}/js/bootstrap-filestyle.min.js"></script>
	<SCRIPT type="text/javascript">
		
		var setting = {
			view: {
				showIcon: showIconForTree
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeClick: beforeClick,
				onClick: onClick
			}
		};

		
		function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		};

		$(document).ready(function(){
			ldapNodes ();
		});
		var log, className = "dark";
		function beforeClick(treeId, treeNode, clickFlag) {
			className = (className === "dark" ? "":"dark");
			showLog(getTime() + treeNode.name );
			loadData(treeNode.name);
			return (treeNode.click != false);
		}
		function onClick(event, treeId, treeNode, clickFlag) {
			showLog("[ "+getTime()+" onClick ]&nbsp;&nbsp;clickFlag = " + clickFlag + " (" + (clickFlag===1 ? "single selected": (clickFlag===0 ? "<b>cancel selected</b>" : "<b>multi selected</b>")) + ")");
		}		
		function showLog(str) {
			console.log(str);
		}
		
		function getTime() {
			var now= new Date(),
			h=now.getHours(),
			m=now.getMinutes(),
			s=now.getSeconds();
			return (h+":"+m+":"+s);
		}
		function loadData(name){
			var rx = /.*(\d{7}).*/g;
			var match = rx.exec(name);
			if($.isArray(match)){
				var result =  match[match.length-1] ;
				console.log("matched: "+ result);
				window.open("${baseUrl}/personCR?empUid="+result, "_self");
				return result; 
			}
			
		}
		function ldapNodes (){
			try {
				console.log("ldapNodes  " ); 
				var servletCommand = "ldapView";
				var baseDN = "OU=IE,DC=iead,DC=local";
				var strategy ="node";
				$.post(
                    servletCommand,
                            {   "baseDN" : baseDN,
                                "strategy" : strategy
                            }, function(data) {
                            	$.fn.zTree.init($("#treeDemo"), setting, data);
                            } ,"json"   );
                             
			} catch (err) {
				console.log(err);
			}
		}
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
				case 'GETPCR':
					 window.open(link, "_self", "scrollbars=1,resizable=1,height=500,width=1200");
					 break;
				}
				
                             
			} catch (err) {
				console.log(err);
			}
		};	
	</SCRIPT>

</HEAD>

<BODY>
<h1>個人程式原始碼 findbugs 分析執行頁面</h1>
<h6>[ 測試版  ]</h6>


<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	<div class="right">
		<ul class="info">
			<li class="title"><h2>1, 使用說明</h2>
				<ul class="list">
				<li>點擊左邊樹狀結構當中的人名項目，就會帶入相關資訊出來。</li>
				<li class="highlight_red">由於是照實抓取公司的LDAP資訊.所以樹狀結構謹供參考。</li>
				<li><input type="button"   id="downloadF_000"   name="downloadF_000"    value="眾人歷史統計報表"  
						onclick="manualCR('000' ,'000' , '000' , '000', 'GETSTATICS' )" ></li>
						<li><a href="${baseUrl}/notifyConfigv01.jsp" target="_blank">通知設定</a>。</li>
				</ul>
				
			</li>
			<li class="title"><h2>2, 操作文件</h2>
				<ul class="list">				
				<li>
				<a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">流程圖</a> 、
				<a href="https://docs.google.com/spreadsheets/d/1EWIVc4Kq3jv6xpNqyZuZNr_L-R5UlxgA2KZDoILuH8w/edit?usp=sharing" target="_blank">人工查核表</a>、 
				<a href="https://docs.google.com/document/d/1K8q7kytn-m4i_lDnrWhslQuLFbVfaLfByu7OcNEclNk/edit?usp=sharing" target="_blank">給受codeReview同事的</a>、
				<a href="https://docs.google.com/document/d/19qdlQiBHw96-gAvmZrTuuF_pCJ5DDf5t8UhDSXO-8C0/edit?usp=sharing" target="_blank">給查核同仁者的</a>、
				<a href="https://docs.google.com/document/d/1qb1ARWv5BEpP-iwll5u6o6lWZbWpZVPvVZ27PLGzndk/edit?usp=sharing" target="_blank">給組長的</a>、
				<a href="https://docs.google.com/document/d/1qJF8MPAKqPT6oUj89YD56hxyc02_N6OV0hdoA1YVsPU/edit?usp=sharing" target="_blank">給協理的</a>、
				<a href="${sonarUrl}/coding_rules#s=createdAt|asc=false" target="_blank">sonar 規則</a>
				。
				</li>
				</ul>
				<ul>
					<div class="CSSTableGenerator" >
					<table>
						 <tr><th>員工編號</th><th>姓名</th> <th>sonar執行按鈕</th><th>sonar執行進度</th><th>執行log</th><th>查核表單</th><th>查核表單完成版本</th><th>sonar連結</th></tr>
    						<tr><td><a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td>
    							<td><a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td>
    							<td>要挑選編碼方式<br/>以及日期(step 2)<br/><a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td>
    							<td>step 2<a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td>
    							<td>觀看為何無法正常分析<br/><a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td>
    							<td>1.查核同仁(step 2 , 4):<br/> (1)上傳:第一次審查、複查<br/>(2)下載用<br/>2.組長為了確認有無複查日期</td>
    							<td>組長上傳(step 5),<br/>協理觀看<br/><a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td>
    							<td>進入sonar(step 2 )<br/><a href="${baseUrl}/img/flow2015_0107.jpg" target="_blank">請參考流程圖。</a></td></tr>
    				 <c:forEach items='${sessionScope["userFolderList"]}'  var="userFolder">
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
			<br/>
			<!-- <img alt="${userFolder['info'].getSonarURL(sonarUrl)}" src="${baseUrl}/img/excel_32_icon.png" onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','000', 'GETPCR' )"  /> 
    	 -->
    	<a onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','000', 'GETPCR' )"    >. </a>
		</td>
		<td>
			<c:forEach items="${operator.getAllVersion(userFolder['info'])}" var="version">
				<c:set var="hasAttatchment" value="${manualCrCtr.getUloadersFilesState(userFolder['info']['projectKey'] ,version  )}" scope="page" />				 			
			 	 ${version}	: ${hasAttatchment} 
			 	 <c:if test="${hasAttatchment eq '已上傳' }">
			 	  
			 	  	<input type="button"   id="downloadF_${userFolder['info']['projectKey']}"   name="downloadF_${userFolder['info']['projectKey']}"    value="查核同仁、組長下載"  
						onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','${hasAttatchment}', 'GETUPLOADERFILE' )" >		
					
			 	  </c:if>
			 	  	<form action="manualCR" method="post"  enctype="multipart/form-data" id="step24${userFolder['info']['projectKey']}" name="step24${userFolder['info']['projectKey']}"  >
						<input  type="file" name="fileSelect"  ID="fileSelect" runat="server" size="50" accept="application/vnd.ms-excel ,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet " />
						<br/>					
						<input  type="submit" value="查核同仁上傳" />
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
			 		${version}	:
					<form action="manualCR" method="post"  enctype="multipart/form-data"  id="step5${userFolder['info']['projectKey']}" name="step5${userFolder['info']['projectKey']}" >
						<input  type="file" name="fileSelect"  ID="fileSelect" runat="server" size="50" accept="application/vnd.ms-excel ,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet " />
						<br/>					
						<input  type="submit" value="組長上傳" />
						<input type="hidden" id ="projectKey" name="projectKey"  value="${userFolder['info']['projectKey']}"  size="0" height="0" />
						<input type="hidden" id ="projectVersion" name="projectVersion"  value="${version}"   size="0" height="0"   /> 
						<input type="hidden" id ="command" name="command"  value="upload"    size="0" height="0"  /> 
						<input type="hidden" id ="encoding" name="encoding"  value="UTF-8"    size="0" height="0"  />
						<input type="hidden" id ="step" name="step"  value="5"    size="0" height="0"  /> 
				 	</form> 
				 	${hasAttatchment}
			 	  <c:if test="${hasAttatchment eq '已上傳' }">
			 	  
			 	  	<input type="button"   id="downloadF_${userFolder['info']['projectKey']}"   name="downloadF_${userFolder['info']['projectKey']}"    value="協理下載"  
						onclick="manualCR('${userFolder['info']['projectKey']}' ,'${userFolder['info']['empdata']['empId']}' , '${version}','${hasAttatchment}', 'GETFINISH' )" >		
					
			 	  </c:if>
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
					</div>
				</ul>
			</li>
		</ul>
		<ul>
			<br/> 
     <table>
     	<tr><td>console</td></tr>
     	<tr><td><textarea id ="console" name="console" rows="30" cols="200">
				 
		</textarea></td></tr>
     </table>
		</ul>
	</div>
</div>
</BODY>
</HTML>