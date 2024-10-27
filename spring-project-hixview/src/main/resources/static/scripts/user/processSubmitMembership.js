import {clearErrorMessage} from '/scripts/manager/module/utils.js';
document.addEventListener('DOMContentLoaded', () => {
    console.log('processSubmitMembership started')

    const membershipForm = document.getElementById('membershipForm');
    const errorMessages = document.querySelector('.txt-error-messages');
    const formElements = new Array(document.getElementById('id'), document.getElementById('password'), document.getElementById('name'), document.getElementById('email'));
    
    membershipForm.addEventListener('submit', function(event) {
        event.preventDefault();
        clearErrorSettings();

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
    })

    async function showFieldErrors(fieldErrorMap) {
        errorMessages.setAttribute('style', 'margin-bottom:1.5rem;');
        for (let [errTarget, errMessage] of Object.entries(fieldErrorMap)) {
            const errTargetElement = document.getElementById(errTarget);
            errTargetElement.setAttribute('style', 'border-color:red;');
            let errClassName = `error-${errTarget}`;
            const errMessageElement = document.getElementById(errClassName);
            errMessageElement.hidden = false;
            errMessageElement.textContent = decodeURIComponent(errMessage).replaceAll('+', ' ');
        }
    }

    async function clearErrorSettings() {
        errorMessages.setAttribute('style', 'margin-bottom:0;');
        formElements.forEach(element => {
            element.setAttribute('style', 'border-color:black;')
        })
        clearErrorMessage();
    }
});