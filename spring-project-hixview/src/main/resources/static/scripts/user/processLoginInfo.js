document.addEventListener('DOMContentLoaded', () => {
    console.log('processLoginInfo started');

    const sessionContainer = document.getElementById('container-session');
    const loginInfoExists = sessionContainer.dataset.loginInfoExists;
    if (loginInfoExists == 'true') {
        document.querySelector('.link-membership').style.display = 'none';
        document.querySelector('.link-login').style.display = 'none';

        const loginFuncDiv = document.querySelector('.div-login-function');
        loginFuncDiv.style.display = 'flex';
        loginFuncDiv.style.position = 'relative';

        const openDialogBtn = document.querySelector('#openDialogBtn');
        openDialogBtn.style.display = 'flex';
        openDialogBtn.style.position = 'static';
        openDialogBtn.setAttribute('style', 'justify-content:center;');

        const loginFuncDialog = document.querySelector('.dialog-login-function');
        const loginFuncForm = document.querySelector('.form-login-function');
        const myPageButton = document.querySelector('#myPageBtn');
        const logoutButton = document.querySelector('#logoutBtn');

        loginFuncDiv.addEventListener('mouseover', () => {
            loginFuncDialog.style.cssText = 'display: flex; position: absolute; top: 26px;';
            loginFuncForm.style.cssText = 'display: flex; flex-direction: column;';
            myPageButton.style.cssText = 'display: flex; width: max-content; height: 30px; align-items: center;';
            logoutButton.style.cssText = 'display: flex; width: max-content; height: 30px; align-items: center;';

            loginFuncDialog.show();
            loginFuncDialog.focus();
        })

        loginFuncDiv.addEventListener('mouseout', () => {
            loginFuncDialog.style.display = 'none';
            loginFuncForm.style.display = 'none';
            myPageButton.style.display = 'none';
            logoutButton.style.display = 'none';

            loginFuncDialog.close();
        })

        logoutButton.addEventListener('click', () => {
            fetch('/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
            .then(async response => {
                if (response.status !== 303) {
                    const errResponse = await response.json();
                    throw errResponse;
                }
                return response.json();
            })
            .then(data => {
                window.location = data['redirectPath'];
            })
            .catch(error => {
                console.log(data['error']);
            });
        })
    }
})