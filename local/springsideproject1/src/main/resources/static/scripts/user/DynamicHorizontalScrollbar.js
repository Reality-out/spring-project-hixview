export function checkOverflow() {
    const body = document.body;
    if (body.scrollWidth > body.clientWidth) {
        body.style.overflowX = 'scroll';
    } else {
        body.style.overflowX = 'hidden';
    }
}