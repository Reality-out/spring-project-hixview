document.addEventListener('DOMContentLoaded', () => {
    console.log('processReceiveArticleNameAndLink started')

    const basicFormItems = document.getElementById('basicFormItems');
    const articleNameArr = JSON.parse(document.getElementById('container-data').dataset.targetArticleNameJson)['targetArticleName'];
    const articleLinkArr = JSON.parse(document.getElementById('container-data').dataset.targetArticleLinkJson)['targetArticleLink'];

    document.getElementById('targetArticleName0').value = articleNameArr[0];
    document.getElementById('targetArticleLink0').value = articleLinkArr[0];
    for (let i = 1; i < articleNameArr.length; i++) {
        const contentFormItem = document.createElement('div');
        contentFormItem.setAttribute('class', 'post-target-article-content');

        const nameFormItem = document.createElement('div');
        nameFormItem.setAttribute('class', 'basic-form-item post-target-article-name');

        const nameLabel = document.createElement('label');
        nameLabel.setAttribute('for', 'targetArticleName' + i);
        nameLabel.textContent = document.getElementById('container-data').dataset.targetArticleName + i;

        const nameInput = document.createElement('input');
        nameInput.setAttribute('type', 'text');
        nameInput.setAttribute('id', 'targetArticleName' + i);
        nameInput.setAttribute('size', "20");
        nameInput.value = articleNameArr[i];

        nameFormItem.append(nameLabel);
        nameFormItem.append(nameInput);

        const linkFormItem = document.createElement('div');
        linkFormItem.setAttribute('class', 'basic-form-item post-target-article-link');

        const linkLabel = document.createElement('label');
        linkLabel.setAttribute('for', 'targetArticleLink' + i);
        linkLabel.textContent = document.getElementById('container-data').dataset.targetArticleLink + i;

        const linkInput = document.createElement('input');
        linkInput.setAttribute('type', 'text');
        linkInput.setAttribute('id', 'targetArticleLink' + i);
        linkInput.setAttribute('size', "20");
        linkInput.value = articleLinkArr[i];

        linkFormItem.append(linkLabel);
        linkFormItem.append(linkInput);

        contentFormItem.append(nameFormItem);
        contentFormItem.append(linkFormItem);
        basicFormItems.append(contentFormItem);
    }
});