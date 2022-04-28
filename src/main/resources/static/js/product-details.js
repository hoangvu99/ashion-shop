let addBtn = document.getElementById('cart-btn');
let quantity = document.getElementById('quantity');
let id = document.getElementById('productId');
let message = document.getElementById('size-message'); 
let cartCount = document.getElementById('cart-item-count');
let faCount = document.getElementById('favorite-item-count');
let favoriteBtn = document.getElementById('favorite-button');

favoriteBtn.addEventListener('click',function(){
	faCount.textContent = parseInt(faCount.textContent) + 1;
})

addBtn.addEventListener('click',function(){
	
	
	let val = $("#size-form input[type='radio']:checked").val();
	if(val == null){
		message.innerText="*Chưa chọn size";
		message.classList.remove("text-success")
		message.classList.add("text-danger");
		message.style.display="block"
	}else{
		let data = [];
		data.push(id.textContent);
		data.push(val);
		data.push(quantity.value);
		
		$.ajax({
			contentType:"application/json",
			type:"post",
			data: JSON.stringify(data),
			url: "/addProductToCart",
			success: function(){
				cartCount.textContent= parseInt(cartCount.textContent)+1
				message.innerText="Đã thêm vào giỏ hàng!";
				message.classList.remove("text-danger")
				message.classList.add("text-success");
				message.style.display="block"
			},
			
			error: function(jqXHR, textStatus, errorThrown) {
            console.log('error while post');
       		 }
			
		});
	}
	
	
})

function selectOnlyThis(id) {
	let size= document.getElementsByClassName('size-checkbox');
	for(var i = 0; i < size.length; i++){
		 document.getElementById(i+1).checked = false;
		
	}
	
    document.getElementById('size-message').style.display="none"
	document.getElementById(id).checked = true;
		
    
}

