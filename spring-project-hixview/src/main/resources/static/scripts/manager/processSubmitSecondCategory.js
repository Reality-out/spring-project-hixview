import {clearErrorMessage} from '/scripts/manager/module/utils.js';
document.addEventListener('DOMContentLoaded', () => {
    console.log('processSubmitSecondCategory started')

    let count = 0;
    const basicForm = document.getElementById('basicForm');
    const basicFormItems = document.getElementById('basicFormItems');
    
    document.getElementById('addBtn').addEventListener('click', () => {
        if (count < 4) {
            count++;
            const formItem = document.createElement('div');
            formItem.setAttribute('class', 'basic-form-item article-subject-second-category');
    
            const label = document.createElement('label');
            label.setAttribute('for', 'subjectSecondCategory' + count);
            label.textContent = document.getElementById('container-data').dataset.subjectSecondCategoryName + count;
    
            const input = document.createElement('input');
            input.setAttribute('type', 'text');
            input.setAttribute('id', 'subjectSecondCategory' + count);
            input.setAttribute('size', "20");
    
            formItem.append(label);
            formItem.append(input);
            basicFormItems.append(formItem);    
        }
    });

    document.getElementById('removeBtn').addEventListener('click', () => {
        if (basicFormItems.lastElementChild.querySelector('input').id !== 'subjectSecondCategory0') {
            count--;
            basicFormItems.removeChild(basicFormItems.lastElementChild);
        }
    });

    document.getElementById('submitBtn').addEventListener('click', function(event) {
        event.preventDefault();
        clearErrorMessage();

        const urlSearchParams = new URLSearchParams(new FormData(basicForm));
        urlSearchParams.append('subjectSecondCategories', JSON.stringify({'subjectSecondCategory': 
                Array.from(document.getElementsByClassName('article-subject-second-category')).map(element => element.querySelector('input').value)}));

        fetch(basicForm.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: urlSearchParams.toString()
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
            if (error.isBeanValidationError) {
                const errElement = document.getElementById('error-bean');
                errElement.hidden = false;
                errElement.textContent = errElement.getAttribute('value');
            };
            showFieldErrors(error.fieldErrorMap);
        });
    })

    async function showFieldErrors(fieldErrorMap) {
        for (let [errTarget, errMessage] of Object.entries(fieldErrorMap)) {
            let errClassName = `error-${errTarget}`;
            if (errClassName === 'error-days' || errClassName === 'error-month' || errClassName === 'error-year') {
                errClassName = 'error-date';
            }
            const errElement = document.getElementById(errClassName);
            errElement.hidden = false;
            errElement.textContent = decodeURIComponent(errMessage).replaceAll('+', ' ');
        }
    }
});