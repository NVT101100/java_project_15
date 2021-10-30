const $ = document.querySelector.bind(document)
const $$ = document.querySelectorAll.bind(document)


const newPostBtns = $$('.new-post-btn')
const postBtn = $('.post-box__btn')
const closeNewPostBtn = $('.post-box__head-close')
const textAreaBox = $('.post-box__content-textarea')
const overplay = $('.overplay')




const app = (() => {	
	return {
		handle(){
			const _this = this
			function checkSubmit() {
				const value = textAreaBox.value
                if (value || imgPostBox.style.display != 'none'){
                    postBtn.classList.add('btn-active')
                } 
				else {
                    postBtn.classList.remove('btn-active')
                }
			}

			textAreaBox.oninput = function(){
                checkSubmit()
            }

			function closeOverplay(){
                overplay.style.display = 'none'
                document.body.style.overflow = 'auto'
            }
            function showOverplay(){
                overplay.style.display = 'flex'
                document.body.style.overflow = 'hidden'
            }
			function showBox() {
                showOverplay()
				checkSubmit()
            }
			newPostBtns.forEach((newPostBtn) => {
                newPostBtn.onclick = function(){
                    showBox()
                }
            })
			closeNewPostBtn.onclick = () => {
                closeOverplay()
            }
		},
		start(){
            this.handle()
        }
	}
})().start()