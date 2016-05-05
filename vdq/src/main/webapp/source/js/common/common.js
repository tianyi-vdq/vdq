
/**获得弹出层居中的方法**/
function getDialogPosition(wrap,offsize){
	var temp = $(wrap).css("width");
	var dl = $(document).scrollLeft(),
		dt = $(document).scrollTop(),
		ww = $(window).width(),
		wh = $(window).height(),
		ow = temp.substring(0,temp.indexOf("p")),
		oh = $(wrap).height(),
		left = (ww - ow) / 2 + dl,
		top = top = (oh < 4 * wh / 7 ? wh * 0.382 - oh / 2 : (wh - oh) / 2) + dt;
	
    left = Math.max(left, dl),
    top = Math.max(top, dt) - offsize;
    sc = new Array();
    sc[0] = top;
    sc[1] = left;
	    
	return sc;
}
/**获得弹出层居中的方法**/
function exitlogin(){
	window.location.href = $.yw.currURL+"/logout.do";
}
function valueTrim(obj){
	$(obj).val($.trim($(obj).val()));
}
function showProcess(isShow, title, msg) {
    if (!isShow) {
        $.messager.progress('close');
        return;
    }
    $.messager.progress({
        title: title,
        msg: msg
    });
}
function autoloadTicketTab(sign){
	 $("#ticketlist").load($.yw.currURL+"/web/workstage/jsonLoadTicketTab.do?s="+Math.random()+"&sign="+sign,function(){
			$.parser.parse($("#ticketlist"));
			setHei();
		});
}
function autoloadtreeTicketTab(status){
	 $("#threeticketList").load($.yw.currURL+"/web/workstage/jsonLoadThreeTicketList.do?s="+Math.random()+"&status="+status,function(){
			$.parser.parse($("#threeticketList"));
			setHei();
		});
}

//跟新同步工单数量
function setCount(){
	$.ajax({  
          type : "get",  
          url : $.yw.currURL+"/web/workstage/jsonLoadTicketListCount.do?s="+Math.random(),   
          async : false,  
          success : function(data){  
        	  data = $.parseJSON(data); 
        	  //alert(data.obj.waitCount);
        	  if($("#waitCount")!=undefined){
        		  
        	  $("#waitCount").text(data.obj.waitCount); 
        	  }
        	 
				if($("#busiCount")!=undefined){
					 $("#busiCount").text(data.obj.busiCount); 
        	  }
				if($("#busiCount1")!=undefined){
					 $("#busiCount1").text("待受理（"+data.obj.busiCount+"）"); 
        	  }
				if($("#alertCount")!=undefined){
					 $("#alertCount").text(data.obj.alertCount); 
        	  }
				if($("#alertCount1")!=undefined){
					 $("#alertCount1").text("预警（"+data.obj.alertCount+"）"); 
        	  }
				if($("#latestCount")!=undefined){
					 $("#latestCount").text(data.obj.latestCount); 
        	  }
				if($("#latestCount1")!=undefined){
					 $("#latestCount1").text("汇报（"+data.obj.latestCount+"）"); 
        	  }
				if($("#count7")!=undefined){
					 $("#count7").text("挂单审核（"+data.obj.count7+"）"); 
        	  }
				if($("#count5")!=undefined){
					 $("#count5").text("回单审核（"+data.obj.count5+"）"); 
        	  }
				if($("#count9")!=undefined){
					 $("#count9").text("退单审核（"+data.obj.count9+"）"); 
        	  }
          }  
          }); 
}