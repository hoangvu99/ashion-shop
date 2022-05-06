let sizeText = document.getElementsByClassName("size-text");
let sizeNum = document.getElementsByClassName("size-num");
let freeSize = document.getElementsByClassName("free-size");

let category = document.getElementById('category')
console.log(category)

category.addEventListener('change', function(e){
	if(e.target.value == 3){
		for(var i = 0; i < sizeText.length; i++){
			sizeText[i].setAttribute("readOnly",true);
		}
		freeSize[0].setAttribute("readOnly",true);
	}else if(e.target.value == 5){
		for(var i = 0; i < sizeText.length; i++){
			sizeText[i].setAttribute("readOnly",true);
		}
		for(var i = 0; i < sizeNum.length; i++){
			sizeNum[i].setAttribute("readOnly",true);
		}
		freeSize[0].removeAttribute("readOnly")
	}else{
		for(var i = 0; i < sizeText.length; i++){
			sizeText[i].removeAttribute("readOnly")
		}
		for(var i = 0; i < sizeNum.length; i++){
			sizeNum[i].removeAttribute("readOnly")
		}
		freeSize[0].removeAttribute("readOnly")
	}
	
});
