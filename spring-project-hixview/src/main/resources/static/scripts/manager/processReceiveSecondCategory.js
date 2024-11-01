document.addEventListener('DOMContentLoaded', () => {
    console.log('processReceiveSecondCategory started')

    const basicFormItems = document.getElementById('basicFormItems');
    const secondCategoryArr = JSON.parse(document.getElementById('container-data').dataset.subjectSecondCategoryJson)['subjectSecondCategory'];

    document.getElementById('subjectSecondCategory0').value = secondCategoryArr[0];
    for (let i = 1; i < secondCategoryArr.length; i++) {
        formItem.setAttribute('class', 'basic-form-item article-subject-second-category');

        const label = document.createElement('label');
        label.setAttribute('for', 'subjectSecondCategory' + i);
        label.textContent = document.getElementById('container-data').dataset.subjectSecondCategoryName + i;

        const input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('id', 'subjectSecondCategory' + i);
        input.setAttribute('size', "20");
        input.value = secondCategoryArr[i];

        formItem.append(label);
        formItem.append(input);
        basicFormItems.append(formItem);
    }
});