import {clearErrorMessage} from '/scripts/manager/module/utils.js';
document.addEventListener('DOMContentLoaded', () => {
    console.log('processSubmitMembership started')

    const membershipForm = document.getElementById('membershipForm');
    const passwordElement = document.getElementById('password');
    const passwordCheckElement = document.getElementById('passwordCheck');
    const passwordCheckErrElement = document.getElementById('error-passwordCheck');
    const formElements = new Array(document.getElementById('id'), passwordElement, passwordCheckElement, document.getElementById('name'), document.getElementById('email'));

    passwordCheckElement.addEventListener('blur', () => {
        if (passwordElement.value != passwordCheckElement.value) {
            validatePasswordCheckError();
        } else {
            invalidatePasswordCheckError();
        }
    })

    passwordElement.addEventListener('keydown', function(event) {
        const pressedKey = event.key.toLowerCase();
        if (event.ctrlKey && (pressedKey == 'c' || pressedKey == 'v')) {
            event.preventDefault();
        }
    })

    passwordCheckElement.addEventListener('keydown', function(event) {
        const pressedKey = event.key.toLowerCase();
        if (event.ctrlKey && (pressedKey == 'c' || pressedKey == 'v')) {
            event.preventDefault();
        }
    })

    passwordElement.addEventListener('contextmenu', function(event) {
        event.preventDefault();
    })

    passwordCheckElement.addEventListener('contextmenu', function(event) {
        event.preventDefault();
    })

    membershipForm.addEventListener('submit', function(event) {
        event.preventDefault();
        clearErrorSettings();
        
        if (passwordElement.value != passwordCheckElement.value) {
            validatePasswordCheckError();
        } else {
            fetch(membershipForm.action, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams(new FormData(membershipForm)).toString()
            })
            .then(async response => {
                if (response.status !== 303) {
                    const errResponse = await response.json();
                    throw errResponse;
                }
                return response.json();
            })
            .then(data => {
                window.location = data['redirectPath'] + '?name=' + data.name;
            })
            .catch(error => {
                showFieldErrors(error.fieldErrorMap);
            });    
        }        
    })

    async function clearErrorSettings() {
        clearErrorMessage();
        formElements.forEach(element => element.setAttribute('style', 'border-color:black;'));
    }

    async function validatePasswordCheckError() {
        passwordCheckElement.setAttribute('style', 'border-color:red;');
        passwordCheckErrElement.hidden = false;
        passwordCheckErrElement.textContent = '비밀번호가 일치하지 않습니다.';
    }

    async function invalidatePasswordCheckError() {
        passwordCheckElement.setAttribute('style', 'border-color:black;');
        passwordCheckErrElement.hidden = true;
        passwordCheckErrElement.textContent = '';
}

    async function showFieldErrors(fieldErrorMap) {
        for (let [errTarget, errMessage] of Object.entries(fieldErrorMap)) {
            const errTargetElement = document.getElementById(errTarget);
            errTargetElement.setAttribute('style', 'border-color:red;');
            let errClassName = `error-${errTarget}`;
            const errMessageElement = document.getElementById(errClassName);
            errMessageElement.hidden = false;
            errMessageElement.textContent = decodeURIComponent(errMessage).replaceAll('+', ' ');
        }
    }
});