document.addEventListener('DOMContentLoaded', () => {
    console.log('processLoginInfo started');

    const sessionContainer = document.getElementById('container-session');
    const notParsedLoginInfo = sessionContainer.dataset.sessionLoginInfo;
    console.log(notParsedLoginInfo);
    if (notParsedLoginInfo) {
        const loginInfo = JSON.parse(notParsedLoginInfo);
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
            loginFuncDialog.style.display = 'flex';
            loginFuncDialog.style.position = 'absolute';
            loginFuncDialog.style.top = '26px';
            
            loginFuncForm.style.display = 'flex';
            loginFuncForm.style.position = 'static';
            loginFuncForm.setAttribute('style', 'flex-direction:column;');

            myPageButton.style.display = 'flex';
            myPageButton.style.position = 'static';
            myPageButton.style.width = 'max-content';
            myPageButton.style.height = '30px';
            myPageButton.setAttribute('style', 'justify-content:center;');

            logoutButton.style.display = 'flex';
            logoutButton.style.position = 'static';
            logoutButton.style.width = 'max-content';
            logoutButton.style.height = '30px';
            logoutButton.setAttribute('style', 'justify-content:center;');

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
    }
})