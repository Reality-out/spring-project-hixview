import {clearErrorMessage} from '/scripts/manager/module/utils.js';
document.addEventListener('DOMContentLoaded', () => {
    console.log('processSubmitLogin started')

    const loginForm = document.getElementById('loginForm');
    const passwordElement = document.getElementById('password');
    const formElements = new Array(document.getElementById('id'), passwordElement);

    passwordElement.addEventListener('keydown', function(event) {
        const pressedKey = event.key.toLowerCase();
        if (event.ctrlKey && (pressedKey == 'c' || pressedKey == 'v')) {
            event.preventDefault();
        }
    })

    passwordElement.addEventListener('contextmenu', function(event) {
        event.preventDefault();
    })

    loginForm.addEventListener('submit', function(event) {
        event.preventDefault();
        clearErrorSettings();
        
        fetch(loginForm.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(new FormData(loginForm)).toString()
        })
        .then(async response => {
            if (response.status !== 303) {
                const errResponse = await response.json();
                throw errResponse;
            }
            return response.json();
        })
        .then(data => {
            const path = data['redirectPath'] || '/';
            const redirectUrl = new URL(path, window.location.origin);
            window.location.href = redirectUrl;
        })
        .catch(error => {
            showFieldErrors(error.fieldErrorMap);
        });
    })

    async function clearErrorSettings() {
        clearErrorMessage();
        formElements.forEach(element => element.setAttribute('style', 'border-color:black;'));
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