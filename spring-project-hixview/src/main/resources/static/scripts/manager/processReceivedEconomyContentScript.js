document.addEventListener('DOMContentLoaded', () => {
    console.log('processReceivedEconomyContentScript started')

    let count = 0;
    const articleFormItems = document.getElementById('article-form-items');
    const economyContentArr = JSON.parse(document.getElementById('container-data').dataset.targetEconomyContentJson)['targetEconomyContent'];

    document.getElementById('targetEconomyContent0').value = economyContentArr[0];
    for (let i = 1; i < economyContentArr.length - 1; i++) {
        count++;
        const formItem = document.createElement('div');
        formItem.setAttribute('class', 'article-form-item article-target-economy-content');

        const label = document.createElement('label');
        label.setAttribute('for', 'targetEconomyContent' + count);
        label.textContent = document.getElementById('container-data').dataset.targetEconomyContentName + count;

        const input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('id', 'targetEconomyContent' + count);
        input.setAttribute('size', "20");
        input.value = economyContentArr[count];

        formItem.append(label);
        formItem.append(input);
        articleFormItems.append(formItem);
    }
});