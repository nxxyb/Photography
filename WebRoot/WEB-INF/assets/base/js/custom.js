/**
 * 异步获取内容
 * url 请求地址
 * id 上层元素id
 **/
function getByAjax(url, id){
	$.ajax({
	    url:url,
	    type:"Post",
	    success: function (data) {
	    	$("#" + id).html(data);
	    }
	});							
}
