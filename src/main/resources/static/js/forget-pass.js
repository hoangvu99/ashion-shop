

let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

let email = document.getElementById('email')

let submitButton = document.getElementById('submit-btn');
let rePass_form = document.getElementById('rePass-form');
let emailErrorMsg = document.getElementById('email-error-message');
let submitMsg = document.getElementById('submit-message');
let emailNotExistMsg = document.getElementById('emailNotExistMsg');

let checkmail = false;


email.addEventListener('input', (e) => {
	if(emailNotExistMsg != null){
		emailNotExistMsg.style.display = 'none';
	}
  if(e.target.value.match(mailformat)){
	
    
    checkmail = true;
    emailErrorMsg.innerText = "Email hợp lệ";
    emailErrorMsg.style.color = '#28a745';
  }else{
   
    checkmail = false;
     emailErrorMsg.innerText = "Email không hợp lệ";
     emailErrorMsg.style.color = '#bd2130';
  }
});


submitButton.addEventListener('click', (e) => {



  if(checkmail == true){
    rePass_form.submit();
  }else{
    submitMsg.innerText="Vui lòng nhập email";
    submitMsg.style.color ='#bd2130';
  }
})
