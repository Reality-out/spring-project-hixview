document.addEventListener('DOMContentLoaded', () => {
    let count = 0;
    const articleFormItems = document.getElementById('article-form-items');
    const secondCategoryArr = JSON.parse(document.getElementById('container-data').dataset.subjectSecondCategoryJson)['subjectSecondCategory'];

    document.getElementById('subjectSecondCategory0').value = secondCategoryArr[0];
    for (let i = 1; i < secondCategoryArr.length - 1; i++) {
        count++;
        const formItem = document.createElement('div');
        formItem.setAttribute('class', 'article-form-item article-subject-second-category');

        const label = document.createElement('label');
        label.setAttribute('for', 'subjectSecondCategory' + count);
        label.textContent = document.getElementById('container-data').dataset.subjectSecondCategoryName + count;

        const input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('id', 'subjectSecondCategory' + count);
        input.setAttribute('size', "20");
        input.value = secondCategoryArr[count];

        formItem.append(label);
        formItem.append(input);
        articleFormItems.append(formItem);
    }
});