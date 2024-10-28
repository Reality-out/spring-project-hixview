export function clearErrorMessage() {
    for (const element of document.getElementsByClassName('txt-error-message')) {
        element.hidden = true;
        element.textContent = '';
    }
}