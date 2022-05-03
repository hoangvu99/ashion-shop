let goBtn = document.getElementById('go')
let page = document.getElementById('page-number')

if(goBtn != null){
	go.addEventListener('click', function(){
	window.location.href = window.location.origin+"/new-orders?page="+page.value;
} );
}

let searchBtn = document.getElementById('search-btn');
let searchInput = document.getElementById('search-inp'); 

searchBtn.addEventListener('click', () => {
	if(searchInput.value.length==0){
		window.location.href = window.location.origin+"/new-orders";
	}else{
		window.location.href = window.location.origin+"/new-orders?s="+searchInput.value;
	}
	
});