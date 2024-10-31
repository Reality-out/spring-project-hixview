document.addEventListener('DOMContentLoaded', () => {
    console.log('processReceiveEconomyContent started')

    const basicFormItems = document.getElementById('basicFormItems');
    const economyContentArr = JSON.parse(document.getElementById('container-data').dataset.targetEconomyContentJson)['targetEconomyContent'];

    document.getElementById('targetEconomyContent0').value = economyContentArr[0];
    for (let i = 1; i < economyContentArr.length; i++) {
        const formItem = document.createElement('div');
        formItem.setAttribute('class', 'basic-form-item article-target-economy-content');

        const label = document.createElement('label');
        label.setAttribute('for', 'targetEconomyContent' + i);
        label.textContent = document.getElementById('container-data').dataset.targetEconomyContentName + i;

        const input = document.createElement('input');
        input.setAttribute('type', 'text');
        input.setAttribute('id', 'targetEconomyContent' + i);
        input.setAttribute('size', "20");
        input.value = economyContentArr[i];

        formItem.append(label);
        formItem.append(input);
        basicFormItems.append(formItem);
    }
});