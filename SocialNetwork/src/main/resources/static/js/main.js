const formElement = document.querySelector('#form-register')
const fullNameInput = formElement.querySelector('[name=fullName]')
const passwordInputs = formElement.querySelectorAll('[type=password]')
const toggleShowPasswordBtn = formElement.querySelectorAll('.form-section__form-group-icon')
const passwordInput = formElement.querySelector('[name=password]')
const formCheckList = formElement.querySelector('.form-section__form-checkList')
const formCheckItems = formCheckList.querySelectorAll('.form-section__form-checkItem')


document.addEventListener("DOMContentLoaded", () => {

	passwordInput.onfocus = () => {
		if(formCheckList.classList.contains('hidden'))
			formCheckList.classList.remove('hidden')
	}	

	// show / hidden password
	Array.from(passwordInputs).forEach((element, index) => {
		toggleShowPasswordBtn[index].onclick = () => {
			if(toggleShowPasswordBtn[index].classList.contains('hidden')) {
				toggleShowPasswordBtn[index].classList.remove('hidden')
				element.type = "text"
			}
			else {
				toggleShowPasswordBtn[index].classList.add('hidden')
				element.type = "password"
			}
		}
	})


	// Validate form register
	Validator({
		form: "#form-register",
		formGroup: ".form-section__form-group",
		errorMessage: ".form-section__form-message",
		rules: [
			Validator.isRequired('#fullName', 'Vui lòng nhập họ tên'),
			Validator.isRequired('#email', 'Vui lòng nhập email'),
			Validator.isEmail('#email'),
			Validator.isRequired('#password', 'Vui lòng nhập mật khẩu'),
			Validator.minLength('#password', 8),
			Validator.minCharAlpha('#password', 1),
			Validator.minNumberOfCharSpecial('#password'),
			Validator.isRequired('#password-confirmation', 'Vui lòng nhập lại mật khẩu'),
			Validator.isConfirmed('#password-confirmation', () => {
				return passwordInput.value
			}, "Mật khẩu nhập lại không chính xác"),
			Validator.isRequired('input[name=gender]'),
		],
		onSubmit: data => {
			let dataUser = JSON.parse(localStorage.getItem('dataUser'))
			localStorage.setItem('dataUser', JSON.stringify(dataUser))
			window.location = 'login.html'
		}
	})
})