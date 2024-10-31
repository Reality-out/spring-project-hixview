export function clearErrorMessage() {
    for (const element of document.getElementsByClassName('txt-error-message')) {
        element.hidden = true;
        element.textContent = '';
    }
}

export function encodeWithUTF8(value) {
    return encodeURIComponent(value.replaceAll("\\+", "%20").replaceAll("%", "%25").replaceAll("\\$", "%24")
    .replaceAll("&", "%26").replaceAll("\\?", "%3F").replaceAll("=", "%3D")
    .replaceAll("#", "%23").replaceAll(":", "%3A").replaceAll("/", "%2F"));
}