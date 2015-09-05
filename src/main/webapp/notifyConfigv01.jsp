<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<HTML>
<HEAD>
	<TITLE>選擇人員名單</TITLE>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<c:set var="baseUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.localPort}${pageContext.servletContext.contextPath}" scope="page" />
       
	<jsp:useBean id="personCtr" class="com.iisigroup.java.tech.controller.PersonContoller" />
	<jsp:useBean id="manualCrCtr" class="com.iisigroup.java.tech.controller.ManualCRController" />
	<jsp:useBean id="operator" class="com.iisigroup.java.tech.controller.operation.UserFolderOp" />
	<jsp:useBean id="queueManager" class="com.iisigroup.java.tech.controller.operation.FileQueueManager" />
	
	<link rel="stylesheet" href="${baseUrl}/css/demo.css" type="text/css">
	<link rel="stylesheet" href="${baseUrl}/css/zTreeStyle.css" type="text/css">
	
	<script type="text/javascript" src="${baseUrl}/js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${baseUrl}/js/jquery.ztree.core-3.5.min.js"></script>
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
				var tmp = $("ul#watchers").val();
				console.log("tmp: "+ tmp);
				try {
					console.log("watchersMgr  " );  
					var servletCommand = "ctrl/watchersMgrCtrl/operateWatchers";
					var strategy ="read";
					$.post(
	                    servletCommand,
	                            {   "empId" : result,
	                                "strategy" : strategy
	                            }, function(data) {
	                            	drawTable(data);
	                            	 $("#targetEmpId").val(result);
	                            } ,"json"   );
	                             
				} catch (err) {
					console.log(err);
				}
				return result; 
			}
		}
		function drawTable(data) {
			 var trtag = $("tr");
			 var row = $("<tr/>");
			  
			 trtag.remove();
			 $("#personDataTable").append(row);
			  row.append($("<th>Id</th>"));
			  row.append($("<th>Name</th>"));
		    for (var i = 0; i < data.length; i++) {
		        drawRow(data[i]);
		    }
		}

		function drawRow(rowData) {
		    var row = $("<tr />")
		   
		    $("#personDataTable").append(row); //this will append tr element to table... keep its reference for a while since we will add cels into it
		    row.append($("<td>" + rowData.id + "</td>"));
		    row.append($("<td>" + rowData.name + "<a onclick='delWatcher(\"" + rowData.id + "\")'>(-)</a></td>"));
		}

		function ldapNodes (){
			try {
				console.log("ldapNodes  " ); 
				var servletCommand = "ctrl/ldapViewCtrl/listTree";
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
		function addWatcher (){
			var targetEmpId = $.trim($("input#targetEmpId").val()) ;
			var addEmpId = $.trim($("input#addEmpId").val()) ;
			console.log("targetEmpId: "+targetEmpId);
			console.log("addEmpId: "+addEmpId);
			try {
				console.log("watchersMgr  " ); 
				var servletCommand = "ctrl/watchersMgrCtrl/operateWatchers";
				var strategy ="add";
				$.post(
                    servletCommand,
                            {   "empId" : targetEmpId,
                    			"addWatcherId" : addEmpId,
                                "strategy" : strategy
                            }, function(data) {
                            	drawTable(data);
                            	 $("#targetEmpId").val(targetEmpId);
                            } ,"json"   );
                             
			} catch (err) {
				console.log(err);
			}
		}
		function delWatcher (delEmpId){
			var targetEmpId = $.trim($("input#targetEmpId").val()) ;
			console.log("targetEmpId: "+targetEmpId);
			console.log("delEmpId: "+delEmpId);
			try {
				console.log("watchersMgr  " ); 
				var servletCommand = "ctrl/watchersMgrCtrl/operateWatchers";
				var strategy ="delete";
				$.post(
                    servletCommand,
                            {   "empId" : targetEmpId,
                    			"delWatcherId" : delEmpId,
                                "strategy" : strategy
                            }, function(data) {
                            	drawTable(data);
                            	 $("#targetEmpId").val(targetEmpId);
                            } ,"json"   );
                             
			} catch (err) {
				console.log(err);
			}
		}
	</SCRIPT>

</HEAD>

<BODY>
<h1>選擇人員名單</h1>
<h6>[ 測試版  ]</h6>


	<div class="content_wrap">
		<div class="zTreeDemoBackground left">
			<ul id="treeDemo" class="ztree"></ul>
		</div>
		<div class="right">
			<ul class="info">
				<li class="title"><h2>1, 使用說明</h2>
					<ul class="list">
						<li>點擊左邊樹狀結構當中的人名項目，就會加入清單中。</li>
						<li class="highlight_red">由於是照實抓取公司的LDAP資訊.所以樹狀結構完全由ISC單位做決定。</li>

					</ul>
				</li>
				<li class="title"><h2>2, 新增人員通知名單</h2>
					<ul class="list">
						<li>請輸入員工編號:
						<input type="text" id="addEmpId" name="addEmpId" />
						<input type="button"   id="addEmpIdBtn" name="addEmpIdBtn"  value="確定"  onclick="addWatcher()" />
						<input type="hidden" id="targetEmpId" name="targetEmpId" value="" />
						</li>
					</ul>
				</li>
				<li class="title"><h2>3, 人員通知名單</h2>
					<table id="personDataTable" class="display" >
						
							<tr>
								<th>Id</th>
								<th>Name</th>								
							</tr>
					
					</table>
				</li>
			</ul>
		</div>
	</div>
</BODY>
</HTML>