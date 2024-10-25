document.addEventListener('DOMContentLoaded', () => {
    console.log('processSubmitArticleNameAndLinkScript started')

    let count = 0;
    const basicForm = document.getElementById('basicForm');
    const basicFormItems = document.getElementById('basicFormItems');
    
    document.getElementById('addBtn').addEventListener('click', () => {
        if (count < 4) {
            count++;
            const contentFormItem = document.createElement('div');
            contentFormItem.setAttribute('class', 'post-target-article-content');
    
            const nameFormItem = document.createElement('div');
            nameFormItem.setAttribute('class', 'basic-form-item post-target-article-name');
    
            const nameLabel = document.createElement('label');
            nameLabel.setAttribute('for', 'targetArticleName' + count);
            nameLabel.textContent = document.getElementById('container-data').dataset.targetArticleName + count;
    
            const nameInput = document.createElement('input');
            nameInput.setAttribute('type', 'text');
            nameInput.setAttribute('id', 'targetArticleName' + count);
            nameInput.setAttribute('size', "20");
    
            nameFormItem.append(nameLabel);
            nameFormItem.append(nameInput);
    
            const linkFormItem = document.createElement('div');
            linkFormItem.setAttribute('class', 'basic-form-item post-target-article-link');
    
            const linkLabel = document.createElement('label');
            linkLabel.setAttribute('for', 'targetArticleLink' + count);
            linkLabel.textContent = document.getElementById('container-data').dataset.targetArticleLink + count;
    
            const linkInput = document.createElement('input');
            linkInput.setAttribute('type', 'text');
            linkInput.setAttribute('id', 'targetArticleLink' + count);
            linkInput.setAttribute('size', "20");
    
            linkFormItem.append(linkLabel);
            linkFormItem.append(linkInput);
    
            contentFormItem.append(nameFormItem);
            contentFormItem.append(linkFormItem);
            basicFormItems.append(contentFormItem);
        }
    });

    document.getElementById('removeBtn').addEventListener('click', () => {
        if (basicFormItems.lastElementChild.querySelector('.post-target-article-name').querySelector('input').id != 'targetArticleName0') {
            count--;
            basicFormItems.removeChild(basicFormItems.lastElementChild);
        }
    });

    document.getElementById('submitBtn').addEventListener('click', function(event) {
        event.preventDefault();
        clearErrorMessage();

        const urlSearchParams = new URLSearchParams(new FormData(basicForm));
        urlSearchParams.append('targetArticleNames', JSON.stringify({'targetArticleName': 
                Array.from(document.getElementsByClassName('post-target-article-name')).map(element => element.querySelector('input').value)}));
        urlSearchParams.append('targetArticleLinks', JSON.stringify({'targetArticleLink': 
                Array.from(document.getElementsByClassName('post-target-article-link')).map(element => element.querySelector('input').value)}));
    
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

    async function clearErrorMessage() {
        for (const element of document.getElementsByClassName('txt-error-message')) {
            element.hidden = true;
            element.textContent = '';
        }
    }

    async function showFieldErrors(fieldErrorMap) {
        for (let [errTarget, errMessage] of Object.entries(fieldErrorMap)) {
            let errClassName = `error-${errTarget}`;
            if (errClassName === 'error-days' || errClassName === 'error-month' || errClassName === 'error-year') {
                errClassName = 'error-date';
            }
            if (errClassName === 'error-classification') {
                continue;
            }
            const errElement = document.getElementById(errClassName);
            errElement.hidden = false;
            errElement.textContent = decodeURIComponent(errMessage).replaceAll('+', ' ');
        }
    }
});