let goBtn = document.getElementById('go')
let page = document.getElementById('page-number')
go.addEventListener('click', function(){
	window.location.href = window.location.origin+"/list-product?page="+page.value;
} );

let searchBtn = document.getElementById('search-btn');
let searchInput = document.getElementById('search-inp'); 

searchBtn.addEventListener('click', () => {
	if(searchInput.value.length==0){
		window.location.href = window.location.origin+"/list-product";
	}else{
		window.location.href = window.location.origin+"/list-product?s="+searchInput.value;
	}
	
});