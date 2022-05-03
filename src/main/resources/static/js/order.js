let cartQuantityInputs = document.getElementsByClassName("order-quantity-input")
let updateBtn = document.getElementById('update-order-btn');

updateBtn.addEventListener('click', function(){
	var data = []; 
	data.push(updateBtn.dataset.orderid)
	for(var i = 0; i < cartQuantityInputs.length; i++){
		data.push(cartQuantityInputs[i].value);
	}
	$.ajax({
			contentType:"application/json",
			type:"post",
			data: JSON.stringify(data),
			url: "/updateOrder",
			success : function(data){
				window.location.href=window.location.origin+"/view-order?id="+data
				
			}});
	
});

function updateCart(){
	
}