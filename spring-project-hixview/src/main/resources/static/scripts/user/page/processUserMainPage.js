document.addEventListener('DOMContentLoaded', () => {
    const domesticContent = document.getElementById('domesticEconomyContents');
    const domesticText = domesticContent.textContent.trim();
    domesticContent.textContent = domesticText.substring(1, domesticText.length - 1).replaceAll(', ', ' / ');

    const foreignContent = document.getElementById('foreignEconomyContents');
    const foreignText = foreignContent.textContent.trim();
    foreignContent.textContent = foreignText.substring(1, foreignText.length - 1).replaceAll(', ', ' / ');

    const dataContainer = document.getElementById('container-data');

    const companyPostNewsItemsArr = document.querySelectorAll('.post-company-news-item');
    const companyTargetArticleNamesArr = JSON.parse(dataContainer.dataset.companyTargetArticleNames)['targetArticleName'];
    const companyTargetArticleLinksArr = JSON.parse(dataContainer.dataset.companyTargetArticleLinks)['targetArticleLink'];
    for (let i = 0; i < companyTargetArticleNamesArr.length; i++) {
        const companyTargetArticle = document.createElement('a');
        companyTargetArticle.setAttribute('href', companyTargetArticleLinksArr[i]);
        companyTargetArticle.textContent = companyTargetArticleNamesArr[i];
        companyPostNewsItemsArr[i].append(companyTargetArticle);
    }

    const industryPostNewsItemsArr = document.querySelectorAll('.post-industry-news-item');
    const industryTargetArticleNamesArr = JSON.parse(dataContainer.dataset.industryTargetArticleNames)['targetArticleName'];
    const industryTargetArticleLinksArr = JSON.parse(dataContainer.dataset.industryTargetArticleLinks)['targetArticleLink'];
    for (let i = 0; i < industryTargetArticleNamesArr.length; i++) {
        const industryTargetArticle = document.createElement('a');
        industryTargetArticle.setAttribute('href', industryTargetArticleLinksArr[i]);
        industryTargetArticle.textContent = industryTargetArticleNamesArr[i];
        industryPostNewsItemsArr[i].append(industryTargetArticle);
    }

    const economyPostNewsItemsArr = document.querySelectorAll('.post-economy-news-item');
    const economyTargetArticleNamesArr = JSON.parse(dataContainer.dataset.economyTargetArticleNames)['targetArticleName'];
    const economyTargetArticleLinksArr = JSON.parse(dataContainer.dataset.economyTargetArticleLinks)['targetArticleLink'];
    for (let i = 0; i < economyTargetArticleNamesArr.length; i++) {
        const economyTargetArticle = document.createElement('a');
        economyTargetArticle.setAttribute('href', economyTargetArticleLinksArr[i]);
        economyTargetArticle.textContent = economyTargetArticleNamesArr[i];
        economyPostNewsItemsArr[i].append(economyTargetArticle);
    }
})