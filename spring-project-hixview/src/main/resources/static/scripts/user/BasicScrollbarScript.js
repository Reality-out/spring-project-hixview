document.addEventListener('DOMContentLoaded', () => {
    console.log('BasicScrollbarScript started')

    const codeOrName = document.getElementById('codeOrName');
    const messageContainerDataset = document.getElementById('container-message').dataset;
    const msgFunctionSearch = messageContainerDataset.functionSearch;
    const msgFunctionFillIn = messageContainerDataset.functionFillin;
    const msgCompany = messageContainerDataset.company;

    document.getElementById('gnbScb').addEventListener('submit', function(event) {
        event.preventDefault();
        const form = document.getElementById('gnbScb');
        const checkURL = codeOrName.dataset.checkUrl;
        fetch(form.action + checkURL + codeOrName.value, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
        })
        .then(async response => {
            if (response.status !== 200) {
                const errResponse = await response.json();
                throw errResponse;
            }
            return response.json();
        })
        .then(async data => {
            window.location = form.action + data.code;
        })
        .catch(errorData => {
            let errorName = errorData.error;
            if (errorName) {
                codeOrName.value = '';
                if (errorName === "notExistCompanyError") {
                    codeOrName.placeholder = `${msgFunctionSearch}될 ${msgCompany}을 ${msgFunctionFillIn}해 주세요.`;
                } else if (errorName === "notFoundCompanyError") {
                    codeOrName.placeholder = `${msgFunctionSearch}된 ${msgCompany}이 없습니다.`;
                }
            }
        })
    });
})