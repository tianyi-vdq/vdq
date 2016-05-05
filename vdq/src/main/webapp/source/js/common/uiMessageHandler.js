function uiMessageHandler(content,type){
	/**
	 * type
	 * 2：挂单
	 * 3：退单
	 * 4：预警工单
	 * 5：回单
	 * 6：汇报
	 * 7：业务受理
	 *****/
	switch(type){
		case "2":
			goNewurl(content,"ticketList.do");
			break;
		case "3":
			goNewurl(content,"ticketList.do");
			break;
		case "4":
			goNewurl(content,"ticketList.do?leftSign=3"+"&st="+Math.random());
			break;
		case "5":
			goNewurl(content,"ticketList.do");
			break;
		case "6":
			goNewurl(content,"ticketList.do?leftSign=4"+"&st="+Math.random());
			break;
		case "7":
			if($.yw.pageURL=='ticketList.do'){
				setCount();
				setloadticketList(getStep());
			}
			break;
	}
}
function getStep(){
	var t =null;
	$("#ywProcess li").each(function(i){
		var f = $(this).find("div").first().hasClass("step-now");
		if(f){
			t = $(this).find("div").first().attr("name");
		}
	});
	return t;
}
function getDivChk(){
	var t =null;
	$("#divCheck li").each(function(i){
		var f = $(this).hasClass("yw-it-tab");
		if(f){
			t = $(this).attr("name");
		}
	});
	return t;
}
function goNewurl(content,url){
	if($.yw.pageURL=='ticketList.do'){
		setCount();
		setloadticketList(getStep());
	}else{
	    $.messager.show({
	        title:'操作确认',
	        msg:'<a href="'+$.yw.currURL+"/web/workstage/"+url+'">'+content+"</a>",
	        timeout:5000,
	        showType:'slide'
	    });
	}
}
function setloadticketList(obj){
	if(obj=="step1"){
		var satus = getDivChk();
		if(satus=="status5"){
			autoloadtreeTicketTab(5);
		}else if(satus=="status7"){
			autoloadtreeTicketTab(7);
		}else if(satus=="status9"){
			autoloadtreeTicketTab(9);
		}
	}
	else if(obj=="step2"){
		autoloadTicketTab(2);
	}else if(obj=="step3"){
		autoloadTicketTab(3);
	}else if(obj=="step4"){
		autoloadTicketTab(4);
	}
}