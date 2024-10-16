document.addEventListener('DOMContentLoaded', () => {
    let count = 0;
    const addForm = document.getElementById('addForm');
    const addFormItems = document.getElementById('add-form-items');

    document.getElementById('addBtn-subjectSecondCategory').addEventListener('click', () => {
        count++;
        const formItem = document.createElement('div');
        formItem.setAttribute('class', 'add-form-item add-subject-second-category');

        const label = document.createElement('label');
        label.setAttribute('for', 'subjectSecondCategory' + count);
        label.textContent = document.querySelector('.message-subject-second-category').textContent + count;

        const input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('id', 'subjectSecondCategory' + count);
        input.setAttribute('size', "20");

        const errorDiv = document.createElement('div');
        errorDiv.hidden = true;
        errorDiv.setAttribute('class', 'txt-error-message error-subjectSecondCategory' + count);

        formItem.append(label);
        formItem.append(input);
        formItem.append(errorDiv);
        addFormItems.append(formItem);
    });

    document.getElementById('removeBtn-subjectSecondCategory').addEventListener('click', () => {
        if (addFormItems.lastElementChild.querySelector('input').id !== 'subjectSecondCategory0') {
            count--;
            addFormItems.removeChild(addFormItems.lastElementChild);
        }
    });

    document.getElementById('submitBtn').addEventListener('click', function(event) {
        event.preventDefault();
        clearErrorMessage();

        const urlSearchParams = new URLSearchParams(new FormData(addForm));
        urlSearchParams.append('subjectSecondCategories', JSON.stringify({'subjectSecondCategory': 
                Array.from(document.getElementsByClassName('add-subject-second-category')).map(element => element.querySelector('input').value)}));

        fetch(addForm.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: urlSearchParams.toString()
        })
        .then(async response => {
            if (response.status != 303) {
                const errResponse = await response.json();
                throw errResponse;
            }
            return response.json();
        })
        .then(data => {
            window.location = addForm.action + '/finish?name=' + data.name;
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

    function clearErrorMessage() {
        for (const element of document.getElementsByClassName('txt-error-message')) {
            element.hidden = true;
            element.textContent = '';
        }
    }

    function showFieldErrors(fieldErrorMap) {
        for (let [errTarget, errMessage] of Object.entries(fieldErrorMap)) {
            let errClassName = `error-${errTarget}`;
            if (errClassName === 'error-days' || errClassName === 'error-month' || errClassName === 'error-year') {
                errClassName = 'error-date';
            }
            if (errClassName === 'error-subjectSecondCategories') {
                continue;
            }
            const errElement = document.getElementById(errClassName);
            errElement.hidden = false;
            errElement.textContent = decodeURIComponent(errMessage).replaceAll('+', ' ');
        }
    }
});