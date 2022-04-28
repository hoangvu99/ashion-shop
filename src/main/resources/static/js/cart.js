let cartQuantityInputs = document.getElementsByClassName("cart-quantity-input")


function updateCart(){
	var data = []; 
	for(var i = 0; i < cartQuantityInputs.length; i++){
		data.push(cartQuantityInputs[i].value);
	}
	
	$.ajax({
			contentType:"application/json",
			type:"post",
			data: JSON.stringify(data),
			url: "/updateCart",
			success : function(){
				window.location.href=window.location.origin+"/cart"
				
			}});
}